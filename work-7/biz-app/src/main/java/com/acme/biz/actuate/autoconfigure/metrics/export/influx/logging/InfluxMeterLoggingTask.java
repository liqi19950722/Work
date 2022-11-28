package com.acme.biz.actuate.autoconfigure.metrics.export.influx.logging;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.util.DoubleFormat;
import io.micrometer.core.instrument.util.MeterPartition;
import io.micrometer.core.instrument.util.StringUtils;
import io.micrometer.influx.InfluxMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;


public class InfluxMeterLoggingTask {

    private Logger logger = LoggerFactory.getLogger(InfluxMeterLoggingTask.class);
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final MeterRegistry delegate;

    public InfluxMeterLoggingTask(MeterRegistry meterRegistry) {
        this.delegate = meterRegistry;

        scheduledExecutorService.scheduleAtFixedRate(this::action, 10000, 10000,
                TimeUnit.MILLISECONDS);
    }

    private void action() {
        for (List<Meter> batch : MeterPartition.partition(delegate, 10000)) {
            batch.stream()
                    .flatMap(m -> m.match(
                            gauge -> writeGauge(gauge.getId(), gauge.value()),
                            counter -> writeCounter(counter.getId(), counter.count()),
                            this::writeTimer,
                            this::writeSummary,
                            this::writeLongTaskTimer,
                            gauge -> writeGauge(gauge.getId(), gauge.value(getBaseTimeUnit())),
                            counter -> writeCounter(counter.getId(), counter.count()),
                            this::writeFunctionTimer,
                            this::writeMeter))
                    .forEach(text -> logger.info(text));
        }
    }


    // VisibleForTesting
    Stream<String> writeMeter(Meter m) {
        List<Field> fields = new ArrayList<>();
        for (Measurement measurement : m.measure()) {
            double value = measurement.getValue();
            if (!Double.isFinite(value)) {
                continue;
            }
            String fieldKey = measurement.getStatistic().getTagValueRepresentation()
                    .replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
            fields.add(new Field(fieldKey, value));
        }
        if (fields.isEmpty()) {
            return Stream.empty();
        }
        Meter.Id id = m.getId();
        return Stream.of(influxLineProtocol(id, id.getType().name().toLowerCase(), fields.stream()));
    }

    private Stream<String> writeLongTaskTimer(LongTaskTimer timer) {
        Stream<Field> fields = Stream.of(new Field("active_tasks", timer.activeTasks()),
                new Field("duration", timer.duration(getBaseTimeUnit())));
        return Stream.of(influxLineProtocol(timer.getId(), "long_task_timer", fields));
    }

    // VisibleForTesting
    Stream<String> writeCounter(Meter.Id id, double count) {
        if (Double.isFinite(count)) {
            return Stream.of(influxLineProtocol(id, "counter", Stream.of(new Field("value", count))));
        }
        return Stream.empty();
    }

    // VisibleForTesting
    Stream<String> writeGauge(Meter.Id id, Double value) {
        if (Double.isFinite(value)) {
            return Stream.of(influxLineProtocol(id, "gauge", Stream.of(new Field("value", value))));
        }
        return Stream.empty();
    }

    // VisibleForTesting
    Stream<String> writeFunctionTimer(FunctionTimer timer) {
        double sum = timer.totalTime(getBaseTimeUnit());
        if (Double.isFinite(sum)) {
            Stream.Builder<Field> builder = Stream.builder();
            builder.add(new Field("sum", sum));
            builder.add(new Field("count", timer.count()));
            double mean = timer.mean(getBaseTimeUnit());
            if (Double.isFinite(mean)) {
                builder.add(new Field("mean", mean));
            }
            return Stream.of(influxLineProtocol(timer.getId(), "histogram", builder.build()));
        }
        return Stream.empty();
    }

    private Stream<String> writeTimer(Timer timer) {
        final Stream<Field> fields = Stream.of(new Field("sum", timer.totalTime(getBaseTimeUnit())),
                new Field("count", timer.count()), new Field("mean", timer.mean(getBaseTimeUnit())),
                new Field("upper", timer.max(getBaseTimeUnit())));

        return Stream.of(influxLineProtocol(timer.getId(), "histogram", fields));
    }

    private Stream<String> writeSummary(DistributionSummary summary) {
        final Stream<Field> fields = Stream.of(new Field("sum", summary.totalAmount()),
                new Field("count", summary.count()), new Field("mean", summary.mean()),
                new Field("upper", summary.max()));

        return Stream.of(influxLineProtocol(summary.getId(), "histogram", fields));
    }

    private String influxLineProtocol(Meter.Id id, String metricType, Stream<Field> fields) {
        String tags = getConventionTags(id).stream().filter(t -> StringUtils.isNotBlank(t.getValue()))
                .map(t -> "," + t.getKey() + "=" + t.getValue().replaceAll(" ","\b").replaceAll(",","\\,")).collect(joining(""));

        return getConventionName(id) + tags + ",metric_type=" + metricType + " "
                + fields.map(Field::toString).collect(joining(",")) + " " + delegate.config().clock().wallTime();
    }


    protected List<Tag> getConventionTags(Meter.Id id) {
        return id.getConventionTags(delegate.config().namingConvention());
    }

    protected String getConventionName(Meter.Id id) {
        return id.getConventionName(delegate.config().namingConvention());
    }
    protected final TimeUnit getBaseTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }
    static class Field {

        final String key;

        final double value;

        Field(String key, double value) {
            // `time` cannot be a field key or tag key
            if (key.equals("time")) {
                throw new IllegalArgumentException("'time' is an invalid field key in InfluxDB");
            }
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + DoubleFormat.decimalOrNan(value);
        }

    }
}

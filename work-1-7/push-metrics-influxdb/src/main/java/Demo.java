import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo {
    static char[] token = "06AuxHXahTz_S0OpGnYL7w4ptg33zUmlY4vm7wYBadpYFnxcfwh4mBjaC8KUaSy0z7FomOv8dU4oSvf4L9r7Vg==".toCharArray();
    static String org = "hangzhou";
    static String bucket = "demo";

    public static void main(String[] args) {
        try (InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket)) {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();


            AtomicInteger lineIndex = new AtomicInteger(0);
            try (LineIterator it = FileUtils.lineIterator(new File("./log_metrics.log"), "UTF-8");){
                while (it.hasNext()) {
                    String line = it.nextLine();
                    // do something with line
                    writeApi.writeRecord(WritePrecision.MS, line);
                    System.out.println(lineIndex.incrementAndGet());
                }
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }


    }
}

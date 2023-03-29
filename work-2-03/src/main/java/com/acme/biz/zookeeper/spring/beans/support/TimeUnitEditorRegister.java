package com.acme.biz.zookeeper.spring.beans.support;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TimeUnitEditorRegister implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(TimeUnit.class, new TimeUnitPropertyEditor());
    }

    static class TimeUnitPropertyEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            var processedText = processText(text);

            Arrays.stream(TimeUnit.values())
                    .filter(value -> value.name().equals(processedText))
                    .findAny()
                    .ifPresentOrElse(this::setValue, () -> {
                    });
        }

        private static String processText(String text) {
            if (text.contains(".")) {
                return text.substring(text.lastIndexOf(".") + 1);
            } else {
                return text;
            }
        }
    }
}

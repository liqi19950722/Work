package com.acme.biz.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="bytebuddy")
public class ByteBuddyProperties {

    private Logging logging = new Logging();

    public Logging getLogging() {
        return logging;
    }

    public void setLogging(Logging logging) {
        this.logging = logging;
    }

    public static class Logging {

        private String service;
        private String controller;

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getController() {
            return controller;
        }

        public void setController(String controller) {
            this.controller = controller;
        }
    }
}

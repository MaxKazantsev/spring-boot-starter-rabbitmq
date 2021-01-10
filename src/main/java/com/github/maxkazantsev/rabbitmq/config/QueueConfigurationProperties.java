package com.github.maxkazantsev.rabbitmq.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Max Kazantsev
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "messaging")
public class QueueConfigurationProperties {

    private Payload payload;

    @Setter
    @Getter
    public static class Payload {
        private String targetPackage;
    }
}

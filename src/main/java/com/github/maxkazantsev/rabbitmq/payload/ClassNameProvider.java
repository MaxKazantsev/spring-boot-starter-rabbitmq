package com.github.maxkazantsev.rabbitmq.payload;

/**
 * @author Max Kazantsev
 */
public interface ClassNameProvider {

    String provide(String payloadClassName);
}

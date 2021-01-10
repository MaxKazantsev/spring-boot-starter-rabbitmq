package com.github.maxkazantsev.rabbitmq.exception.handler;

/**
 * @author Max Kazantsev
 */
public interface ListenerExceptionHandler {
    void handle(Throwable throwable);

    Class<? extends Throwable> getType();
}

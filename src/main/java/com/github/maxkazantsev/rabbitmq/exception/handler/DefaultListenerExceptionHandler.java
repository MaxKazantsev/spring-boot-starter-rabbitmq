package com.github.maxkazantsev.rabbitmq.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Max Kazantsev
 */
@Slf4j
@Component
public class DefaultListenerExceptionHandler implements ListenerExceptionHandler {
    @Override
    public void handle(Throwable throwable) {
        log.error("Listener threw unexpected error", throwable);
    }

    @Override
    public Class<Exception> getType() {
        return Exception.class;
    }
}

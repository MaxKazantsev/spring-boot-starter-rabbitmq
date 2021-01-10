package com.github.maxkazantsev.rabbitmq.exception;

import lombok.RequiredArgsConstructor;

/**
 * @author Max Kazantsev
 */
@RequiredArgsConstructor
public class CauseExceptionClassProvider {

    private final Throwable exception;

    public Class<? extends Throwable> getCauseExceptionClass() {
        Throwable cause = exception.getCause();
        return cause == null ? exception.getClass() :cause.getClass();
    }
}

package com.github.maxkazantsev.rabbitmq.exception;

import com.github.maxkazantsev.rabbitmq.exception.handler.ListenerExceptionHandler;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Max Kazantsev
 */
public class QueueExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

    private final Map<Class<? extends Throwable>, ListenerExceptionHandler> handlers;

    public QueueExceptionStrategy(List<ListenerExceptionHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(ListenerExceptionHandler::getType, Function.identity()));
    }

    @Override
    public boolean isFatal(Throwable t) {
        getHandler(t).handle(t);
        return super.isFatal(t);
    }

    private ListenerExceptionHandler getHandler(Throwable throwable) {
        CauseExceptionClassProvider provider = new CauseExceptionClassProvider(throwable);
        return handlers.getOrDefault(provider.getCauseExceptionClass(), handlers.get(Exception.class));
    }
}

package com.github.maxkazantsev.rabbitmq.payload;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * @author Max Kazantsev
 */
@RequiredArgsConstructor
public class MessageConversionPostProcessor implements MessagePostProcessor {

    private static final String TYPE_ID_HEADER =  "__TypeId__";
    private final ClassNameProvider classNameProvider;

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        String messageClassName = message.getMessageProperties().getHeader(TYPE_ID_HEADER).toString();

        message.getMessageProperties().setHeader(TYPE_ID_HEADER, classNameProvider.provide(messageClassName));

        return message;
    }
}

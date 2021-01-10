package com.github.maxkazantsev.rabbitmq.config;

import com.github.maxkazantsev.rabbitmq.exception.handler.ListenerExceptionHandler;
import com.github.maxkazantsev.rabbitmq.exception.QueueExceptionStrategy;
import com.github.maxkazantsev.rabbitmq.payload.ClassNameProvider;
import com.github.maxkazantsev.rabbitmq.payload.CorrespondingClassNameProvider;
import com.github.maxkazantsev.rabbitmq.payload.MessageConversionPostProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.validation.SmartValidator;

import java.util.List;


/**
 * @author Max Kazantsev
 */
@Configuration
@RequiredArgsConstructor
public class QueueConfig implements RabbitListenerConfigurer {

    private final SmartValidator smartValidator;

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory validatingHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setValidator(smartValidator);
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(validatingHandlerMethodFactory());
    }

    @ConditionalOnMissingBean(ClassNameProvider.class)
    @Bean
    public ClassNameProvider classNameProvider(QueueConfigurationProperties properties) {
        return new CorrespondingClassNameProvider(properties);
    }

    @Bean
    public MessagePostProcessor messagePostProcessor(ClassNameProvider provider) {
        return new MessageConversionPostProcessor(provider);
    }

    @Bean
    public QueueExceptionStrategy queueExceptionStrategy(List<ListenerExceptionHandler> handlers) {
        return new QueueExceptionStrategy(handlers);
    }

    @Bean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                 ConnectionFactory connectionFactory,
                                                                 MessagePostProcessor postProcessor,
                                                                 QueueExceptionStrategy strategy) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setErrorHandler(new ConditionalRejectingErrorHandler(strategy));
        factory.setAfterReceivePostProcessors(postProcessor);

        return factory;
    }
}

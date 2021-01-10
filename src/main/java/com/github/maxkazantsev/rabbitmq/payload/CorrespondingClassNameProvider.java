package com.github.maxkazantsev.rabbitmq.payload;

import com.github.maxkazantsev.rabbitmq.config.QueueConfigurationProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @author Max Kazantsev
 */
public class CorrespondingClassNameProvider implements ClassNameProvider {

    private final QueueConfigurationProperties properties;
    private final ClassPathScanningCandidateComponentProvider scanner;

    public CorrespondingClassNameProvider(QueueConfigurationProperties properties) {
        this.properties = properties;
        this.scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RabbitHandlerPayload.class));
    }

    @Override
    public String provide(String payloadClassName) {
        return getCorrespondingClassName(payloadClassName);
    }

    private String getCorrespondingClassName(String payloadClassName) {
        Set<BeanDefinition> beans = scanner.findCandidateComponents(properties.getPayload().getTargetPackage());
        return new PayloadClassDefinitionProvider(payloadClassName, beans)
                .getCorrespondingClassDefinition().getBeanClassName();
    }
}

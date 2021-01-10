package com.github.maxkazantsev.rabbitmq.payload;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author Max Kazantsev
 */
public class PayloadClassDefinitionProvider {

    private final String simplePayloadClassName;
    private final Set<BeanDefinition> beans;

    public PayloadClassDefinitionProvider(String payloadClassName, Set<BeanDefinition> beans) {
        this.simplePayloadClassName = getSimpleName(payloadClassName);
        this.beans = beans;
    }

    public BeanDefinition getCorrespondingClassDefinition() {
        List<BeanDefinition> filteredBeans = filterBeans();

        if (filteredBeans.isEmpty()) {
            throw new IllegalStateException("No corresponding class for message mapping found");
        }

        if (filteredBeans.size() > 1) {
            throw new IllegalStateException(format("Ambiguous payload class selection for name: %s", simplePayloadClassName));
        }

        return filteredBeans.get(0);
    }

    private List<BeanDefinition> filterBeans() {
        return beans.stream()
                .filter(bean -> getSimpleName(bean.getBeanClassName()).equals(simplePayloadClassName))
                .collect(Collectors.toList());
    }

    private String getSimpleName(String fullName) {
        if (fullName != null) {
            return fullName.substring(fullName.lastIndexOf('.') + 1);
        }
        return Strings.EMPTY;
    }
}

package com.kumuluz.ee.migrations.liquibase.cdi;

import com.kumuluz.ee.migrations.liquibase.LiquibaseContainerProducer;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.DeploymentException;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionPoint;

public class LiquibaseContainerCdiExtension implements Extension {


    public void validateInjectionPoints(@Observes ProcessInjectionPoint<?, ?> pip) {

        if (pip.getInjectionPoint().getBean().getBeanClass() == LiquibaseContainerProducer.class) {

            final LiquibaseConfigurationUtil configurationUtil = LiquibaseConfigurationUtil.getInstance();

            // In order to Inject Liquibase container, at least one liquibase configuration needs to be provided
            if (configurationUtil.getLiquibaseConfigs().size() == 0) {
                pip.addDefinitionError(new DeploymentException("Liquibase configuration needs to be provided in KumuluzEE config."));
                return;
            }

            LiquibaseChangelog liquibaseChangelog = pip.getInjectionPoint().getAnnotated().getAnnotation(LiquibaseChangelog.class);

            if (liquibaseChangelog == null || liquibaseChangelog.jndiName().equals("")) {

                // If no jndiName is specified and more than 1 configuration is preset than an error must be thrown
                if (configurationUtil.getLiquibaseConfigs().size() > 1) {
                    pip.addDefinitionError(new DeploymentException("Injection point '" + pip.getInjectionPoint() +
                            "' annotated with @LiquibaseChangelog has an empty jndiName, but there are " +
                            "multiple configurations provided."));
                }
            }

        }

    }

}

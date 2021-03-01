package com.kumuluz.ee.migrations.liquibase.cdi;

import com.kumuluz.ee.common.config.DataSourceConfig;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.migrations.liquibase.LiquibaseContainerProducer;
import com.kumuluz.ee.migrations.liquibase.LiquibaseExtension;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.DeploymentException;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import java.util.List;

public class LiquibaseCdiExtension implements Extension {

    public void validateConfigurations(@Observes BeforeBeanDiscovery event) {

        List<DataSourceConfig> dataSourceConfigs = EeConfig.getInstance().getDatasources();
        List<LiquibaseConfig> liquibaseConfigs = LiquibaseConfigurationUtil.getInstance().getLiquibaseConfigs();

        liquibaseConfigs.forEach(config -> dataSourceConfigs.stream()
                .filter(ds -> ds.getJndiName().equals(config.getJndiName()))
                .findFirst()
                .orElseThrow(() -> new DeploymentException("Liquibase configuration with jndi name '"
                        + config.getJndiName() + "' does not match any data source's jndi name."))
        );
    }

    public void validateInjectionPoints(@Observes ProcessInjectionPoint<?, ?> pip) {

        if (pip.getInjectionPoint().getBean().getBeanClass() == LiquibaseContainerProducer.class) {

            final LiquibaseConfigurationUtil liquibaseConfigurationUtil = LiquibaseConfigurationUtil.getInstance();

            // In order to inject Liquibase container, at least one liquibase configuration needs to be provided
            if (liquibaseConfigurationUtil.getLiquibaseConfigs().size() == 0) {
                pip.addDefinitionError(new DeploymentException("Liquibase configuration needs to be provided in KumuluzEE config."));
                return;
            }

            LiquibaseChangelog liquibaseChangelog = pip.getInjectionPoint().getAnnotated().getAnnotation(LiquibaseChangelog.class);

            if (liquibaseChangelog == null || liquibaseChangelog.jndiName().equals("")) {

                // If no jndiName is specified and more than 1 configuration is preset than an error must be thrown
                if (liquibaseConfigurationUtil.getLiquibaseConfigs().size() > 1) {
                    pip.addDefinitionError(new DeploymentException("Injection point '"
                            + pip.getInjectionPoint()
                            + "' annotated with @LiquibaseChangelog has an empty jndiName, but there are "
                            + "multiple configurations provided."));
                }

            } else {

                // Validate that referenced Liquibase changelog is present in KumuluzEE configuration file
                for (LiquibaseConfig config : liquibaseConfigurationUtil.getLiquibaseConfigs()) {
                    if (config.getJndiName().equals(liquibaseChangelog.jndiName())) {
                        return;
                    }
                }

                pip.addDefinitionError(new DeploymentException("No liquibase configurations has been found for jndi name '"
                        + liquibaseChangelog.jndiName() + "'."));
            }
        }
    }

}

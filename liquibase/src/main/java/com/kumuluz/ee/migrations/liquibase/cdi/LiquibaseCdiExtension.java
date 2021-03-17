package com.kumuluz.ee.migrations.liquibase.cdi;

import com.kumuluz.ee.common.config.DataSourceConfig;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.migrations.liquibase.LiquibaseContainerProducer;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.DeploymentException;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import java.util.List;
import java.util.logging.Logger;

/**
 * Validates injection points and Liquibase configuration.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseCdiExtension implements Extension {

    /**
     * Validates every LiquibaseContainer injection.
     *
     * @param pip - Observed ProcessInjectionPoint event
     */
    public void validateInjectionPoints(@Observes ProcessInjectionPoint<?, ?> pip) {

        if (pip.getInjectionPoint().getBean().getBeanClass().isInstance(LiquibaseContainerProducer.class)) {

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

    /**
     * Validates that each Liquibase configuration is referencing a configured data source
     * and that the provided changelog file exists.
     *
     * @param event - Observed AfterBeanDiscovery event
     */
    public void validateConfigurations(@Observes AfterBeanDiscovery event) {

        Logger.getLogger("").info("Validate configuration. Test123");

        List<DataSourceConfig> dataSourceConfigs = EeConfig.getInstance().getDatasources();
        List<LiquibaseConfig> liquibaseConfigs = LiquibaseConfigurationUtil.getInstance().getLiquibaseConfigs();

        for (LiquibaseConfig config : liquibaseConfigs) {

            DataSourceConfig dataSourceConfig = dataSourceConfigs.stream()
                    .filter(ds -> ds.getJndiName().equals(config.getJndiName()))
                    .findFirst()
                    .orElse(null);

            if (dataSourceConfig == null) {
                event.addDefinitionError(new DeploymentException("Liquibase configuration with jndi name '" + config.getJndiName()
                        + "' does not match any data source's jndi name."));
            }

            if (ClassLoader.getSystemClassLoader().getResource(config.getFile()) == null) {
                event.addDefinitionError(new DeploymentException("Liquibase changelog file '" + config.getFile()
                        + "' does not exist."));
            }
        }
    }

}

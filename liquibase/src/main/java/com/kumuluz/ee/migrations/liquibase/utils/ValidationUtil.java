package com.kumuluz.ee.migrations.liquibase.utils;

import com.kumuluz.ee.common.config.DataSourceConfig;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;

import javax.enterprise.inject.spi.DeploymentException;
import java.util.List;

public class ValidationUtil {

    public static void validateConfigurations() {

        List<DataSourceConfig> dataSourceConfigs = EeConfig.getInstance().getDatasources();
        List<LiquibaseConfig> liquibaseConfigs = LiquibaseConfigurationUtil.getInstance().getLiquibaseConfigs();

        for (LiquibaseConfig config : liquibaseConfigs) {

            DataSourceConfig dataSourceConfig = dataSourceConfigs.stream()
                    .filter(ds -> ds.getJndiName().equals(config.getJndiName()))
                    .findFirst()
                    .orElse(null);

            if (dataSourceConfig == null) {
                throw new DeploymentException("Liquibase configuration with jndi name '" + config.getJndiName()
                        + "' does not match any data source's jndi name.");
            }
        }
    }

}

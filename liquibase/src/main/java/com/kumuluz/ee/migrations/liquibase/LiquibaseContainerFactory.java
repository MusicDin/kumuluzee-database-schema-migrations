package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.common.config.DataSourceConfig;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.exceptions.AmbiguousLiquibaseConfigurationException;
import com.kumuluz.ee.migrations.liquibase.exceptions.InvalidLiquibaseConfigurationException;
import com.kumuluz.ee.migrations.liquibase.exceptions.LiquibaseConfigurationNotFoundException;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;

import java.util.List;

/**
 * {@link LiquibaseContainer} factory.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseContainerFactory {

    private final String jndiName;

    public LiquibaseContainerFactory(String jndiName) {
        this.jndiName = jndiName;
    }

    /**
     * Creates new LiquibaseContainer based on provided JNDI name.
     *
     * @return Returns LiquibaseContainer object.
     */
    public LiquibaseContainer createLiquibaseContainer() {

        LiquibaseConfig liquibaseConfig = getLiquibaseConfig(jndiName);
        DataSourceConfig dataSourceConfig = getDataSourceConfig(liquibaseConfig.getJndiName());

        return new LiquibaseContainer(liquibaseConfig, dataSourceConfig);
    }

    /**
     * Gets Liquibase configuration.
     *
     * @param jndiName - Liquibase configuration JNDI name.
     * @return Function returns LiquibaseConfig based on provided JNDI name.
     * @throws AmbiguousLiquibaseConfigurationException - Exception is thrown when Liquibase configuration JNDI name
     *                                                  is not specified, but there are more than 1 configurations
     *                                                  available.
     * @throws LiquibaseConfigurationNotFoundException  - Exception is thrown when Liquibase configuration with provided
     *                                                  JNDI name could not be found.
     */
    private LiquibaseConfig getLiquibaseConfig(String jndiName) throws AmbiguousLiquibaseConfigurationException, InvalidLiquibaseConfigurationException {

        List<LiquibaseConfig> liquibaseConfigs = LiquibaseConfigurationUtil.getInstance().getLiquibaseConfigs();

        if (jndiName == null || jndiName.equals("")) {

            if (liquibaseConfigs.size() == 1) {
                return liquibaseConfigs.get(0);
            } else {
                throw new AmbiguousLiquibaseConfigurationException();
            }

        } else {

            return liquibaseConfigs
                    .stream()
                    .filter(config -> config.getJndiName().equals(jndiName))
                    .findFirst()
                    .orElseThrow(() -> new LiquibaseConfigurationNotFoundException(jndiName));
        }
    }

    /**
     * Gets data source configuration.
     *
     * @param jndiName - Data source configuration JNDI name.
     * @return Returns DataSourceConfig based on provided JNDI name.
     * @throws InvalidLiquibaseConfigurationException - Exception is thrown when no data source configuration matches
     *                                                provided JNDI name.
     */
    private DataSourceConfig getDataSourceConfig(String jndiName) throws InvalidLiquibaseConfigurationException {

        List<DataSourceConfig> dataSourceConfigs = EeConfig.getInstance().getDatasources();

        return dataSourceConfigs
                .stream()
                .filter(ds -> ds.getJndiName().equals(jndiName))
                .findFirst()
                .orElseThrow(() -> new InvalidLiquibaseConfigurationException(jndiName));
    }
}

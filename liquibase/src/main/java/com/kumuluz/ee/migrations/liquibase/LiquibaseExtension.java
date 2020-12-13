package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.EeExtensionDef;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.migrations.MigrationUtil;
import com.kumuluz.ee.migrations.liquibase.configurations.KeeDatasourceConfiguration;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseDatasourceConfiguration;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseMigrationUtil;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@EeExtensionDef(name = "Liquibase", group = "migrations")
public class LiquibaseExtension implements Extension {

    private Logger LOG = Logger.getLogger(LiquibaseExtension.class.getName());

    public static final String MIGRATIONS_CONFIG_PREFIX  = "kumuluzee.migrations";
    public static final String LIQUIBASE_CONFIG_PREFIX   = "kumuluzee.migrations.liquibase";
    public static final String DATASOURCES_CONFIG_PREFIX = "kumuluzee.datasources";

    @Override
    public void load() {

    }

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {

        MigrationUtil migrationUtil = new LiquibaseMigrationUtil();

        List<LiquibaseDatasourceConfiguration> liquibaseDatasources = LiquibaseConfigurationUtil.getLiquibaseDatasourceConfigurations();

        // Exit if no datasource is provided
        if(liquibaseDatasources.isEmpty()) {
            return;
        }

        for(LiquibaseDatasourceConfiguration liquibaseDS : liquibaseDatasources) {

            if(liquibaseDS.jndiName.isEmpty() || liquibaseDS.changelog.isEmpty()) {
                continue;
            }

            Optional<KeeDatasourceConfiguration> keeDS = LiquibaseConfigurationUtil.getDatasourceConfiguration(liquibaseDS.jndiName.get());
            if(keeDS.isPresent()){

                // startup dropAll
                if(liquibaseDS.startupDropAll) {
                    migrationUtil.dropAll(
                            keeDS.get().connectionUrl,
                            keeDS.get().username,
                            keeDS.get().password,
                            liquibaseDS.changelog.get());
                }

                // startup update
                if(liquibaseDS.startupUpdate) {
                    migrationUtil.update(
                            keeDS.get().connectionUrl,
                            keeDS.get().username,
                            keeDS.get().password,
                            liquibaseDS.changelog.get());
                }
            }
        }

    }

    @Override
    public boolean isEnabled() {
        return ConfigurationUtil.getInstance().getBoolean(MIGRATIONS_CONFIG_PREFIX + "." + "enabled").orElse(true);
    }

}

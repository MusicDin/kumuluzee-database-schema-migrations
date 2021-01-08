package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.EeExtensionDef;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.migrations.MigrationUtil;
import com.kumuluz.ee.migrations.liquibase.configurations.KeeDatasourceConfiguration;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseChangelogConfiguration;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseMigrationUtil;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@EeExtensionDef(name = "Liquibase", group = "migrations")
public class LiquibaseExtension implements Extension {

    public static final String MIGRATIONS_CONFIG_PREFIX  = "kumuluzee.migrations";

    @Override
    public void load() {

    }

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {

        MigrationUtil migrationUtil = new LiquibaseMigrationUtil();

        List<LiquibaseChangelogConfiguration> liquibaseChangelogs = LiquibaseConfigurationUtil.getLiquibaseChangelogConfigurations();

        // Exit if no datasource is provided
        if(liquibaseChangelogs.isEmpty()) {
            return;
        }

        for(LiquibaseChangelogConfiguration liquibaseChangelog : liquibaseChangelogs) {

            if(liquibaseChangelog.jndiName.isEmpty() || liquibaseChangelog.file.isEmpty()) {
                continue;
            }

            Optional<KeeDatasourceConfiguration> keeDS = LiquibaseConfigurationUtil.getDatasourceConfiguration(liquibaseChangelog.jndiName.get());
            if(keeDS.isPresent()){

                // startup dropAll
                if(liquibaseChangelog.startupDropAll) {
                    migrationUtil.dropAll(
                            keeDS.get().connectionUrl,
                            keeDS.get().username,
                            keeDS.get().password,
                            liquibaseChangelog.file.get());
                }

                // startup update
                if(liquibaseChangelog.startupUpdate) {
                    migrationUtil.update(
                            keeDS.get().connectionUrl,
                            keeDS.get().username,
                            keeDS.get().password,
                            liquibaseChangelog.file.get());
                }

            }
        }

    }

    @Override
    public boolean isEnabled() {
        return ConfigurationUtil.getInstance().getBoolean(MIGRATIONS_CONFIG_PREFIX + ".enabled").orElse(true);
    }

}

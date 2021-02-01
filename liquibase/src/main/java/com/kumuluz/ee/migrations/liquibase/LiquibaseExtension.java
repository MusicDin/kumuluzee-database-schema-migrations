package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.EeExtensionDef;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.migrations.MigrationUtil;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseMigrationUtil;

@EeExtensionDef(name = "Liquibase", group = "migrations")
public class LiquibaseExtension implements Extension {

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {
        MigrationUtil migrationUtil = new LiquibaseMigrationUtil();
        migrationUtil.migrate();
    }

    @Override
    public void load() {
        // No actions to be done on load
    }

    @Override
    public boolean isEnabled() {
        return ConfigurationUtil.getInstance().getBoolean("kumuluzee.migrations.enabled").orElse(true);
    }

}

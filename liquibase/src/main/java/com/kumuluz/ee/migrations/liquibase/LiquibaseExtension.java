package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.EeExtensionDef;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

/**
 * KumuluzEE framework extension for Liquibase.
 *
 * @author Din Music
 * @since 1.0.0
 */
@EeExtensionDef(name = "Liquibase", group = "migrations")
public class LiquibaseExtension implements Extension {

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {
        // Nothing to be done at initialization
    }

    @Override
    public void load() {
        // Nothing to be done in load phase
    }

    @Override
    public boolean isEnabled() {
        return ConfigurationUtil.getInstance().getBoolean("kumuluzee.migrations.enabled").orElse(true);
    }

}

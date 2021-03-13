package com.kumuluz.ee.migrations.liquibase.cdi;

import com.kumuluz.ee.migrations.MigrationUtil;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseMigrationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

/**
 * Migration initializer.
 *
 * @author Din Music
 * @since 1.0.0
 */
@ApplicationScoped
public class MigrationInitializer {

    /**
     * Triggers Liquibase migration when application scope is initialized.
     *
     * @param init - Initialized application scope object.
     */
    public void migrate(@Observes @Initialized(ApplicationScoped.class) Object init) {
        MigrationUtil migrationUtil = new LiquibaseMigrationUtil();
        migrationUtil.migrate();
    }

}

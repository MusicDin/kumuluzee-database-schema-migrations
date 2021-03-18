package com.kumuluz.ee.migrations.liquibase.tests.beans;

import com.kumuluz.ee.migrations.liquibase.LiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import liquibase.Liquibase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Bean containing injected {@link LiquibaseContainer} with empty {@link LiquibaseChangelog} annotation.
 *
 * @author Din Music
 * @since 1.0.0
 */
@ApplicationScoped
public class EmptyAnnotatedLiquibaseContainer {

    @Inject
    @LiquibaseChangelog
    private LiquibaseContainer liquibaseContainer;

    public Liquibase getLiquibase() {
        return liquibaseContainer.createLiquibase();
    }
}

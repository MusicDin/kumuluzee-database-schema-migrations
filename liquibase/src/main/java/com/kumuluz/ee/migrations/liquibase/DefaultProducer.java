package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.migrations.MigrationUtil;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseMigrationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class DefaultProducer {

    @Produces
    @ApplicationScoped
    public MigrationUtil getMigrationUtil(){
        return new LiquibaseMigrationUtil();
    }

}

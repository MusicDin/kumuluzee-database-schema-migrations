package com.kumuluz.ee.migrations.liquibase.utils;

import com.kumuluz.ee.migrations.MigrationUtil;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseMigrationUtil extends MigrationUtil {

    public LiquibaseMigrationUtil() {
        super();
    }

    // TODO: Add support for FileSystemResourceAccessor if data is not accessible via ClassLoaderResourceAccessor

    @Override
    public void update(String url, String username, String password, String changelogFile) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
            liquibase.validate();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropAll(String url, String username, String password, String changelogFile) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.dropAll();
            liquibase.validate();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

}

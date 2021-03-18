package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.common.config.EeConfig;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Tests if migration util migrates database schema at application startup.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class StartupMigrationTest extends Arquillian {

    public static final String SELECT_ALL = "SELECT * FROM TEST_TABLE";

    @Deployment
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsResource("startup-migration-config.yml", "config.yml")
                .addAsResource("test-changelog.xml", "db/changelog.xml");
    }

    @Test
    public void configurationUtilTest() throws SQLException {

        Statement stmt = DriverManager
                .getConnection(EeConfig.getInstance().getDatasources().get(0).getConnectionUrl())
                .createStatement();

        ResultSet rs = stmt.executeQuery(SELECT_ALL);

        while (rs.next()) {
            Assert.assertEquals("123456789", rs.getString(1));
            Assert.assertEquals("This is test field.", rs.getString(2));
        }
    }

}

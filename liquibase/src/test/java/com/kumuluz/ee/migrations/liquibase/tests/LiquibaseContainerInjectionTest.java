package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.migrations.liquibase.LiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.LiquibaseContainerProducer;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Tests {@link com.kumuluz.ee.migrations.liquibase.LiquibaseContainer} injection.
 *
 * @author Din Music
 * @since 1.0.0
 */
@RunWith(Arquillian.class)
public class LiquibaseContainerInjectionTest {

    @Deployment
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(LiquibaseConfigurationUtil.class)
                .addClass(LiquibaseConfig.class)
                .addClass(LiquibaseContainer.class)
                .addClass(LiquibaseContainerProducer.class)
                .addClass(LiquibaseChangelog.class)
                .addAsResource("correct-config.yml", "config.yml")
                .addAsResource("test-changelog.xml", "db/test-changelog.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    @LiquibaseChangelog(jndiName = "jdbc/TestDS")
    private LiquibaseContainer annotatedLiquibaseContainer;

    @Test
    public void annotatedContainerTest() {

        Liquibase liquibase = annotatedLiquibaseContainer.createLiquibase();

        try {
            liquibase.update("");

        } catch (LiquibaseException e) {
            Assert.fail("Liquibase update failed!");
        }

        try {
            liquibase.dropAll();

        } catch (LiquibaseException e) {
            Assert.fail("Liquibase dropAll failed!");
        }
    }

}

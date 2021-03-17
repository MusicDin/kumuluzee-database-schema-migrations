package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.migrations.liquibase.LiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.LiquibaseContainerProducer;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import com.kumuluz.ee.migrations.liquibase.cdi.LiquibaseCdiExtension;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.annotations.Test;

import javax.enterprise.inject.spi.DefinitionException;

/**
 * Tests if invalid Liquibase changelog file is recognized at deployment.
 *
 * @author Din Music
 * @since 1.0.0
 */
@Test
public class InvalidChangelogFileTest extends Arquillian {

    @Deployment
    @ShouldThrowException(value = DefinitionException.class, testable = true)
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(LiquibaseCdiExtension.class)
                .addClass(LiquibaseContainerProducer.class)
                .addClass(LiquibaseContainer.class)
                .addClass(LiquibaseConfigurationUtil.class)
                .addClass(LiquibaseConfig.class)
                .addClass(LiquibaseChangelog.class)
                .addAsResource("invalid-changelog-config.yml", "config.yml");
    }

    public void invalidChangelogTest() {
        // See @ShouldThrowException in deployment
    }

}

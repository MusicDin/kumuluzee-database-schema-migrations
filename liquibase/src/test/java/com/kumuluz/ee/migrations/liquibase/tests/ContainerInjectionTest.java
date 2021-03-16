package com.kumuluz.ee.migrations.liquibase.tests;

import com.beust.jcommander.ParameterException;
import com.kumuluz.ee.migrations.liquibase.LiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.LiquibaseContainerProducer;
import com.kumuluz.ee.migrations.liquibase.LiquibaseExtension;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import liquibase.CatalogAndSchema;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

/**
 * Tests {@link com.kumuluz.ee.migrations.liquibase.LiquibaseContainer} injection.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class ContainerInjectionTest extends Arquillian {

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
    private LiquibaseContainer unannotatedLiquibaseContainer;

    @Inject
    @LiquibaseChangelog
    private LiquibaseContainer annotatedLiquibaseContainerWithNoParameter;

    @Inject
    @LiquibaseChangelog(jndiName = "jdbc/TestDS")
    private LiquibaseContainer annotatedLiquibaseContainer;

    @Test
    public void unannotatedContainerNoExceptionTest() throws LiquibaseException {

        Liquibase liquibase = unannotatedLiquibaseContainer.createLiquibase();

        Assert.assertNotNull(liquibase);

        Contexts initContext = new Contexts("init");
        liquibase.update(initContext);
        liquibase.dropAll();
    }

    @Test
    public void annotatedContainerWithNoParameterNoExceptionTest() throws LiquibaseException {

        Liquibase liquibase = annotatedLiquibaseContainerWithNoParameter.createLiquibase();

        Assert.assertNotNull(liquibase);

        Contexts initContext = new Contexts("init");
        liquibase.update(initContext);
        liquibase.dropAll();
    }

    @Test
    public void annotatedContainerNoExceptionTest() throws LiquibaseException {

        Liquibase liquibase = annotatedLiquibaseContainer.createLiquibase();

        Assert.assertNotNull(liquibase);

        Contexts initContext = new Contexts("init");
        liquibase.update(initContext);
        liquibase.dropAll();
    }

    @Test(expectedExceptions = LiquibaseException.class)
    public void noTableExceptionTest() throws LiquibaseException {

        System.out.println("Executing test...");

        Liquibase liquibase = annotatedLiquibaseContainer.createLiquibase();

        Assert.assertNotNull(liquibase);

        Contexts errorContext = new Contexts("error");
        System.out.println("Dropping...");
        liquibase.dropAll();
        System.out.println("All dropped.");
        System.out.println("Updating...");
        liquibase.update(errorContext);
        System.out.println("Updated.");

        System.out.println("Test executed");
    }

}

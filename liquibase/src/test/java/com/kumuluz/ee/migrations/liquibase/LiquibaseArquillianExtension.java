package com.kumuluz.ee.migrations.liquibase;

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * Registers {@link LiquibaseLibraryAppender} with the Arquillian.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseArquillianExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        extensionBuilder.service(AuxiliaryArchiveAppender.class, LiquibaseLibraryAppender.class);
    }

}

package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.testing.arquillian.spi.MavenDependencyAppender;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Adds required dependencies to the deployments.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseDependencyAppender implements MavenDependencyAppender {

    private static final ResourceBundle versionsBundle = ResourceBundle.getBundle("META-INF/kumuluzee/migrations/liquibase/versions");

    @Override
    public List<String> addLibraries() {

        List<String> libs = new ArrayList<>();
        libs.add("com.h2database:h2:" + versionsBundle.getString("h2-version"));
        libs.add("org.liquibase:liquibase-core:" + versionsBundle.getString("liquibase-core-version"));

        return libs;
    }

}

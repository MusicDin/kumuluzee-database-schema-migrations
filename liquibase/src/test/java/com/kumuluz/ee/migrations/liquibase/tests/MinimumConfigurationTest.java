/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Tests minimum configuration containing default values.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class MinimumConfigurationTest extends Arquillian {

    @Deployment
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsResource("minimum-config.yml", "config.yml")
                .addAsResource("test-changelog.xml", "db/changelog-master.xml");
    }

    public static final String[] emptyArray = new String[0];

    @Test
    public void minimumConfigurationTest() {

        LiquibaseConfigurationUtil configurationUtil = LiquibaseConfigurationUtil.getInstance();
        LiquibaseConfig config = configurationUtil.getLiquibaseConfigs().get(0);

        Assert.assertEquals("jdbc/TestDS", config.getJndiName());
        Assert.assertEquals("db/changelog-master.xml", config.getFile());
        Assert.assertEquals(emptyArray, config.getContexts().toArray());
        Assert.assertEquals(emptyArray, config.getLabels().toArray());
        Assert.assertFalse(config.isStartupDropAll());
        Assert.assertFalse(config.isStartupUpdate());
    }

}

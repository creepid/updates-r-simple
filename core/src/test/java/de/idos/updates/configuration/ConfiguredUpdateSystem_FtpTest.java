/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.configuration;

import de.idos.updates.NumericVersion;
import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Updater;
import de.idos.updates.Version;
import de.idos.updates.server.FtpServer;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rusakovich
 */
public class ConfiguredUpdateSystem_FtpTest {

    private FtpServer ftpServer;

    private File configuration;
    Properties properties = new Properties();

    @Before
    public void startServer() throws Exception {
        ftpServer = new FtpServer();
        ftpServer.start();
    }

    @Before
    public void createConfiguration() throws Exception {
        configuration = new File(".", "update.properties");
        properties.put("update.applicationName", "updateunittest");
        properties.put("update.LatestVersion.repository.type", "FTP");
        properties.put("update.LatestVersion.repository.location", "localhost");
        properties.put("update.strategy", "LatestVersion");
        properties.store(new FileOutputStream(configuration), "");
    }

    @After
    public void stopServer() throws Exception {
        ftpServer.stop();
    }

    @After
    public void deleteConfiguration() throws Exception {
        FileUtils.deleteQuietly(configuration);
    }

    @After
    public void deleteInstalledUpdates() throws Exception {
        String userHome = System.getProperty("user.home");
        FileUtils.deleteQuietly(new File(userHome, ".updateunittest"));
    }

    @Test
    public void usesConfiguredHttpRepository() throws Exception {
        System.out.println("****** usesConfiguredHttpRepository ******");
        UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
        Updater updater = getUpdaterThatHasRun(updateSystem);
        Version latestVersion = updater.getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(5, 0, 4))));
    }

    @Test
    public void storesInAppNameFolder() throws Exception {
        System.out.println("****** storesInAppNameFolder ******");
        UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
        getUpdaterThatHasRun(updateSystem).updateToLatestVersion();
        String userHome = System.getProperty("user.home");
        assertThat(new File(userHome, ".updateunittest/versions").exists(), is(true));
    }

    private Updater getUpdaterThatHasRun(UpdateSystem updateSystem) {
        Updater updater = updateSystem.checkForUpdates();
        updater.runCheck();
        return updater;
    }

}

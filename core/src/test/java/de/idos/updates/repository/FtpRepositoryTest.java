/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.repository;

import de.idos.updates.NumericVersion;
import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import de.idos.updates.Version;
import de.idos.updates.install.InstallationStrategy;
import de.idos.updates.server.FtpServer;
import de.idos.updates.store.FilesystemInstallationStarter;
import de.idos.updates.store.FilesystemVersionStore;
import de.idos.updates.store.FtpFileDataInVersion;
import de.idos.updates.store.Installation;
import java.io.File;
import org.apache.commons.net.ftp.FTPFile;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author rusakovich
 */
public class FtpRepositoryTest {

    private static FtpServer ftpServer;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    FtpRepository repository = new FtpRepository("localhost");

    @BeforeClass
    public static void setUp() throws Exception {
        ftpServer = new FtpServer();
        ftpServer.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ftpServer.stop();
    }

    @Test
    public void retrievesLatestVersionFromServer() throws Exception {
        Version latestVersion = repository.getLatestVersion();
        assertThat(latestVersion, is(sameVersionAs(new NumericVersion(5, 0, 4))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptBadUrls() throws Exception {
        new FtpRepository("xx");
    }

    @Test
    public void createsHttpInstaller() throws Exception {
        Installation installation = mock(Installation.class);
        InstallationStrategy<FTPFile> strategy = repository.createInstallationStrategy(installation);
        Version mock = mock(Version.class);
        FTPFile ftpFile = new FTPFile();
        ftpFile.setName("X");
        strategy.installElement(ftpFile, mock);
        verify(installation).addContent(isA(FtpFileDataInVersion.class));
    }

    @Test
    public void worksIfNoReporterIsRegistered() throws Exception {
        repository.transferVersionTo(
                new NumericVersion(5, 0, 4),
                new FilesystemVersionStore(folder.getRoot(), new FilesystemInstallationStarter()));

        assertThat(new File(folder.getRoot(), "5.0.4").exists(), is(true));
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;
import java.net.InetAddress;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author rusakovich
 */
public class FtpInstallerTest {

    private static final String DEFAULT_LOGIN = "anonymous";
    private static final String DEFAULT_WORKING_DIR = "/availableVersions";

    Version version = mock(Version.class);
    ProgressReport report = mock(ProgressReport.class);
    Installation installation = mock(Installation.class);
    FtpFileInstaller ftpInstaller;

    @Before
    public void setUp() throws Exception {
        InetAddress address = InetAddress.getByName("www.topby.by");
        ftpInstaller = new FtpFileInstaller(report, address, installation, DEFAULT_LOGIN, DEFAULT_WORKING_DIR);
    }

    @Test
    public void reportsInstallation() throws Exception {
        FTPFile ftpFile = new FTPFile();
        ftpFile.setName("name");
        ftpInstaller.installElement(ftpFile, version);
        verify(report).installingFile(ftpFile.getName());
    }

    @Test
    public void forwardsFinalizationCallToInstallationInstance() throws Exception {
        ftpInstaller.finalizeInstallation();
        verify(installation).finish();
    }

}

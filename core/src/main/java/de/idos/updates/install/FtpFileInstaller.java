/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.install;

import de.idos.updates.Version;
import de.idos.updates.store.DataImport;
import de.idos.updates.store.FtpFileDataInVersion;
import de.idos.updates.store.Installation;
import de.idos.updates.store.ProgressReport;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author rusakovich
 */
public class FtpFileInstaller implements InstallationStrategy<FTPFile> {

    private final String workingDir;
    private final String login;

    private ProgressReport report;
    private FTPClient ftpClient;
    private Installation installation;

    private void connect(InetAddress inetAddress) {
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(inetAddress);
            ftpClient.enterLocalPassiveMode();

            if (login != null) {
                ftpClient.login(login, null);
            }

            if (workingDir != null) {
                ftpClient.changeWorkingDirectory(workingDir);
            }

        } catch (IOException ex) {
            report.versionLookupFailed(ex);
            ftpClient = null;
        }
    }

    public FtpFileInstaller(ProgressReport report, InetAddress inetAddress, Installation installation,
            String login, String workingDir) {
        this.report = report;
        this.installation = installation;
        this.login = login;
        this.workingDir = workingDir;
        connect(inetAddress);
    }

    public FtpFileInstaller(ProgressReport report, InetAddress inetAddress, Installation installation) {
        this(report, inetAddress, installation, null, null);
    }

    @Override
    public List<FTPFile> findAllElementsToInstall(Version version) throws IOException {
        if (ftpClient == null) {
            return Collections.EMPTY_LIST;
        }
        ftpClient.changeWorkingDirectory(version.asString());
        FTPFile[] ftpFiles = ftpClient.listFiles();
        ftpClient.changeToParentDirectory();
        return Arrays.asList(ftpFiles);
    }

    @Override
    public void installElement(FTPFile element, Version version) throws IOException {
        if (ftpClient == null) {
            return;
        }
        ftpClient.changeWorkingDirectory(version.asString());
        report.installingFile(element.getName());
        DataImport dataImport = new DataImport().reportProgressTo(report);
        FtpFileDataInVersion dataInVersion = new FtpFileDataInVersion(ftpClient, element, dataImport);
        installation.addContent(dataInVersion);
        ftpClient.changeToParentDirectory();
    }

    @Override
    public void handleException() {
        installation.abort();
    }

    @Override
    public void finalizeInstallation() {
        installation.finish();
    }

}

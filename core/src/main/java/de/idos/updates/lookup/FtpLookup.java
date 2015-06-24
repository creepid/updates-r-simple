/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.lookup;

import de.idos.updates.Update;
import de.idos.updates.UpdateDescription;
import de.idos.updates.Version;
import de.idos.updates.VersionFactory;
import de.idos.updates.VersionFinder;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author rusakovich
 */
public class FtpLookup implements LookupStrategy {

    private final String workingDir;
    private final String login;

    private InetAddress inetAddress;

    public FtpLookup(InetAddress inetAddress) {
        this(inetAddress, null, null);
    }

    public FtpLookup(InetAddress inetAddress, String login, String workingDir) {
        this.inetAddress = inetAddress;
        this.login = login;
        this.workingDir = workingDir;

    }

    @Override
    public Update findLatestUpdate() throws IOException {
        Version latestVersion = findLatestVersion();
        return new UpdateDescription(latestVersion);
    }

    private Version findLatestVersion() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(inetAddress);
        ftpClient.enterLocalPassiveMode();

        if (login != null) {
            ftpClient.login(login, null);
        }

        if (workingDir != null) {
            ftpClient.changeWorkingDirectory(workingDir);
        }

        FTPFile[] availableVersionsDirs = ftpClient.listDirectories();
        List<String> strings = new ArrayList<String>();
        for (FTPFile ftpFile : availableVersionsDirs) {
            strings.add(ftpFile.getName());
        }

        List<Version> versions = new VersionFactory().createVersionsFromStrings(strings);
        return new VersionFinder().findLatestVersion(versions);
    }

}

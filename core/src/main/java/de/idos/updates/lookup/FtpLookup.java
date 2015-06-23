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

    private static final String DEFAULT_LOGIN = "anonymous";
    private String login = DEFAULT_LOGIN;

    private InetAddress inetAddress;

    public FtpLookup(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
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

        ftpClient.login(login, null);
        
        ftpClient.changeWorkingDirectory("/availableVersions");

        FTPFile[] availableVersionsDirs = ftpClient.listDirectories();
        List<String> strings = new ArrayList<String>();
        for (FTPFile ftpFile : availableVersionsDirs) {
            strings.add(ftpFile.getName());
        }

        List<Version> versions = new VersionFactory().createVersionsFromStrings(strings);
        return new VersionFinder().findLatestVersion(versions);
    }

    public void setLogin(String login) {
        this.login = login;
    }

}

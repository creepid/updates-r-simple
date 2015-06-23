/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.repository;

import de.idos.updates.install.FtpFileInstaller;
import de.idos.updates.install.InstallationStrategy;
import de.idos.updates.lookup.FtpLookup;
import de.idos.updates.lookup.LookupStrategy;
import de.idos.updates.store.Installation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author rusakovich
 */
public class FtpRepository extends AbstractRepository<FTPFile> {

    private InetAddress inetAddress;

    public FtpRepository(String address) {
        try {
            this.inetAddress = InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException("Please specify a valid inet address for your FTP repository", ex);
        }
    }

    @Override
    protected InstallationStrategy<FTPFile> createInstallationStrategy(Installation installation) {
        return new FtpFileInstaller(getReport(), inetAddress, installation);
    }

    @Override
    protected LookupStrategy createLookup() {
        return new FtpLookup(inetAddress);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.store;

import java.io.File;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author rusakovich
 */
public class FtpFileDataInVersion implements DataInVersion {

    private FTPFile ftpFile;
    private DataImport dataImport;
    private FTPClient ftpClient;

    public FtpFileDataInVersion(FTPClient ftpClient, FTPFile ftpFile) {
        this(ftpClient, ftpFile, new DataImport());
    }

    public FtpFileDataInVersion(FTPClient ftpClient, FTPFile ftpFile, DataImport dataImport) {
        this.ftpClient = ftpClient;
        this.ftpFile = ftpFile;
        this.dataImport = dataImport;
    }

    @Override
    public void storeIn(File versionFolder) {
        InputStreamFactory factory = new FtpFileStreamFactory(ftpClient, ftpFile);
        dataImport.takeDataFromFactory(factory).andStoreThemIn(versionFolder, ftpFile.getName());
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.store;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author rusakovich
 */
public class FtpFileStreamFactory implements InputStreamFactory {

    private FTPFile ftpFile;
    private FTPClient ftpClient;

    public FtpFileStreamFactory(FTPClient ftpClient, FTPFile ftpFile) {
        this.ftpClient = ftpClient;
        this.ftpFile = ftpFile;
    }

    @Override
    public InputStream openStream() throws IOException {
        return ftpClient.retrieveFileStream(ftpFile.getName());
    }

    @Override
    public long getExpectedSize() throws IOException {
        return getFileSize();
    }

    private long getFileSize() throws IOException {
        return ftpFile.getSize();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.store;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author rusakovich
 */
public class FtpFileStreamFactoryTest {

    @Test
    public void reportsContentLengthAsSize() throws Exception {
        FTPClient ftpClient = new FTPClient();
        FTPFile ftpFile = new FTPFile();
        ftpFile.setSize(4000);

        long expectedSize = new FtpFileStreamFactory(ftpClient, ftpFile).getExpectedSize();
        assertThat(expectedSize, is(4000L));
    }

}

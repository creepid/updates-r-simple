/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.store;

import java.net.InetAddress;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 *
 * @author rusakovich
 */
public class FtpFileDataInVersionTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void addsToGivenImport() throws Exception {
        FTPFile ftpFile = new FTPFile();
        ftpFile.setName("ftpFileName");

        DataImport dataImport = mock(DataImport.class);
        when(dataImport.takeDataFromFactory(any(InputStreamFactory.class))).thenReturn(dataImport);
        
        FTPClient ftpClient = mock(FTPClient.class);
        
        new FtpFileDataInVersion(ftpClient, ftpFile, dataImport).storeIn(folder.getRoot());
        
        verify(dataImport).takeDataFromFactory(isA(FtpFileStreamFactory.class));
        verify(dataImport).andStoreThemIn(folder.getRoot(), "ftpFileName");
    }

}

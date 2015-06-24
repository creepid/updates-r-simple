/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.lookup;

import de.idos.updates.NumericVersion;
import static de.idos.updates.NumericVersionMatchers.sameVersionAs;
import de.idos.updates.Update;
import de.idos.updates.UpdateAvailability;
import de.idos.updates.server.FtpServer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rusakovich
 */
public class FtpLookupTest {

    private static final String DEFAULT_LOGIN = "anonymous";
    private static final String DEFAULT_WORKING_DIR = "/availableVersions";

    private static FtpServer ftpServer;

    @BeforeClass
    public static void setUp() throws Exception {
        ftpServer = new FtpServer();
        ftpServer.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ftpServer.stop();
    }

    FtpLookup lookup;

    @Before
    public void createLookup() throws UnknownHostException {
        lookup = new FtpLookup(InetAddress.getByName("localhost"), DEFAULT_LOGIN, DEFAULT_WORKING_DIR);
    }

    @Test
    public void returnsLatestUpdate() throws Exception {
        Update latest = lookup.findLatestUpdate();
        assertThat(latest.isUpdateFrom(new NumericVersion(5, 0, 3)), is(UpdateAvailability.Available));
    }

    @Test
    public void returnsLatestUpdate2() throws Exception {
        Update latest = lookup.findLatestUpdate();
        assertThat(latest.isUpdateFrom(new NumericVersion(5, 0, 4)), is(UpdateAvailability.NotAvailable));
    }

    @Test
    public void returnsExpectedUpdateVersion() throws Exception {
        Update latest = lookup.findLatestUpdate();
        assertThat(latest.getVersion(), is(sameVersionAs(new NumericVersion(5, 0, 4))));
    }

}

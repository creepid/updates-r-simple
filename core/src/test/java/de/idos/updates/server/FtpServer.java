/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.idos.updates.server;

import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;

/**
 *
 * @author rusakovich
 */
public class FtpServer {

    private static final int SERVER_PORT = 21;
    static final String ANONYMOUS = "anonymous";
    static final String HOME = "/";

    private FakeFtpServer fakeFtpServer = new FakeFtpServer();

    private FileSystem createContext() {
        FileSystem fileSystem = new UnixFakeFileSystem();

        fileSystem.add(new FileEntry(HOME + "availableVersions/4.2.1/content", "./fileToUpdate"));
        fileSystem.add(new FileEntry(HOME + "availableVersions/4.2.1/fileToUpdate", "Hullo, Update 4.2.1"));
        fileSystem.add(new FileEntry(HOME + "availableVersions/5.0.4/anotherFileToUpdate", "Hullo, World"));
        fileSystem.add(new FileEntry(HOME + "availableVersions/5.0.4/content", "fileToUpdate\n" + "anotherFileToUpdate"));
        fileSystem.add(new FileEntry(HOME + "availableVersions/5.0.4/fileToUpdate", "Hullo, Update 5.0.4"));

        return fileSystem;
    }

    public void start() {
        fakeFtpServer.setServerControlPort(SERVER_PORT);

        UserAccount anonymous = new UserAccount();
        anonymous.setUsername(ANONYMOUS);
        anonymous.setHomeDirectory(HOME);
        anonymous.setPasswordRequiredForLogin(false);
        
        fakeFtpServer.addUserAccount(anonymous);
        
        fakeFtpServer.setFileSystem(createContext());
        fakeFtpServer.start();
    }

    public void stop() {
        fakeFtpServer.stop();
    }

    public static void main(String[] args) {
        new FtpServer().start();
    }

}

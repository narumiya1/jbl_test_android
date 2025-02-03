package com.example.jbl.tools;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;

import java.io.File;

public class MyFtpServer implements AutoCloseable {
  private FtpServer ftpServer;

  public MyFtpServer(Context context) {
    FtpServerFactory serverFactory = new FtpServerFactory();
    ListenerFactory factory = new ListenerFactory();

    // set the port of the listener
    factory.setPort(2121);

    // replace the default listener
    serverFactory.addListener("default", factory.createListener());

    BaseUser baseUser = new BaseUser();
    baseUser.setName("tbn");
    baseUser.setPassword("3resi");
    File sdcard = Environment.getExternalStorageDirectory();
    File file = new File(sdcard, "/tbn/");
    if (!file.isDirectory()) {
      if (file.mkdirs()) Log.d("FTP", "Success create ftp home directory");
      else Log.d("FTP", "Extract string resource");
    }
    String path = sdcard.getPath();
    baseUser.setHomeDirectory(path);
    try {
      serverFactory.getUserManager().save(baseUser);
    } catch (
        FtpException e) {
      throw new RuntimeException(e);
    }

    // start the server
    FtpServer server = serverFactory.createServer();
    try {
      server.start();
    } catch (FtpException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    if (ftpServer != null && !ftpServer.isStopped()) {
      ftpServer.stop();
    }
  }
}


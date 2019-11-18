package io;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Execute implements Runnable {
  private Socket socket;
  Execute(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try {
      InputStream inputStream = socket.getInputStream();
      byte[] data = new byte[1024];
      int len;
      while ((len = inputStream.read(data)) != -1) {
        String str = new String(data, 0, len);
        System.out.println(str);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

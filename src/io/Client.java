package io;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

public class Client implements Runnable {
  private Socket socket;
  int id;

  public Client(Socket socket, int id) {
    this.socket = socket;
    this.id = id;
  }

  public void run() {
    try {
      this.socket.getOutputStream().write((new Date() + ": hello " + id).getBytes());
      this.socket.getOutputStream().flush();
    } catch (Exception e) {
      e.printStackTrace();
    }

    while (true) {
      Random random = new Random();
      try {
        Thread.sleep(1000 + random.nextInt(1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

package io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer implements Runnable{
  private ServerSocket serverSocket;
  private ExecutorService executorService;

  public BIOServer(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
    this.executorService = Executors.newFixedThreadPool(10);
  }

  public void run() {
    System.out.println("Server start");
    while (true) {
      try {
        Socket socket = serverSocket.accept();
        this.executorService.submit(new Execute(socket));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

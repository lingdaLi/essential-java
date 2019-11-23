package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class NIOClient implements Runnable {
  private SocketChannel socketChannel;
  public NIOClient(String host, int port) throws IOException {
    InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
    this.socketChannel = SocketChannel.open(inetSocketAddress);
  }

  public void run() {
    ArrayList<String> messages = new ArrayList<>();
    messages.add("Teams");
    messages.add("Azure");
    messages.add("Office");
    messages.add("Skype");
    messages.add("Xbox");

    for (String product : messages) {
      byte[] bytes = product.getBytes();
      ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
      try {
        this.socketChannel.write(byteBuffer);
        byteBuffer.clear();
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    try {
      this.socketChannel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

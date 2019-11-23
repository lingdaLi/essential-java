package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/*
Java NIO use a single thread(selector) to monitor incoming connection
*/
public class NIOServer implements Runnable {
  private ServerSocketChannel serverSocketChannel;

  public NIOServer(String host, int port) throws IOException {
    SocketAddress socketAddress = new InetSocketAddress(host, port);
    this.serverSocketChannel = ServerSocketChannel.open().bind(socketAddress);
    this.serverSocketChannel.configureBlocking(false);
  }

  public void run() {
    try {
      Selector selector = Selector.open();
      int validOps = this.serverSocketChannel.validOps();
      this.serverSocketChannel.register(selector, validOps);

      while (true) {
        // Blocked until there is new available connection
        selector.select();
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey selectionKey = iterator.next();
          try {
            process(selectionKey, selector);
          } catch (IOException ex) {
            ex.printStackTrace();
            return;
          }

          iterator.remove();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void process(SelectionKey selectionKey, Selector selector) throws IOException {
    if(selectionKey.isAcceptable()) {
      SocketChannel socketChannel = this.serverSocketChannel.accept();
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, socketChannel.validOps());
    }

    if(selectionKey.isReadable()) {
      SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
      socketChannel.configureBlocking(false);
      ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
      StringBuilder stringBuilder = new StringBuilder();
      while (socketChannel.read(byteBuffer) > 0) {
        stringBuilder.append(new String(byteBuffer.array(), 0, byteBuffer.position()));
        byteBuffer.clear();
      }

      String message = stringBuilder.toString();
      System.out.println("Read: " + message);
    }

    if(selectionKey.isWritable()) {
      String message = "Server heart beat";
      ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
      SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
      socketChannel.configureBlocking(false);
      socketChannel.write(byteBuffer);
      byteBuffer.clear();
    }
  }
}

import io.BIOServer;
import io.Client;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket socket = new Socket("127.0.0.1", 8000);
        Thread server = new Thread(new BIOServer(serverSocket));
        server.start();
        for (int i = 0; i < 20; i++) {
            Client client = new Client(socket, i);
            Thread clientT = new Thread(client);
            clientT.start();
            Thread.sleep(1000);
        }
    }
}

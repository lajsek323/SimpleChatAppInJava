import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {


    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Server s = new Server(serverSocket);
        s.start();
    }


    public  void start() throws IOException {

        try{


            while(!serverSocket.isClosed()){

                Socket socket = serverSocket.accept();
                System.out.println("New Client conencted");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();



            }
        } catch (IOException e){
            if(serverSocket != null){
                serverSocket.close();
            }
        }








    }



}

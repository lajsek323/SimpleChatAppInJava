import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {



    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientName;

    public ClientHandler(Socket socket){
        try {

            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server: " + clientName + " has entered the  chat!");

        } catch (IOException e) {
            closeEv(socket,bufferedReader,bufferedWriter);
        }


    }


    public void broadcastMessage(String message){
        for(ClientHandler clientHandler : clientHandlers){
            try{


                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();


            } catch (IOException e) {
                closeEv(socket,bufferedReader,bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){

        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientName + " left the chat!");

    }


    public void closeEv(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)  {

        removeClientHandler();

        try {

            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }



    }

    @Override
    public void run() {


           String messFromClient;

           while (socket.isConnected()) {

               try {

                   messFromClient = bufferedReader.readLine();
                   broadcastMessage(messFromClient);

           }

       catch(IOException e){
               closeEv(socket, bufferedReader, bufferedWriter);
               break;
           }
       }


    }
}

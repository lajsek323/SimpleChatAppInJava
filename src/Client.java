import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client  {


    private  Socket socket;
    private  BufferedReader bufferedReader;
    private  BufferedWriter bufferedWriter;
    private  String userName;


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username: ");
        String userName = scanner.nextLine();
        Socket s = new Socket("localhost",5000);
        Client client = new Client(s,userName);
        client.listenForMessage();
        client.sendMessage();

    }


    public Client(Socket socket, String userName) {

        try {
            this.socket = socket;
            this.userName = userName;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            closeEv(socket, bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();


            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){

                String messToSend = scanner.nextLine();
                bufferedWriter.write(userName + ": " + messToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }



        } catch (IOException e) {
            closeEv(socket, bufferedReader,bufferedWriter);
        }


    }

    public void closeEv(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)  {

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




    public void listenForMessage(){


        new Thread(() -> {

            String msgFromChat;

            while(socket.isConnected()){
                try{

                    msgFromChat = bufferedReader.readLine();
                    System.out.println(msgFromChat);
                } catch (IOException e) {
                    closeEv(socket, bufferedReader,bufferedWriter);
                }

            }
        }).start();


    }



}

/*
Java Main logic Code Understand this before making this project  Basic Idea

import java.io.*;
import java.net.*;
public class Server {
    public static void main(String[]args) throws IOException {
        ServerSocket server = new ServerSocket(7777);
        System.out.println("Server has started and is waiting for connection");
        Socket socket = server.accept();
        System.out.println("Connection is successfully established.");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader from_server = new BufferedReader(new InputStreamReader(System.in));
        String msg_from_client;
        String msg_from_server;
        while ((msg_from_client = br.readLine()) != null) {
            System.out.println("Client : ");
            System.out.println(msg_from_client);
            if (msg_from_client.equals("end")) {
                break;
            }
            msg_from_server = from_server.readLine();
            System.out.println("Server : ");
            pw.println(msg_from_server);
        }
        socket.close();
        server.close();
    }
}
*/

// Lets Build This Using Threads

import java.net.*;
import java.io.*;
public class Server{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready for connection");
            System.out.println("Waiting .....");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            startReading();
            startWriting();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void startReading(){
        Runnable r1 = ()->{
        System.out.println("Starting to read....");
        String client_msg;
        while(true){
            try{
            client_msg = br.readLine();
            if(client_msg==null || client_msg.equals("end")){
                System.out.println("Client decided to end chat.Closing Server....");
                socket.close();
                System.exit(0);
                break;
            }
        System.out.println("Client:" + client_msg);
        }
        catch(Exception e){
                e.printStackTrace();
        }}
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable w1 = ()->{
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                try{
                   String server_message = br2.readLine();
                   out.println(server_message);
                   out.flush();
                    if(server_message.equals("end")){
                        socket.close();
                        System.exit(0);
                        break;
                    }

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

        };
        new Thread(w1).start();
    }

    public static void main(String[] args) {
        System.out.println("Server Starting ");
        new Server();
    }
}

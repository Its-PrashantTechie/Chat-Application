/*
Java Main logic Code Understand this before making this project  Basic Idea

import java.io.*;
import java.net.*;
public class Client {
    public static void main(String[]args) throws IOException{
        Socket socket = new Socket("localhost",7777);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader toServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String c_msg ;
        String s_msg;
        System.out.println("Happy Chatting, If You Wish To Not Continue Type end");
        while(true){
            c_msg = br.readLine();
            System.out.println("Client : ");
            out.println(c_msg);
            if(c_msg.equals("end")){
                break;
            }
            s_msg = toServer.readLine();
            System.out.println("Server : ");
            System.out.println(s_msg);
        }
        socket.close();
    }
}
 */

// Lets make for  a client
import java.net.*;
import java.io.*;

public class Client{
    Socket socket;
    BufferedReader br;
    PrintWriter out ;
    public Client(){
        try{
            System.out.println("Sending request to the server");
            socket = new Socket("localhost" ,7777);
            System.out.println("Connection successfully established");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
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
                        System.out.println("Server decided to end chat.Closing Client...");
                        socket.close();
                        System.exit(0);
                        break;
                    }
                    System.out.println("Server:" + client_msg);
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

    public static void main(String[]args){
        System.out.println("Client is Ready");
        new Client();

    }
}

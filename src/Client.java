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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;
import javax.swing.*;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messagebox = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("Sending request to the server");
            socket = new Socket("localhost", 7777);
            System.out.println("Connection successfully established");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            createGUI();
            handleEvents();
            startReading();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        this.setTitle("TCP/IP Chat Application (Client-Mode)");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setText("Client Terminal");

        // -- UI
        Color primaryBlue = new Color(33, 150, 243);
        Color darkBackground = new Color(30, 30, 30);
        Color textColor = Color.WHITE;

        // 1. Heading Style with Logo
        heading.setFont(new Font("Roboto", Font.BOLD, 25));
        heading.setOpaque(true);
        heading.setBackground(primaryBlue);
        heading.setForeground(textColor);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            // This looks for the image relative to your compiled class files
            URL imgUrl = Client.class.getResource("/ChatLogo.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                heading.setIcon(new ImageIcon(scaledImg));
            } else {
                System.out.println("Image not found! Check if it's in the 'src' folder.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Message Box Style
        messagebox.setFont(font);
        messagebox.setBackground(darkBackground);
        messagebox.setForeground(textColor);
        messagebox.setEditable(false);
        messagebox.setLineWrap(true);
        messagebox.setWrapStyleWord(true);
        messagebox.setCaretColor(Color.WHITE);

        // 3. Message Input Style
        messageInput.setFont(font);
        messageInput.setBackground(new Color(50, 50, 50));
        messageInput.setForeground(textColor);
        messageInput.setCaretColor(Color.WHITE);
        messageInput.setHorizontalAlignment(SwingConstants.LEFT);
        messageInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryBlue, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Layout setup
        JScrollPane scrollPane = new JScrollPane(messagebox);
        scrollPane.setBorder(null); // Clean look

        this.setLayout(new BorderLayout());
        this.add(heading, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) { // Enter Key
                    String contentToSend = messageInput.getText();
                    if(!contentToSend.trim().isEmpty()) {
                        messagebox.append("Me: " + contentToSend + "\n");
                        out.println(contentToSend);
                        out.flush();
                        messageInput.setText("");
                        messageInput.requestFocus();

                        if(contentToSend.equals("end")) {
                            System.exit(0);
                        }
                    }
                }
            }
        });
    }

    public void startReading() {
        Runnable r1 = () -> {
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg == null || msg.equals("end")) {
                        JOptionPane.showMessageDialog(this, "Server ended the chat.");
                        socket.close();
                        System.exit(0);
                        break;
                    }
                    messagebox.append("Server: " + msg + "\n");
                }
            } catch (Exception e) {
                System.out.println("Connection closed.");
            }
        };
        new Thread(r1).start();
    }

    public static void main(String[] args) {
        new Client();
    }
}
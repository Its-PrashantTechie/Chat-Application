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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;
import javax.swing.*;

public class Server extends JFrame {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // GUI Components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messagebox = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready for connection");
            System.out.println("Waiting .....");

            socket = server.accept();

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
        this.setTitle("TCP/IP Chat Application (Server-Mode)");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setText("Server Control Center");

        // UI
        Color serverColor = new Color(46, 125, 50);
        Color darkBackground = new Color(30, 30, 30);
        Color textColor = Color.WHITE;

        // 1. Heading Styling
        heading.setFont(new Font("Roboto", Font.BOLD, 25));
        heading.setOpaque(true);
        heading.setBackground(serverColor);
        heading.setForeground(textColor);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            // This looks for the image relative to your compiled class files
            URL imgUrl = Server.class.getResource("/ChatLogo.png");

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
        messageInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(serverColor, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Layout setup
        JScrollPane scrollPane = new JScrollPane(messagebox);
        scrollPane.setBorder(null);

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
                        JOptionPane.showMessageDialog(this, "Client ended the chat.");
                        messageInput.setEnabled(false);
                        socket.close();
                        System.exit(0);
                        break;
                    }
                    messagebox.append("Client: " + msg + "\n");
                }
            } catch (Exception e) {
                System.out.println("Connection closed.");
            }
        };
        new Thread(r1).start();
    }

    public static void main(String[] args) {
        new Server();
    }
}
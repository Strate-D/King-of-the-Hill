/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.server;

import kingofthehill.rmimultiplayer.Client;
import kingofthehill.rmimultiplayer.Message;
import kingofthehill.rmimultiplayer.TextMessage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bas
 */
public class VoiceServer {

    private final List<Client> connectedClients;
    public static List<Message> lastMessages;

    public VoiceServer() {
        this.connectedClients = new ArrayList<>();
        VoiceServer.lastMessages = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket listener = new ServerSocket(9090)) {
            writeMessage("Server is online on IP: " + listener.getInetAddress());

            while (true) {
                System.out.println("Waiting for clients to connect...");

                Socket socket = listener.accept();
                writeMessage("Client " + socket.getInetAddress() + " connected");

                try {

                    Client c = new Client(socket, connectedClients);
                    for(Client cli : connectedClients)
                    {
                        cli.addClient(c);
                    }
                    connectedClients.add(c);
                    
                    c.sendLastMessages(lastMessages);
                } catch (Exception ex) {
                } finally {
                    //socket.close();
                }
                
                Thread.sleep(10);
            }
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(VoiceServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeMessage(String message) {
        System.out.println(message);
        lastMessages.add(new TextMessage(-10, message));
    }
}

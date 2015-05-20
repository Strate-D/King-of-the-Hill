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
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bas
 */
public class VoiceServer {

    private final List<Client> connectedClients;
    private List<Message> lastMessages;

    private int portNumber = 9090;

    public VoiceServer(int portnumber) {
        this.connectedClients = new ArrayList<>();
        lastMessages = new ArrayList<>();

        this.portNumber = portnumber;
    }
    
    public List<Message> getMessages()
    {
        return unmodifiableList(this.lastMessages);
    }
    
    public void addMessage(Message message)
    {
        this.lastMessages.add(message);
    }

    public void start() {
        Thread t = new Thread(() -> {
            try (ServerSocket listener = new ServerSocket(portNumber)) {

                //writeMessage("Server is running...");
                
                while (true) {
                    System.out.println("Waiting for clients to connect...");

                    Socket socket = listener.accept();
                    //writeMessage("Client " + socket.getInetAddress() + " connected");

                    try {

                        Client c = new Client(socket, connectedClients, this);
                        for (Client cli : connectedClients) {
                            cli.addClient(c);
                        }
                        connectedClients.add(c);

                        //c.sendLastMessages(lastMessages);
                    } catch (Exception ex) {
                    } finally {
                        //socket.close();
                    }

                    Thread.sleep(10);
                }
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(VoiceServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        t.start();
    }

    public void writeMessage(String message) {
        System.out.println(message);
        lastMessages.add(new TextMessage(-10, message));
    }

    public void removeClient(Client toRemove) {
        int index = -1;
        for (int i = 0; i < connectedClients.size(); i++) {
            if (connectedClients.get(i).equals(toRemove)) {
                index = i;
            } else {
                connectedClients.get(i).removeClient(toRemove);
            }
        }

        if (index != -1) {
            connectedClients.remove(index);
        }
    }
}

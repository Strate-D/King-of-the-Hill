/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.rmimultiplayer;

import kingofthehill.server.VoiceServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Bas
 */
public class Client implements Serializable {

    private Socket socket;
    private ObjectOutputStream sender;
    private ObjectInputStream reader;
    private List<Client> knownClients;
    private int clientID;
    private String name;
    private boolean isAlive = true;

    private static int clientCounter = 0;

    public Client(Socket socket, List<Client> knownClients) {
        this.socket = socket;
        clientCounter += 1;
        this.clientID = clientCounter;
        this.knownClients = new ArrayList<>(knownClients);

        try {
            sender = new ObjectOutputStream(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
        } catch (Exception ex) {
        }

        try {
            sender.writeObject(new InfoMessage(clientCounter, "CLIENT_ID"));
        } catch (IOException ex) {
        }

        startMessageReader();
    }

    public void addClient(Client c) {
        knownClients.add(c);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void sendLastMessages(List<Message> lastMessages) {
        for (Message m : lastMessages) {
            this.sendMessage(m);
        }
    }

    public void sendMessage(Message message) {
        // Message contains a client id
        // replace the client id for a name
        if (message.getSender() == -10) {
            message.setSenderName("SERVER");
        }

        if (message.getSender() == this.clientID) {
            message.setSenderName(this.name);
            if (!this.isAlive) {
                return;
            }
        }

        for (Client c : knownClients) {
            if (message.getSender() == c.clientID) {
                if (!c.isAlive) {
                    return;
                }
                message.setSenderName(c.name);
                break;
            }
        }

        // The message now has a client name
        // Send the message
        try {
            if (!this.isAlive) {
                return;
            }
            sender.writeObject(message);
        } catch (Exception ecx) {
            System.out.println(ecx.getMessage());
        }
    }

    private void startMessageReader() {
        Thread t = new Thread(() -> {
            while (true) {
                if (!isAlive) {
                    break;
                }

                Message object = null;
                try {
                    object = (Message) reader.readObject();
                } catch (Exception ex) {
                    continue;
                }

                SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
                
                System.out.println(dt.format(new Date()) + " Message received from " + socket.getInetAddress());

                if (object instanceof InfoMessage) {
                    InfoMessage mess = (InfoMessage) object;
                    if (mess.getDefine().equals("CLIENT_NAME")) {
                        System.out.print("\r<< Client(" + clientID + ") changed name from \'" + name + "\' to \'" + mess.getData() + "\' >>\n");
                        name = (String) mess.getData();
                        continue;
                    } else if (mess.getDefine().equals("KICK_CLIENT")) {
                        System.out.print("\r<< Client " + mess.getData() + " will be kicked >>\n");
                        Client c = getClient((int) mess.getData());
                        c.sendMessage(new InfoMessage(null, "KICK"));
                        c.setClientDead();
                        continue;
                    }
                } else if (object instanceof AudioMessage) {

                } else {
                    VoiceServer.lastMessages.add(object);
                }

                for (Client c : knownClients) {
                    c.sendMessage(object);
                }
                if (object instanceof AudioMessage) {
                } else {
                    sendMessage(object);
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
        });
        t.start();
    }

    private void setClientDead() {
        this.isAlive = false;
    }

    private Client getClient(int id) {
        if (id == this.clientID) {
            return this;
        }

        for (Client c : knownClients) {
            if (!c.isAlive) {
                continue;
            }
            if (id == c.clientID) {
                return c;
            }
        }

        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.rmimultiplayer;

import java.io.EOFException;
import kingofthehill.server.VoiceServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import static java.lang.System.gc;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private boolean isAlive;
    private VoiceServer parent;

    private static int clientCounter = 0;

    public Client(Socket socket, List<Client> knownClients, VoiceServer parent) {
        this.socket = socket;
        clientCounter += 1;
        this.clientID = clientCounter;
        this.knownClients = new ArrayList<>(knownClients);
        this.parent = parent;
        this.isAlive = true;

        try {
            sender = new ObjectOutputStream(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
        } catch (Exception ex) {
        }

        try {
            sender.writeObject(new InfoMessage(this.clientID, "CLIENT_ID"));
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

    public void sendMessageToMyself(Message message) {
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
                    continue;
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
            this.isAlive = false;
            System.out.println(ecx.getMessage());
        }
    }

    private void startMessageReader() {
        Thread t = new Thread(() -> {
            while (isAlive) {

                if (!this.socket.isConnected()) {
                    this.killClient();
                    break;
                }

                Message object = null;
                try {
                    object = (Message) reader.readObject();
                } catch (EOFException|SocketException ex) {
                    this.killClient();
                } catch (Exception ex) {
                    continue;
                } 

                SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");

                System.out.println(dt.format(new Date()) + " Message received from " + socket.getInetAddress() + " : " + object.getHeader());

                if (object instanceof InfoMessage) {
                    InfoMessage mess = (InfoMessage) object;
                    if (mess.getDefine().equals("CLIENT_NAME")) {
                        System.out.print("\r<< Client(" + clientID + ") changed name from \'" + name + "\' to \'" + mess.getData() + "\' >>\n");
                        name = (String) mess.getData();
                        sendMessageToAll(new TextMessage(-10, name + " joined the game"));
                        continue;
                    } else if (mess.getDefine().equals("KICK_CLIENT")) {
                        System.out.print("\r<< Client " + mess.getData() + " will be kicked >>\n");
                        Client c = getClient((int) mess.getData());
                        c.sendMessageToMyself(new InfoMessage(null, "KICK"));
                        c.killClient();
                        continue;
                    } else if (mess.getDefine().equals("LEAVE_PARTY")) {
                        this.sendMessageToAll(new TextMessage(-10, name + " left the game"));
                        System.out.println(name + " left the game");
                        this.killClient();
                        continue;
                    } else if (mess.getDefine().equals("GET_LAST_MESSAGES")) {
                        System.out.println("\r<< Sending previous messages to client(" + mess.getSender() + ")");
                        Client c = getClient((int) mess.getData());
                        c.sendMessageToMyself(new InfoMessage(this.parent.getMessages(), "SEND_LAST_MESSAGES"));
                        continue;
                    }
                } else if (object instanceof AudioMessage) {

                } else {
                    this.parent.addMessage(object);
                }

                this.sendMessageToAll(object);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }

                gc();
            }

            this.killClient();
        });
        t.start();
    }

    private void sendMessageToAll(Message message) {
        for (Client c : knownClients) {
            c.sendMessageToMyself(message);
        }

        if (!(message instanceof AudioMessage)) {
            this.sendMessageToMyself(message);
        }
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

    public void removeClient(Client toRemove) {
        this.knownClients.remove(toRemove);
    }

    public void killClient() {
        try {
            this.isAlive = false;
            this.socket.close();
            this.parent.removeClient(this);
        } catch (Exception ex) {
        }
    }
}

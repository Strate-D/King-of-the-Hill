/**
 *
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

    /**
     * The constructor for a client. The server holds a list of Client objects.
     * There objects run their own threads for reading and sending messages
     *
     * @param socket The socket on wich the client is connected
     * @param knownClients The otherclients that are already connected
     * @param parent The VoiceServer paren who controls all the other Client
     * objects
     */
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
            System.out.printf("kingofthehill.rmimultiplayer.Client constructor: " + ex.getMessage());
        }

        try {
            sender.writeObject(new InfoMessage(this.clientID, "CLIENT_ID"));
        } catch (IOException ex) {
            System.out.printf("kingofthehill.rmimultiplayer.Client constructor: " + ex.getMessage());
        }

        startMessageReader();
    }

    /**
     * Adds a new client to the list of the knownclients
     *
     * @param c The new client to add to the knownclients list
     */
    public void addClient(Client c) {
        knownClients.add(c);
    }

    /**
     * Returns the socket on wich the client is connected
     *
     * @return The socket object
     */
    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Send a message to the current client
     *
     * @param message The message to send
     */
    public void sendMessageToMyself(Message message) {
        /**
         * Message contains a client id Replace the client id for a name
         */
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

        /**
         * The message now has a client name Send the message
         */
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

    /**
     * Start the Client Messages reader. This method handles the messages that
     * are recieved from the client and sends them out to all the other clients
     */
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
                } catch (EOFException | SocketException ex) {
                    this.killClient();
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("kingofthehill.rmimultiplayer.Client startMessageReader() @ ClientID(" + this.clientID + "): " + ex.getMessage());
                    continue;
                }

                if (object == null) {
                    System.out.println("kingofthehill.rmimultiplayer.Client startMessageReader() @ ClientID(" + this.clientID + "): " + "null object found");
                    continue;
                }

                SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");

                System.out.println(dt.format(new Date()) + " Message received from " + socket.getInetAddress() + " : " + object.getHeader());

                if (object instanceof InfoMessage) {
                    InfoMessage mess = (InfoMessage) object;
                    switch (mess.getDefine()) {
                        case "CLIENT_NAME":
                            System.out.print("\r<< Client(" + clientID + ") changed name from \'" + name + "\' to \'" + mess.getData() + "\' >>\n");
                            name = (String) mess.getData();
                            sendMessageToAll(new TextMessage(-10, name + " joined the game"));
                            continue;
                        case "KICK_CLIENT": {
                            System.out.print("\r<< Client " + mess.getData() + " will be kicked >>\n");
                            Client c = getClient((int) mess.getData());
                            c.sendMessageToMyself(new InfoMessage(null, "KICK"));
                            c.killClient();
                            continue;
                        }
                        case "LEAVE_PARTY":
                            this.sendMessageToAll(new TextMessage(-10, name + " left the game"));
                            System.out.println(name + " left the game");
                            this.killClient();
                            continue;
                        case "GET_LAST_MESSAGES": {
                            System.out.println("\r<< Sending previous messages to client(" + mess.getSender() + ")");
                            Client c = getClient((int) mess.getData());
                            c.sendMessageToMyself(new InfoMessage(this.parent.getMessages(), "SEND_LAST_MESSAGES"));
                            continue;
                        }
                    }
                } else if (object instanceof AudioMessage) {
                    /**
                     * If there was an AudioMessage send, do not do anything
                     */
                    System.out.println(ByteArrayToString((byte[]) object.getData()));
                } else {
                    this.parent.addMessage(object);
                }

                this.sendMessageToAll(object);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    System.out.println("kingofthehill.rmimultiplayer.Client startMessageReader() @ ClientID(" + this.clientID + "): " + ex.getMessage());
                }

                gc();
            }

            this.killClient();
        });
        t.start();
    }

    private String ByteArrayToString(byte[] input) {
        String output = "";
        for (byte b : input) {
            Integer i = ((int) b);
            output += i.toString();
        }

        return output;
    }

    /**
     * Sends a message to all known clients
     *
     * @param message The message to send
     */
    private void sendMessageToAll(Message message) {
        for (Client c : knownClients) {
            c.sendMessageToMyself(message);
        }

        if (!(message instanceof AudioMessage)) {
            this.sendMessageToMyself(message);
        }
    }

    /**
     * Return the Client object retrieved from an id
     *
     * @param id The id of the client to find
     * @return The found client object
     */
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

    /**
     * Removes a client from the known clients list
     *
     * @param toRemove The client object to remove
     */
    public void removeClient(Client toRemove) {
        this.knownClients.remove(toRemove);
    }

    /**
     * Kills the current client. It removes itself from the VoiceServer client
     * list
     */
    public void killClient() {
        try {
            this.isAlive = false;
            this.socket.close();
            this.parent.removeClient(this);
        } catch (Exception ex) {
            System.out.println("kingofthehill.rmimultiplayer.Client killClient() @ ClientID(" + this.clientID + "): " + ex.getMessage());
        }
    }
}

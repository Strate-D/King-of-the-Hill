/**
 *
 */
package kingofthehill.server;

import kingofthehill.rmimultiplayer.Client;
import kingofthehill.rmimultiplayer.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;

/**
 * The server for send text messages and audio messages
 *
 * @author Bas
 */
public class VoiceServer {

    private final List<Client> connectedClients;
    private List<Message> lastMessages;

    private int portNumber = 9090;

    /**
     * Constructor for create a new VoiceServer object
     *
     * @param portnumber The portnumber that the VoiceServer can use to listen
     * on
     */
    public VoiceServer(int portnumber) {
        this.connectedClients = new ArrayList<>();
        lastMessages = new ArrayList<>();

        this.portNumber = portnumber;
    }

    /**
     * Return the messages that have been send
     *
     * @param lobby
     * @return The messages as read-only list
     */
    public List<Message> getMessages(String lobby) {
        ArrayList<Message> last = new ArrayList();
        for (Message m : this.lastMessages) {
            if (m.getLobbyName().equals(lobby)) {
                last.add(m);
            }
        }

        return unmodifiableList(last);
    }

    /**
     * Add a messages to the list of the last messages
     *
     * @param message The new message
     */
    public void addMessage(Message message) {
        this.lastMessages.add(message);
    }

    /**
     * Start running the VoiceServer so it starts accepting clients
     */
    public void start() {
        Thread t = new Thread(() -> {
            try (ServerSocket listener = new ServerSocket(portNumber)) {

                while (true) {
                    System.out.println("Waiting for clients to connect...");

                    Socket socket = listener.accept();

                    try {

                        Client c = new Client(socket, connectedClients, this);
                        for (Client cli : connectedClients) {
                            cli.addClient(c);
                        }
                        connectedClients.add(c);
                    } catch (Exception ex) {
                        System.out.println("kingofthehill.rmimultiplayer.VoiceServer start(): " + ex.getMessage());
                    }

                    Thread.sleep(10);
                }
            } catch (InterruptedException | IOException ex) {
                System.out.println("kingofthehill.rmimultiplayer.VoiceServer start(): " + ex.getMessage());
            }
        });
        t.start();
    }

    /**
     * Removes a client from the list of clients
     *
     * @param toRemove The client that needs to be removed
     */
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

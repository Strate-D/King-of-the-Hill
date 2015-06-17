package kingofthehill.client;

import java.rmi.NotBoundException;
import kingofthehill.domain.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import kingofthehill.lobby.ILobby;

/**
 * Class that manages the rmi connection of the client
 *
 * @author Jur
 */
public class ClientManager {

    private ILobby lobby;
    private Registry registry = null;
    private final int portNumber = 9999;
    private static final String bindingName = "Lobby";
    private final String ipAddress;

    public static VoiceClient AudioChat;

    /**
     * Gets gamemanager object from registery
     *
     * @param ipAddress ip adress of the remote registery
     */
    public ClientManager(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Tries to locate registery and bind the gamemanager
     *
     * @return true if the registery is located and the gamemanager is bound,
     * else false
     */
    public boolean locate() {
        /**
         * Print IP address and port number for registry
         */
        System.out.println("Client: IP Address: " + ipAddress);
        System.out.println("Client: Port number " + portNumber);

        /**
         * Locate registry at IP address and port number
         */
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
            registry.list();
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
            return false;
        }

        /**
         * Print result locating registry
         */
        if (registry != null) {
            System.out.println("Client: Registry located");
        } else {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: Registry is null pointer");
            return false;
        }

        /**
         * Bind gamemanager using registry
         */
        if (registry != null) {
            try {
                lobby = (ILobby) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind gamemanager");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                lobby = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind gamemanager");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                lobby = null;
            }
        }

        /**
         * Print result binding gamemanager
         */
        if (lobby != null) {
            System.out.println("Client: Gamemanager bound");
        } else {
            System.out.println("Client: Gamemanager is null pointer");
            return false;
        }

        return true;
    }

    /**
     * Gets the url of the server
     *
     * @return string with the url of the server
     */
    public String getServerUrl() {
        return ipAddress;
    }

    /**
     * Returns the GameManager
     *
     * @return The GameManager object
     */
    public ILobby getLobby() {
        return this.lobby;
    }

    /**
     * Setup the audiochat audio format and the VoiceClient
     *
     * @param ipAddress IPAddress of the server
     * @param port The port of the server
     * @param username The username used to connect
     * @return true if the setup was successfull; otherwise false
     */
    public static boolean setupAudioChat(String ipAddress, int port, String username) {
        if (ClientManager.AudioChat == null) {
            ClientManager.AudioChat = new VoiceClient(ipAddress, port, username);
        } else {
            if (!ClientManager.AudioChat.isStarted()) {
                ClientManager.AudioChat = new VoiceClient(ipAddress, port, username);
                return true;
            }
            return false;
        }

        return true;
    }

    /**
     * Checks if the AudioChat is active and running
     *
     * @return true if the VoiceChat is running; otherwise false
     */
    public static boolean isAudioChatRunning() {
        if (ClientManager.AudioChat != null) {
            if (ClientManager.AudioChat.isStarted()) {
                return true;
            }
        }

        return false;
    }
}

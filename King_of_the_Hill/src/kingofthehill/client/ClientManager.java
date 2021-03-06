package kingofthehill.client;

import java.rmi.NotBoundException;
import kingofthehill.domain.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class that manages the rmi connection of the client
 *
 * @author Jur
 */
public class ClientManager {

    private IGameManager gm;
    private Registry registry = null;
    private final int portNumber = 9999;
    private static final String bindingName = "GameInfo";
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
     * @param startVoiceClient
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
                gm = (IGameManager) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind gamemanager");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                gm = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind gamemanager");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                gm = null;
            }
        }

        /**
         * Print result binding gamemanager
         */
        if (gm != null) {
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
     *
     * @return
     */
    public IGameManager getGameManager() {
        return this.gm;
    }

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

    public static boolean isAudioChatRunning() {
        if (ClientManager.AudioChat != null) {
            if (ClientManager.AudioChat.isStarted()) {
                return true;
            }
        }

        return false;
    }
}

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
    public boolean locate(boolean startVoiceClient) {
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

        if (startVoiceClient) {
            try {
                VoiceClient vc = new VoiceClient(ipAddress, 9090, "Corpelijn");
                vc.start();
                System.out.println("Client: Voice client started");
            } catch (Exception ex) {
                System.out.println("Client: Cannot start voice client");
                System.out.println("Client: Exception: " + ex.getMessage());
            }
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
}

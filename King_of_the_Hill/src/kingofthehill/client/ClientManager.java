package kingofthehill.client;

import java.rmi.NotBoundException;
import kingofthehill.domain.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import kingofthehill.upgradeinfo.UpgradeInfo;

/**
 * Class that manages the game. Contains all the units and objects that are part
 * of it.
 *
 * @author Jur
 */
public class ClientManager{
    private IGameManager gm;
    private Registry registry = null;
    private int portNumber = 9999;
    private static final String bindingName = "GameInfo";
    private String ipAddress;

    /**
     * Gets gamemanager object from registery
     * @param ipAddress ip adress of the remote registery
     */
    public ClientManager(String ipAddress){
        this.ipAddress = ipAddress;
    }
    
    public boolean locate(){
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
         * Bind student administration using registry
         */
        if (registry != null) {
            try {
                gm = (IGameManager) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                gm = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                gm = null;
            }
        }
        
        /**
         * Print result binding student administration
         */
        if (gm != null) {
            System.out.println("Client: Student administration bound");
        } else {
            System.out.println("Client: Student administration is null pointer");
        }
        
        return true;
    }
    
    public IGameManager getGameManager(){
        return this.gm;
    }
}

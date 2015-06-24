/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import kingofthehill.lobby.ILobby;
import kingofthehill.lobby.Lobby;

/**
 *
 * @author Dennis
 */
public class RMIserver {

    /**
     * Set the port number of the server for RMI communication
     */
    private static final int portNumber = 9999;
    
    // Set binding name for student administration
    private static final String bindingName = "Lobby";

    /**
     * References to registry and student administration
     */
    private Registry registry = null;

    private ILobby lobby = null;
    
    public RMIserver() {
        /**
         * Send a welcome message
         */
        System.out.println("SERVER USING REGISTRY");

        /**
         * Print IP addresses and network interfaces
         */
        printIPAddresses();

        /**
         * Print port number for registry
         */
        System.out.println("Server: Port number " + portNumber);

        /**
         * Create a gamemanager for running the server game
         */
        try {
            //gameManager = new GameManager();
            lobby = new Lobby();
            System.out.println("Server: Game created");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create game");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            lobby = null;
        }

        /**
         * Create registry at port number
         */
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            System.out.println("Server: Registry created on port number " + portNumber);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            registry = null;
        }

        /**
         * Bind student administration using registry
         */
        try {
            registry.rebind(bindingName, lobby);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot bind student administration");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }

        /**
         * Start an AudioServer for chatting
         */
        try {
            VoiceServer as = new VoiceServer(9090);
            as.start();
            System.out.println("Server: Audio server started");
        } catch (Exception ex) {
            System.out.println("Server: Cannot start the audio server");
            System.out.println("Server: Exception: " + ex.getMessage());
        }
    }

    /**
     * Print IP addresses and network interfaces
     */
    private void printIPAddresses() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server: IP Address: " + localhost.getHostAddress());
            /**
             * Just in case this host has multiple IP addresses....
             */
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            if (allMyIps != null && allMyIps.length > 1) {
                System.out.println("Server: Full list of IP addresses:");
                for (InetAddress allMyIp : allMyIps) {
                    System.out.println("    " + allMyIp);
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server: Cannot get IP address of local host");
            System.out.println("Server: UnknownHostException: " + ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create server
        RMIserver server = new RMIserver();
    }

}

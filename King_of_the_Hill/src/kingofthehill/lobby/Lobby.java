/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.lobby;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import kingofthehill.domain.GameManager;

/**
 *
 * @author Dennis
 */
public class Lobby extends UnicastRemoteObject implements ILobby {

    private ArrayList<GameManager> games;
    
    public Lobby() throws RemoteException{
        this.games = new ArrayList<>();
    }
    
    @Override
    public synchronized void createGame(String name) throws RemoteException{
        try {
            games.add(new GameManager(name));
        } catch (RemoteException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void removeGame(int index) throws RemoteException{
        games.remove(index);
    }

    @Override
    public synchronized void joinGame(int index, String playername) throws RemoteException{
        try {
            games.get(index).addPlayer(playername, false);
        } catch (RemoteException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.lobby;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import kingofthehill.domain.GameManager;
import kingofthehill.domain.IGameManager;

/**
 *
 * @author Dennis
 */
public class Lobby extends UnicastRemoteObject implements ILobby {

    private ArrayList<IGameManager> games;
    private Timer timer;

    public Lobby() throws RemoteException {
        this.games = new ArrayList<>();

        timer = new Timer();
        timer.schedule(new Lobby.CheckLoop(), 0, 5000);
    }

    /**
     * CheckLoop class with timertask to call the checkFinished method in a loop
     */
    private class CheckLoop extends java.util.TimerTask {

        @Override
        public void run() {
            checkFinished();
        }
    }

    /**
     * Check if the game is finished, if finished than remove game from list of game
     */
    private void checkFinished() {
        for(IGameManager gm : games){
            try {
                if(gm.checkFinished() != 0){
                    games.remove(gm);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void createGame(String name) throws RemoteException {
        try {
            if (name != null) {
                if (checkGameName(name)) {
                    games.add(new GameManager(name));
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void removeGame(int index) throws RemoteException {
        games.remove(index);
    }

    @Override
    public synchronized void joinGame(String name, String playername) throws RemoteException {
        try {
            if (name != null && playername != null) {
                getGame(name).addPlayer(playername, true);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public synchronized void joinGame(int index, String playername) throws RemoteException {
        try {
            if (playername != null) {
                games.get(index).addPlayer(playername, false);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public IGameManager getGame(int index) throws RemoteException {
        return games.get(index);
    }

    @Override
    public IGameManager getGame(String gameName) throws RemoteException {
        if (gameName != null) {
            for (IGameManager gm : games) {
                if (gm.getName().equals(gameName)) {
                    return gm;
                }
            }
        }
        return null;
    }

    @Override
    public ArrayList<String> getGames() throws RemoteException {
        ArrayList<String> stringGames = new ArrayList<>();

        for (IGameManager gm : games) {
            stringGames.add(gm.toString());
        }

        return stringGames;
    }


    /**
     * Checks if name of new game already exists in the list of games
     *
     * @param name name of the game to create
     * @return true if name doesnt exists, false if name exists
     * @throws RemoteException
     */
    private boolean checkGameName(String name) throws RemoteException {
        for (IGameManager gm : games) {
            if (gm.getName().equals(name)) {
                return false;
            }
        }

        return true;
    }

}

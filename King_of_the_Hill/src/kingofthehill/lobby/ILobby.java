/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.lobby;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import kingofthehill.domain.IGameManager;

/**
 *
 * @author Dennis
 */
public interface ILobby extends Remote{
    
    /**
     * Creates a new game with the name of the player in it; KoekBenaan's lobby 
     * and adds it to the list of games in the lobby
     * 
     * @param name name of the new game
     * @throws RemoteException 
     */
    public void createGame(String name) throws RemoteException;
    
    /**
     * Removes a game for the list of games in the lobby
     * 
     * @param index index of the game in the list
     * @throws RemoteException 
     */
    public void removeGame(int index) throws RemoteException;
    
    /**
     * Joins a game
     * 
     * @param index index of the game in the list
     * @param playername name of the player to join the game
     * @throws RemoteException 
     */
    public void joinGame(int index, String playername) throws RemoteException;
    
    /**
     * Gets a game with the givin' index
     * 
     * @param index index of the game in the list
     * @return IGameManager of the found game or null if game with index is not found in list of games
     * @throws RemoteException 
     */
    public IGameManager getGame(int index) throws RemoteException;
    
    /**
     * Gets a game with the givin' name
     * 
     * @param gameName
     * @return IGameManager of the found game or null if game with index is not found in list of games
     * @throws RemoteException 
     */
    public IGameManager getGame(String gameName) throws RemoteException;
    
    /**
     * Gets all games in the lobby
     * 
     * @return arraylist with strings that represent games in the lobby
     * @throws RemoteException 
     */
    public ArrayList<String> getGames() throws RemoteException;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import kingofthehill.rmimultiplayer.IGameInfo;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface of the gamemanager for multiplayer
 *
 * @author Dennis
 */
public interface IGameManager extends Remote {

    /**
     * Gets the gameinfo from the remote gamemanager on the server
     *
     * @return IGameInfo gameinfo object which contains all the important game
     * information
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public IGameInfo getGameInfo() throws RemoteException;

    /**
     * Adds a new player to an existing game
     *
     * @param player name of the player to add, may not be null or empty
     * @param isAi true if the player to add is an AI, else false
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public void addPlayer(String player, boolean isAi) throws RemoteException;

    /**
     * Removes an existing player from an existing game
     *
     * @param player name of the player to be removed, may not be null or empty
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public void removePlayer(String player) throws RemoteException;

    /**
     * Sets an joined player ready
     *
     * @param player name of the player to set ready
     * @return true if player is ready, else false
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public boolean setPlayerReady(String player) throws RemoteException;

    /**
     * Gets the join status of a player
     *
     * @param player name of the player to check
     * @return true if player is ready, else false
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public boolean getPlayerReady(String player) throws RemoteException;

    /**
     * Status of the game, if the game can be started
     *
     * @return true if all 4 players are ready, else false
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public boolean readyGame() throws RemoteException;

    /**
     * Places a unit at a lane of the base of the player
     *
     * @param playername name of the player
     * @param unit unit that has to be placed
     * @param index in which lane the unit has to be placed
     * @param cost the cost of the unit that has to be placed
     * @return true if unit is succesfully placed, else false
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    //Unit to unittype
    public boolean placeUnitAtBaseMulti(String playername, Unit unit, int index, int cost) throws RemoteException;

    /**
     * Bid on mysterybox
     *
     * @param playername name of the player that bids on the mysterybox
     * @param bid amount of resources
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public void bidMysteryboxMulti(String playername, int bid) throws RemoteException;

    /**
     * Let the server know this player does still exsist
     *
     * @param playername The current player name
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public void sendPlayerSignal(String playername) throws RemoteException;

    /**
     * When the player is inactive for too long, the AI takes over the player
     *
     * @param playername The name of the player to replace with an AI
     * @throws RemoteException If something goes wrong on communicating with the
     * server
     */
    public void setPlayerToAI(String playername) throws RemoteException;
}

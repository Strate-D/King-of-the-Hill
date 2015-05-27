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
 *
 * @author Dennis
 */
public interface IGameManager extends Remote{
    public IGameInfo getGameInfo() throws RemoteException;
    
    public void addPlayer(String player, boolean isAi) throws RemoteException;
    
    public boolean setPlayerReady(String player) throws RemoteException;
    
    public boolean readyGame() throws RemoteException;
    
    public boolean placeUnitAtBase(IPlayer player, Unit unit, int index, int cost) throws RemoteException;
    //Unit to unittype
    public boolean placeUnitAtBaseMulti(String playername, Unit unit, int index, int cost) throws RemoteException;
    
    public void bidMysteryboxMulti(String playername, int bid) throws RemoteException;
}
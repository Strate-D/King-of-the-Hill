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
    
    public void addPlayer(IPlayer player) throws RemoteException;
    
    public boolean placeUnitAtBase(IPlayer player, Unit unit, int index, int cost) throws RemoteException;
}
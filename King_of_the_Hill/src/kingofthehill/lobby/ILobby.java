/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.lobby;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Dennis
 */
public interface ILobby extends Remote{
    public void createGame(String name) throws RemoteException;
    
    public void removeGame(int index) throws RemoteException;
    
    public void joinGame(int index, String playername) throws RemoteException;
}

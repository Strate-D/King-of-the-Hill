/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.lobby;

import java.rmi.RemoteException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rick
 */
public class LobbyTest {
    
    @Test
    public void NameTest() throws RemoteException{
        
        Lobby lobby = new Lobby();
        lobby.createGame("Rick's Lobby");
        assertEquals(lobby.getGame(0), lobby.getGame("Rick's Lobby"));
        
        lobby.removeGame(0);
        try {
            lobby.getGame(0);
            fail("that index shouldn't exist");
        } catch(IndexOutOfBoundsException ex){
        }
}
}

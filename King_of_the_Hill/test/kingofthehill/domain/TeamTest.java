/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.awt.List;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bas
 */
public class TeamTest 
{
    @Test public void testGetters()
    {
        /**
         * Test the getters by creating some classes and reading the values
         */
        
        // default value
        IPlayer player1 = new AI("computer1");
        IPlayer player2 = new AI("computer2");
        IPlayer player3 = new Player("Bas", 10);
        ArrayList<IPlayer> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        
        
        // 1: normal test
        Team team1 = new Team(1, players);
        assertEquals(players, team1.getPlayers());
        assertEquals(1, team1.getNr());
        assertEquals(player1, team1.getPlayers().get(0));
        assertEquals(player2, team1.getPlayers().get(1));
        
        // 2: fail test
        try
        {
            Team team2 = new Team(2, null);
            fail("List of players can not be null, can be empty");
        }
        catch (IllegalArgumentException ecx)
        {
        }
            
        // 3: fail test
        try
        {
            Team team3 = new Team(-3, players);
            fail ("Teamnumber cannot be less than zero!");
        }
        catch ( IllegalArgumentException ecx)
        {
        }
        
        // 4: normal test
        Team team4 = new Team(4, new ArrayList<>());
        assertEquals(4, team4.getNr());
        assertEquals(new ArrayList<>(), team4.getPlayers());
        
        // 5: normal test
        team1.addPlayer(player3);
        assertEquals(player3, team1.getPlayers().get(2));
    }
    
    @Test public void testAddPlayer()
    {
        /**
         * Test the method by creating an empty list and adding the players later
         */
        
        // default value
        IPlayer ai1 = new AI("Computer1");
        IPlayer ai2 = new AI("Computer2");
        IPlayer player = new Player("Bas", 10);
        ArrayList<IPlayer> players = new ArrayList<>();
        players.add(ai1);
        players.add(ai2);
        
        // 1: normal test
        Team team1 = new Team(1, new ArrayList<>());
        team1.addPlayer(ai1);
        team1.addPlayer(ai2);
        assertEquals(ai1, team1.getPlayers().get(0));
        assertEquals(ai2, team1.getPlayers().get(1));
        assertEquals(players, team1.getPlayers());
        
        // 2: fail test
        Team team2 = new Team(2, new ArrayList<>());
        team2.addPlayer(ai1);
        team2.addPlayer(ai2);
        team2.addPlayer(ai1);
        assertEquals(2, team2.getPlayers().size());
        assertEquals(ai1, team2.getPlayers().get(0));
        assertEquals(ai2, team2.getPlayers().get(1));
        
        // 3: fail test
        assertEquals(false, team2.addPlayer(null));
        
        // 4: normal test
        team1.addPlayer(player);
        assertEquals(player, team1.getPlayers().get(2));
    }
}

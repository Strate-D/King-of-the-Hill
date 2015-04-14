/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dennis
 */
public class IPlayerTest 
{   
   @Test public void testGetters()
   {
        /**
         * Test the getters by creating some objects and check their values
        */
       
        // default value
        String name = "Bastiaan";
        int exp = 10;
        
        // 1: normal player test
        IPlayer player = new Player(name, exp);
        assertEquals(name, player.getName());
        assertEquals(exp, player.getExp());
        assertEquals(0, player.getScore());
        assertTrue(player.getUpgrades().isEmpty());
        assertNull(player.getTeam());
        assertEquals(100, player.getMoney());
        assertNull(player.getBase());
        
        // 2: normal ai test
        IPlayer ai = new AI(name);
        assertEquals(name, ai.getName());
        assertEquals(0, ai.getExp());
        assertEquals(0, ai.getScore());
        assertTrue(ai.getUpgrades().isEmpty());
        assertNull(ai.getTeam());
        assertEquals(100, ai.getMoney());
        assertNull(ai.getBase());
        
        // 3: fail test
        try{
            player = new Player("", 1);
            fail("Player without a name is created");
        }
        catch(IllegalArgumentException exc){}
        
        // 4: fail test
        try{
            player = new Player(name, -1);
            fail("Player with negative exp is created");
        }
        catch(IllegalArgumentException exc){}
        
        // 5: fail test
        try{
            player = new AI("");
            fail("Ai without a name is created");
        }
        catch(IllegalArgumentException exc){}
   }
   
   @Test public void testSetters()
   {
       /**
        * Test the setters by creating some objects and check their values
       */
       
       // default value
       String name = "Rick";
       IPlayer player = new Player(name, 10);
       Base base = new Base(player);
       ArrayList<IPlayer> players = new ArrayList<>();
       players.add(player);   
       Team team = new Team(1, players);
       
       // 1: normal player test
       player.setBase(base);
       assertEquals(base, player.getBase());
       player.setTeam(team);
       assertEquals(team, player.getTeam());
       
       // 2: normal ai test
       IPlayer ai = new AI(name);
       ai.setBase(base);
       assertEquals(base, ai.getBase());
       ai.setTeam(team);
       assertEquals(team, ai.getTeam());
       
       // 3: fail test
       ai.setBase(null);
       assertEquals(base, ai.getBase());
       
       //4: fail test
       player.setTeam(null);
       assertEquals(team, player.getTeam());
   }
   
   @Test public void testAddPayMoney()
   {
        /**
        * Test the setters by creating some objects and check their values
        */
       
       // default value
       String name = "Bertha";
       
       // 1: normal player test
       IPlayer player = new Player(name, 10);
       player.addMoney(10);
       assertEquals(110, player.getMoney());
       assertTrue(player.payMoney(10));
       assertFalse(player.payMoney(101));
       assertTrue(player.payMoney(100));
       assertFalse(player.payMoney(1));
       
       // 2: normal ai test
       IPlayer ai = new AI(name);
       ai.addMoney(99);
       assertEquals(199, ai.getMoney());
       assertTrue(ai.payMoney(10));
       assertFalse(ai.payMoney(190));
       assertTrue(ai.payMoney(100));
       assertFalse(ai.payMoney(91)); 
   }
}

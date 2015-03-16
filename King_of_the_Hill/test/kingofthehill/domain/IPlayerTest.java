/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Bas
 */
public class IPlayerTest 
{   
   @Test public void testGetters()
   {
        String name = "Bastiaan";
        int exp = 10;
        
        IPlayer player = new Player(name, exp);
        assertEquals(name, player.getName());
        assertEquals(exp, player.getExp());
        assertEquals(0, player.getScore());
        assertEquals(100, player.getMoney());
        assertNull(player.getUpgrades());
        assertNull(player.getTeam());
        assertNull(player.getBase());
   }
   
   @Test public void testSetters()
   {
       IPlayer player = new Player("Rickske", 10);
   }
}

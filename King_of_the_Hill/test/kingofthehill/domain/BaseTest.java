/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dennis
 */
public class BaseTest 
{
    @Test public void testGetters()
    {
        /**
         * Test the getters by creating some classe and check their values
         */
        
        // default value
        IPlayer player = new Player("Henkie", 10);
        Base b = new Base(player);
        
        // 1: normal test
        assertEquals(100, b.getHealthPoints());
        assertEquals(player, b.getOwner());
        assertNull(b.getLane(0));
        assertNull(b.getLane(8));
        assertNull(b.getUnit(0));
        assertNull(b.getUnit(32));
           
        // 2: fail test
        try
        {
            b = new Base(null);
            fail("Owner cannot be null");
        }
        catch (IllegalArgumentException exc) {}
        
        // 3: fail test
        assertNull(b.getLane(-1));
        assertNull(b.getLane(8));
        assertNull(b.getUnit(-1));
        assertNull(b.getUnit(32));
    }
    
    @Test public void testSetters()
    {
        /**
         * Test the setters by creating some classe and check their values
         */
        
        // default value
        String name = "Henkie";
        
        IPlayer player1 = new Player(name, 10);
        IPlayer player2 = new AI(name);
        Base b1 = new Base(player1);
        Base b2 = new Base(player2);
        Lane l = new Lane(b1, b2);
        b1.setLane(0, l);
        Unit u = new Melee(20, 20, 20, 20, player1);
        b1.setUnit(0, u);
        
        // 1: normal test
        assertEquals(1, b1.getLanes().size());
        assertEquals(1, b1.getUnits().size());
        assertEquals(l, b1.getLane(0));
        assertEquals(u, b1.getUnit(0));
        
        // fail test  
        assertFalse(b1.setLane(-1, l));
        assertFalse(b1.setUnit(-1, u));
        assertFalse(b1.setLane(0, null));
        assertFalse(b1.setUnit(0, null));      
    }
    
    @Test public void testRemoveUnit()
    {
        /**
         * Test the remove unit methode
         */
        
        // default value
        String name = "Frenkie";
        
        IPlayer player1 = new Player(name, 10);
        IPlayer player2 = new AI(name);
        Base b1 = new Base(player1);
        Base b2 = new Base(player2);
        Lane l = new Lane(b1, b2);
        b1.setLane(0, l);
        Unit u = new Melee(20, 20, 20, 20, player1);
        b1.setUnit(0, u);      
        
        // 1: normal test
        assertEquals(1, b1.getUnits().size());
        b1.removeUnit(u);
        assertEquals(0, b1.getUnits().size());
        
        // 2: remove unit that is null
        b1.setUnit(0, u);
        b1.removeUnit(null);
        assertEquals(1, b1.getUnits().size());
        
        // 3: remove unit that doesnt exist in b1
        u = new Ranged(20, 20, 20, 20, player2, 10);
        b1.removeUnit(u);
        assertEquals(1, b1.getUnits().size());
    }
    
    @Test public void receiveDamage()
    {
        /**
         * Test the reveive damage methode
         */
        
        // default value
        IPlayer player = new Player("Boer Geert", 10);
        Base b = new Base(player);
        
        // 1: normal test
        assertEquals(100, b.getHealthPoints());
        b.receiveDamage(10);
        assertEquals(90, b.getHealthPoints());
        b.receiveDamage(91);
        assertEquals(0, b.getHealthPoints());
        
        // 2: negative damage
        b.receiveDamage(-1);
        assertEquals(0, b.getHealthPoints());     
    }
}

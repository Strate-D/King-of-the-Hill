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
 * @author Bas
 */
public class UnitTest
{
    @Test public void testGetters()
    {
    }
    
    @Test public void testSetters()
    {
        /**
         * Test the following setters
         * - setLane()
         * - setBase()
         */
        
        // default value
        IPlayer ai1 = new AI("computer1");
        IPlayer player1 = new Player("speler1", 10);
        Base b1 = new Base(ai1);
        Base b2 = new Base(player1);
        Lane l = new Lane(b1, b2);
        
        // 1: normal test
        Unit u1 = new Melee(10, 10, 10, 10, ai1);
        u1.setLane(l);
        assertEquals(l, u1.getLane());
        assertNull(u1.getBase());
        
        // 2: normal test
        Unit u2 = new Defence(20, 20, 20, 20, player1);
        u2.setLane(l);
        assertEquals(null, u2.getBase());
        assertEquals(100, u2.getPosition());
        
        // 3: fail test
        u2.setBase(b1);
        if(b1.equals(u2.getBase()))
            fail("Unit base is the base of an enemy player");
        
        // 4: fail test
        u1 = new Melee(15, 20, 13, 21, player1);
        try
        {
            u1.setLane(null);
            if(u1.getLane() == null )
                fail("Unit lane cannot be set to null");
        }
        catch (NullPointerException ecx)
        {
        }
        
        // 5: fail test
        try
        {
            u1.setBase(null);
            if(u1.getBase() == null)
                fail("Unit base cannot be set to null");
        }
        catch(NullPointerException ecx)
        {
        }
    }
    
    @Test public void testKillUnit()
    {
        /**
         * Test the method KillUnit with the 3 unit classes
         */
        
        // default value
        IPlayer ai = new AI("computer1");
        IPlayer player = new Player("speler1", 10);
        Unit m1 = new Melee(10, 10, 10, 10, ai);
        Unit m2 = new Melee(11, 11, 11, 11, player);
        Unit r1 = new Ranged(12, 12, 12, 12, ai, 12);
        Unit r2 = new Ranged(13, 13, 13, 13, player, 13);
        Unit d1 = new Defence(14, 14, 14, 14, ai);
        Unit d2 = new Defence(15, 15, 15 ,15, player);
        // Melee Unit Tests
        // m1
        try {
            m1.receiveDamage(-3);
                fail("Unit can't take negative damage");
            
        }
        catch(IllegalArgumentException e){}
        assertFalse(m1.receiveDamage(1)); // False Melee Unit doesn't die.
        assertTrue(m1.receiveDamage(1000)); // True Melee Unit dies.
        //assertNull(m1); // m1 should be null since it died.
        // m2
        try {
            m2.receiveDamage(-3);
                fail("Unit can't take negative damage");
            
        }
        catch(IllegalArgumentException e){}
        assertFalse(m2.receiveDamage(20)); // False Melee Unit barely doesn't die
        assertTrue(m2.receiveDamage(21)); // True Melee Unit dies.
        //assertNull(m2); // m2 should be null since it died.

        // Ranged Unit Tests  
        // r1
        try{
            r1.receiveDamage(-5);
            fail("Unit can't take negative damage");
        }
        catch (IllegalArgumentException e) {}
        assertFalse(r1.receiveDamage(0)); // False Ranged Unit doesn't die.
        assertTrue(r1.receiveDamage(500)); // True Ranged Unit dies.
        //assertNull(r1); // r1 should be null since it died.
        // r2
        try{
            r2.receiveDamage(-25);
            fail("Unit can't take negative damage");
        }
        catch (IllegalArgumentException e) {}
        assertFalse(r2.receiveDamage(22)); // False Ranged Unit barely doesn't die.
        assertTrue(r2.receiveDamage(18)); // True Ranged Unit dies.
        //assertNull(r2); // r2 should be null since it died.
        
        // Defence Unit Tests  
        // d1
        try{
           d1.receiveDamage(-5);
               fail("Unit can't take negative damage");
           
        }
        catch (IllegalArgumentException e) {}
        assertFalse(d1.receiveDamage(16)); // False Ranged Unit doesn't die.
        assertTrue(d1.receiveDamage(500)); // True Ranged Unit dies.
        //assertNull(d1); // r1 should be null since it died.
        // d2
        try{
           d2.receiveDamage(-15);
               fail("Unit can't take negative damage");
           
        }
        catch (IllegalArgumentException e) {}
        assertFalse(d2.receiveDamage(23)); // False Ranged Unit barely doesn't die.
        assertTrue(d2.receiveDamage(50)); // True Ranged Unit dies.
        //assertNull(d2); // r2 should be null since it died.
        
    }
    
    @Test public void testRecieveDamage()
    {
        /**
         * Test the method KillUnit with the 3 unit classes
         */
        
        // default value
        IPlayer ai = new AI("computer1");
        IPlayer player = new Player("speler1", 10);
        Unit m1 = new Melee(10, 10, 10, 10, ai);
        Unit m2 = new Melee(11, 11, 11, 11, player);
        Unit r1 = new Ranged(12, 12, 12, 12, ai, 12);
        Unit r2 = new Ranged(13, 13, 13, 13, player, 13);
        Unit d1 = new Defence(14, 14, 14, 14, ai);
        Unit d2 = new Defence(15, 15, 15 ,15, player);
        // Melee Unit Tests
        // m1
        try {
            m1.receiveDamage(-3);
            fail("Unit can't take negative damage");
            
        }
        catch(IllegalArgumentException e){}
        m1.receiveDamage(11);
        assertEquals(9, m1.getHealth() - m1.getDamage()); 
        
        // Ranged Unit Tests
        // r1
        try {
            r1.receiveDamage(-6);
            fail("Unit can't take negative damage");
        }
        catch(IllegalArgumentException e) {}
        r1.receiveDamage(15);
        assertEquals(9, r1.getHealth() - r1.getDamage());
        
        // Defence Unit Tests
        // d1
        try {
            d1.receiveDamage(-2);
            fail("Unit can't take negative damage");
        }
        catch(IllegalArgumentException e){}
        d1.receiveDamage(19);
        assertEquals(9, d1.getHealth() - d1.getDamage());
        // d2
        try {
            d2.receiveDamage(-124);
            fail("Unit can't take negative damage");
        }
        catch(IllegalArgumentException e){}
        d2.receiveDamage(20);
        assertEquals(10, d2.getHealth() - d2.getDamage());
    }
    
    @Test public void testMoveUnit()
    {
    }
}

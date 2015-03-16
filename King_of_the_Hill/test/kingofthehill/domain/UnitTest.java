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
        u2.setBase(b2);
        assertEquals(b2, u2.getBase());
        assertNull(u2.getLane());
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
        Melee m1 = new Melee(10, 10, 10, 10, ai);
        Melee m2 = new Melee(11, 11, 11, 11, player);
        Ranged r1 = new Ranged(12, 12, 12, 12, ai, 12);
        Ranged r2 = new Ranged(13, 13, 13, 13, player, 13);
        Defence d1 = new Defence(14, 14, 14, 14, ai);
        Defence d2 = new Defence(15, 15, 15 ,15, player);
    }
    
    @Test public void testRecieveDamage()
    {
    }
    
    @Test public void testMoveUnit()
    {
    }
}

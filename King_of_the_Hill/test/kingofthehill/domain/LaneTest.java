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
public class LaneTest 
{
    @Test public void testGetters()
    {
        /**
        * Test the getters by creating some objects and check their values
        */
       
        // default value
        Base base1 = new Base(new Player("Henkie", 1337));
        Base base2 = new Base(new Player("Frenkie", 2123));
        Lane l = new Lane(base1, base2);
        
        // 1: normal test
        assertEquals(base1, l.getBaseEnd1());
        assertEquals(base2, l.getBaseEnd2());
        assertEquals(0, l.getUnits().size());
        
        // 2: fail test
        try{
            Lane l2 = new Lane(null, base2);
            fail("baseEnd1 cannot be null");
        }
        catch(IllegalArgumentException exc) {}
        
        // 3: fail test
        try{
            Lane l2 = new Lane(base1, null);
            fail("baseEnd2 cannot be null");
        }
        catch(IllegalArgumentException exc) {}
    }
    
    @Test public void testAddUnit()
    {
        /**
        * Test adding a unit to a lane and check their values
        */
        
        // default value
        IPlayer player1 = new AI("Jurske");
        Base base1 = new Base(player1);
        Base base2 = new Base(new Player("Baske", 2123));
        Lane l = new Lane(base1, base2);
        Unit u = new Defence(12, 12, 12, 12, player1);
        
        // 1: normal test
        assertEquals(0, l.getUnits().size());
        l.addUnit(u);
        assertEquals(1, l.getUnits().size());
        
        // 2; fail test
        l.addUnit(null);
        assertEquals(1, l.getUnits().size());
    }
    
    @Test public void testRemoveUnit()
    {
        /**
        * Test removing a unit to a lane and check their values
        */
        
        // default value
        IPlayer player1 = new AI("Koek");
        Base base1 = new Base(player1);
        Base base2 = new Base(new Player("Dennis", 2123));
        Lane l = new Lane(base1, base2);
        Unit u = new Defence(12, 12, 12, 12, player1);
        l.addUnit(u);
        l.addUnit(u);
        
        // 1: normal test
        l.removeUnit(u);
        assertEquals(1, l.getUnits().size());
        
        // 2: fail test
        l.removeUnit(null);
        assertEquals(1, l.getUnits().size());
    }
}

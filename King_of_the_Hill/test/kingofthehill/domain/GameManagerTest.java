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
public class GameManagerTest 
{
    @Test public void testGetters()
    {
        /**
         * Test the constructor and the getPlayers() method
         */
        
        // normal test
        GameManager gm = new GameManager(new Player("Bas", 100));
        assertEquals(new Player("Bas", 100), gm.getPlayers().get(0));
        assertEquals(new AI("AI1"), gm.getPlayers().get(1));
        assertEquals(new AI("AI2"), gm.getPlayers().get(2));
        assertEquals(new AI("AI3"), gm.getPlayers().get(3));
        
        assertEquals(1, gm.getPlayers().get(0).getTeam().getNr());
        assertEquals(2, gm.getPlayers().get(1).getTeam().getNr());
        assertEquals(1, gm.getPlayers().get(2).getTeam().getNr());
        assertEquals(2, gm.getPlayers().get(3).getTeam().getNr());
        
        // normal test
        gm = new GameManager(new AI("AI0"));
        assertEquals(new AI("AI0"), gm.getPlayers().get(0));
        assertEquals(1, gm.getPlayers().get(0).getTeam().getNr());
        
        // fail test
        try
        {
            gm = new GameManager(null);
        }
        catch (NullPointerException ecx) {}
    }
    
    @Test public void testGenerateMysterybox()
    {
        fail("No javadoc written!");
    }
    
    @Test public void testPlaceUnitAtLane()
    {
        // create values
        IPlayer ai = new AI("AI1");
        Unit def = new Defence(10, 10, 10, 10, ai);
        GameManager gm = new GameManager(new Player("Bas", 10));
        
        // normal test
        assertFalse(gm.placeUnitAtLane(ai, def, 0, 100));
        
        // normal test
        assertTrue(gm.placeUnitAtLane(ai, def, 1, 200));
        
        // fail test : null value
        try
        {
            gm.placeUnitAtLane(null, null, 2, 10);
            fail("Values cannot be null");
        }
        catch (NullPointerException ecx) {}
    }
    
    @Test public void testPlaceUnitAtBase()
    {
        // create values
        IPlayer ai = new AI("AI1");
        Unit def = new Defence(10, 10, 10, 10, ai);
        GameManager gm = new GameManager(new Player("Bas", 10));
        
        // normal test
        assertFalse(gm.placeUnitAtBase(ai, def, 1, 100));
        
        // normal test
        assertTrue(gm.placeUnitAtBase(ai, def, 1, 200));
        
        // fail test : null value
        try
        {
            gm.placeUnitAtBase(null, null, 2, 10);
            fail("Values cannot be null");
        }
        catch (NullPointerException ecx) {}
    }
}

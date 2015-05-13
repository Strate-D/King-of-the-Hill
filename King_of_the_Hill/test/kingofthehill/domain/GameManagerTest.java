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
        
        //default value
        Player bas = new Player("Bas", 100);
        GameManager gm = new GameManager(bas);
                
        // normal test
        assertEquals(bas, gm.getPlayers().get(0));
        assertEquals(4, gm.getPlayers().size());
        
        assertEquals(1, gm.getPlayers().get(0).getTeam().getNr());
        assertEquals(2, gm.getPlayers().get(1).getTeam().getNr());
        assertEquals(1, gm.getPlayers().get(2).getTeam().getNr());
        assertEquals(2, gm.getPlayers().get(3).getTeam().getNr());
        
        // normal test
        AI rick = new AI("Rick");
        gm = new GameManager(rick);
        assertEquals(rick, gm.getPlayers().get(0));
        assertEquals(1, gm.getPlayers().get(0).getTeam().getNr());
        
        // fail test
        try
        {
            gm = new GameManager(null);
            fail("GameManager cannot have an null player");
        }
        catch (IllegalArgumentException ecx) {}
    }

    @Test public void testPlaceUnitAtLane()
    {
        // create values
        IPlayer ai = new AI("AI1");
        Unit def = new Defence(10, 10, 10, ai);
        GameManager gm = new GameManager(new Player("Bas", 10));
        Base base1 = new Base(ai);
        IPlayer player = new Player("Henkie", 100);
        Base base2 = new Base(player);
        Lane lane = new Lane(base1, base2);
        
        // normal test
        assertFalse(gm.placeUnitAtLane(ai, def, 0, 100));
        ai.setBase(base1);
        assertFalse(gm.placeUnitAtLane(ai, def, 0, 100));
        base1.setLane(0, lane);   
        assertTrue(gm.placeUnitAtLane(ai, def, 0, 100));
        
        // normal test NO MUNNIE
        assertFalse(gm.placeUnitAtLane(ai, def, 1, 200));
        
        // fail test : null value
        assertFalse(gm.placeUnitAtLane(null, null, 2, 10));
    }
    
    @Test public void testPlaceUnitAtBase()
    {
        // create values
        IPlayer ai = new AI("AI1");
        Unit def = new Defence(10, 10, 10, ai);
        GameManager gm = new GameManager(new Player("Bas", 10));
        Base base = new Base(ai);
        
        // normal test
        assertFalse(gm.placeUnitAtBase(ai, def, 1, 100));
        ai.setBase(base);
        assertTrue(gm.placeUnitAtBase(ai, def, 1, 100));
        
        // normal test
        assertFalse(gm.placeUnitAtBase(ai, def, 1, 200));
        
        // fail test : null value
        assertFalse(gm.placeUnitAtBase(null, null, 2, 10));
    }
}

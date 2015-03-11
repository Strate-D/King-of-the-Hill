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
public class GameModeTest 
{
    @Test public void testGameMode()
    {
        /**
         * Test if the enum returns the correct values when requested
         */
        
        GameMode gm = GameMode.COOP;
        
        // 1: normal value
        assertEquals(GameMode.COOP.getDescription(), gm.getDescription());
        assertEquals("2vs2 co-op", gm.getDescription());
        assertEquals(GameMode.COOP, gm);
        
        // 2: another normal value
        gm = GameMode.F4A;
        assertEquals(GameMode.F4A.getDescription(), gm.getDescription());
        assertEquals("Free for All", gm.getDescription());
        assertEquals(GameMode.F4A, gm);
        
        // 3: another normal value
        gm = GameMode.SINGLEPLAYER;
        assertEquals(GameMode.SINGLEPLAYER.getDescription(), gm.getDescription());
        assertEquals("Singleplayer vs AI", gm.getDescription());
        assertEquals(GameMode.SINGLEPLAYER, gm);
    }
}

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
public class BaseTest 
{
    @Test public void testGetters()
    {
        /**
         * Test the getters by creating some classe and check their values
         */
        
        // 1: normal value
        IPlayer player = new Player("Henkie", 10);
        Base b = new Base(player);
        assertEquals(player, b.getOwner());
        assertEquals(100, b.getHealthPoints());
        
        // 2: fail test
        try
        {
            b = new Base(null);
            fail("Owner cannot be null");
        }
        catch (IllegalArgumentException exc)
        {
        }
    }
    
    @Test public void testSetters()
    {
        
    }
    
    @Test public void testRemoveUnit()
    {
        
    }
}

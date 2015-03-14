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
public class AITest 
{
    @Test public void testGetters()
    {
    }
    
    @Test public void testSetters()
    {
    }
    
    @Test public void testCheckPassword()
    {
        /**
         * Test the password check
         */
        
        // default value
        
        IPlayer ai = new AI();
        ai.checkPassword("Welkom01");
    }
}

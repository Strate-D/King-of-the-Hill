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
         /**
         * Test the password check
         */
        
        // default value
        IPlayer ai = new AI();
        
        // 1: normal value
        Team team1 = new Team(1, null);
        ai.setTeam(team1);
        assertEquals(team1, ai.getTeam());
        assertEquals(team1.getNr(), ai.getTeam().getNr());
        
        // 2: normal value
        
    }
    
    @Test public void testCheckPassword()
    {
        /**
         * Test the password check
         */
        
        // default value
        IPlayer ai = new AI();
        
        // 1: normal value
        assertEquals(true, ai.checkPassword("Welkom01"));
        
        // 2: another normal value
        assertEquals(true, ai.checkPassword("Dit is een test wachtwoord!"));
    }
}

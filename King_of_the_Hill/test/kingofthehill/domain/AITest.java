/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.ArrayList;
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
        /**
         * Test the following getters
         * - getName()
         * - getExp()
         * - getScore()
         * - getUpgrades()
         * - getMoney()
         * - getTeam()
         * - getBase()
         */
        
        // 1: normal test
        IPlayer player1 = new AI("computer1");
        assertEquals(12, player1.getExp());
        assertEquals(0, player1.getScore());
        assertEquals(new ArrayList<>(), player1.getUpgrades());
        assertEquals(null, player1.getTeam());
        assertEquals(100, player1.getMoney());
        assertEquals(null, player1.getBase());
        
        // 2: fail test
        try
        {
            IPlayer player2 = new AI("");
            fail("AI name cannot be empty!");
        }
        catch (IllegalArgumentException ecx)
        {
        }
    }
    
    @Test public void testSetters()
    {
         /**
         * Test the following setters
         *  - setTeam
         *  - setBase
         */
        
        // default value
        IPlayer ai = new AI("computer1");
        
        // 1: normal test
        Team team1 = new Team(1, new ArrayList<>());
        ai.setTeam(team1);
        assertEquals(team1, ai.getTeam());
        assertEquals(team1.getNr(), ai.getTeam().getNr());
        
        // 2: normal test
        Base base = new Base(ai);
        ai.setBase(base);
        assertEquals(base, ai.getBase());
        
        // 3: fail test
        try
        {
            ai.setTeam(null);
            fail("Team cannot be null");
        }
        catch (IllegalArgumentException ecx) {}
        
        // 4: fail test
        try
        {
            ai.setBase(null);
            fail("Base cannot be null");
        }
        catch (IllegalArgumentException ecx) {}
    }
    
    @Test public void testCheckPassword()
    {
        /**
         * Test the password check
         */
        
        // default value
        IPlayer ai = new AI("computer1");
        
        // 1: normal value
        assertEquals(true, ai.checkPassword("Welkom01"));
        
        // 2: another normal value
        assertEquals(true, ai.checkPassword("Dit is een test wachtwoord!"));
    }
    
    @Test public void testAddMoney()
    {
        /**
         * Test the addMoney method
         */
        
        // default value
        IPlayer ai = new AI("ai1");
        
        // 1: normal test
        ai.addMoney(10);
        assertEquals(110, ai.getMoney());
        
        // 2: normal test
        ai.addMoney(100);
        assertEquals(210, ai.getMoney());
        
        // 3: fail test
        ai.addMoney(-10);
        assertEquals(210, ai.getMoney());
    }
    
    @Test public void testPayMoney()
    {
        /** 
         * Test the payMoney method
         */
        
        // default value
        IPlayer ai = new AI("ai1");
        
        // 1: normal test
        if(!ai.payMoney(10))
            fail("Not paid");
        assertEquals(90, ai.getMoney());
        
        // 2: normal test
        if(!ai.payMoney(90))
            fail("Not paid");
        assertEquals(0, ai.getMoney());
        
        // 3: normal test
        if(ai.payMoney(10))
            fail("There is no money available");
        assertEquals(0, ai.getMoney());
        
        // 4: fail test
        if(ai.payMoney(-10))
            fail("Not possible to pay with a negative value");
        assertEquals(0, ai.getMoney());
    }
}

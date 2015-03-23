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
 * @author Rick
 */
public class PlayerTest 
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
        IPlayer player1 = new Player("Rick", 0);
        assertEquals(0, player1.getExp());
        assertEquals(0, player1.getScore());
        assertEquals(new ArrayList<>(), player1.getUpgrades());
        assertEquals(null, player1.getTeam());
        assertEquals(100, player1.getMoney());
        assertEquals(null, player1.getBase());
        
        // 2: fail test
        try
        {
            IPlayer player2 = new Player("", 0);
            fail("Speler name cannot be empty!");
        }
        catch (IllegalArgumentException exc)
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
        IPlayer player1 = new Player("Bas", 10);
        
        // 1: normal test
        Team team1 = new Team(1, new ArrayList<>());
        player1.setTeam(team1);
        assertEquals(team1, player1.getTeam());
        assertEquals(team1.getNr(), player1.getTeam().getNr());
        
        // 2: normal test
        Base base = new Base(player1);
        player1.setBase(base);
        assertEquals(base, player1.getBase());
        
        // 3: fail test
        try
        {
            team1 = new Team(1, null);
            fail ("Playerlist cannot be null");
        }
        catch (NullPointerException ecx) {}
        
        // 4: fail test
        try
        {
            team1 = new Team(-1, new ArrayList<>());
            fail("Teamnummer cannot be negative");
        }
        catch (IllegalArgumentException ecx) {}
        
        // 5: fail test
        try
        {
            base = new Base(null);
            fail ("Player cannot be null");
        }
        catch (NullPointerException ecx) {}
    }
    
    @Test public void testCheckPassword()
    {
        /**
         * Test the password check
         */
        
        // default value
        IPlayer player = new Player("Dennis", 0);
        
        // 1: normal value
        assertEquals(true, player.checkPassword("Welkom01"));
        
        // 2: another normal value
        assertEquals(true, player.checkPassword("Dit is een test wachtwoord!"));
    }
    
    @Test public void testAddMoney()
    {
        /**
         * Test the addMoney method
         */
        
        // default value
        IPlayer player = new Player("jur", 10);
        
        // 1: normal test
        player.addMoney(10);
        assertEquals(110, player.getMoney());
        
        // 2: normal test
        player.addMoney(100);
        assertEquals(210, player.getMoney());
        
        // 3: fail test
        player.addMoney(-10);
        assertEquals(210, player.getMoney());
    }
    
    @Test public void testPayMoney()
    {
        /** 
         * Test the payMoney method
         */
        
        // default value
        IPlayer player = new AI("player1");
        
        // 1: normal test
        if(!player.payMoney(10))
            fail("Not paid");
        assertEquals(90, player.getMoney());
        
        // 2: normal test
        if(!player.payMoney(90))
            fail("Not paid");
        assertEquals(0, player.getMoney());
        
        // 3: normal test
        if(player.payMoney(10))
            fail("There is no money available");
        assertEquals(0, player.getMoney());
        
        // 4: fail test
        if(player.payMoney(-10))
            fail("Not possible to pay with a negative value");
        assertEquals(0, player.getMoney());
    }
}

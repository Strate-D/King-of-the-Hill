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
public class MysteryboxTest 
{
    @Test public void testGetters()
    {
        /**
         * The methods will be tested by creating some new classes and set
         * and get the values placed in it
         */
        
        // default value
        Mysterybox mbox;
        IPlayer ai = new AI("computer1");
        
        // 1: normal value
        Upgrade u1 = new Upgrade(10.0, 10.0, 10.0, 10.0, UnitType.ALL);
        mbox = new Mysterybox(0, u1, null, 0);
        assertEquals(0, mbox.getResourceAmount());
        assertEquals(u1, mbox.getUpgrade());
        
        // 2: normal value
        mbox = new Mysterybox(100, null, null, 0);
        assertEquals(100, mbox.getResourceAmount());
        
        // 3: normal value
        UnitType unitType = UnitType.ALL;
        int amount = 1;
        mbox = new Mysterybox(0, null, unitType, amount);
        assertEquals(0, mbox.getResourceAmount());
        assertEquals(unitType, mbox.getUnitType());
        assertEquals(1, mbox.getAmount());
        
        // 4: normal value
        Upgrade u2 = new Upgrade(5.0, 1.0, 6.0, 10.0, UnitType.DEFENCE);
        mbox = new Mysterybox(300, u2, null, 0);
        assertEquals(300, mbox.getResourceAmount());
        assertEquals(u2, mbox.getUpgrade());
             
        // 5: normal value
        Upgrade u3 = new Upgrade(2.0, 4.0, 10.0, 15.0, UnitType.MELEE);
        unitType = UnitType.RANGED;
        mbox = new Mysterybox(0, u3, unitType, 2);
        assertEquals(0, mbox.getResourceAmount());
        assertEquals(u3, mbox.getUpgrade());
        assertEquals(2, mbox.getAmount());
        assertEquals(unitType, mbox.getUnitType());
        
        // 6: normal value
        unitType = UnitType.ALL;
        mbox = new Mysterybox(50, null, unitType, 3);
        assertEquals(50, mbox.getResourceAmount());
        assertEquals(3, mbox.getAmount());
        
        // 7: normal value
        unitType = UnitType.RANGED;
        Upgrade u4 = new Upgrade(5.0, 5.0, 5.0, 5.0, UnitType.RANGED);
        mbox = new Mysterybox(200, u4, unitType, 4);
        assertEquals(200, mbox.getResourceAmount());
        assertEquals(u4, mbox.getUpgrade());
        assertEquals(4, mbox.getAmount());
        assertEquals(unitType, mbox.getUnitType());
        
        // 8: fail value
        try{   
            mbox = new Mysterybox(-10, null, null, 0);
            fail("Resource amount cannot be smaller than 0");
        }
        catch (IllegalArgumentException exc) {}
        
        // 9: fail value
        try{
            unitType = UnitType.MELEE;
            mbox = new Mysterybox(0, null, unitType, 0);
            fail("Amount of units must be 1 or more when unitType is not null");
        }
        catch (IllegalArgumentException exc) {}
        
        // 10: fail value
        try{
            unitType = UnitType.DEFENCE;
            mbox = new Mysterybox(0, null, unitType, -10);
            fail("Amount of units cannot be negative when unitType is not null");
        }
        catch (IllegalArgumentException exc) {}
    }
}

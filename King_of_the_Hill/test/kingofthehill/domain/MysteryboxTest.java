/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.ArrayList;
import java.util.List;
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
        mbox = new Mysterybox(0, u1, null);
        assertEquals(0, mbox.getResourceAmount());
        assertEquals(u1, mbox.getUpgrade());
        
        // 2: normal value
        mbox = new Mysterybox(100, null, null);
        assertEquals(100, mbox.getResourceAmount());
        
        // 3: normal value
        List<Unit> units1 = new ArrayList<>();
        units1.add(new Melee(100, 10, 10, 10, ai));
        mbox = new Mysterybox(0, null, units1);
        assertEquals(0, mbox.getResourceAmount());
        assertEquals(units1, mbox.getUnits());
        assertEquals(units1.get(0), mbox.getUnits().get(0));
        
        // 4: normal value
        Upgrade u2 = new Upgrade(5.0, 1.0, 6.0, 10.0, UnitType.DEFENCE);
        mbox = new Mysterybox(300, u2, null);
        assertEquals(300, mbox.getResourceAmount());
        assertEquals(u2, mbox.getUpgrade());
             
        // 5: normal value
        Upgrade u3 = new Upgrade(2.0, 4.0, 10.0, 15.0, UnitType.MELEE);
        List<Unit> units2 = new ArrayList<>();
        units2.add(new Ranged(100, 10, 10, 10, ai, 20));
        units2.add(new Defence(150, 20, 20, 20, ai));
        mbox = new Mysterybox(0, u3, units2);
        assertEquals(0, mbox.getResourceAmount());
        assertEquals(u3, mbox.getUpgrade());
        assertEquals(units2, mbox.getUnits());
        
        // 6: normal value
        List<Unit> units3 = new ArrayList<>();
        units3.add(new Melee(100, 10, 10, 10, ai));
        units3.add(new Melee(100, 10, 10, 10, ai));
        units3.add(new Melee(100, 10, 10, 10, ai));
        mbox = new Mysterybox(50, null, units3);
        assertEquals(50, mbox.getResourceAmount());
        assertEquals(units3, mbox.getUnits());
        
        // 7: normal value
        List<Unit> units4 = new ArrayList<>();
        units4.add(new Ranged(100, 10, 10, 10, ai, 20));
        units4.add(new Defence(150, 20, 20, 20, ai));
        units4.add(new Melee(200, 10, 10, 10, ai));
        Upgrade u4 = new Upgrade(5.0, 5.0, 5.0, 5.0, UnitType.RANGED);
        mbox = new Mysterybox(200, u4, units4);
        assertEquals(200, mbox.getResourceAmount());
        assertEquals(u4, mbox.getUpgrade());
        assertEquals(units4, mbox.getUnits());
        
        // 8: fail value
        try
        {   
            mbox = new Mysterybox(-10, null, null);
            fail("Resource amount cannot be smaller than 0");
        }
        catch (IllegalArgumentException exc)
        {
        }
    }
}

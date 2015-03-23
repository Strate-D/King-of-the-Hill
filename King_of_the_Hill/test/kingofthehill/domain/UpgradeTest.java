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
public class UpgradeTest 
{
    @Test public void testGetters()
    {
        /**
         * The method will be tested by creating a few classes and reading
         * the value of the getters.
         */
        
        float DELTA = (float) 1e-15;
        
        // 1: normal test (same values, positive)
        
        Upgrade upg = new Upgrade(10.0, 10.0, 10.0, 10.0, UnitType.DEFENCE);
        assertEquals(10.0, upg.getModHealth(), DELTA);
        assertEquals(10.0, upg.getModAttack(), DELTA);
        assertEquals(10.0, upg.getModArmor(), DELTA);
        assertEquals(10.0, upg.getModMovementSpeed(), DELTA);
        assertEquals(UnitType.DEFENCE, upg.getTargetUnit());
        
        // 2: normal test (different values, positive)
        
        upg = new Upgrade(15.2, 12.1, 3.5, 4.0, UnitType.ALL);
        assertEquals(15.2, upg.getModHealth(), DELTA);
        assertEquals(12.1, upg.getModAttack(), DELTA);
        assertEquals(3.5, upg.getModArmor(), DELTA);
        assertEquals(4.0, upg.getModMovementSpeed(), DELTA);
        assertEquals(UnitType.ALL, upg.getTargetUnit());
        
        // 3: normal test (same values, negative)
        
        upg = new Upgrade(-9.0, -9.0, -9.0, -9.0, UnitType.RANGED);
        assertEquals(-9.0, upg.getModHealth(), DELTA);
        assertEquals(-9.0, upg.getModAttack(), DELTA);
        assertEquals(-9.0, upg.getModArmor(), DELTA);
        assertEquals(-9.0, upg.getModMovementSpeed(), DELTA);
        assertEquals(UnitType.RANGED, upg.getTargetUnit());
        
        // 4: normal test (different values, negative)
        
        upg = new Upgrade(-17.8, -2.5, -12.5, -0.5, UnitType.MELEE);
        assertEquals(-17.8, upg.getModHealth(), DELTA);
        assertEquals(-2.5, upg.getModAttack(), DELTA);
        assertEquals(-12.5, upg.getModArmor(), DELTA);
        assertEquals(-0.5, upg.getModMovementSpeed(), DELTA);
        assertEquals(UnitType.MELEE, upg.getTargetUnit());
        
        // 5: normal test (different values, positive and negative, 
        //                 value equal to zero)
        
        upg = new Upgrade(15.8, -2.5, 37.5, 0.0, UnitType.ALL);
        assertEquals(15.8, upg.getModHealth(), DELTA);
        assertEquals(-2.5, upg.getModAttack(), DELTA);
        assertEquals(37.5, upg.getModArmor(), DELTA);
        assertEquals(0.0, upg.getModMovementSpeed(), DELTA);
        assertEquals(UnitType.ALL, upg.getTargetUnit());
        
        // 6: normal test (different values, positive and negative, 
        //                 value equals to zero, multiple decimals
        
        upg = new Upgrade(1.25, 12.63, 0.0, -22.153, UnitType.RANGED);
        assertEquals(1.25, upg.getModHealth(), DELTA);
        assertEquals(12.63, upg.getModAttack(), DELTA);
        assertEquals(0.0, upg.getModArmor(), DELTA);
        assertEquals(-22.153, upg.getModMovementSpeed(), DELTA);
        assertEquals(UnitType.RANGED, upg.getTargetUnit());
        
        // 7: fail test (set null values)
        Double d = null;
        try
        {
            upg = new Upgrade(d, d, d, d, UnitType.RANGED);
            fail("(null) values are not accepted");
        }
        catch (IllegalArgumentException exc)
        {
        }
        
        // *: fail test : null value
        try
        {
            upg = new Upgrade(d, d, d, d, UnitType.RANGED);
            fail("(null) values are not accepted");
        }
        catch (IllegalArgumentException exc)
        {
        }
    }
}

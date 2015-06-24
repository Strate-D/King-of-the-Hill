/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.unitinfo;

import org.junit.Test;
import static org.junit.Assert.*;
import kingofthehill.domain.UnitType;

/**
 *
 * @author rick
 */
public class UnitsToSpawnTest {

    @Test
    public void testGetters() {

        //normal tests
        UnitsToSpawn units = new UnitsToSpawn(1, UnitType.MELEE);
        assertEquals(UnitType.MELEE, units.getUnitType());
        assertEquals(1, units.getSpawnPoint());

        //fail tests
        try {
            UnitsToSpawn units2 = new UnitsToSpawn(-1, UnitType.MELEE);
            fail("spawnpoint cannot be negative");
        } catch (IllegalArgumentException exc) {
        }
        try {
            UnitsToSpawn units3 = new UnitsToSpawn(33, UnitType.MELEE);
            fail("there are only 32 spawnpoint");
        } catch (IllegalArgumentException exc) {
        }


    }
}

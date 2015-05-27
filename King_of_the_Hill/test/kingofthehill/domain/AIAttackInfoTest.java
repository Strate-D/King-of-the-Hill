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
 * @author rick
 */
public class AIAttackInfoTest {

    @Test
    public void TestGetters() {

        //normal test
        AIAttackInfo testInfo = new AIAttackInfo(1);
        assertEquals(1, testInfo.getLane());
        assertEquals(0, testInfo.getDefence());
        assertEquals(0, testInfo.getDefendingUnits());
        assertEquals(0, testInfo.getUpcomingUnits());

        //fail test
        try {
            AIAttackInfo testInfo2 = new AIAttackInfo(9);
            fail("Lane has to be between 1 and 8");
        } catch (IllegalArgumentException exc) {

        }
    }
    @Test
    public void TestSetters() {
        
        //normal test
        AIAttackInfo testInfo = new AIAttackInfo(1);
        testInfo.setDefence(1);
        assertEquals(testInfo.getDefence(), 1);
        testInfo.setDefendingUnits(1);
        assertEquals(testInfo.getDefendingUnits(), 1);
        testInfo.setUpcomingUnits(1);
        assertEquals(testInfo.getUpcomingUnits(), 1);
        
        
        //fail test
        try{
            testInfo.setDefence(-1);
            fail("Defence has to be greater then zero");
        } catch(IllegalArgumentException exc){
            
        }
       try{
            testInfo.setUpcomingUnits(-1);
            fail("Upcoming units has to be greater then zero");
        } catch(IllegalArgumentException exc){
            
        }
       try{
            testInfo.setDefendingUnits(-1);
            fail("Defending units has to be greater then zero");
        } catch(IllegalArgumentException exc){
            
        }
    }
}

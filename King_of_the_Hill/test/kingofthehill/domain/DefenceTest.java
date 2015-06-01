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
public class DefenceTest {

    @Test
    public void TestGetters() {

        IPlayer player1 = new AI("computer1");
        Unit defence = new Defence(1, 1, 1, player1);
        defence.setPosition(1);
        assertEquals(1, defence.getPosition());
    }

    @Test
    public void TestCanAttack() {
        try {
            IPlayer player1 = new AI("computer1");
            Unit defence = new Defence(1, 1, 1, player1);
            defence.canAttackUnit();
        } catch (Exception exc) {
            fail("unit couldn't attack");
        }
    }
}

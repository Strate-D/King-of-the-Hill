/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

/**
 * Class containing all the info about a Defencive unit. Extends Unit.
 * @author Jur
 */
public class Defence extends Unit{
    //TODO
    public Defence(int health, int attack, int armor, UnitType unittype, int movementSpeed, IPlayer owner) {
        super(health, attack, armor, unittype, movementSpeed, owner);
        //TODO
    }

    @Override
    public void doNextAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Unit canAttackUnit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

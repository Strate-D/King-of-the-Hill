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
    /**
     * Creates a new defensive unit with the given parameters. Unit has to be set to
     * a lane or base manually!
     * 
     * @param health Amount of health the unit has. Must be positive.
     * @param attack Amount of attack the unit has. Must be positive.
     * @param armor Amount of armor the unit has. Must be 0 or positive.
     * @param movementSpeed Movementspeed of the unit. Must be positive.
     * @param owner Owner of the unit, may not be null.    
    */
    public Defence(int health, int attack, int armor, int movementSpeed, IPlayer owner) {
        super(health, attack, armor, UnitType.DEFENCE, movementSpeed, owner);
        //TODO
    }

    @Override
    public void doNextAction() {
        Unit targetUnit = this.canAttackUnit();
        if (targetUnit != null){
            targetUnit.receiveDamage(this.getAttack());
            
            if(targetUnit.canAttackUnit() == this) {
                this.receiveDamage(targetUnit.getAttack());
            }
        }
        
    }

    @Override
    public Unit canAttackUnit() {
        return null;
        //return u;
    }
}

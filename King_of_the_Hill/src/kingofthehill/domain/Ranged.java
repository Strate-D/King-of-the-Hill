/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

/**
 *
 * @author Jur
 */
public class Ranged extends Unit {

    private final int attackRange;
    
    public Ranged(int health, int attack, int armor,
            int movementSpeed, IPlayer owner, int attackRange){
        super(health, attack, armor, UnitType.RANGED, movementSpeed, owner);
        this.attackRange = attackRange;
    }

    @Override
    public void doNextAction() {
        Unit targetUnit = this.canAttackUnit();
        //Check if it is possible to attack
        if(targetUnit != null){
            //Deal damage
            targetUnit.receiveDamage(this.getAttack());
            //Check if target unit can attack back, to make combat fair
            if(targetUnit.canAttackUnit() == this){
                this.receiveDamage(targetUnit.getAttack());
            }
        } else{
            this.moveUnit();
        }
    }

    @Override
    public Unit canAttackUnit() {
        Lane lane = this.getLane();
        if (lane == null) {
            return null;
        } else {
            IPlayer owner = this.getOwner();
            //Check to which side the unit is moving
            //and find the closest unit
            int closestDistance = -1;
            Unit closestUnit = null;
            if (lane.getBaseEnd1().getOwner() == owner) {
                for (Unit u : lane.getUnits()) {
                    if ((u.getPosition() - this.getPosition() < closestDistance
                            || closestDistance == -1)) {
                        closestDistance = u.getPosition() - this.getPosition();
                        closestUnit = u;
                    }
                }
            } else {
                for (Unit u : lane.getUnits()) {
                    if ((this.getPosition() - u.getPosition() < closestDistance
                            || closestDistance == -1)) {
                        closestDistance = u.getPosition() - this.getPosition();
                        closestUnit = u;
                    }
                }
            }
            //Check if the unit is within attack range
            if (closestDistance != -1 && closestDistance <= this.attackRange) {
                return closestUnit;
            }
            return null;
        }
    }

}

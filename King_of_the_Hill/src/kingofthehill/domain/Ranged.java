/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

/**
 * Class containing all the information about the ranged unit Extends Unit
 *
 * @author Jur
 */
public class Ranged extends Unit {

    private final int attackRange;
    private int lastAction;

    /**
     * Creates a new ranged unit with the given parameters. Unit has to be set
     * to a lane or base manually!
     *
     * @param health Amount of health the unit has. Must be positive.
     * @param attack Amount of attack the unit has. Must be positive.
     * @param armor Amount of armor the unit has. Must be 0 or positive.
     * @param movementSpeed Movementspeed of the unit. Must be positive.
     * @param owner Owner of the unit, may not be null.
     * @param attackRange attack range of the unit, must be higher than 1.
     */
    public Ranged(int health, int attack, int armor,
            int movementSpeed, IPlayer owner, int attackRange) {
        super(health, attack, armor, UnitType.RANGED, movementSpeed, owner);
        if (attackRange < 1) {
            throw new IllegalArgumentException("Attackrange must be at least 1!");
        }
        this.attackRange = attackRange;
        this.lastAction = 0;
    }

    @Override
    public void doNextAction() {
        //Check if unit may do an action
        if (lastAction == 0) {
            // Check if the unit is in one of the base spots
            if (this.getBase() != null) {
                Lane newLane = this.getBase().getLane(this);
                this.getBase().removeUnit(this);
                //this.setLane(newLane);
                newLane.addUnit(this);
            }

            Unit targetUnit = this.canAttackUnit();
            //Check if it is possible to attack
            if (targetUnit != null) {
                //Check if target unit can attack back, to make combat fair
                if (targetUnit.canAttackUnit() == this) {
                    if(this.receiveDamage(targetUnit.getAttack())){
                        targetUnit.getOwner().addPoints(5);
                    }
                }
                //Deal damage
                if(targetUnit.receiveDamage(this.getAttack())){
                    this.getOwner().addPoints(5);
                }
                lastAction = 60;
            } else {
                this.moveUnit();
                lastAction = 2;
            }
        } else {
            lastAction--;
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
                        //Check if unit is not friendly
                        if (u.getOwner() != this.getOwner()) {
                            closestDistance = u.getPosition() - this.getPosition();
                            closestUnit = u;
                        }
                    }
                }
            } else {
                for (Unit u : lane.getUnits()) {
                    if ((this.getPosition() - u.getPosition() < closestDistance
                            || closestDistance == -1)) {
                        //Check if unit is not friendly
                        if (u.getOwner() != this.getOwner()) {
                            closestDistance = this.getPosition() - u.getPosition();
                            closestUnit = u;
                        }
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

/**
 *
 */
package kingofthehill.domain;

/**
 * Class containing all the information about a Melee unit. Extends Unit.
 *
 * @author Jur
 */
public class Melee extends Unit {

    int lastAction;

    /**
     * Creates a new melee unit with the given parameters. Unit has to be set to
     * a lane or base manually!
     *
     * @param health Amount of health the unit has. Must be positive.
     * @param attack Amount of attack the unit has. Must be positive.
     * @param armor Amount of armor the unit has. Must be 0 or positive.
     * @param movementSpeed Movementspeed of the unit. Must be positive.
     * @param owner Owner of the unit, may not be null.
     */
    public Melee(int health, int attack, int armor, int movementSpeed, IPlayer owner) {
        super(health, attack, armor, UnitType.MELEE, movementSpeed, owner);
        lastAction = 0;
    }

    @Override
    public void doNextAction() {
        /**
         * Check if unit may do an action
         */
        if (lastAction == 0) {
            /**
             * Check if the unit is in one of the base spots
             */
            if (this.getBase() != null) {
                int pos = this.getBase().getUnitIndex(this) % 4;
                Lane newLane = this.getBase().getLane(this);
                this.getBase().removeUnit(this);
                newLane.addUnit(this);
                if (newLane.getBaseEnd1() == this.getOwner().getBase()) {
                    this.setPosition(pos * 55);
                } else {
                    this.setPosition(1000 - pos * 55);
                }
            }

            Unit targetUnit = this.canAttackUnit();
            /**
             * Check if it is possible to attack
             */
            if (targetUnit != null) {
                /**
                 * Check if target unit can attack back, to make combat fair
                 */
                if (targetUnit.canAttackUnit() == this) {
                    if (this.receiveDamage(targetUnit.getAttack())) {
                        targetUnit.getOwner().addPoints(5);
                    }
                }

                /**
                 * Deal damage
                 */
                if (targetUnit.receiveDamage(this.getAttack())) {
                    targetUnit.getOwner().addPoints(-5);
                }
                lastAction = 20;

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
            /**
             * Check to which side the unit is moving and find the closest unit
             */
            int closestDistance = -1;
            Unit closestUnit = null;
            if (lane.getBaseEnd1().getOwner() == owner) {
                //Check for lanes
                for (Unit u : lane.getUnits()) {
                    if ((u.getPosition() - this.getPosition() < closestDistance
                            || closestDistance == -1)) {
                        /**
                         * Check if unit is not friendly
                         */
                        if (u.getOwner() != this.getOwner()) {
                            closestDistance = u.getPosition() - this.getPosition();
                            closestUnit = u;
                        }
                    }
                }
                //Check the defence spots
                for (Unit u : lane.getBaseEnd2().getUnits()) {
                    int index = lane.getBaseEnd2().getUnitIndex(u);
                    int pos = 1000 - (index % 4 * 55);
                    int laneIndex = index / 4;
                    if (this.getLane() == lane.getBaseEnd2().getLane(laneIndex)) {
                        if(pos - this.getPosition() < closestDistance || closestDistance == -1) {
                            closestDistance = pos - this.getPosition();
                            closestUnit = u;
                        }
                    }
                }
            } else {
                for (Unit u : lane.getUnits()) {
                    if ((this.getPosition() - u.getPosition() < closestDistance
                            || closestDistance == -1)) {
                        /**
                         * Check if unit is not friendly
                         */
                        if (u.getOwner() != this.getOwner()) {
                            closestDistance = this.getPosition() - u.getPosition();
                            closestUnit = u;
                        }
                    }
                }
                //Check the defence spots
                for (Unit u : lane.getBaseEnd1().getUnits()) {
                    int index = lane.getBaseEnd1().getUnitIndex(u);
                    int pos = (index % 4 * 55);
                    int laneIndex = index / 4;
                    if (this.getLane() == lane.getBaseEnd1().getLane(laneIndex)) {
                        if(this.getPosition() - pos  < closestDistance || closestDistance == -1) {
                            closestDistance = this.getPosition() - pos; 
                            closestUnit = u;
                        }
                    }
                }
            }
            /**
             * Check if the unit is within attack range
             */
            if (closestDistance != -1 && closestDistance <= 50) {
                return closestUnit;
            }
            return null;
        }
    }

}

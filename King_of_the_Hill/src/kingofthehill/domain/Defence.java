/**
 * 
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
     * @param health Amount of health the unit has. Must be positive.
     * @param attack Amount of attack the unit has. Must be positive.
     * @param armor Amount of armor the unit has. Must be 0 or positive.
     * @param owner Owner of the unit, may not be null.    
    */
    public Defence(int health, int attack, int armor, IPlayer owner) {
        super(health, attack, armor, UnitType.DEFENCE, 1, owner);
    }

    @Override
    public void doNextAction() {
//        Unit targetUnit = this.canAttackUnit();
//        if (targetUnit != null){
//            targetUnit.receiveDamage(this.getAttack());
//            
//            if(targetUnit.canAttackUnit() == this) {
//                this.receiveDamage(targetUnit.getAttack());
//            }
//        }
        //Just sit around
    }

    @Override
    public Unit canAttackUnit() {
        int unitIndex = this.getBase().getUnitIndex(this);
        Lane lane = this.getBase().getLane(unitIndex / 4);
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
                int unitPosition = unitIndex % 4 * 55;
                for (Unit u : lane.getUnits()) {
                    if ((u.getPosition() - unitPosition < closestDistance
                            || closestDistance == -1)) {
                        /**
                         * Check if unit is not friendly
                         */
                        if (u.getOwner() != this.getOwner()) {
                            closestDistance = u.getPosition() - unitPosition;
                            closestUnit = u;
                        }
                    }
                }
            } else {
                int unitPosition = 1000 - (unitIndex % 4 * 55);
                for (Unit u : lane.getUnits()) {
                    if ((unitPosition - u.getPosition() < closestDistance
                            || closestDistance == -1)) {
                        /**
                         * Check if unit is not friendly
                         */
                        if (u.getOwner() != this.getOwner()) {
                            closestDistance = unitPosition - u.getPosition();
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

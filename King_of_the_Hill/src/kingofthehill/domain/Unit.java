/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

/**
 * Abstract class as template for all units
 *
 * @author Jur
 */
public abstract class Unit {

    private int health;
    private int attack;
    private int armor;
    private int cost;
    private UnitType type;
    private int cooldown;
    private int viewRange;
    private int damage;
    private int movementSpeed;
    private IPlayer owner;
    private Base base;
    private Lane lane;

    /**
     * Calculates the health of the unit with all the upgrades
     *
     * @return The health of the unit. Is at least 1.
     */
    public int getHealth() {
        double result = this.health;
        for (Upgrade upg : this.owner.getUpgrades()) {
            if (upg.getTargetUnit() == this.type || upg.getTargetUnit() == UnitType.ALL) {
                result = result * (upg.getModHealth() + 1.00);
            }
        }
        int resultInt = (int) result;
        if (resultInt > 1) {
            return resultInt;
        } else {
            return 1;
        }
    }

    /**
     * Calculates the attack of the unit with all the upgrades
     *
     * @return The attack of the unit. Is at least 1.
     */
    public int getAttack() {
        double result = this.attack;
        for (Upgrade upg : this.owner.getUpgrades()) {
            if (upg.getTargetUnit() == this.type || upg.getTargetUnit() == UnitType.ALL) {
                result = result * (upg.getModAttack() + 1.00);
            }
        }
        int resultInt = (int) result;
        if (resultInt > 1) {
            return resultInt;
        } else {
            return 1;
        }
    }

    /**
     * Calculates the armor of the unit with all the upgrades
     *
     * @return The armor of the unit. Will never be negative
     */
    public int getArmor() {
        double result = this.armor;
        for (Upgrade upg : this.owner.getUpgrades()) {
            if (upg.getTargetUnit() == this.type || upg.getTargetUnit() == UnitType.ALL) {
                result = result * (upg.getModArmor() + 1.00);
            }
        }
        int resultInt = (int) result;
        if (resultInt > 0) {
            return resultInt;
        } else {
            return 0;
        }
    }

    /**
     * Calculates the movement speed of the unit with all the upgrades
     *
     * @return The movement speed of the unit. Will never be negative
     */
    public int getMovementSpeed() {
        double result = this.movementSpeed;
        for (Upgrade upg : this.owner.getUpgrades()) {
            if (upg.getTargetUnit() == this.type || upg.getTargetUnit() == UnitType.ALL) {
                result = result * (upg.getModMovementSpeed() + 1.00);
            }
        }
        int resultInt = (int) result;
        if (resultInt > 0) {
            return resultInt;
        } else {
            return 0;
        }
    }

    public void doNextAction() {
        throw new UnsupportedOperationException("TODO doNextAction()");
    }

    private Unit canAttackUnit() {
        throw new UnsupportedOperationException("TODO canAttackUnit()");
    }

    private boolean killUnit() {
        throw new UnsupportedOperationException("TODO killUnit()");
    }

    public boolean receiveDamage(int damagepoints) {
        int resultingDamage = damagepoints - this.armor;
        if (resultingDamage > 0) {
            this.damage = this.damage + resultingDamage;
        } else {
            this.damage++;
        }
        if(this.damage >= this.getHealth()){
            this.killUnit();
            return true;
        }
        return false;
    }

    /**
     * Returns the cost of the unit
     *
     * @return The cost of the unit. Will always be positive.
     */
    public int getCost() {
        if (this.cost > 0) {
            return this.cost;
        } else {
            return 1;
        }
    }
}

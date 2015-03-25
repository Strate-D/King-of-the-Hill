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
    private int position;
    private IPlayer owner;
    private Base base;
    private Lane lane;
    
    public Unit(int health, int attack, int armor,
            UnitType unittype, int movementSpeed, IPlayer owner){
        if(health < 1 || attack < 1 || armor < 0  || unittype == null || movementSpeed < 0 || owner == null){
            throw new IllegalArgumentException("Unit couldn't be made!");
        }
        this.health = health;
        this.attack = attack;
        this.armor = armor;
        this.type = unittype;
        this.movementSpeed = movementSpeed;
        this.owner = owner;
        this.position = 0;
        this.damage = 0;
    }

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

    /**
     * Let's the unit perform its next action
     */
    public abstract void doNextAction();

    /**
     * Checks if this unit can attack a enemy unit
     *
     * @return The unit it can attack, can be null.
     */
    public abstract Unit canAttackUnit();

    /**
     * Kills this unit
     */
    private void killUnit() {
        if (this.lane != null) {
            this.lane.removeUnit(this);
        }
        if (this.base != null) {
            this.base.removeUnit(this);
        }
    }

    /**
     * Adds a amount of damage to the Unit
     * @param damagepoints Amount of damage that is dealt.
     * @return If the unit is killed or not
     */
    public boolean receiveDamage(int damagepoints) {
        if(damagepoints < 0){
            throw new IllegalArgumentException("Unit can't take negative damage");
        }
        int resultingDamage = damagepoints - this.armor;
        if (resultingDamage > 0) {
            this.damage = this.damage + resultingDamage;
        } else {
            this.damage++;
        }
        if (this.damage >= this.getHealth()) {
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
    /**
     * Returns the damage that was done to the unit
     * @return The damage done to the unit.
     */
    public int getDamage() {
        return this.damage;
    }
    /**
     * Returns the base object of the unit.
     *
     * @return Can be null.
     */
    public Base getBase() {
        return this.base;
    }

    /**
     * Sets the base of the unit If the lane was not null, it will be set to
     * null.
     *
     * @param base The new base of the unit
     */
    public void setBase(Base base) {
        this.base = base;
        this.lane = null;
    }

    /**
     * Sets the new lane of the unit If the base was not null, it will be set to
     * null
     * Also changes the position of the unit as close as possible to his own base.
     * @param lane The new lane of the unit
     */
    public void setLane(Lane lane) {
        this.lane = lane;
        this.base = null;
        
        if (lane.getBaseEnd1().getOwner() == this.getOwner()){
            this.position = 0;
        } else {
            this.position = 100;
        }
    }

    /**
     * Returns the lane object in wich the unit is placed.
     *
     * @return Can be null
     */
    public Lane getLane() {
        return this.lane;
    }

    /**
     * Returns the position of the unit on a lane
     *
     * @return Int between 0 and 100. -1 if unit is placed on a base.
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Gets the cooldown time of a unit
     *
     * @return Always a positive int
     */
    public int getCooldown() {
        if (this.cooldown > 0) {
            return this.cooldown;
        } else {
            return 1;
        }
    }

    /**
     * Gets the view range of the unit
     *
     * @return Always a positive int
     */
    public int getViewRange() {
        if (this.viewRange > 0) {
            return this.viewRange;
        } else {
            return 1;
        }
    }

    /**
     * Gets the owner of the unit.
     *
     * @return Cannot be null
     */
    public IPlayer getOwner() {
        return this.owner;
    }

    /**
     * Moves the unit with use of the movementspeed
     * If the unit reaches the base on the other side,
     * the unit will be destroyed and damage will be dealt.
     */
    public void moveUnit() {
        //Get lane and check wich way the unit has to move and move the unit
        Lane lane = this.getLane();
        if (lane != null) {
            if (lane.getBaseEnd1().getOwner() == this.getOwner()) {
                this.position += this.getMovementSpeed();
            } else {
                this.position -= this.getMovementSpeed();
            }
            //Check if unit has reached the base, if so, do damage
            if (this.position > 100) {
                lane.getBaseEnd2().receiveDamage(this.getAttack());
                this.killUnit();
            } else if (this.position < 0) {
                lane.getBaseEnd1().receiveDamage(this.getAttack());
                this.killUnit();
            }
        }
    }
}

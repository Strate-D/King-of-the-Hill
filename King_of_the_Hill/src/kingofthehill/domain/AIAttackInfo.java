/**
 *
 */
package kingofthehill.domain;

/**
 * The information about a lane and the units placed on the lane can be stored
 * in this object
 *
 * @author Bas
 */
public class AIAttackInfo {

    private int lane;
    private int upcomingUnits;
    private int defendingUnits;
    private int defence;

    /**
     * Creates a new AIAttack object
     *
     * @param lane The lane of this information
     */
    public AIAttackInfo(int lane) {
        this.lane = lane;
        this.upcomingUnits = 0;
        this.defendingUnits = 0;
        this.defence = 0;
    }

    /**
     * Set the upcoming attack units of the enemy
     *
     * @param amount A value bigger than 0 with the amount of units
     */
    public void setUpcomingUnits(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be bigger than zero");
        }

        this.upcomingUnits = amount;
    }

    /**
     * Set the defending unit on the lane. These are the Melee and Ranged unit
     * of the defending player
     *
     * @param amount A value bigger than 0 with the amount of units
     */
    public void setDefendingUnits(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be bigger than zero");
        }
        this.defendingUnits = amount;
    }

    /**
     * Set the defending units at the base on the specified lane.
     *
     * @param amount A value bigger than 0 with the amount of units
     */
    public void setDefence(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be bigger than zero");
        }
        this.defence = amount;
    }

    /**
     * Returns the lane of the object
     *
     * @return The lane index number
     */
    public int getLane() {
        return lane;
    }

    /**
     * Returns the amount of attacking units of the enemy
     *
     * @return The amount of attacking units
     */
    public int getUpcomingUnits() {
        return upcomingUnits;
    }

    /**
     * Returns the amount of defending units of the player at the lane
     *
     * @return The amount of defending units
     */
    public int getDefendingUnits() {
        return defendingUnits;
    }

    /**
     * Returns the amount of defence units at the base of the player
     *
     * @return The amount of defence units
     */
    public int getDefence() {
        return defence;
    }
}

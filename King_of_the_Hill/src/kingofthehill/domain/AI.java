/**
 *
 */
package kingofthehill.domain;

import static java.lang.System.gc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kingofthehill.unitinfo.UnitInfo;
import kingofthehill.unitinfo.UnitsToSpawn;

/**
 * Class containing all the information about a AI. Implements IPlayer. Contains
 * AI algoritm.
 *
 * @author Jur
 */
public class AI implements IPlayer {

    private String name;
    private int exp;
    private int score;
    private ArrayList<Upgrade> upgrades;
    private Team team;
    private int money;
    private Base base;

    /**
     * AI memory for calculating the next step
     */
    private int stepsSinceLastDefence;
    private int stepsSinceLastMelee;
    private int stepsSinceLastRanged;
    private int stepsSinceLastSpawn;
    private int randomSeed = 123456789;
    private AIState aiType;
    private double[] chances;
    private ArrayList<UnitsToSpawn> stillToSpawn;

    /**
     * Creates a new AI object
     *
     * @param name May not be null.
     */
    public AI(String name) {
        /**
         * Check if there was entered a name
         */
        if (name.isEmpty()) {
            throw new IllegalArgumentException("There is no name entered");
        }

        /**
         * Set the initial values of the AI player
         */
        this.name = name;
        this.score = 0;
        this.upgrades = new ArrayList();
        this.team = null;
        this.money = 100;
        this.base = null;

        /**
         * Set the initial values for the AI thinking pattern. Steps: These
         * values form a cooldown so the AI cannot spawn to much units
         * DefenceAtLanes: Stores the defence units currently at each lane
         * AttackAtLanes: Stores the attack units currently in each lane
         * RandomSeed: A value to make the AI a bit more random in his actions
         * StillToSpawn: A list of units that the AI wants to spawn in his
         * upcoming turns
         */
        this.stepsSinceLastDefence = 0;
        this.stepsSinceLastMelee = 0;
        this.stepsSinceLastRanged = 0;
        this.stepsSinceLastSpawn = 0;
        this.randomSeed = this.getNextRandom(0, 999999999);
        setAIType(AIState.DEFENSIVE);
        this.stillToSpawn = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getExp() {
        return this.exp;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public boolean checkPassword(String password) {
        return true;
    }

    @Override
    public void addUpgrade(Upgrade upgrade) {
        if (upgrade != null) {
            upgrades.add(upgrade);
        }
    }

    @Override
    public List<Upgrade> getUpgrades() {
        return Collections.unmodifiableList(upgrades);
    }

    @Override
    public void setTeam(Team newTeam) {
        if (newTeam != null) {
            this.team = newTeam;
        }
    }

    @Override
    public int getMoney() {
        return this.money;
    }

    @Override
    public Team getTeam() {
        return this.team;
    }

    /**
     * Return the amount of defence units at the specified lane
     *
     * @param lane The lane that from wich you want to have the amount of units
     * @return The amount of units on the specified lane
     */
    public int getDefenceAtLane(int lane) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (getBase().getUnit(lane * 4 + i) != null) {
                count++;
            }
        }

        return count;
    }

    /**
     * Returns the amount of attack units at the specified lane
     *
     * @param lane The lane that from wich you want to have the amount of units
     * @return The amount of units on the specified lane
     */
    public int getAttackAtLane(int lane) {
        int count = 0;
        for (int i = 0; i < this.getBase().getLane(lane).getUnits().size(); i++) {
            if (this.getBase().getLane(lane).getUnits().get(i).getOwner() == this) {
                count++;
            }
        }

        return count;
    }

    /**
     * Returns the randomSeed value of the AI
     *
     * @return The random seed value
     */
    public int getRandomSeed() {
        return randomSeed;
    }

    @Override
    public void addMoney(int amount) {
        if (amount > 0) {
            this.money += amount;
        }
    }

    @Override
    public boolean payMoney(int amount) {
        if (amount > 0) {
            if (amount <= this.money) {
                this.money -= amount;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Base getBase() {
        return this.base;
    }

    @Override
    public void setBase(Base newBase) {
        if (newBase != null) {
            this.base = newBase;
        }
    }

    /**
     * Set a new randomSeed
     *
     * @param seed The new random seed to be set
     */
    public void setRandomSeed(int seed) {
        this.randomSeed = seed;
    }

    /**
     * Changes the type of AI. The new type can be Defensive, Modernate or
     * Agressive. When the new type is set, the values for change calculation
     * are set too
     *
     * @param type The new type of AI
     */
    public void setAIType(AIState type) {
        /**
         * Set the values for random choices a.k.a. How much chance is there to
         * spawn defence for AI type Total value must be 100 to work correctly
         * Chances order: Defence, Attack, Upgrade, Mysterybox
         */
        if (type == AIState.DEFENSIVE) {
            this.chances = new double[]{65.0, 20.0, 10.0, 5.0};
        } else if (type == AIState.AGRESSIVE) {
            this.chances = new double[]{12.5, 70.0, 12.5, 5.0};
        } else if (type == AIState.MODERNATE) {
            this.chances = new double[]{40.0, 40.0, 15.0, 5.0};
        }

        /**
         * Check if the chances values are correct
         */
        if (this.chances.length < 4) {
            throw new UnsupportedOperationException("AI must ALWAYS have 4 persentage values");
        }
        if (this.chances[0] + this.chances[1] + this.chances[2] + this.chances[3] != 100) {
            throw new UnsupportedOperationException("AI persentage values must be a total of 100");
        }

        /**
         * Set the AI type
         */
        aiType = type;
    }

    /**
     * Returns the current type of AI
     *
     * @return The current type of the AI
     */
    public AIState getAIType() {
        return aiType;
    }

    @Override
    public void addPoints(int points) {
        this.score += points;
    }

    /**
     * Places a new unit in the list of units that need to be spawned
     *
     * @param unit The type of the new unit
     * @param spawnPoint The location on the base where the unit should be
     * spawned
     */
    public void spawnUnit(UnitType unit, int spawnPoint) {
        this.stillToSpawn.add(new UnitsToSpawn(spawnPoint, unit));
    }

    /**
     * =========================================================================
     * HERE FOLLOWS THE CODE THAT MAKES AN AI THINK ABOUT HIS MOVES !!! DO NOT
     * CHANGE THIS CODE UNLESS YOU KNOW WHAT YOU ARE DOING !!!
     * =========================================================================
     */
    /**
     * The functions calculates the next action that the AI will take to defend
     * itself
     *
     * @param gm The gamemanager object needed to spawn units
     */
    public void doNextAction(GameManager gm) {
        /**
         * Do the following actions: 1. Check if there are units that still need
         * to be placed 2. Place defence at lanes it the AI type allows this 3.
         * Check if being attacked on lane, if so: spawn attack\defence units 4.
         * Decide a next action to do
         *
         * If the AI has spawned units for a specific action, it cannot spawn
         * units for the next action
         */

        /**
         * 1. Place units from que
         */
        spawnUnits(gm);
        if (this.stillToSpawn.isEmpty()) {
            /**
             * 2. Place defence at lanes
             */
            defendBase();

            /**
             * 3. Check if there are attack unit coming
             */
            fightOffEnemyUnits();

            /**
             * 4. Do the next thing you like
             */
            doRandomAction(gm);
        }

        /**
         * The cooldown for placing units
         */
        stepsSinceLastDefence--;
        stepsSinceLastMelee--;
        stepsSinceLastRanged--;
        stepsSinceLastSpawn--;
    }

    /**
     * This method checks if there are units that need to be spawned
     *
     * @param gm The gamemanager object needed to spawn units
     * @return A boolean indicating if there has been spawned a unit
     */
    private boolean spawnUnits(GameManager gm) {
        boolean hasPlacedUnits = false;

        if (stepsSinceLastSpawn > 0) {
            return true;
        } else if (stepsSinceLastSpawn < 0) {
            stepsSinceLastSpawn = 0;
        }

        int index = -1;
        if (this.stillToSpawn.size() > 0) {
            for (int i = 0; i < this.stillToSpawn.size(); i++) {
                if (canPlaceUnit(this.stillToSpawn.get(i).getUnitType())) {
                    createUnitAtLane(this.stillToSpawn.get(i).getUnitType(), gm, this.stillToSpawn.get(i).getSpawnPoint());
                    index = i;
                    hasPlacedUnits = true;
                    stepsSinceLastSpawn = 120;
                    break;
                }
            }
        }

        if (index != -1) {
            this.stillToSpawn.remove(this.stillToSpawn.get(index));
        }

        return hasPlacedUnits;
    }

    /**
     * Check on the lanes if there are units coming towards the base of the AI
     * If there are enemy on their way, the AI will spawn units to defend itself
     */
    private void fightOffEnemyUnits() {
        /**
         * Check on every lane if there are attack units coming towards the base
         * Save the lanes with upcoming units in a list
         */
        ArrayList<AIAttackInfo> AttackInfo = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            AIAttackInfo info = new AIAttackInfo(i);
            AttackInfo.add(info);

            info.setDefendingUnits(this.getAttackAtLane(i));
            info.setUpcomingUnits(this.getBase().getLane(i).getUnits().size() - info.getDefendinUnits());
            info.setDefence(this.getDefenceAtLane(i));
        }

        /**
         * Remove attack info where there are more defending units than
         * attacking units
         */
        ArrayList<AIAttackInfo> toRemove = new ArrayList<>();
        for (AIAttackInfo aia : AttackInfo) {
            if (aia.getUpcomingUnits() <= aia.getDefendinUnits() + aia.getDefence()) {
                toRemove.add(aia);
            }
            if (aia.getUpcomingUnits() == 0) {
                toRemove.add(aia);
            }
        }

        for (AIAttackInfo aia : toRemove) {
            AttackInfo.remove(aia);
        }

        /**
         * The list AttackInfo now containts all the information about the
         * upcoming Units from the enemy and the units that the AI has placed on
         * that lane The defencing units are also saved in this list Sort the
         * list so the lane with the most attacking units is on top
         */
        for (int i = 0; i < AttackInfo.size(); i++) {
            for (int j = 0; j < AttackInfo.size(); j++) {
                int differance1 = AttackInfo.get(i).getUpcomingUnits() - (AttackInfo.get(i).getDefendinUnits() + AttackInfo.get(i).getDefence());
                int differance2 = AttackInfo.get(j).getUpcomingUnits() - (AttackInfo.get(j).getDefendinUnits() + AttackInfo.get(j).getDefence());
                if (differance1 > differance2) {
                    AIAttackInfo helper = AttackInfo.get(i);
                    AttackInfo.set(i, AttackInfo.get(j));
                    AttackInfo.set(j, helper);
                }
            }
        }

        /**
         * The list now contains the information about the lanes and the amount
         * of personal units and enemy units. The AI now has to decide to take
         * any action or leave the lane alone The AGRESSIVE AI will spawn a
         * melee and ranged unit at the lane to attack the aproaching units. The
         * MODERNATE AI will spawn only one melee or a ranged unit. If they are
         * both not possible to spawn, the MODERNATE AI will throw a dice and
         * maybe decide to spawn a defence unit. The DEFENSIVE AI will spawn a
         * defence unit, and if that is not possible it will spawn a Melee or
         * Ranged unit
         */
        for (int i = 0; i < AttackInfo.size(); i++) {
            if (this.getAIType() == AIState.AGRESSIVE) {
                /**
                 * Spawn a Melee and a Ranged unit to defend against upcoming
                 * units
                 */
                this.spawnUnit(UnitType.MELEE, AttackInfo.get(i).getLane() * 4 + 3);
                this.spawnUnit(UnitType.RANGED, AttackInfo.get(i).getLane() * 4 + 3);
            } else if (this.getAIType() == AIState.MODERNATE) {
                /**
                 * Spawn a Melee or a Ranged unit. If not possible, decide if a
                 * defence unit is an option
                 */
                if (canPlaceUnit(UnitType.MELEE)) {
                    this.spawnUnit(UnitType.MELEE, AttackInfo.get(i).getLane() * 4 + 3);
                } else if (canPlaceUnit(UnitType.RANGED)) {
                    this.spawnUnit(UnitType.RANGED, AttackInfo.get(i).getLane() * 4 + 3);
                } else if (canPlaceUnit(UnitType.DEFENCE)) {
                    /**
                     * Find a suitable spot to spawn a defence unit
                     */
                    int newSpot = -1;
                    while (newSpot == -1 || this.getBase().getUnit(newSpot) != null) {
                        newSpot = getNextRandom(0, 2) + AttackInfo.get(i).getLane() * 4;
                    }

                    this.spawnUnit(UnitType.DEFENCE, newSpot);
                }
            } else if (this.getAIType() == AIState.DEFENSIVE) {
                /**
                 * Spawn a Defence unit. If that is not possible spawn a Melee
                 * or Ranged unit
                 */

                if (canPlaceUnit(UnitType.DEFENCE)) {
                    /**
                     * Find a suitable spot to spawn a defence unit
                     */
                    int newSpot = -1;
                    while (newSpot == -1 || this.getBase().getUnit(newSpot) != null) {
                        newSpot = getNextRandom(0, 2) + AttackInfo.get(i).getLane() * 4;
                    }
                    this.spawnUnit(UnitType.DEFENCE, newSpot);
                } else if (canPlaceUnit(UnitType.RANGED)) {
                    this.spawnUnit(UnitType.RANGED, AttackInfo.get(i).getLane() * 4 + 3);
                } else if (canPlaceUnit(UnitType.MELEE)) {
                    this.spawnUnit(UnitType.MELEE, AttackInfo.get(i).getLane() * 4 + 3);
                }
            }
        }
    }

    /**
     * Lets the AI do a next action at random
     *
     * @param gm Gamemanager object needed to spawn units if required
     */
    private void doRandomAction(GameManager gm) {
        double choicePer = getNextRandom(0, 1000) / 10;

        if (choicePer < chances[0]) {
            /**
             * Place Defence
             */
            placeDefenceUnit();
        } else if (choicePer < chances[0] + chances[1]) {
            /**
             * Place Attack
             */
            placeMeleeUnit();
            placeRangedUnit();
        } else if (choicePer < chances[0] + chances[1] + chances[2]) {
            /**
             * Do Upgrade
             */
        } else {
            /**
             * Bid on Mysterybox
             */
            bidOnMysterybox(gm);
        }
    }

    /**
     * Places defence units at the spots in front of the base
     */
    private void defendBase() {
        if (this.aiType == AIState.DEFENSIVE) {
            /**
             * Defensive AI always checks if it has at least 2 defensive units
             * at each lane. If it has not at least 2 defensive units, it will
             * place extra units at that lane If it is detected that a defensive
             * unit was killed, the defensive AI will also spawn a melee unit to
             * help it protect himself
             */

            placeDefenceAtBase(2);

        } else if (this.aiType == AIState.AGRESSIVE) {
            /**
             * Agressive AI does not need that much defence. The Agressive AI
             * gets a random number and decides if it needs any defensive units.
             * If it detects that there is one of its defence units missing, it
             * will spawn extra attack units to protect it
             *
             * The code for placing defensive units for AGRESSIVE AI is not
             * placed here, but in the thinkOfNextAction method
             */
        } else if (this.aiType == AIState.MODERNATE) {
            /**
             * Modernate AI does the same as the Defensive AI, but less. Where
             * the defencive AI places at least 2 units at every lane, the
             * Modernate AI places only one unit at every lane
             */

            placeDefenceAtBase(1);
        }
    }

    /**
     * Places a Defence unit at every lane. The amount is secified in the
     * defenceWidth paramter This method is used by the DEFENSIVE and MODERNATE
     * AI
     *
     * @param defenceWidth The amount of units per lane
     */
    private void placeDefenceAtBase(int defenceWidth) {
        /**
         * Check the amount of defensive units at each lane
         */
        for (int i = 0; i < 8; i++) {
            /**
             * Check if there are two or more defensive units at each lane
             */
            int defence_lane = this.getDefenceAtLane(i);
            if (defence_lane < defenceWidth) {
                /**
                 * There are less than 2 units at the lane, the AI will spawn
                 * some new units
                 *
                 * Get a new "random" position for spawning the unit on the
                 * specified lane. This can be [0, 1, 2], [4, 5, 6] [8, 9, 10],
                 * enz
                 */
                int newSpot = -1;

                /**
                 * Check if the spot is not already taken. If it is taken
                 * Generate a new spot
                 */
                while (newSpot == -1 || this.getBase().getUnit(newSpot) != null) {
                    newSpot = getNextRandom(0, defenceWidth) + i * 4;
                }

                /**
                 * Create the unit object
                 */
                this.spawnUnit(UnitType.DEFENCE, newSpot);
            }
        }
    }

    /**
     * Places a Defensive unit at the base
     */
    private void placeDefenceUnit() {
        /**
         * Check if the enemy still exsits
         */
        int[] laneRange = baseLanesToDefend();

        /**
         * Get a random lane to place defence units
         */
        int lane = getNextRandom(laneRange[0], laneRange[1]);

        /**
         * Check if there are not already 3 defence units in that lane
         */
        int defence_lane = this.getDefenceAtLane(lane);

        /**
         * The agressive AI does not want more that 1 defensive unit at a lane
         * If there is already a defence unit at the lane, skip
         */
        if (defence_lane >= 1 && this.getAIType() == AIState.AGRESSIVE) {
            return;
        }

        if (defence_lane < 3) {
            /**
             * Place an extra defence unit
             *
             * Get a new "random" position for spawning the unit on the
             * specified lane. This can be [0, 1, 2], [4, 5, 6] [8, 9, 10], enz
             */
            int newSpot = -1;

            /**
             * Check if the spot is not already taken. If it is taken Generate a
             * new spot
             */
            while (newSpot == -1 || this.getBase().getUnit(newSpot) != null) {
                newSpot = getNextRandom(0, 2) + lane * 4;
            }

            /**
             * Create the unit object
             */
            this.spawnUnit(UnitType.DEFENCE, newSpot);
        }
    }

    /**
     * Places a Melee unit at the base
     */
    private void placeMeleeUnit() {
        /**
         * Check if the enemy still exsits
         */
        int[] laneRange = baseLanesToDefend();

        /**
         * Get a random lane to place melee units
         */
        int lane = getNextRandom(laneRange[0], laneRange[1]);

        /**
         * Check if the lane spot to spawn attack units is taken or not
         */
        if (this.getBase().getUnit(lane * 4 + 3) == null) {
            /**
             * Place an extra attack unit
             *
             * Create the unit object
             */
            this.spawnUnit(UnitType.MELEE, 3 + lane * 4);
        }
    }

    /**
     * Places a Ranged unit at the base
     */
    private void placeRangedUnit() {
        /**
         * Check if the enemy still exsits
         */
        int[] laneRange = baseLanesToDefend();

        /**
         * Get a random lane to place ranged units
         */
        int lane = getNextRandom(laneRange[0], laneRange[1]);

        /**
         * Check if the lane spot to spawn attack units is taken or not
         */
        if (this.getBase().getUnit(lane * 4 + 3) == null) {
            /**
             * Place an extra attack unit
             *
             * Create the unit object
             */
            this.spawnUnit(UnitType.RANGED, 3 + lane * 4);
        }
    }

    /**
     * Check if the enemy bases are still alive
     *
     * @return The minimum and maximum lane index number that still need to be
     * defended
     */
    private int[] baseLanesToDefend() {
        int minlane = 0;
        int maxlane = 7;

        if (this.getBase().getLane(0).getBaseEnd1().getHealthPoints() == 0) {
            minlane = 4;
        }
        if (this.getBase().getLane(0).getBaseEnd2().getHealthPoints() == 0) {
            minlane = 4;
        }

        if (this.getBase().getLane(4).getBaseEnd1().getHealthPoints() == 0) {
            maxlane = 3;
        }
        if (this.getBase().getLane(4).getBaseEnd2().getHealthPoints() == 0) {
            maxlane = 3;
        }

        if (minlane > maxlane) {
            minlane = 0;
            maxlane = 0;
        }

        return new int[]{minlane, maxlane};
    }

    /**
     * Returns a random number between the specified range depending on the
     * random seed of the AI
     *
     * @param low The minimum number
     * @param high The maximum number
     * @return A random number between the minimum and maximum range
     */
    private int getNextRandom(int low, int high) {
        return (int) (Math.random() * this.getRandomSeed()) % ((high - low) + 1) + low;
    }

    /**
     * Checks if the AI can place an unit of the specified type
     *
     * @param unit The type of unit the AI want to place
     * @return A boolean indicating if the unit can be placed
     */
    private boolean canPlaceUnit(UnitType unit) {
        if (unit == UnitType.DEFENCE) {
            if (this.stepsSinceLastDefence <= 0) {
                this.stepsSinceLastDefence = 0;
                return true;
            } else {
                return false;
            }
        } else if (unit == UnitType.MELEE) {
            if (this.stepsSinceLastMelee <= 0) {
                this.stepsSinceLastMelee = 0;
                return true;
            } else {
                return false;
            }
        } else if (unit == UnitType.RANGED) {
            if (this.stepsSinceLastRanged <= 0) {
                this.stepsSinceLastRanged = 0;
                return true;
            } else {
                return false;
            }
        } else {
            if (this.stepsSinceLastDefence <= 0 && this.stepsSinceLastMelee <= 0 && this.stepsSinceLastRanged <= 0) {
                this.stepsSinceLastDefence = 0;
                this.stepsSinceLastMelee = 0;
                this.stepsSinceLastRanged = 0;
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Creates an unit a the specified spawnpoint
     *
     * @param unit The type of unit to create
     * @param gm Gamemanager object to spawn a unit
     * @param spawnPoint The point at the base where the unit need to be spawned
     */
    private void createUnitAtLane(UnitType unit, GameManager gm, int spawnPoint) {
        UnitInfo ui;
        switch (unit) {
            case DEFENCE:
                ui = UnitInfo.getDefenceUnit(this);
                gm.placeUnitAtBase(this, ui.getUnit(), spawnPoint, ui.getCost());
                stepsSinceLastDefence = (int) (ui.getCooldown());
                break;
            case MELEE:
                ui = UnitInfo.getMeleeUnit(this);
                gm.placeUnitAtBase(this, ui.getUnit(), spawnPoint, ui.getCost());
                stepsSinceLastMelee = (int) (ui.getCooldown());
                break;
            case RANGED:
                ui = UnitInfo.getRangedUnit(this);
                gm.placeUnitAtBase(this, ui.getUnit(), spawnPoint, ui.getCost());
                stepsSinceLastRanged = (int) (ui.getCooldown());
                break;
        }
    }

    /**
     * Lets the AI make a bid on the Mysterybox
     *
     * @param gm The gamemanager holding the mysterybox object
     */
    private void bidOnMysterybox(GameManager gm) {
        /**
         * Check if the AI has enough money to bid on the mysterybox If the AI
         * as just enough money it will not bid because it needs it to spawn
         * units
         *
         * Check if there is a mysterybox availible
         */
        if (gm.getMysterybox() == null) {
            /**
             * There is no mysterybox, quit the method
             */
            return;
        }

        /**
         * Check if you have enough money to bid on the mysterybox
         */
        if (this.getMoney() + 5 <= gm.getMysterybox().getHighestBid()) {
            /**
             * Not enough money, quit the method
             */
            return;
        }

        /**
         * Make a chance calculation to 'make the decision' on bidding on the
         * Mysterybox
         */
        double chance = getNextRandom(0, 1000) / 10;
        if (chance < 30) {
            gm.getMysterybox().Bid(this, gm.getMysterybox().getHighestBid() + getNextRandom(0, 5));
        }
    }
}

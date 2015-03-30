/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kingofthehill.unitinfo.UnitInfo;

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

    // AI memory for calculating the next step
    private int stepsSinceLastDefence;
    private int stepsSinceLastMelee;
    private int stepsSinceLastRanged;
    private int defenceAtLanes[];
    private int attackAtLanes[];
    private int randomSeed;
    private AIState aiType;
    private boolean debugInfo;
    private double[] chances;

    /**
     * Creates a new AI object
     *
     * @param name May not be null.
     */
    public AI(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Illegal arguments given!");
        }
        this.name = name;
        this.exp = 12;
        this.score = 0;
        this.upgrades = new ArrayList();
        this.team = null;
        this.money = 100;
        this.base = null;

        //Define the last action of the AI and the defence and attack units the 
        //AI has. The values for defence and attack are 0 because it has nothing
        //placed yet
        this.stepsSinceLastDefence = 0;
        this.stepsSinceLastMelee = 0;
        this.stepsSinceLastRanged = 0;
        this.defenceAtLanes = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        this.attackAtLanes = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        this.randomSeed = 123456789;
        setAIType(AIState.DEFENSIVE);
        this.debugInfo = false;
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

    public int getDefenceAtLane(int lane) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (getBase().getUnit(lane * 4 + i) != null) {
                count++;
            }
        }

        defenceAtLanes[lane] = count;
        return count;
    }

    public int getAttackAtLane(int lane) {
        int count = 0;
        for (int i = 0; i < this.getBase().getLane(lane).getUnits().size(); i++) {
            if (this.getBase().getLane(lane).getUnits().get(i).getOwner() == this) {
                count++;
            }
        }

        attackAtLanes[lane] = count;
        return count;
    }

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

    public void setRandomSeed(int seed) {
        this.randomSeed = seed;
    }

    public void setAIType(AIState type) {
        // Set the values for random choices
        // a.k.a. How much chance is there to spawn defence for AI type
        // Total value must be 100 to work correctly
        // Chances order: Defence, Attack, Upgrade, Mysterybox
        if (type == AIState.DEFENSIVE) {
            this.chances = new double[]{65.0, 20.0, 10.0, 5.0};
        } else if (type == AIState.AGRESSIVE) {
            this.chances = new double[]{12.5, 70.0, 12.5, 5.0};
        } else if (type == AIState.MODERNATE) {
            this.chances = new double[]{40.0, 40.0, 15.0, 5.0};
        }

        // Check if the chances values are correct
        if (this.chances.length < 4) {
            throw new UnsupportedOperationException("AI must ALWAYS have 4 persentage values");
        }
        if (this.chances[0] + this.chances[1] + this.chances[2] + this.chances[3] != 100) {
            throw new UnsupportedOperationException("AI persentage values must be a total of 100");
        }

        // Set the AI type
        aiType = type;
    }

    public AIState getAIType() {
        return aiType;
    }

    public void setPrintDebug(boolean newState) {
        this.debugInfo = newState;
    }

    private boolean getPrintDebug() {
        return debugInfo;
    }

    public int areDefenceUnitsAtLane(int lane) {
        if (lane < 0 || lane > 7) {
            throw new IllegalArgumentException("Lane cannot be lower than zero or higher than 7");
        }

        ArrayList<Boolean> ret_lane = new ArrayList<>(Arrays.asList(new Boolean[]{
            getBase().getUnit(lane * 4) != null,
            getBase().getUnit(lane * 4 + 1) != null,
            getBase().getUnit(lane * 4 + 2) != null,
            getBase().getUnit(lane * 4 + 3) != null
        }));

        return (int) ret_lane.stream().filter(p -> p == true).count();

    }

    /**
     * =========================================================================
     * HERE FOLLOWS THE CODE THAT MAKES AN AI THINK ABOUT HIS MOVES !!! DO NOT
     * CHANGE THIS CODE UNLESS YOU KNOW WHAT YOU ARE DOING !!!
     * =========================================================================
     */
    public void doNextAction(AI player, GameManager gm) {
        // Do the following actions:
        // 1. Place defence at lanes it the AI type allows this
        // 2. Check if being attacked on lane, if so: spawn attack\defence units
        // 3. Decide a next action to do
        //
        // If the AI has spawned units for a specific action, it cannot spawn 
        // units for the next action
        
        if(placeDefenceUnits(player, gm))
        {}
        else if(prepareForAttack(player, gm))
        {}
        thinkOfNextAction(player, gm);

        // The cooldown for placing units
        stepsSinceLastDefence--;
        stepsSinceLastMelee--;
        stepsSinceLastRanged--;
    }
    
    private boolean prepareForAttack(AI player, GameManager gm)
    {
        boolean hasPlacedUnits = false;
        // Check if the AI can place units to defend himself
        // Check if there is still a cooldown on the Melee units
        if (this.stepsSinceLastMelee <= 0) {
            this.stepsSinceLastMelee = 0;
        }
      
        // Check if there is still a cooldown on the Ranged units
        if (this.stepsSinceLastRanged <= 0) {
            this.stepsSinceLastRanged = 0;
        } 
        
        // Check if there is still a cooldown on the Defence units
        if (this.stepsSinceLastDefence <= 0) {
            this.stepsSinceLastDefence = 0;
        } 
        
        // Check if there can be placed any unit
        if(stepsSinceLastMelee != 0 && stepsSinceLastRanged != 0 && stepsSinceLastDefence != 0)
        {
            // No use to continue, player cannot spawn any units
            // return true so it does not start to check the other functions
            return true;
        }
        
        
        // Check on every lane if there are attack units coming towards the base
        // Save the lanes with upcoming units in a list
        ArrayList<AIAttackInfo> AttackInfo = new ArrayList<>();
        for(int i = 0; i<8; i++)
        {
            AIAttackInfo info = new AIAttackInfo(i);
            AttackInfo.add(info);
            
            int ownUnits = 0;
            int enemyUnits = 0;
            for(Unit u : player.getBase().getLane(i).getUnits())
            {
                if(u.getOwner() == player)
                {
                    ownUnits ++;
                }
                else
                    enemyUnits ++;
            }
            
            info.setUpcomingUnits(enemyUnits);
            info.setDefendingUnits(ownUnits);
            
            int defenceUnits = 0;
            for(int j = 0; j<4; j++)
            {
                if(player.getBase().getUnit(j) != null)
                {
                    defenceUnits ++;
                }
            }
            info.setDefence(defenceUnits);
        }
        
        // The list AttackInfo now containts all the information about the upcoming
        // Units from the enemy and the units that the AI has placed on that lane
        // The defencing units are also saved in this list
        
        // Sort the list so the lane with the most attacking units is on top
        for(int i = 0; i<AttackInfo.size(); i++)
        {
            for(int j = 0; j<AttackInfo.size(); j++)
            {
                int differance1 = AttackInfo.get(i).getUpcomingUnits() - (AttackInfo.get(i).getDefendinUnits() + AttackInfo.get(i).getDefence());
                int differance2 = AttackInfo.get(j).getUpcomingUnits() - (AttackInfo.get(j).getDefendinUnits() + AttackInfo.get(j).getDefence());
                if(differance1 > differance2)
                {
                    AIAttackInfo helper = AttackInfo.get(i);
                    AttackInfo.set(i, AttackInfo.get(j));
                    AttackInfo.set(j, helper);
                }
            }
        }
        
        // Remove attack info where there are more defending units than attacking
        // units
        for(int i = 0; i<AttackInfo.size(); i++)
        {
            if(AttackInfo.get(i).getUpcomingUnits() <= AttackInfo.get(i).getDefendinUnits() + AttackInfo.get(i).getDefence()){
                AttackInfo.remove(i);
                i=0;
            }
            if(AttackInfo.get(i).getUpcomingUnits() == 0) {
                AttackInfo.remove(i);
                i=0;
            }
        }
        
        // Take action to defend the base
        if(AttackInfo.size() > 0)
        {
            // AI must spawn units to defend himself
            if(this.stepsSinceLastMelee == 0)
            {
                UnitInfo ui = UnitInfo.getMeleeUnit(player);
                gm.placeUnitAtBase(
                        player, 
                        ui.getUnit(),
                        AttackInfo.get(0).getLane() * 4 + 3,
                        ui.getKosten());
                OutputDebugInfo(player, player.getName() + ": ", "Melee spawned");
                this.stepsSinceLastMelee = ui.getCooldown();
                hasPlacedUnits = true;
                return hasPlacedUnits;
            }
            else if(this.stepsSinceLastRanged == 0)
            {
                UnitInfo ui = UnitInfo.getRangeUnit(player);
                gm.placeUnitAtBase(
                        player, 
                        ui.getUnit(),
                        AttackInfo.get(0).getLane() * 4 + 3,
                        ui.getKosten());
                OutputDebugInfo(player, player.getName() + ": ", "Ranged spawned");
                this.stepsSinceLastRanged = ui.getCooldown();
                hasPlacedUnits = true;
                return hasPlacedUnits;
            }
        }
        
        return hasPlacedUnits;
    }

    private boolean thinkOfNextAction(AI player, GameManager gm) {
        double choicePer = getNextRandom(player, 0, 1000) / 10;

        if (choicePer < chances[0]) {
            // Place Defence
            placeDefenceUnit(player, gm);
        } else if (choicePer < chances[0] + chances[1]) {
            // Place Attack
            placeMeleeUnit(player, gm);
            placeRangedUnit(player, gm);
        } else if (choicePer < chances[0] + chances[1] + chances[2]) {
            // Do Upgrade
        } else {
            // Bid on Mysterybox
        }
        
        return true;
    }

    private boolean placeDefenceUnits(AI player, GameManager gm) {
        if (this.aiType == AIState.DEFENSIVE) {
            // Defensive AI always checks if it has at least 2 defensive units
            // at each lane. If it has not at least 2 defensive units, it will
            // place extra units at that lane
            // If it is detected that a defensive unit was killed, the defensive
            // AI will also spawn a melee unit to help it protect himself

            return placeDefenceLine(player, gm, 2);

        } else if (this.aiType == AIState.AGRESSIVE) {
            // Agressive AI does not need that much defence. The Agressive AI 
            // gets a random number and decides if it needs any defensive units.
            // If it detects that there is one of its defence units missing,
            // it will spawn extra attack units to protect it

            // The code for placing defensive units for AGRESSIVE AI is not
            // placed here, but in the thinkOfNextAction method
        } else if (this.aiType == AIState.MODERNATE) {
            // Modernate AI does the same as the Defensive AI, but less. Where
            // the defencive AI places at least 2 units at every lane, the 
            // Modernate AI places only one unit at every lane

            return placeDefenceLine(player, gm, 1);
        }
        
        return false;
    }

    /**
     * Places a Defence unit at every lane. The amount is secified in the
     * defenceWidth paramter This method is used by the DEFENSIVE and MODERNATE
     * AI
     *
     * @param player Player to spawn the units
     * @param gm GameManager that controls the units spawning
     * @param defenceWidth The amount of units per lane
     */
    private boolean placeDefenceLine(AI player, GameManager gm, int defenceWidth) {
        boolean hasPlacedUnits = false;
        
        // Check the amount of defensive units at each lane
        for (int i = 0; i < 8; i++) {
            // Check if there is still a cooldown on the Defence units
            if (this.stepsSinceLastDefence <= 0) {
                this.stepsSinceLastDefence = 0;
            } else {
                return false;
            }

            // Check if there are two or more defensive units at each lane
            int defence_lane = player.getDefenceAtLane(i);
            if (defence_lane < defenceWidth) {
                // There are less than 2 units at the lane, the AI will
                // spawn some new units

                // Get a new "random" position for spawning the unit on 
                // the specified lane. This can be [0, 1, 2], [4, 5, 6]
                // [8, 9, 10], enz
                int newSpot = -1;

                // Check if the spot is not already taken. If it is taken
                // Generate a new spot
                while (newSpot == -1 || player.getBase().getUnit(newSpot) != null) {
                    newSpot = getNextRandom(player, 0, defenceWidth) + i * 4;
                }

                // Create the unit object
                UnitInfo ui = UnitInfo.getDefenceUnit(player);
                gm.placeUnitAtBase(
                        player,
                        ui.getUnit(),
                        newSpot,
                        ui.getKosten());
                
                OutputDebugInfo(player, player.getName() + ": ", "Defence spawned");

                stepsSinceLastDefence = ui.getCooldown();
                hasPlacedUnits = true;
            }
        }
        
        return hasPlacedUnits;
    }

    private void placeDefenceUnit(AI player, GameManager gm) {
        // Check if there is still a cooldown on the Defence units
        if (this.stepsSinceLastDefence <= 0) {
            this.stepsSinceLastDefence = 0;
        } else {
            return;
        }

        // Check if the enemy still exsits
        int[] laneRange = baseLanesToDefend(player);
        
        // Get a random lane to place defence units
        int lane = getNextRandom(player, laneRange[0], laneRange[1]);

        // Check if there are not already 3 defecen units in that lane
        int defence_lane = player.getDefenceAtLane(lane);
        
        // The agressive AI does not want more that 1 defensive unit at a lane
        // If there is already a defence unit at the lane, skip
        if(defence_lane >= 1 && player.getAIType() == AIState.AGRESSIVE)
            return;
        
        if (defence_lane < 3) {
            // Place an extra defence unit

            // Get a new "random" position for spawning the unit on 
            // the specified lane. This can be [0, 1, 2], [4, 5, 6]
            // [8, 9, 10], enz
            int newSpot = -1;

            // Check if the spot is not already taken. If it is taken
            // Generate a new spot
            while (newSpot == -1 || player.getBase().getUnit(newSpot) != null) {
                newSpot = getNextRandom(player, 0, 2) + lane * 4;
            }

            // Create the unit object
            UnitInfo ui = UnitInfo.getDefenceUnit(player);
            gm.placeUnitAtBase(
                    player,
                    ui.getUnit(),
                    newSpot,
                    ui.getKosten());
            
            OutputDebugInfo(player, player.getName() + ": ", "Defence spawned");

            stepsSinceLastDefence = ui.getCooldown();
        }
    }

    private void placeMeleeUnit(AI player, GameManager gm) {
        // Check if there is still a cooldown on the Melee units
        if (this.stepsSinceLastMelee <= 0) {
            this.stepsSinceLastMelee = 0;
        } else {
            return;
        }
        
        // Check if the enemy still exsits
        int[] laneRange = baseLanesToDefend(player);
        
        // Get a random lane to place melee units
        int lane = getNextRandom(player, laneRange[0], laneRange[1]);

        // Check if the lane spot to spawn attack units is taken or not
        if (player.getBase().getUnit(lane * 4 + 3) == null) {
            // Place an extra attack unit

            // Create the unit object
            UnitInfo ui = UnitInfo.getMeleeUnit(player);
            gm.placeUnitAtBase(
                    player,
                    ui.getUnit(),
                    3 + lane * 4,
                    ui.getKosten());
            
            OutputDebugInfo(player, player.getName() + ": ", "Melee spawned");

            stepsSinceLastMelee = ui.getCooldown();
        }
    }

    private void placeRangedUnit(AI player, GameManager gm) {
        // Check if there is still a cooldown on the Melee units
        if (this.stepsSinceLastRanged <= 0) {
            this.stepsSinceLastRanged = 0;
        } else {
            return;
        }

        // Check if the enemy still exsits
        int[] laneRange = baseLanesToDefend(player);
        
        // Get a random lane to place ranged units
        int lane = getNextRandom(player, laneRange[0], laneRange[1]);

        // Check if the lane spot to spawn attack units is taken or not
        if (player.getBase().getUnit(lane * 4 + 3) == null) {
            // Place an extra attack unit

            // Create the unit object
            UnitInfo ui = UnitInfo.getRangeUnit(player);
            gm.placeUnitAtBase(
                    player,
                    ui.getUnit(),
                    3 + lane * 4,
                    ui.getKosten());

            OutputDebugInfo(player, player.getName() + ": ", "Ranged spawned");
            
            stepsSinceLastRanged = ui.getCooldown();
        }
    }
    
    private int[] baseLanesToDefend(AI player)
    {
        int minlane = 0;
        int maxlane = 7;
        
        if(player.getBase().getLane(0).getBaseEnd1().getHealthPoints() == 0)
            minlane = 4;
        if(player.getBase().getLane(0).getBaseEnd2().getHealthPoints() == 0)
            minlane = 4;
        
        if(player.getBase().getLane(4).getBaseEnd1().getHealthPoints() == 0)
            maxlane = 3;
        if(player.getBase().getLane(4).getBaseEnd2().getHealthPoints() == 0)
            maxlane = 3;
        
        return new int[] {minlane, maxlane};
    }

    private int getNextRandom(AI player, int low, int high) {
        return (int) (Math.random() * player.getRandomSeed()) % (high + 1) + low;
    }

    private void OutputDebugInfo(AI player, String pretext, Object data) {
        if (getPrintDebug()) {
            if (player.getName().equals("AI1")) {
                System.out.println(pretext + data.toString());
            }
        }
    }
}

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
 * Class containing all the information about a AI. Implements IPlayer. Contains AI algoritm.
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
    private List<LastAIAction> lastActions;
    private int defenceAtLanes[];
    private int attackAtLanes[];
    private int randomSeed;
    private AIState aiType;
    private boolean debugInfo;
    
    /**
     * Creates a new AI object
     * @param name May not be null.
     */
    public AI(String name){
        if(name.isEmpty()){
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
        this.lastActions = new ArrayList<>();
        this.defenceAtLanes = new int[] {0,0,0,0,0,0,0,0};
        this.attackAtLanes = new int[] {0,0,0,0,0,0,0,0};
        this.randomSeed = 123456789;
        this.aiType = AIState.DEFENSIVE;
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
        if(newTeam != null){
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
    
    public List<LastAIAction> getLastActions()
    {
        return Collections.unmodifiableList(lastActions);
    }
    
    public int getDefenceAtLane(int lane)
    {
        int count  = 0;
        for(int i = 0; i<4; i++)
        {
            if(getBase().getUnit(lane * 4 + i) != null)
                count ++;
        }     
        
        defenceAtLanes[lane] = count;
        return count;
    }
    
    public int getAttackAtLane(int lane)
    {
        int count = 0;
        for(int i = 0; i<this.getBase().getLane(lane).getUnits().size(); i++)
        {
            if(this.getBase().getLane(lane).getUnits().get(i).getOwner() == this)
                count ++;
        }
        
        attackAtLanes[lane] = count;
        return count;
    }
    
    public int getRandomSeed()
    {
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
        if(amount > 0){
            if(amount <= this.money){
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
        if(newBase != null){
            this.base = newBase;
        }
    }
    
    public void setRandomSeed(int seed)
    {
        this.randomSeed = seed;
    }
    
    public void addDoneAction(LastAIAction action)
    {
        lastActions.add(action);
    }
    
    public void resetDoneActions()
    {
        lastActions.clear();
    }
    
    public void setAIType(AIState type)
    {
        aiType = type;
    }
    
    public AIState getAIType()
    {
        return aiType;
    }
    
    public void setPrintDebug(boolean newState)
    {
        this.debugInfo = newState;
    }
    
    private boolean getPrintDebug()
    {
        return debugInfo;
    }

    public int areDefenceUnitsAtLane(int lane)
    {
        if(lane < 0 || lane > 7)
            throw new IllegalArgumentException("Lane cannot be lower than zero or higher than 7");
        
        ArrayList<Boolean> ret_lane = new ArrayList<>(Arrays.asList(new Boolean[] {
            getBase().getUnit(lane * 4) != null,
            getBase().getUnit(lane * 4 + 1) != null,
            getBase().getUnit(lane * 4 + 2) != null,
            getBase().getUnit(lane * 4 + 3) != null
        }));
 
        return (int)ret_lane.stream().filter(p -> p == true).count();
        
    }
    
    
    
    
    
    /**
     * =========================================================================
     *        HERE FOLLOWS THE CODE THAT MAKES AN AI THINK ABOUT HIS MOVES
     *     !!! DO NOT CHANGE THIS CODE UNLESS YOU KNOW WHAT YOU ARE DOING !!!
     * =========================================================================
     */
    
    public void doNextAction(AI player, GameManager gm)
    {
        switch(player.aiType)
        {
            case AGRESSIVE:
                break;
            case DEFENSIVE:
                OperateDefensiveAI(player, gm);
                break;
        }
    }
    
    public void OperateDefensiveAI(AI player, GameManager gm)
    {
        OutputDebugInfo(player, "AI operation for: ", player.getName());

        boolean iNeededToPlaceDefence = false;

        // Let the AI decide what to do
        for (int i = 0; i < 8; i++) {
            //Check the AI defences
            int currentDefence = player.areDefenceUnitsAtLane(i);
            //OutputDebugInfo(player, "Defence at lane " + i + ": ", currentDefence);
            if (currentDefence < 2) {
                // Check if the AI has lost units
                int oldDefence = player.getDefenceAtLane(i);
                OutputDebugInfo(player, "Defence at lane " + i + " in last turn : ", currentDefence);
                if (oldDefence >= currentDefence) {
                    OutputDebugInfo(player, "Units that i lost: ", oldDefence - currentDefence);
                    // The AI has lost units
                    // TODO: Spawn an extra attack unit for more strenght

                    // Get a new place to spawn a defence unit
                    int randomNewDefenceSpot = i * 4 + Math.abs(getNextRandom(player, 0, 2));
                    while (player.getBase().getUnit(randomNewDefenceSpot) != null) {
                        randomNewDefenceSpot = i * 4 + Math.abs(getNextRandom(player, 0, 2));
                    }

                    OutputDebugInfo(player, "Spawn defence unit at lane " + i + " on spot[" + i * 4 + "-" + (i * 4 + 3) + "]: ", randomNewDefenceSpot);

                    UnitInfo ui = UnitInfo.getDefenceUnit(player);
                    gm.placeUnitAtBase(
                            player,
                            ui.getUnit(),
                            randomNewDefenceSpot,
                            ui.getKosten());

                    iNeededToPlaceDefence = true;
                }
            }
        }

        // Decide to do other stuff when there are enough defence units placed
        if (!iNeededToPlaceDefence) {
            //OutputDebugInfo(player, "There is enough defence everywhere", "");
            // Decide what to do with the rest of the money:
            // - Spawn attack unit
            // - Spawn extra defence unit
            // - Bid on the Mysterybox
            // - Do an upgrade for units

            //Percentage that indicate if a action should be done
            double[] chance = new double[]{80.0, 50.0, 10.0, 15.0};
            double chanceState = 100;
            int toDoAction = 0;
            while (chanceState > chance[toDoAction]) {
                toDoAction = getNextRandom(player, 0, 3);
                chanceState = getNextRandom(player, 0, 100);
                OutputDebugInfo(player, "", "action=" + toDoAction + "    " + chanceState + "/" + chance[toDoAction]);
            }

            if (toDoAction == 0) {
                //Spawn attack unit
                OutputDebugInfo(player, "--Place an attack unit", "");
                
                int newSpot = Math.abs(getNextRandom(player, 0, 7));
                
                UnitInfo ui = UnitInfo.getMeleeUnit(player);
                gm.placeUnitAtBase(player,
                        ui.getUnit(),
                        newSpot * 4 + 3,
                        ui.getKosten());
            } else if (toDoAction == 1) {
                // Spawn extra defence unit
                OutputDebugInfo(player, "--Place an extra defence unit", "");

                int randomNewDefenceSpot = Math.abs(getNextRandom(player, 0, 31));
//                while (player.getBase().getUnit(randomNewDefenceSpot) != null || randomNewDefenceSpot % 4 == 3 /* ||
//                        player.getDefenceAtLane((int)Math.floor(randomNewDefenceSpot / 4)) != 3*/ ) {
//                    randomNewDefenceSpot = Math.abs(getNextRandom(player, 0, 31));
//                }

                UnitInfo ui = UnitInfo.getDefenceUnit(player);
                gm.placeUnitAtBase(player,
                        ui.getUnit(),
                        randomNewDefenceSpot,
                        ui.getKosten());
            } else if (toDoAction == 2) {
                // Bid on Mysterybox
                OutputDebugInfo(player, "--Bid on the mysterybox", "");
            } else {
                // Do an upgrade for units
                OutputDebugInfo(player, "--Do an upgrade for units", "");
            }
        }
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

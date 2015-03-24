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
        this.team = newTeam;
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
        this.base = newBase;
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
}

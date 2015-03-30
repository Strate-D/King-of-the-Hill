/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

/**
 *
 * @author Bas
 */
public class AIAttackInfo{
    private int lane;
    private int upcomingUnits;
    private int defendingUnits;
    private int defence;
    
    public AIAttackInfo(int lane)
    {
        this.lane = lane;
        this.upcomingUnits = 0;
        this.defendingUnits = 0;
        this.defence = 0;
    }
    
    public void setUpcomingUnits(int amount)
    {
        this.upcomingUnits = amount;
    }
    
    public void setDefendingUnits(int amount)
    {
        this.defendingUnits = amount;
    }
    
    public void setDefence(int amount)
    {
        this.defence = amount;
    }
    
    public int getLane()
    {
        return lane;
    }
    
    public int getUpcomingUnits()
    {
        return upcomingUnits;
    }
    
    public int getDefendinUnits()
    {
        return defendingUnits;
    }
    
    public int getDefence()
    {
        return defence;
    }
}

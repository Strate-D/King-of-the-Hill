/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.unitinfo;

import kingofthehill.domain.Defence;
import kingofthehill.domain.IPlayer;
import kingofthehill.domain.Melee;
import kingofthehill.domain.Ranged;
import kingofthehill.domain.Unit;
import kingofthehill.domain.UnitType;

/**
 *
 * @author Bas
 */
public class UnitInfo 
{
    private int cost;
    private Unit unit;
    private int cool;
    private UnitType type;
    
    public int getCost()
    {
        return cost;
    }
    
    public Unit getUnit()
    {
        return unit;
    }
    
    public int getCooldown()
    {
        return cool;
    }
    
    public UnitType getUnitType()
    {
        return type;
    }
    
    private UnitInfo(int kosten, Unit unit, int cooldown, UnitType type)
    {
        this.cost = kosten;
        this.unit = unit;
        this.cool = cooldown;
        this.type = type;
    }
    
    public static UnitInfo getDefenceUnit(IPlayer owner)
    {
        final int HP = 100;
        final int ATK = 10;
        final int AMR = 10;
        final int SPD = 0;
        final int COST = 1;
        final int COOL = 30;
        
        Unit def = new Defence(HP, ATK, AMR, SPD, owner);
        return new UnitInfo(COST, def, COOL, UnitType.DEFENCE);
    }
    
    public static UnitInfo getMeleeUnit(IPlayer owner)
    {
        final int HP = 50;
        final int ATK = 15;
        final int AMR = 5;
        final int SPD = 1;
        final int COST = 2;
        final int COOL = 80;
        
        Unit mel = new Melee(HP, ATK, AMR, SPD, owner);
        return new UnitInfo(COST, mel, COOL, UnitType.MELEE);
    }
    
    public static UnitInfo getRangedUnit(IPlayer owner)
    {
        final int HP = 50;
        final int ATK = 10;
        final int AMR = 3;
        final int SPD = 2;
        final int COST = 3;
        final int RAN = 300;
        final int COOL = 120;
        
        Unit rang = new Ranged(HP, ATK, AMR, SPD, owner, RAN);
        return new UnitInfo(COST, rang, COOL, UnitType.RANGED);
    }
}

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

/**
 *
 * @author Bas
 */
public class UnitInfo 
{
    private int kosten;
    private Unit unit;
    private int cool;
    
    public int getKosten()
    {
        return kosten;
    }
    
    public Unit getUnit()
    {
        return unit;
    }
    
    public int getCooldown()
    {
        return cool;
    }
    
    private UnitInfo(int kosten, Unit unit, int cooldown)
    {
        this.kosten = kosten;
        this.unit = unit;
        this.cool = cooldown;
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
        return new UnitInfo(COST, def, COOL);
    }
    
    public static UnitInfo getMeleeUnit(IPlayer owner)
    {
        final int HP = 100;
        final int ATK = 10;
        final int AMR = 5;
        final int SPD = 1;
        final int COST = 1;
        final int COOL = 40;
        
        Unit mel = new Melee(HP, ATK, AMR, SPD, owner);
        return new UnitInfo(COST, mel, COOL);
    }
    
    public static UnitInfo getRangeUnit(IPlayer owner)
    {
        final int HP = 100;
        final int ATK = 10;
        final int AMR = 10;
        final int SPD = 2;
        final int COST = 1;
        final int RAN = 10;
        final int COOL = 40;
        
        Unit rang = new Ranged(HP, ATK, AMR, SPD, owner, RAN);
        return new UnitInfo(COST, rang, COOL);
    }
}

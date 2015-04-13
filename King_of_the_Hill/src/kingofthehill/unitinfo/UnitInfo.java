/**
 * 
 */
package kingofthehill.unitinfo;

import kingofthehill.domain.Defence;
import kingofthehill.domain.IPlayer;
import kingofthehill.domain.Melee;
import kingofthehill.domain.Ranged;
import kingofthehill.domain.Unit;
import kingofthehill.domain.UnitType;

/**
 * Contains unit information to spawn a unit
 * @author Bas
 */
public class UnitInfo 
{
    private int cost;
    private Unit unit;
    private int cool;
    private UnitType type;
    
    /**
     * Returns the cost of the new unit
     * @return the cost of the new unit
     */
    public int getCost()
    {
        return cost;
    }
    
    /**
     * Returns the created unit
     * @return the newly created unit
     */
    public Unit getUnit()
    {
        return unit;
    }
    
    /**
     * Returns the cooldown for the unittype
     * @return The cooldown for the unittype
     */
    public int getCooldown()
    {
        return cool;
    }
    
    /**
     * Returns the Unittype
     * @return The unittype
     */
    public UnitType getUnitType()
    {
        return type;
    }
    
    /**
     * Creates a new UnitInfo object containning unit info
     * @param kosten
     * @param unit
     * @param cooldown
     * @param type 
     */
    private UnitInfo(int kosten, Unit unit, int cooldown, UnitType type)
    {
        this.cost = kosten;
        this.unit = unit;
        this.cool = cooldown;
        this.type = type;
    }
    
    /**
     * Creates a new Defence unit
     * @param owner The new owner of the unit
     * @return The unitinformation, including the unit
     */
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
    
    /**
     * Creates a new Melee unit
     * @param owner The new owner of the unit
     * @return The unitinformation, including the unit
     */
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
    
    /**
     * Create a new Ranged unit
     * @param owner The new owner of the unit
     * @return The unitinformation, including the unit
     */
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

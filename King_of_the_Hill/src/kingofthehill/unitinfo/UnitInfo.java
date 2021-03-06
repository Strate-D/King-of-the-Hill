/**
 * 
 */
package kingofthehill.unitinfo;

import kingofthehill.domain.Defence;
import kingofthehill.domain.IPlayer;
import kingofthehill.domain.Melee;
import kingofthehill.domain.Ranged;
import kingofthehill.domain.Resource;
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
        final int HP = 300;
        final int ATK = 10;
        final int AMR = 5;
        final int COST = 10;
        final int COOL = 150;
        
        Unit def = new Defence(HP, ATK, AMR, owner);
        return new UnitInfo(COST, def, COOL, UnitType.DEFENCE);
    }
    
    /**
     * Creates a new Melee unit
     * @param owner The new owner of the unit
     * @return The unitinformation, including the unit
     */
    public static UnitInfo getMeleeUnit(IPlayer owner)
    {
        final int HP = 70;
        final int ATK = 15;
        final int AMR = 7;
        final int SPD = 1;
        final int COST = 7;
        final int COOL = 100;
        
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
        final int COST = 15;
        final int RAN = 300;
        final int COOL = 200;
        
        Unit rang = new Ranged(HP, ATK, AMR, SPD, owner, RAN);
        return new UnitInfo(COST, rang, COOL, UnitType.RANGED);
    }
    
    /**
     * Create a new Resource unit
     * @param owner The new owner of the unit
     * @return The unitinformation, including the unit
     */
    public static UnitInfo getResourceUnit(IPlayer owner) 
    {
        final int HP = 50;
        final int ATK = 1;
        final int AMR = 5;
        final int COST = 20;
        final int COOL = 900;
        
        Unit res = new Resource(HP, ATK, AMR, owner);
        return new UnitInfo(COST, res, COOL, UnitType.RESOURCE);
    }
}

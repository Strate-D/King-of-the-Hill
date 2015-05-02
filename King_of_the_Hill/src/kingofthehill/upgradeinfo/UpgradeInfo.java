/**
 * 
 */
package kingofthehill.upgradeinfo;

import kingofthehill.domain.UnitType;
import kingofthehill.domain.Upgrade;

/**
 * Creates and stores upgrade information
 * @author Dennis
 */
public class UpgradeInfo {
    private final Upgrade upgrade;
    
    /**
     * Returns the upgrade
     * @return The created upgrade
     */
    public Upgrade getUpgrade(){
       return upgrade;
    }
    
    /**
     * Creates a new UpgradeInfo object
     * @param upgrade 
     */
    private UpgradeInfo(Upgrade upgrade){
        this.upgrade = upgrade;
    }
    
    /**
     * Creates a new upgrade for the unitType
     * @param targetUnit UnitType to upgrade
     * @return A new upgrade object with the upgrade
     */
    public static UpgradeInfo getWeakUpgrade(UnitType targetUnit){ 
        final double HEALTH = 1;
        final double ATTACK = 1;
        final double ARMOR = 1;
        final double SPEED = 1;
        
        Upgrade weakUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(weakUpgrade);
    }
    
    /**
     * Creates a new upgrade for the unitType
     * @param targetUnit UnitType to upgrade
     * @return A new upgrade object with the upgrade
     */
    public static UpgradeInfo getWeakNormalUpgrade(UnitType targetUnit){
        final double HEALTH = 2;
        final double ATTACK = 2;
        final double ARMOR = 2;
        final double SPEED = 2;
        
        Upgrade normalUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(normalUpgrade);
    }
    
    /**
     * Creates a new upgrade for the unitType
     * @param targetUnit UnitType to upgrade
     * @return A new upgrade object with the upgrade
     */
    public static UpgradeInfo getNormalUpgrade(UnitType targetUnit){
        final double HEALTH = 3;
        final double ATTACK = 3;
        final double ARMOR = 3;
        final double SPEED = 3;
        
        Upgrade normalUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(normalUpgrade);
    }
    
    /**
     * Creates a new upgrade for the unitType
     * @param targetUnit UnitType to upgrade
     * @return A new upgrade object with the upgrade
     */
    public static UpgradeInfo getNormalStrongUpgrade(UnitType targetUnit){
        final double HEALTH = 4;
        final double ATTACK = 4;
        final double ARMOR = 4;
        final double SPEED = 4;
        
        Upgrade normalUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(normalUpgrade);
    }
    
    /**
     * Creates a new upgrade for the unitType
     * @param targetUnit UnitType to upgrade
     * @return A new upgrade object with the upgrade
     */
    public static UpgradeInfo getStrongUpgrade(UnitType targetUnit){
        final double HEALTH = 5;
        final double ATTACK = 5;
        final double ARMOR = 5;
        final double SPEED = 5;
        
        Upgrade strongUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(strongUpgrade);
    }
    
    /**
     * Creates a new upgrade for the unitType
     * @param targetUnit UnitType to upgrade
     * @return A new upgrade object with the upgrade
     */
    public static UpgradeInfo getUberUpgrade(UnitType targetUnit){
        final double HEALTH = 6;
        final double ATTACK = 6;
        final double ARMOR = 6;
        final double SPEED = 6;
        
        Upgrade strongUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(strongUpgrade);
    }
}

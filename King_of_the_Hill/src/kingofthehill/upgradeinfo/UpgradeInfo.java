/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.upgradeinfo;

import kingofthehill.domain.Unit;
import kingofthehill.domain.UnitType;
import kingofthehill.domain.Upgrade;

/**
 *
 * @author Dennis
 */
public class UpgradeInfo {
    private Upgrade upgrade;
    
    public Upgrade getUpgrade(){
       return upgrade;
    }
    
    private UpgradeInfo(Upgrade upgrade){
        this.upgrade = upgrade;
    }
    
    public static UpgradeInfo getWeakUpgrade(UnitType targetUnit){
        final double HEALTH = 1;
        final double ATTACK = 1;
        final double ARMOR = 1;
        final double SPEED = 1;
        
        Upgrade weakUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(weakUpgrade);
    }
    
    public static UpgradeInfo getNormalUpgrade(UnitType targetUnit){
        final double HEALTH = 1;
        final double ATTACK = 1;
        final double ARMOR = 1;
        final double SPEED = 1;
        
        Upgrade normalUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(normalUpgrade);
    }
    
    public static UpgradeInfo getStrongUpgrade(UnitType targetUnit){
        final double HEALTH = 1;
        final double ATTACK = 1;
        final double ARMOR = 1;
        final double SPEED = 1;
        
        Upgrade strongUpgrade = new Upgrade(HEALTH, ATTACK, ARMOR, SPEED, targetUnit);
        return new UpgradeInfo(strongUpgrade);
    }
}

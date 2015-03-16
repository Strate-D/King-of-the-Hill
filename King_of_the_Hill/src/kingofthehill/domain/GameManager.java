/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.List;

/**
 * Class that manages the game. Contains all the units and objects that are part of it.
 * @author Jur
 */
public class GameManager {
    private List<IPlayer> players;
    private Mysterybox mysterybox;
    private GameMode gameMode;
    
    public GameManager(IPlayer player, GameMode gameMode){
        
    }
    
    /**
     * Let all the units do their next action.
     */
    private void operateUnits(){
        for(IPlayer p : players){
            Base b = p.getBase();
            if(b != null){
                for(Lane l : b.getLanes()){
                    for(Unit u : l.getUnits()){
                        u.doNextAction();
                    }
                }
            }
        }
    }
    
    /**
     * TODO
     */
    private void generateMysterybox(){
        throw new UnsupportedOperationException("TODO generateMysterybox");
    }
    
    /**
     * Does a step in the game (1/30 of a second).
     */
    public void doStep(){
        throw new UnsupportedOperationException("TODO doStep");
    }
    
    /**
     * Places a unit at the selected lane
     * @param player The player that places the unit. May not be null.
     * @param unit The unit that has to be placed. May not be null.
     * @param index Number between 0 and 7, with 0 to 3 being a group of lanes
     * and 4 to 7. 0 to 3 is the group of lanes where this base is baseEnd1. Must be between 0 and 7.
     * @param cost The cost of the unit, must be higher than 0.
     */
    public boolean placeUnitAtLane(IPlayer player, Unit unit, int index, int cost){
        if(player == null || unit == null || index < 0 || index > 7 || cost < 1){
            return false;
        }
        if(player.getMoney() < cost){
            return false;
        }
        Base base = player.getBase();
        if (base != null){
            Lane l = base.getLane(index);
            if(l != null){
                l.addUnit(unit);
                player.payMoney(cost);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @param player
     * @param unit
     * @param index 
     */
    public void placeUnitAtBase(IPlayer player, Unit unit, int index){
        
    }
}

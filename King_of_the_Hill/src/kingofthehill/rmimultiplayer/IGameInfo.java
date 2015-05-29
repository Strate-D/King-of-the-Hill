/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.rmimultiplayer;

import java.util.Iterator;
import java.util.List;
import kingofthehill.domain.IPlayer;
import kingofthehill.domain.Mysterybox;
import kingofthehill.domain.Unit;

/**
 *
 * @author Dennis
 */
public interface IGameInfo{
    /**
     * Gets all the players in the game
     * @return List of IPlayers
     */
    public List<IPlayer> getPlayers();
    
    /**
     * Gets the mysterybox of the game
     * @return mysterybox or null if mysterybox isn't active
     */
    public Mysterybox getMysterybox();
    
    /**
     * Gets the resourcetimer
     * @return int with the resourcetimer
     */
    public int getResourcetimer();
    
    /**
     * Gets the mysteryboxtimer
     * @return int with the mysteryboxtimer
     */
    public int getMysteryboxtimer();
    
    /**
     * Gets the mysteryboxtime when an mysterybox is active
     * @return int with the mysteryboxtime
     */
    public int getMysteryboxtime();
    
    /**
     * Get all the units for drawing purposes
     * @return A iterator of units
     */
    public Iterator<Unit> getLaneUnits();
    
    /**
     * Checks and moves the player to the first position in the list to be sure the player is always in the top left corner of the field
     * @param name name of the player
     */
    public void setFirstPlayer(String name);
    
    /**
     * doStep methode for the client, so the client can move the active units without being dependent of the remote gamemanager
     */
    public void doStep();
}

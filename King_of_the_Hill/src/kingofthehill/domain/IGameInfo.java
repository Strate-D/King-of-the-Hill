/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Dennis
 */
public interface IGameInfo{ 
    public List<IPlayer> getPlayers();
    
    public Mysterybox getMysterybox();
    
    public int getResourcetimer();
    
    public int getMysteryboxtimer();
    
    public int getMysteryboxtime();
    
    public Iterator<Unit> getLaneUnits();
}

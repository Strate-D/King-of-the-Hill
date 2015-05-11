/**
 * 
 */
package kingofthehill.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Object that contains information about the team
 * @author Jur
 */
public class Team implements Serializable {
    private int nr;
    private List<IPlayer> players;
    
    /**
     * Creates a new Team object
     * @param nr Teamnumber. Must be bigger than 0
     * @param players List of players in the team. May not be null, may be empty.
     * @throws IllegalArgumentException 
     */
    public Team(int nr, List<IPlayer> players) throws IllegalArgumentException{
        if(nr < 0){
            throw new IllegalArgumentException("Number may not be smalle than 1");
        }
        if(players == null){
            throw new IllegalArgumentException("Players may not be NULL");
        }
        
        this.nr = nr;
        this.players = players;
    }
    
    /**
     * Returns the team number
     * @return the team number
     */
    public int getNr(){
        return this.nr;
    }
    
    /**
     * Returns the player list
     * @return The player list. Is never null
     */
    public List<IPlayer> getPlayers(){
        return Collections.unmodifiableList(players);
    }
    
    /**
     * Adds a new player to the team
     * @param newplayer Must be not null
     * @return If the player is added or not. Will also return true if the player was in the team.
     */
    public boolean addPlayer(IPlayer newplayer){
        if(newplayer == null){
            return false;
        }
        if(players.contains(newplayer)){
            return true;
        }
        players.add(newplayer);
        newplayer.setTeam(this);
        return true;
    }
}

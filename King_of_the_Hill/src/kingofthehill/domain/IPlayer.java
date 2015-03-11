/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.List;

/**
 * Interface for all the players in a game. AI and human players.
 * @author Jur
 */
public interface IPlayer {
    /**
     * 
     * @return The name of the player
     */
    public String getName();
    
    /**
     * 
     * @return The total exp points of the player
     */
    public int getExp();
    
    /**
     * 
     * @return The score of the player in the current game
     */
    public int getScore();
    
    /**
     * 
     * @return All the upgrades the players has.
     */
    public List<Upgrade> getUpgrades();
    
    /**
     * Checks if the entered password is correct
     * @param password The entered password. Is not allowed to be null!
     * @return If the password is correct or not
     */
    public boolean checkPassword(String password);
    
    /**
     * Sets the team of the player
     * @param newTeam The new team of the player. Can be null.
     */
    public void setTeam(Team newTeam);
}

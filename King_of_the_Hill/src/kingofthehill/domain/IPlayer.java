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
     * Gets the players team
     * @return The team of the player, can be null
     */
    public Team getTeam();
    
    /**
     * Sets the team of the player
     * @param newTeam The new team of the player. Can be null.
     */
    public void setTeam(Team newTeam);
    
    /**
     * Gets the money of the player
     * @return Returns the amount of money the player has.
     */
    public int getMoney();
    
    /**
     * Adds money to the players amount of money
     * @param amount The amount that has to be added, has to be positive
     */
    public void addMoney(int amount);
    
    /**
     * Lowers the amount of money of the player
     * @param amount The amount of money the player has to pay, has to be positive
     * @return Boolean if the player has enough money or not, if not the money is not lowered
     */
    public boolean payMoney(int amount);
    
    /**
     * Gets the base of the player
     * @return The base of the player, can be null.
     */
    public Base getBase();
    
    /**
     * Sets the base for the player.
     * @param newBase The new base, can be null.
     */
    public void setBase(Base newBase);
    
    /**
     * Adds points to the player
     * @param points The amount of points that has to be added, can be negative
     */
    public void addPoints(int points);
}

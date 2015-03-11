/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

/**
 * Enums for all types of gamemodes
 * @author Jur
 */
public enum GameMode {
    F4A("Free for All"),
    Coop("2vs2 co-op");
    
    private final String descr;
    
    private GameMode(final String descr) {
        this.descr = descr;
    }
    
    /**
     * Gets the description of the gamemode
     * @return A string with the description of the gamemode
     */
    public String getDescription(){
        return this.descr;
    }
}

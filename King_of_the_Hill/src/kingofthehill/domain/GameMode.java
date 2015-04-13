/**
 * 
 */
package kingofthehill.domain;

/**
 * Enums for all types of gamemodes
 * @author Jur
 */
public enum GameMode {
    F4A("Free for All"),
    COOP("2vs2 co-op"),
    SINGLEPLAYER("Singleplayer vs AI");
    
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

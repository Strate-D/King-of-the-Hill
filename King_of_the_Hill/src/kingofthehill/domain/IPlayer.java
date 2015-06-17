/**
 *
 */
package kingofthehill.domain;

import java.util.List;

/**
 * Interface for all the players in a game. AI and human players.
 *
 * @author Jur
 */
public interface IPlayer {

    /**
     * Returns the name of the player
     *
     * @return The name of the player
     */
    public String getName();

    /**
     * Returns the experiance points the player has gained
     *
     * @return The total exp points of the player
     */
    public int getExp();

    /**
     * Returns the total score of the player in the current game
     *
     * @return The score of the player in the current game
     */
    public int getScore();

    /**
     * Adds an upgrade to a player
     *
     * @param upgrade Specifies the upgrade that needs to be added
     */
    public void addUpgrade(Upgrade upgrade);

    /**
     * Returns all the upgrades the player has in the current game
     *
     * @return All the upgrades the player has.
     */
    public List<Upgrade> getUpgrades();

    /**
     * Gets the players team
     *
     * @return The team of the player, can be null
     */
    public Team getTeam();

    /**
     * Sets the team of the player
     *
     * @param newTeam The new team of the player. Can be null.
     */
    public void setTeam(Team newTeam);

    /**
     * Gets the money of the player
     *
     * @return Returns the amount of money the player has.
     */
    public int getMoney();

    /**
     * Adds money to the players amount of money
     *
     * @param amount The amount that has to be added, has to be positive
     */
    public void addMoney(int amount);

    /**
     * Lowers the amount of money of the player
     *
     * @param amount The amount of money the player has to pay, has to be
     * positive
     * @return Boolean if the player has enough money or not, if not the money
     * is not lowered
     */
    public boolean payMoney(int amount);

    /**
     * Gets the base of the player
     *
     * @return The base of the player, can be null.
     */
    public Base getBase();

    /**
     * Sets the base for the player.
     *
     * @param newBase The new base, can be null.
     */
    public void setBase(Base newBase);

    /**
     * Adds points to the player
     *
     * @param points The amount of points that has to be added, can be negative
     */
    public void addPoints(int points);

    /**
     * Gets the connectionTimer of the player. -1 means it's an AI so there is
     * no connection 0 means connection has timed out, so player has to be
     * replaced with AI Higher then 0 means amount of frames left till timeout
     *
     * @return The connectiontimer value
     */
    public int getConnectionTimer();

    /**
     * Resets the connectiontimer. Has to be called by the client, so that the
     * server knows that the connection is still alive.
     */
    public void resetConnectionTimer();

    /**
     * Lower the connectiontimer. Is called each frame by the server to make
     * sure that the client is still connected.
     */
    public void lowerConnectionTimer();

    /**
     * Returns the gamemode of the game the player is in, can be null
     */
    public GameMode getGameMode();

    /**
     * Sets the gamemode of the player, not null
     *
     * @return
     */
    public void setGameMode(GameMode newGameMode);
}

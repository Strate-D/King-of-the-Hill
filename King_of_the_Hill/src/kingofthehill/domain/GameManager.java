/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that manages the game. Contains all the units and objects that are part
 * of it.
 *
 * @author Jur
 */
public class GameManager {

    private List<IPlayer> players;
    private Mysterybox mysterybox;
    private GameMode gameMode;
    private int resourceTimer;

    /**
     * Creates a new gameManager, also creating a new game with it.
     *
     * @param player The player that is playing may not be null.
     */
    public GameManager(IPlayer player) {
        //Set resourceTimer to 0
        resourceTimer = 0;

        if (player == null) {
            throw new IllegalArgumentException("Player may not be null");
        }
        //Add players
        this.players = new ArrayList<>();
        this.players.add(player);
        this.players.add(new AI("AI1"));
        this.players.add(new AI("AI2"));
        this.players.add(new AI("AI3"));

        //Create teams
        Team team1 = new Team(1, new ArrayList<>());
        Team team2 = new Team(2, new ArrayList<>());
        boolean firstTeam = true;
        for (IPlayer p : this.players) {
            if (firstTeam) {
                team1.addPlayer(p);
                firstTeam = false;
            } else {
                team2.addPlayer(p);
                firstTeam = true;
            }
        }

        //Give players bases;
        for (IPlayer p : this.players) {
            Base b = new Base(p);
        }
        //Give the bases lanes
        int i = 0;
        for (IPlayer p : this.players) {
            //Get bases at ends of the lanes
            Base base1 = p.getBase();
            Base base2;
            if (i + 1 >= this.players.size()) {
                base2 = this.players.get(0).getBase();
            } else {
                base2 = this.players.get(i + 1).getBase();
            }
            //Set lanes
            for (int j = 0; j < 4; j++) {
                Lane lane = new Lane(base1, base2);
                base1.setLane(j, lane);
                base2.setLane(j + 4, lane);
            }
            i++;
        }
    }

    /**
     * Let all the units do their next action.
     */
    private void operateUnits() {
        for (IPlayer p : players) {
            Base b = p.getBase();
            if (b != null) {
                for (Lane l : b.getLanes()) {
                    for (Unit u : l.getUnits()) {
                        u.doNextAction();
                    }
                }
            }
        }
    }

    /**
     * TODO
     */
    private void generateMysterybox() {
        throw new UnsupportedOperationException("TODO generateMysterybox");
    }

    /**
     * Does a step in the game (1/30 of a second).
     */
    public void doStep() {
        //Check if players should get resources.
        if (this.resourceTimer > 600) {
            giveResources();
            this.resourceTimer = 0;
        }
        //Operate all units
        operateUnits();

        //Keep track of timers
        this.resourceTimer++;
    }

    /**
     * Calculates how many resources every player should get and gives it to
     * them.
     */
    private void giveResources() {
        for (IPlayer p : this.players) {
            p.addMoney(10);
        }
    }

    /**
     * Places a unit at the selected lane
     *
     * @param player The player that places the unit. May not be null.
     * @param unit The unit that has to be placed. May not be null.
     * @param index Number between 0 and 7, with 0 to 3 being a group of lanes
     * and 4 to 7. 0 to 3 is the group of lanes where this base is baseEnd1.
     * Must be between 0 and 7.
     * @param cost The cost of the unit, must be higher than 0.
     */
    public boolean placeUnitAtLane(IPlayer player, Unit unit, int index, int cost) {
        //Check input
        if (player == null || unit == null || index < 0 || index > 7 || cost < 1) {
            return false;
        }
        //Check if player has enough money
        if (player.getMoney() < cost) {
            return false;
        }
        //Place unit if possible
        Base base = player.getBase();
        if (base != null) {
            Lane l = base.getLane(index);
            if (l != null) {
                l.addUnit(unit);
                player.payMoney(cost);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a defencive unit to the base on the given place.
     *
     * @param player The player that places the unit, may not be null.
     * @param unit The unit that has to be placed, may not be null.
     * @param index The index for the unit, must be between 0 and 31. 0 to 15
     * being the group that is at the lane where the base of the player is
     * baseEnd1, 16 to 31 being the other lane.
     * @param cost The cost of the unit, must be higher than 0.
     */
    public boolean placeUnitAtBase(IPlayer player, Unit unit, int index, int cost) {
        //Check input
        if (player == null || unit == null || index > 31 || index < 0 || cost < 1) {
            return false;
        }
        //Check if player has enough money
        if (player.getMoney() < cost) {
            return false;
        }
        //Place unit if possible
        Base b = player.getBase();
        if (b != null) {
            if (b.setUnit(index, unit)) {
                player.payMoney(cost);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Gets all the players in this game
     *
     * @return A unmodifiable list, will never be empty
     */
    public List<IPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}

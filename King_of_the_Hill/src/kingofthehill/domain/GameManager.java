/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import static java.lang.System.gc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import kingofthehill.unitinfo.UnitInfo;

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

    private boolean DebugLevelAI = false;

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
            p.setBase(b);
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
//                    for (Unit u : l.getUnits()) {
//                        u.doNextAction();
//                    }

                    //Bas C code
                    List<Unit> doneUnits = new ArrayList<>();
                    while (doneUnits.size() < l.getUnits().size()) {
                        try {
                            for (Unit u : l.getUnits()) {
                                if (!doneUnits.contains(u)) {
                                    u.doNextAction();
                                    doneUnits.add(u);
                                }
                            }
                        } catch (Exception ecx) {
                        }
                    }

                }

//                for(Unit u : b.getUnits())
//                {
//                    u.doNextAction();
//                }
                //Bas C code
                List<Unit> doneUnits = new ArrayList<>();
                while (doneUnits.size() < b.getUnits().size()) {
                    try {
                        for (Unit u : b.getUnits()) {
                            if (!doneUnits.contains(u)) {
                                doneUnits.add(u);
                                u.doNextAction();
                            }
                        }
                    } catch (Exception ecx) {
                    }
                }
            }
        }
    }

    /**
     * Get all the units for drawing purposes
     *
     * @return A iterator of units
     */
    public Iterator<Unit> getUnits() {
        ArrayList<Unit> list = new ArrayList<>();
        for (IPlayer p : players) {
            Base b = p.getBase();
            if (b != null) {
                for (Lane l : b.getLanes()) {
                    for (Unit u : l.getUnits()) {
                        list.add(u);
                    }
                }
            }
        }
        return list.iterator();
    }

    /**
     * TODO
     */
    private void generateMysterybox() {
        throw new UnsupportedOperationException("TODO generateMysterybox");
    }

    /**
     * Does actions for the AI player.
     *
     * @param player The AI player that needs to do something
     */
    private void operateAIPlayer(AI player) {
        OutputDebugInfo(player, "AI operation for: ", player.getName());

        boolean iNeededToPlaceDefence = false;

        // Let the AI decide what to do
        for (int i = 0; i < 8; i++) {
            //Check the AI defences
            int currentDefence = player.areDefenceUnitsAtLane(i);
            //OutputDebugInfo(player, "Defence at lane " + i + ": ", currentDefence);
            if (currentDefence < 2) {
                // Check if the AI has lost units
                int oldDefence = player.getDefenceAtLane(i);
                OutputDebugInfo(player, "Defence at lane " + i + " in last turn : ", currentDefence);
                if (oldDefence >= currentDefence) {
                    OutputDebugInfo(player, "Units that i lost: ", oldDefence - currentDefence);
                    // The AI has lost units
                    // TODO: Spawn an extra attack unit for more strenght

                    // Get a new place to spawn a defence unit
                    int randomNewDefenceSpot = i * 4 + Math.abs(getNextRandom(player, 0, 2));
                    while (player.getBase().getUnit(randomNewDefenceSpot) != null) {
                        randomNewDefenceSpot = i * 4 + Math.abs(getNextRandom(player, 0, 2));
                    }

                    OutputDebugInfo(player, "Spawn defence unit at lane " + i + " on spot[" + i * 4 + "-" + (i * 4 + 3) + "]: ", randomNewDefenceSpot);

                    UnitInfo ui = UnitInfo.getDefenceUnit(player);
                    this.placeUnitAtBase(
                            player,
                            ui.getUnit(),
                            randomNewDefenceSpot,
                            ui.getKosten());

                    iNeededToPlaceDefence = true;
                }
            }
        }

        // Decide to do other stuff when there are enough defence units placed
        if (!iNeededToPlaceDefence) {
            //OutputDebugInfo(player, "There is enough defence everywhere", "");
            // Decide what to do with the rest of the money:
            // - Spawn attack unit
            // - Spawn extra defence unit
            // - Bid on the Mysterybox
            // - Do an upgrade for units

            //Percentage that indicate if a action should be done
            double[] chance = new double[]{80.0, 50.0, 10.0, 15.0};
            double chanceState = 100;
            int toDoAction = 0;
            while (chanceState > chance[toDoAction]) {
                toDoAction = getNextRandom(player, 0, 3);
                chanceState = getNextRandom(player, 0, 100);
                OutputDebugInfo(player, "", "action=" + toDoAction + "    " + chanceState + "/" + chance[toDoAction]);
            }

            if (toDoAction == 0) {
                //Spawn attack unit
                OutputDebugInfo(player, "--Place an attack unit", "");
                
                int newSpot = Math.abs(getNextRandom(player, 0, 7));
                
                UnitInfo ui = UnitInfo.getMeleeUnit(player);
                this.placeUnitAtBase(player,
                        ui.getUnit(),
                        newSpot * 4 + 3,
                        ui.getKosten());
            } else if (toDoAction == 1) {
                // Spawn extra defence unit
                OutputDebugInfo(player, "--Place an extra defence unit", "");

                int randomNewDefenceSpot = Math.abs(getNextRandom(player, 0, 31));
                while (player.getBase().getUnit(randomNewDefenceSpot) != null || randomNewDefenceSpot % 4 == 3) {
                    randomNewDefenceSpot = Math.abs(getNextRandom(player, 0, 31));
                }

                UnitInfo ui = UnitInfo.getDefenceUnit(player);
                this.placeUnitAtBase(player,
                        ui.getUnit(),
                        randomNewDefenceSpot,
                        ui.getKosten());
            } else if (toDoAction == 2) {
                // Bid on Mysterybox
                OutputDebugInfo(player, "--Bid on the mysterybox", "");
            } else {
                // Do an upgrade for units
                OutputDebugInfo(player, "--Do an upgrade for units", "");
            }
        }
    }

    private int getNextRandom(AI player, int low, int high) {
        return (int) (Math.random() * player.getRandomSeed()) % (high + 1) + low;
    }

    private void OutputDebugInfo(AI player, String pretext, Object data) {
        if (DebugLevelAI) {
            if (player.getName().equals("AI1")) {
                System.out.println(pretext + data.toString());
            }
        }
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

        //Operate all AI's
        //Check if there are any AI players
        for (IPlayer player : players) {
            if (player instanceof AI) // The IPlayer is AI. Operate the AI player
            {
                operateAIPlayer((AI) player);
            }
        }

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
     * @return true if unit is placed at lane, else false
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
     * @return true if unit is placed at base, else false
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

    public void setDebugLevelAI(boolean value) {
        this.DebugLevelAI = value;
    }
}

/**
 *
 */
package kingofthehill.domain;

import kingofthehill.rmimultiplayer.IGameInfo;
import kingofthehill.rmimultiplayer.GameInfo;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import kingofthehill.unitinfo.UnitInfo;
import kingofthehill.upgradeinfo.UpgradeInfo;

/**
 * Class that manages the game. Contains all the units and objects that are part
 * of it.
 *
 * @author Jur
 */
public class GameManager extends UnicastRemoteObject implements IGameManager {

    private List<IPlayer> players;
    private List<String> readyPlayers;
    private Mysterybox mysterybox;
    private int resourceTimer;
    private int mysteryboxTimer;
    private int mysteryboxTime;
    private Random mysteryboxRandom;
    private GameInfo gameInfo;
    private Timer timer;
    private boolean readyGame;

    private boolean DebugLevelAI = false;

    /**
     * Creates a new gameManager, also creating a new game with it.
     *
     * @throws java.rmi.RemoteException
     */
    public GameManager() throws RemoteException {
        /**
         * Set resourceTimer to 0
         */
        this.players = new ArrayList<>();
        this.readyPlayers = new ArrayList<>();
        this.resourceTimer = 0;
        this.mysteryboxTimer = 0;
        this.mysteryboxTime = 0;
        this.readyGame = false;
    }

    /**
     *
     * @param player
     * @param isAi
     * @throws RemoteException
     */
    @Override
    public void addPlayer(String player, boolean isAi) throws RemoteException {
        if (player != null) {
            if (isAi) {
                AI ai;
                int count = 0;

                for (IPlayer p : players) {
                    if (p instanceof AI) {
                        count = +1;
                    }
                }

                ai = new AI("ArtificialIntelligence" + count);

                Random random = new Random();
                switch (random.nextInt(3)) {
                    case 0:
                        ai.setAIType(AIState.AGRESSIVE);
                        break;
                    case 1:
                        ai.setAIType(AIState.DEFENSIVE);
                        break;
                    case 2:
                        ai.setAIType(AIState.MODERNATE);
                        break;
                }

                players.add(ai);
                this.setPlayerReady(ai.getName());
            } else {
                players.add(new Player(player, 0));
            }
        }
    }

    /**
     *
     * @param player
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean setPlayerReady(String player) throws RemoteException {
        for (String p : readyPlayers) {
            if (p.equals(player)) {
                readyPlayers.remove(player);
                return false;
            }
        }

        readyPlayers.add(player);

        if (readyPlayers.size() > 3) {
            startGame();
        }

        return true;
    }

    /**
     * Start game when there are a total of 4 players
     */
    private void startGame() {
        /**
         * Create teams
         */
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

        /**
         * Give players bases
         */
        for (IPlayer p : this.players) {
            Base b = new Base(p);
            p.setBase(b);
        }

        /**
         * Give the bases lanes
         */
        int i = 0;
        for (IPlayer p : this.players) {
            /**
             * Get bases at ends of the lanes
             */
            Base base1 = p.getBase();
            Base base2;
            if (i + 1 >= this.players.size()) {
                base2 = this.players.get(0).getBase();
            } else {
                base2 = this.players.get(i + 1).getBase();
            }

            /**
             * Set lanes
             */
            for (int j = 0; j < 4; j++) {
                Lane lane = new Lane(base1, base2);
                base1.setLane(j, lane);
                base2.setLane(j + 4, lane);
            }
            i++;
        }

        /**
         * Set mysteryboxtime at random for the first time
         */
        mysteryboxRandom = new Random();
        mysteryboxTime = mysteryboxRandom.nextInt(1800) + 1800;

        /**
         * Create new GameInfo object and set the info for the first time;
         */
        gameInfo = new GameInfo();
        gameInfo.setInfo(players, mysterybox, resourceTimer, mysteryboxTimer, mysteryboxTime);

        /**
         * Create and schedule timer for dostep method
         */
        timer = new Timer();
        timer.schedule(new GameLoop(), 0, 1000 / 60);

        readyGame = true;
    }

    @Override
    public boolean readyGame() throws RemoteException {
        return this.readyGame;
    }

    private class GameLoop extends java.util.TimerTask {

        @Override
        public void run() {
            doStep();
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
    public Iterator<Unit> getLaneUnits() {
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

    private void generateMysterybox() {
        Random r = new Random();
        UnitType unitType = null;

        /**
         * Random choose unittype for upgrade
         */
        switch (r.nextInt(2)) {
            /**
             * Defence
             */
            case 0:
                unitType = UnitType.DEFENCE;
                break;
            /**
             * Melee
             */
            case 1:
                unitType = UnitType.MELEE;
                break;
            /**
             * Ranged
             */
            case 2:
                unitType = UnitType.RANGED;
                break;
        }

        /**
         * Random generate content mysterybox
         */
        switch (r.nextInt(2)) {
            /**
             * Mysterybox has resources
             */
            case 0:
                mysterybox = new Mysterybox(r.nextInt(90) + 10, null, null, 0);
                break;
            /**
             * Mysterybox has upgrade
             */
            case 1:
                if (unitType != null) {
                    UpgradeInfo upgradeInfo = null;
                    /**
                     * Random choose upgradetype
                     */
                    switch (r.nextInt(6)) {
                        /**
                         * Weak upgrade
                         */
                        case 0:
                            upgradeInfo = UpgradeInfo.getWeakUpgrade(unitType);
                            break;
                        /**
                         * Weak-normal upgrade
                         */
                        case 1:
                            upgradeInfo = UpgradeInfo.getWeakNormalUpgrade(unitType);
                            break;
                        /**
                         * Normal upgrade
                         */
                        case 2:
                            upgradeInfo = UpgradeInfo.getNormalUpgrade(unitType);
                            break;
                        /**
                         * Normal-strong upgrade
                         */
                        case 3:
                            upgradeInfo = UpgradeInfo.getNormalStrongUpgrade(unitType);
                            break;
                        /**
                         * Strong upgrade
                         */
                        case 4:
                            upgradeInfo = UpgradeInfo.getStrongUpgrade(unitType);
                            break;
                        /**
                         * Uber upgrade
                         */
                        case 5:
                            upgradeInfo = UpgradeInfo.getUberUpgrade(unitType);
                            break;
                    }

                    if (upgradeInfo != null) {
                        mysterybox = new Mysterybox(0, upgradeInfo.getUpgrade(), null, 0);
                    }
                }
                break;
            /**
             * Mysterybox has unit(s)
             */
            case 2:
                mysterybox = new Mysterybox(0, null, unitType, r.nextInt(9) + 1);
                break;
        }
    }

    /**
     * Does actions for the AI player.
     *
     * @param player The AI player that needs to do something. Cannot be null
     */
    private void operateAIPlayer(AI player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        player.doNextAction(this);
    }

    /**
     * Does a step in the game (1/60 of a second).
     */
    private void doStep() {
        //Create game info for clients
        gameInfo.setInfo(this.players, this.mysterybox, this.resourceTimer, this.mysteryboxTimer, this.mysteryboxTime);

        //Check player connections
        for (IPlayer p : this.players) {
            p.lowerConnectionTimer();
            if (p.getConnectionTimer() == 0) {
                //replace player with si
            }
        }

        /**
         * Check if players should get resources.
         */
        if (this.resourceTimer > 600) {
            giveResources();
            this.resourceTimer = 0;
        }

        /**
         * Operate all units
         */
        operateUnits();

        /**
         * Operate all AI's Check if there are any AI players
         */
        for (IPlayer player : players) {
            if (player instanceof AI) {
                /**
                 * The IPlayer is AI. Operate the AI player
                 */
                operateAIPlayer((AI) player);
            }
        }

        /**
         * Generate mysterybox
         */
        if (mysteryboxTimer > mysteryboxTime) {
            generateMysterybox();
            mysteryboxTimer = 0;
            mysteryboxTime = mysteryboxRandom.nextInt(1800) + 1800;
        }

        /**
         * Unpack mysterybox
         */
        if (mysterybox != null) {
            if (mysteryboxTimer > mysterybox.getDuration()) {
                IPlayer higestBidder = mysterybox.getHigestBidder();
                if (higestBidder != null) {
                    if (mysterybox.getResourceAmount() != 0) {
                        higestBidder.addMoney(mysterybox.getResourceAmount());
                    } else if (mysterybox.getUpgrade() != null) {
                        higestBidder.addUpgrade(mysterybox.getUpgrade());
                    } else if (mysterybox.getUnitType() != null) {
                        //todo
                    }
                }
                mysterybox = null;
            }
        }

        /**
         * Keep track of timers
         */
        this.resourceTimer++;
        this.mysteryboxTimer++;
    }

    /**
     * Calculates how many resources every player should get and gives it to
     * them.
     */
    private void giveResources() {
        for (IPlayer p : this.players) {
            int munnie = 10;
            for (Unit u : p.getBase().getUnits()) {
                if (u instanceof Resource) {
                    munnie += 2;
                }
            }
            p.addMoney(munnie);
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
        /**
         * Check input
         */
        if (player == null || player.getBase() == null || unit == null || index < 0 || index > 7 || cost < 1 || player.getBase().getHealthPoints() <= 0) {
            return false;
        }

        /**
         * Check if player has enough money
         */
        if (player.getMoney() < cost) {
            return false;
        }

        /**
         * Place unit if possible
         */
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
     *
     * @param name
     * @return
     */
    private IPlayer getPlayer(String name) {
        for (IPlayer p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    /**
     *
     * @param playername
     * @param unit
     * @param index
     * @param cost
     * @return
     */
    @Override
    public boolean placeUnitAtBaseMulti(String playername, Unit unit, int index, int cost) {
        IPlayer player = getPlayer(playername);
        Unit unitNew = null;

        //Check unittype
        switch (unit.getType()) {
            case MELEE:
                unitNew = UnitInfo.getMeleeUnit(player).getUnit();
                break;
            case RANGED:
                unitNew = UnitInfo.getRangedUnit(player).getUnit();
                break;
            case DEFENCE:
                unitNew = UnitInfo.getDefenceUnit(player).getUnit();
                break;
            case RESOURCE:
                unitNew = UnitInfo.getResourceUnit(player).getUnit();
                break;
        }

        return placeUnitAtBase(getPlayer(playername), unitNew, index, cost);
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
    @Override
    public boolean placeUnitAtBase(IPlayer player, Unit unit, int index, int cost) {
        /**
         * Check input
         */
        if (player == null || player.getBase() == null || unit == null || index > 31 || index < 0 || cost < 1 || player.getBase().getHealthPoints() <= 0) {
            return false;
        }

        /**
         * Check if player has enough money
         */
        if (player.getMoney() < cost) {
            return false;
        }

        /**
         * Place unit if possible
         */
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

    /**
     * Returns the mysterybox object
     *
     * @return The mysterybox object currently active in the game
     */
    public Mysterybox getMysterybox() {
        return this.mysterybox;
    }

    @Override
    public void bidMysteryboxMulti(String playername, int bid) {
        IPlayer player = getPlayer(playername);

        if (mysterybox != null && player != null) {
            mysterybox.bid(player, bid);
        }
    }

    @Override
    public IGameInfo getGameInfo() throws RemoteException {
        return this.gameInfo;
    }
}

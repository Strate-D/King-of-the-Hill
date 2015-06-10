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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.gameInfo = new GameInfo();
    }
    
    @Override
    public synchronized void addPlayer(String player, boolean isAi) throws RemoteException {
        /**
         * Check input
         */
        if (player != null) {
            if (isAi) {
                AI ai;
                int count = 1;

                /**
                 * Check how many AI players there are and generate an unique AI
                 * name
                 */
                for (IPlayer p : players) {
                    if (p instanceof AI) {
                        count++;
                    }
                }
                
                ai = new AI("ArtificialIntelligence" + count);

                /**
                 * Randomly set state of AI
                 */
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

            /**
             * Update gameinfo for lobby
             */
            gameInfo.setInfo(this.players, this.mysterybox, this.resourceTimer, this.mysteryboxTimer, this.mysteryboxTime);
        }
    }
    
    @Override
    public synchronized void removePlayer(String player) throws RemoteException {
        /**
         * Check input
         */
        if (player != null) {
            players.remove(getPlayer(player));
        }

        /**
         * Update gameinfo for lobby
         */
        gameInfo.setInfo(this.players, this.mysterybox, this.resourceTimer, this.mysteryboxTimer, this.mysteryboxTime);
    }
    
    @Override
    public synchronized boolean setPlayerReady(String player) throws RemoteException {
        /**
         * Check input
         */
        if (player == null) {
            return false;
        }

        /**
         * Check if player with the same name already exsists
         */
        for (String p : readyPlayers) {
            if (p.equals(player)) {
                readyPlayers.remove(player);
                return false;
            }
        }
        
        readyPlayers.add(player);

        /**
         * Start game when all 4 players are ready
         */
        if (readyPlayers.size() > 3) {
            startGame();
        }
        
        return true;
    }
    
    @Override
    public boolean getPlayerReady(String player) {
        /**
         * Check if player with name is ready
         */
        for (String p : readyPlayers) {
            if (p.equals(player)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Start game
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
         * Set the info for the first time in the gameinfo object
         */
        gameInfo.setInfo(players, mysterybox, resourceTimer, mysteryboxTimer, mysteryboxTime);

        /**
         * Create and schedule timer for dostep method
         */
        timer = new Timer();
        timer.schedule(new GameLoop(), 0, 1000 / 30);
        
        readyGame = true;
    }
    
    @Override
    public boolean readyGame() throws RemoteException {
        return this.readyGame;
    }
    
    @Override
    public synchronized void sendPlayerSignal(String playername) throws RemoteException {
        //Get the player
        IPlayer player = this.getPlayer(playername);
        //Set hearthbeat
        player.resetConnectionTimer();
    }
    
    @Override
    public synchronized void setPlayerToAI(String playername) throws RemoteException {
        //Create new AI
        AI newPlayer = new AI("AI" + playername);

        //Get old player
        IPlayer oldPlayer = this.getPlayer(playername);

        //Give all upgrades to new player
        for (Upgrade u : oldPlayer.getUpgrades()) {
            newPlayer.addUpgrade(u);
        }
        //Give the units
        for (Lane l : oldPlayer.getBase().getLanes()) {
            for (Unit u : l.getUnits()) {
                if (u.getOwner() == oldPlayer) {
                    u.setOwner(newPlayer);
                }
            }
        }
        for (Unit u : oldPlayer.getBase().getUnits()) {
            if (u.getOwner() == oldPlayer) {
                u.setOwner(newPlayer);
            }
        }
        //Give the player the base
        oldPlayer.getBase().setOwner(newPlayer);
        newPlayer.setBase(oldPlayer.getBase());
        //Set type
        newPlayer.setAIType(AIState.AGRESSIVE);
        //Add to the list and remove old one
        this.players.set(this.players.indexOf(oldPlayer), newPlayer);
        System.out.println("Replaced player!");
    }
    
    @Override
    public void setPlayerFaction(String playername, String faction) throws RemoteException {
        IPlayer player = this.getPlayer(playername);
        if (player != null) {
            if (faction.equals("melee")) {
                player.addUpgrade(new Upgrade(0.1, 0.1, 0.1, 0.1, UnitType.MELEE));
            } else if (faction.equals("ranged")) {
                player.addUpgrade(new Upgrade(0.1, 0.1, 0.1, 0.1, UnitType.RANGED));
            } else if (faction.equals("defence")) {
                player.addUpgrade(new Upgrade(0.1, 0.1, 0.1, 0.1, UnitType.DEFENCE));
            }
        }
    }

    /**
     * GameLoop class with timertask to call the doStep method in a loop
     */
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
    public synchronized Iterator<Unit> getLaneUnits() {
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
        int random;

        /**
         * Random choose unittype for upgrade
         */
        random = r.nextInt(10);
        if (random >= 0 && random <= 2) {
            unitType = UnitType.DEFENCE;
        } else if (random >= 3 && random <= 5) {
            unitType = UnitType.MELEE;
        } else if (random >= 6 && random <= 8) {
            unitType = UnitType.RANGED;
        } else {
            unitType = UnitType.ALL;
        }

        /**
         * Random generate content mysterybox
         */
        random = r.nextInt(10);
        if (random >= 0 && random <= 8) {
            //Mysterybox with resources
            mysterybox = new Mysterybox(r.nextInt(90) + 10, null, null, 0);
        } else {
            //Mysterybox with upgrades
            random = r.nextInt(63);
            UpgradeInfo upgradeInfo;
            if (random == 62) {
                upgradeInfo = UpgradeInfo.getUberUpgrade(unitType);
            } else if (random <= 61 && random >= 60) {
                upgradeInfo = UpgradeInfo.getStrongUpgrade(unitType);
            } else if (random <= 59 && random >= 56) {
                upgradeInfo = UpgradeInfo.getNormalStrongUpgrade(unitType);
            } else if (random <= 55 && random >= 48) {
                upgradeInfo = UpgradeInfo.getNormalUpgrade(unitType);
            } else if (random <= 47 && random >= 31) {
                upgradeInfo = UpgradeInfo.getWeakNormalUpgrade(unitType);
            } else {
                upgradeInfo = UpgradeInfo.getWeakUpgrade(unitType);
            }
            mysterybox = new Mysterybox(0, upgradeInfo.getUpgrade(), null, 0);
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
    private synchronized void doStep() {
        //Create game info for clients
        gameInfo.setInfo(this.players, this.mysterybox, this.resourceTimer, this.mysteryboxTimer, this.mysteryboxTime);

        //Check player connections
        for (IPlayer p : this.players) {
            p.lowerConnectionTimer();
            if (p.getConnectionTimer() == 0) {
                try {
                    this.setPlayerToAI(p.getName());
                } catch (RemoteException ex) {
                    System.out.println("Failed to set player to AI");
                }
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
            //Check if give to player or to teammate
            if (p.getBase().getHealthPoints() != 0) {
                p.addMoney(munnie);
            } else {
                p.getBase().getLane(0).getBaseEnd2().getLane(0).getBaseEnd2().getOwner().addMoney(munnie / 2);
            }
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
    public synchronized boolean placeUnitAtLane(IPlayer player, Unit unit, int index, int cost) {
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
     * Searches the list with players by name
     *
     * @param name name of the player to be found
     * @return Iplayer if player with the same name is found, else null
     */
    private synchronized IPlayer getPlayer(String name) {
        for (IPlayer p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        
        return null;
    }

    /**
     * Adds a defensive unit to the base on the given place of multiplayer.
     *
     * @param playername The playername that places the unit, may not be null.
     * @param unit The unit that has to be placed, may not be null.
     * @param index The index for the unit, must be between 0 and 31. 0 to 15
     * being the group that is at the lane where the base of the player is
     * baseEnd1, 16 to 31 being the other lane.
     * @param cost The cost of the unit, must be higher than 0.
     * @return true if unit is placed at base, else false
     */
    @Override
    public synchronized boolean placeUnitAtBaseMulti(String playername, Unit unit, int index, int cost) {
        IPlayer player = getPlayer(playername);
        Unit unitNew = null;

        /**
         * Check unittype
         */
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
     * Adds a defensive unit to the base on the given place.
     *
     * @param player The player that places the unit, may not be null.
     * @param unit The unit that has to be placed, may not be null.
     * @param index The index for the unit, must be between 0 and 31. 0 to 15
     * being the group that is at the lane where the base of the player is
     * baseEnd1, 16 to 31 being the other lane.
     * @param cost The cost of the unit, must be higher than 0.
     * @return true if unit is placed at base, else false
     */
    public synchronized boolean placeUnitAtBase(IPlayer player, Unit unit, int index, int cost) {
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
    public synchronized List<IPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Returns the mysterybox object
     *
     * @return The mysterybox object currently active in the game
     */
    public synchronized Mysterybox getMysterybox() {
        return this.mysterybox;
    }
    
    @Override
    public synchronized void bidMysteryboxMulti(String playername, int bid) {
        IPlayer player = getPlayer(playername);
        
        if (mysterybox != null && player != null) {
            mysterybox.bid(player, bid);
        }
    }
    
    @Override
    public synchronized IGameInfo getGameInfo() throws RemoteException {
        return this.gameInfo;
    }
}

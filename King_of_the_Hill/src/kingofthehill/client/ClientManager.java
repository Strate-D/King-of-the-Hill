package kingofthehill.client;

import java.rmi.NotBoundException;
import kingofthehill.domain.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import kingofthehill.upgradeinfo.UpgradeInfo;

/**
 * Class that manages the game. Contains all the units and objects that are part
 * of it.
 *
 * @author Jur
 */
public class ClientManager{

    private List<IPlayer> players;
    private Mysterybox mysterybox;
    private GameMode gameMode;
    private int resourceTimer;
    private int mysteryboxTimer;
    private int mysteryboxTime;
    private Random mysteryboxRandom;
    
    private IGameManager gm;
    private Registry registry = null;
    private int portNumber = 9999;
    private static final String bindingName = "GameInfo";

    /**
     * Gets gamemanager object from registery
     * @param ipAddress ip adress of the remote registery
     */
    public ClientManager(String ipAddress){
        /**
         * Print IP address and port number for registry
         */
        System.out.println("Client: IP Address: " + ipAddress);
        System.out.println("Client: Port number " + portNumber);
        
        /**
         * Locate registry at IP address and port number
         */
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }

        /**
         * Print result locating registry
         */
        if (registry != null) {
            System.out.println("Client: Registry located");
        } else {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: Registry is null pointer");
        }
        
        /**
         * Bind student administration using registry
         */
        if (registry != null) {
            try {
                gm = (IGameManager) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                gm = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                gm = null;
            }
        }
        
        /**
         * Print result binding student administration
         */
        if (gm != null) {
            System.out.println("Client: Student administration bound");
        } else {
            System.out.println("Client: Student administration is null pointer");
        }
    }
    
    public IGameManager getGameManager(){
        return this.gm;
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
        switch(r.nextInt(2)){
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
        switch(r.nextInt(2)){
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
                if(unitType != null){
                    UpgradeInfo upgradeInfo = null;
                    /**
                     * Random choose upgradetype
                     */
                    switch(r.nextInt(6)){
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
                    
                    if(upgradeInfo != null){
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
     * @param player The AI player that needs to do something. Cannot be null
     */
    private void operateAIPlayer(AI player) {
        if(player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        //player.doNextAction(this);
    }

    /**
     * Does a step in the game (1/30 of a second).
     */
    public void doStep() {
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
         * Operate all AI's
         * Check if there are any AI players
         */
        for (IPlayer player : players) {
            if (player instanceof AI) 
            {
                /**
                 * The IPlayer is AI. Operate the AI player
                 */
                operateAIPlayer((AI) player);
            }
        }
        
        /**
         * Generate mysterybox
         */
        if(mysteryboxTimer > mysteryboxTime){
            generateMysterybox();
            mysteryboxTimer = 0;
            mysteryboxTime = mysteryboxRandom.nextInt(1800) + 1800;
        }
        
        /**
         * Unpack mysterybox
         */
        if(mysterybox != null){
            if(mysteryboxTimer > mysterybox.getDuration()){
                IPlayer higestBidder = mysterybox.getHigestBidder();
                if(higestBidder != null){
                    if(mysterybox.getResourceAmount() != 0){
                        higestBidder.addMoney(mysterybox.getResourceAmount());
                    }
                    else if(mysterybox.getUpgrade() != null){
                        higestBidder.addUpgrade(mysterybox.getUpgrade());
                    }
                    else if(mysterybox.getUnitType() != null){
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
            p.addMoney(10);
        }
    }

    /**
     * Places a unit at the selected lane
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
     * Adds a defencive unit to the base on the given place.
     * @param player The player that places the unit, may not be null.
     * @param unit The unit that has to be placed, may not be null.
     * @param index The index for the unit, must be between 0 and 31. 0 to 15
     * being the group that is at the lane where the base of the player is
     * baseEnd1, 16 to 31 being the other lane.
     * @param cost The cost of the unit, must be higher than 0.
     * @return true if unit is placed at base, else false
     */
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
     * @return A unmodifiable list, will never be empty
     */
    public List<IPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    
    /**
     * Returns the mysterybox object
     * @return The mysterybox object currently active in the game
     */
    public Mysterybox getMysterybox()
    {
        return this.mysterybox;
    }
}

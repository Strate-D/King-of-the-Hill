/**
 * 
 */
package kingofthehill.rmimultiplayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kingofthehill.domain.Base;
import kingofthehill.domain.IPlayer;
import kingofthehill.domain.Lane;
import kingofthehill.domain.Mysterybox;
import kingofthehill.domain.Unit;

/**
 * Class that has all the important gameinfo of a multiplayer game
 * @author Dennis
 */
public class GameInfo implements IGameInfo, Serializable{
    private List<IPlayer> players;
    private Mysterybox mbox;
    private int resourceTimer;
    private int mysteryboxTimer;
    private int mysteryboxTime;
    
    /**
     * Set new information into the GameInfo class
     * @param players The new List of Players
     * @param mbox The new Mysterybox
     * @param resourcetimer The new timer for the resource cooldown
     * @param mysteryboxtimer The timer for counting down to the next mysterybox
     * @param mysteryboxtime The timer for leaving the mysterybox active
     */
    public void setInfo(List<IPlayer> players, Mysterybox mbox, int resourcetimer, int mysteryboxtimer, int mysteryboxtime){
        this.players = players;
        this.mbox = mbox;
        this.resourceTimer = resourcetimer;
        this.mysteryboxTimer = mysteryboxtimer;
        this.mysteryboxTime = mysteryboxtime;
    }

    @Override
    public List<IPlayer> getPlayers() {
        return players;
    }

    @Override
    public Mysterybox getMysterybox() {
        return mbox;
    }

    @Override
    public int getResourcetimer() {
        return resourceTimer;
    }

    @Override
    public int getMysteryboxtimer() {
        return mysteryboxTimer;
    }

    @Override
    public int getMysteryboxtime() {
        return mysteryboxTime;
    }
    
    @Override
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

    @Override
    public void setFirstPlayer(String name) {
        while(!this.getPlayers().get(0).getName().equals(name)){
            IPlayer buffer = this.getPlayers().get(0);
            this.getPlayers().remove(buffer);
            this.getPlayers().add(buffer);
        }
    }
    
    /**
     * Does a step in the game (1/60 of a second).
     */
    @Override
    public void doStep() {           
        /**
         * Operate all units
         */
        operateUnits();
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
                            System.out.println("kingofthehill.rmimultiplayer.GameInfo operateUnits(): " + ecx.getMessage());
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
                        System.out.println("kingofthehill.rmimultiplayer.GameInfo operateUnits(): " + ecx.getMessage());
                    }
                }
            }
        }
    }
}

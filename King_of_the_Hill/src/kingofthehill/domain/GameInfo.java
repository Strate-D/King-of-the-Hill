/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class GameInfo implements IGameInfo, Serializable{
    private List<IPlayer> players;
    private Mysterybox mbox;
    private int resourcetimer;
    private int mysteryboxtimer;
    private int mysteryboxtime;
    
    public void setInfo(List<IPlayer> players, Mysterybox mbox, int resourcetimer, int mysteryboxtimer, int mysteryboxtime){
        this.players = players;
        this.mbox = mbox;
        this.resourcetimer = resourcetimer;
        this.mysteryboxtimer = mysteryboxtimer;
        this.mysteryboxtime = mysteryboxtime;
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
        return resourcetimer;
    }

    @Override
    public int getMysteryboxtimer() {
        return mysteryboxtimer;
    }

    @Override
    public int getMysteryboxtime() {
        return mysteryboxtime;
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
}

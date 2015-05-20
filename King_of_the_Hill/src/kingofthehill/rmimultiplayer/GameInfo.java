/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    @Override
    public void setFirstPlayer(String name) {
        while(!this.getPlayers().get(0).getName().equals(name)){
            IPlayer buffer = this.getPlayers().get(0);
            this.getPlayers().remove(buffer);
            this.getPlayers().add(buffer);
        }
    }
}

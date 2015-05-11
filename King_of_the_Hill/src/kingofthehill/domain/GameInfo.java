/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.List;

/**
 *
 * @author Dennis
 */
public class GameInfo {
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

    public List<IPlayer> getPlayers() {
        return players;
    }

    public Mysterybox getMbox() {
        return mbox;
    }

    public int getResourcetimer() {
        return resourcetimer;
    }

    public int getMysteryboxtimer() {
        return mysteryboxtimer;
    }

    public int getMysteryboxtime() {
        return mysteryboxtime;
    }
}

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
public interface IGameInfo{ 
    public void setInfo(List<IPlayer> players, Mysterybox mbox, int resourcetimer, int mysteryboxtimer, int mysteryboxtime);
}

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
 * Lane object which contains all the info of a lane in the game.
 *
 * @author Jur
 */
public class Lane {

    private final Base baseEnd1;
    private final Base baseEnd2;
    private ArrayList<Unit> units;

    public Lane(Base end1, Base end2){
        if (end1 == null || end2 == null){
            throw new IllegalArgumentException("Lane couldn't be made, ends cannot be null");
        }
        this.baseEnd1 = end1;
        this.baseEnd2 = end2;
        this.units = new ArrayList<>();
    }
    
    /**
     * Gets the base which is at the first end of the lane
     *
     * @return A base object, cannot be null.
     */
    public Base getBaseEnd1() {
        return this.baseEnd1;
    }

    /**
     * Gets the base which is at the second end of the lane
     *
     * @return A base object, cannot be null
     */
    public Base getBaseEnd2() {
        return this.baseEnd2;
    }

    /**
     * Gets a list of all the units in the lane
     *
     * @return A unmodifiable list
     */
    public List<Unit> getUnits() {
        return Collections.unmodifiableList(this.units);
    }

    /**
     * Adds the given unit to the lane and sets the lane of the unit
     *
     * @param unit The unit that has to be added, may not be null.
     */
    public void addUnit(Unit unit) {
        if (unit != null) {
            unit.setLane(this);
            this.units.add(unit);
        }
    }
    
    /**
     * Removes the unit from the lane, and sets it lane to null.
     * @param unit The unit that has to be removed, may not be null.
     */
    public void removeUnit(Unit unit) {
        if(unit != null){
            unit.setLane(null);
            this.units.remove(unit);
        }
    }
}

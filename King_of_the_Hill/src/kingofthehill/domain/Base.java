/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Base unit, wich contains all the data of a base
 *
 * @author Jur
 */
public class Base {

    private int healthPoints;
    private Lane[] lanes;
    private Unit[] units;
    private IPlayer owner;

    /**
     * Creates a new base with the given owner.
     *
     * @param owner May not be null!
     */
    public Base(IPlayer owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner may not be null!");
        }
        this.owner = owner;
        this.lanes = new Lane[8];
        this.units = new Unit[32];
        this.healthPoints = 100;
    }

    /**
     * Gets the amount of health the base has left
     *
     * @return 0 or higher with max 100.
     */
    public int getHealthPoints() {
        return this.healthPoints;
    }

    /**
     * Gets the owner of the base
     *
     * @return Cannot be null!
     */
    public IPlayer getOwner() {
        return this.owner;
    }

    /**
     * Gets the lane of the given index
     *
     * @param index Number between 0 and 7, with 0 to 3 being a group of lanes
     * and 4 to 7.
     * @return Returns the lane if there is one on the given index, else null.
     */
    public Lane getLane(int index) {
        try {
            return this.lanes[index];
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Sets the lane on the given index.
     *
     * @param index Number between 0 and 7. 0 to 3 for baseEnd1 and 4 to 7 for
     * baseEnd2.
     * @param lane The lane that needs to be set.
     * @return Returns if succesfull or not.
     */
    public boolean setLane(int index, Lane lane) {
        try {
            this.lanes[index] = lane;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * Gets the unit on the given index
     * @param index Number between 0 and 31. 0 to 3 for lane[0], 4 to 7 for lane[1] etc..
     * @return The unit on the given index can be null
     */
    public Unit getUnit(int index) {
        try {
            return this.units[index];
        } catch (Exception ex) {
            return null;
        }
    }
    
    /**
     * Sets the unit on the given index
     * @param index Number between 0 and 31. 0 to 3 for lane[0], 4 to 7 for lane[1] etc..
     * @param unit The unit that has to be set on the given index, can be null.
     * @return If succesfull or not, if space is occupied unit will not be placed
     */
    public boolean setUnit(int index, Unit unit) {
        //Check if index is not occupied.
        if(getUnit(index) != null){
            return false;
        }
        
        //Place unit
        try {
            this.units[index] = unit;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * Removes the given unit from the base
     * @param unit The unit that has to be removed
     */
    public void removeUnit(Unit unit){
        int i = 0;
        for(Unit u : this.units){
            if(u == unit){
                this.units[i] = null;
                return;
            }
            i++;
        }
    }
    
    /**
     * Gets all the active units at the base
     * @return List, can be empty, will not be null, will not have null objects in it.
     */
    public List<Unit> getUnits(){
        ArrayList<Unit> list = new ArrayList<>();
        for(Unit u : this.units){
            if(u != null){
                list.add(u);
            }
        }
        return list;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

import java.util.Collections;
import java.util.List;

/**
 * Object that contains all the info about the mystery box in the game.
 *
 * @author Jur
 */
public class Mysterybox {

    private int resourceAmount;
    private Upgrade upgrade;
    private List<Unit> units;

    /**
     * Creates a new Mysterybox object that contains info about the mysterybox.
     *
     * @param resources Amount of resources in the mysterybox. May not be a
     * negative number!
     * @param upgrade Optional upgrade in the mysterybox. May be null.
     * @param units Optional list of units in the mysterybox. May be null.
     */
    public Mysterybox(int resources, Upgrade upgrade, List<Unit> units) {
        //Check input
        if (resources < 0) {
            throw new IllegalArgumentException("Amount of resources may not be lower than 0");
        }

        //Set fields
        this.resourceAmount = resources;
        this.upgrade = upgrade;
        this.units = units;
    }

    /**
     * Gets the amount of resources in the mysterybox.
     * @return The amount of resources. Always 0 or higher.
     */
    public int getResourceAmount() {
        return this.resourceAmount;
    }

    /**
     * Gets the upgrade in the mysterybox.
     * @return The upgrade in the mysterybox. Can be null.
     */
    public Upgrade getUpgrade() {
        return this.upgrade;
    }

    /**
     * Gets the units in the mysterbox
     * @return The list of units. Can be null.
     */
    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);
    }
}

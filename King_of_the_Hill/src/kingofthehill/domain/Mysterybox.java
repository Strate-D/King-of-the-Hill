/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.domain;

/**
 * Object that contains all the info about the mystery box in the game.
 *
 * @author Jur
 */
public class Mysterybox {

    private int resourceAmount;
    private Upgrade upgrade;
    private UnitType unitType;
    private int amount;
    private static final int duration = 1800;
    private IPlayer highestBidder;
    private int higestBid;

    /**
     * Creates a new Mysterybox object that contains info about the mysterybox.
     *
     * @param resources Amount of resources in the mysterybox. May not be a
     * negative number!
     * @param upgrade Optional upgrade in the mysterybox. May be null.
     * @param unitType Optional unittype in the mysterybox. May be null.
     * @param amount Optional amount of units in the mysterybox. May be 0 if
     * unittype is null.
     */
    public Mysterybox(int resources, Upgrade upgrade, UnitType unitType, int amount) {
        //Check input
        if (resources < 0) {
            throw new IllegalArgumentException("Amount of resources may not be lower than 0");
        }

        if (unitType != null && amount < 1) {
            throw new IllegalArgumentException("Amount of units must 1 or more if unittype is not null");
        }

        //Set fields
        this.resourceAmount = resources;
        this.upgrade = upgrade;
        this.unitType = unitType;
        this.amount = amount;
        this.higestBid = 0;
    }

    /**
     * Bid on the mysterybox
     *
     * @param bidder player that bids on the mysterybox
     * @param bid amount of gold the bidder bid on the mysterybox
     */
    public void Bid(IPlayer bidder, int bid) {
        if (bidder != null) {
            if (bid > higestBid) {
                //give money back to previous higest bidder
                highestBidder.addMoney(bid);         
                
                //set new higest bidder and bid
                this.highestBidder = bidder;
                this.higestBid = bid;
                
                //take money from new higest bidder
                highestBidder.payMoney(bid);  
            }
        }
    }

    /**
     * Gets the amount of resources in the mysterybox.
     *
     * @return The amount of resources. Always 0 or higher.
     */
    public int getResourceAmount() {
        return this.resourceAmount;
    }

    /**
     * Gets the upgrade in the mysterybox.
     *
     * @return The upgrade in the mysterybox. Can be null.
     */
    public Upgrade getUpgrade() {
        return this.upgrade;
    }

    /**
     * Returns the duration of the mysterybox in frames. (amount of seconds
     * times 60)
     *
     * @return Returns the duration
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Returns the type of unit that the mysterybox contains
     *
     * @return Returns the UnitType
     */
    public UnitType getUnitType() {
        return this.unitType;
    }

    /**
     * Returns the amount of units that the mysterybox contains
     *
     * @return Returns the amount of units
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Return the highest bid
     * @return Returns the highest bid
     */
    public int getHighestBid() {
        return this.higestBid;
    }
    
    /**
     * Return the higest bidder
     * @return Returns the player that has the highest bid
     */
    public IPlayer getHigestBidder(){
        return this.highestBidder;
    }
}

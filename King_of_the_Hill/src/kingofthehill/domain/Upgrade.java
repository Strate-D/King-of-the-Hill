/**
 * 
 */
package kingofthehill.domain;

/**
 * Contains information about a certain upgrade for units.
 * @author Jur
 */
public class Upgrade {
    private double modHealth;
    private double modAttack;
    private double modArmor;
    private double modMovementSpeed;
    private UnitType targetUnit;
    
    /**
     * Creates a new upgrade. (1 means add 100% of current units value)
     * @param modHealth Percentage that is added or removed. Not NULL.
     * @param modAttack Percentage that is added or removed. Not NULL.
     * @param modArmor Percentage that is added or removed. Not NULL.
     * @param modMovementSpeed Percentage that is added or removed. Not NULL.
     * @param targetUnit The type of unit that it is used for. Not NULL.
     * @throws IllegalArgumentException 
     */
    public Upgrade(Double modHealth, Double modAttack, Double modArmor, Double modMovementSpeed, UnitType targetUnit) throws IllegalArgumentException{
        /**
         * Check input
         */
        if(modHealth == null || modAttack == null || modArmor == null || modMovementSpeed == null || targetUnit == null){
            throw new IllegalArgumentException("Variable must have a value!");
        }
        
        /**
         * Fill fields
         */
        this.modHealth = modHealth;
        this.modAttack = modAttack;
        this.modArmor = modArmor;
        this.modMovementSpeed = modMovementSpeed;
        this.targetUnit = targetUnit;
    }
    
    /**
     * Percentage that the original health of the unit is being added or removed.
     * @return The modification of the health. Can be negative or 0.
     */
    public double getModHealth(){
        return this.modHealth;
    }
    
    /**
     * Percentage that the original attack of the unit is being added or removed.
     * @return The modification of the attack. Can be negative or 0.
     */
    public double getModAttack(){
        return this.modAttack;
    }
    
    /**
     * Percentage that the original armor of the unit is being added or removed.
     * @return The modification of the armor. Can be negative or 0.
     */
    public double getModArmor(){
        return this.modArmor;
    }
    
    /**
     * Percentage that the original movement speed of the unit is being added or removed.
     * @return The modification of the movement speed. Can be negative or 0.
     */
    public double getModMovementSpeed(){
        return this.modMovementSpeed;
    }
    
    /**
     * Gets the type of unit that the upgrade is used for
     * @return The unittype.
     */
    public UnitType getTargetUnit(){
        return this.targetUnit;
    }
}

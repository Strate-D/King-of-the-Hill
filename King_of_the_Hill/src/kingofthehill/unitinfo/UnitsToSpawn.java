/**
 * 
 */
package kingofthehill.unitinfo;

import kingofthehill.domain.UnitType;

/**
 * Creates a new object to store units that need to be spawned
 * @author Bas
 */
public class UnitsToSpawn {

    private int spawnPoint;
    private UnitType unitType;

    /**
     * Creates a new UnitsToSpawn object
     * @param spawnPoint Specifies the spawnpoint of the unit
     * @param unitType Specifies the unittype
     */
    public UnitsToSpawn(int spawnPoint, UnitType unitType) {
        this.spawnPoint = spawnPoint;
        this.unitType = unitType;
    }

    /**
     * Returns the spawnpoint of the unit
     * @return The spawnpoint of the unit
     */
    public int getSpawnPoint() {
        return this.spawnPoint;
    }

    /**
     * Returns the Unittype of the unit
     * @return The Unittype
     */
    public UnitType getUnitType() {
        return this.unitType;
    }
}

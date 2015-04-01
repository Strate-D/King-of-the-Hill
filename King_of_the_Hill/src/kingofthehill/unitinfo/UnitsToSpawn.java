/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.unitinfo;

import kingofthehill.domain.UnitType;

/**
 *
 * @author Bas
 */
public class UnitsToSpawn {

    private int spawnPoint;
    private UnitType unitType;

    public UnitsToSpawn(int spawnPoint, UnitType unitType) {
        this.spawnPoint = spawnPoint;
        this.unitType = unitType;
    }

    public int getSpawnPoint() {
        return this.spawnPoint;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }
}

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
 * Class containing all the information about a human player
 * @author Jur
 */
public class Player implements IPlayer{
    
    private String name;
    private int exp;
    private int score;
    private ArrayList<Upgrade> upgrades;
    private Team team;
    private int money;
    private Base base;
    
    /**
     * Creates a new player object
     * @param name Name of the player, may not be empty
     * @param exp Exp points of the player, may not be lower than 0.
     */
    public Player(String name, int exp){
        if(name.isEmpty() || exp < 0){
            throw new IllegalArgumentException("Illegal arguments given!");
        }
        this.name = name;
        this.exp = exp;
        this.score = 0;
        this.upgrades = new ArrayList();
        this.team = null;
        this.money = 100;
        this.base = null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getExp() {
        return this.exp;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public boolean checkPassword(String password) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void addUpgrade(Upgrade upgrade){
        upgrades.add(upgrade);
    }
    
    @Override
    public List<Upgrade> getUpgrades() {
        return Collections.unmodifiableList(upgrades);
    }

    @Override
    public void setTeam(Team newTeam) {
        if(newTeam != null){
            this.team = newTeam; 
        }
    }

    @Override
    public int getMoney() {
        return this.money;
    }

    @Override
    public Team getTeam() {
        return this.team;
    }

    @Override
    public void addMoney(int amount) {
        if (amount > 0) {
            this.money += amount;
        }
    }

    @Override
    public boolean payMoney(int amount) {
        if(amount > 0){
            if(amount <= this.money){
                this.money -= amount;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Base getBase() {
        return this.base;
    }

    @Override
    public void setBase(Base newBase) {
        if(newBase != null){
            this.base = newBase;
        }
    }

    @Override
    public void addPoints(int points) {
        this.score += points;
    }
    
}

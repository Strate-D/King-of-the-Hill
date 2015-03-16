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
 * Class containing all the information about a AI. Implements IPlayer. Contains AI algoritm.
 * @author Jur
 */
public class AI implements IPlayer {

    private String name;
    private int exp;
    private int score;
    private ArrayList<Upgrade> upgrades;
    private Team team;
    private int money;
    private Base base;
    
    /**
     * Creates a new AI object
     * @param name May not be null.
     */
    public AI(String name){
        if(name.isEmpty()){
            throw new IllegalArgumentException("Illegal arguments given!");
        }
        this.name = name;
        this.exp = 12;
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
        return true;
    }

    @Override
    public List<Upgrade> getUpgrades() {
        return Collections.unmodifiableList(upgrades);
    }

    @Override
    public void setTeam(Team newTeam) {
        this.team = newTeam;
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
        this.base = newBase;
    }

}

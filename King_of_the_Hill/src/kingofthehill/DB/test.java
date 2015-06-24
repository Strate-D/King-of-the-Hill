/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.DB;

import kingofthehill.domain.IPlayer;

/**
 *
 * @author Jur
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("First score: " + DatabaseMediator.getScore("Batman"));
        DatabaseMediator.increaseScore("Batman", 1);
        System.out.println("Increased score: " + DatabaseMediator.getScore("Batman"));
        System.out.println("Printing highscore: ");
        for(IPlayer p : DatabaseMediator.getHighscores()) {
            System.out.println(p.getExp());
        }
    }
    
}

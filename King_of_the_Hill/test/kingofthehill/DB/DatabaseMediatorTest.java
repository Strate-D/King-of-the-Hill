/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.DB;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Jur
 */
public class DatabaseMediatorTest {
    @Test
    public void addNewPlayerTest() {
        //Test empty name
        Assert.assertFalse("Player name cannot be empty", DatabaseMediator.addNewPlayer("", "asdf"));
        
        //Test empty password
        Assert.assertFalse("Player name cannot be empty", DatabaseMediator.addNewPlayer("Penk", ""));
        
        //Test correct
        Assert.assertTrue("Player should be added!", DatabaseMediator.addNewPlayer("Penk", "asdf"));
        
        //Test existing player
        Assert.assertFalse("Player allready exists!", DatabaseMediator.addNewPlayer("Penk", "asdf"));
    }
    
    @Test
    public void getHighscoresTest() {
        //Add player
        DatabaseMediator.addNewPlayer("Henk", "asdf");
        //Test if not empty
        Assert.assertFalse("Highscores can't be empty!", DatabaseMediator.getHighscores().isEmpty());
    }
    
    @Test
    public void getScoreTest() {
        //Add player
        DatabaseMediator.addNewPlayer("Henk", "asdf");
        //Test non existing player
        Assert.assertEquals("Player doesn't exists! Should return -1", DatabaseMediator.getScore("UchMam"), -1);
        
        //Test empty name
        Assert.assertEquals("Player doesn't exists! Should return -1", DatabaseMediator.getScore(""), -1);
        
        //Get correct score
        Assert.assertEquals("Should return 0!", DatabaseMediator.getScore("Henk"), 0);
    }
    
    @Test
    public void increaseScoreTest() {
        //Add player
        DatabaseMediator.addNewPlayer("Henk", "asdf");
        
        //Test negative score
        DatabaseMediator.increaseScore("Henk", -10);
        Assert.assertEquals("Should return 0!", DatabaseMediator.getScore("Henk"), 0);
        
        //Test 0 score
        DatabaseMediator.increaseScore("Henk", 0);
        Assert.assertEquals("Should return 0!", DatabaseMediator.getScore("Henk"), 0);
        
        //Test normal score
        DatabaseMediator.increaseScore("Henk", 10);
        Assert.assertEquals("Should return 10!", DatabaseMediator.getScore("Henk"), 10);
    }
    
    @Test
    public void checkLogin() {
        //Add player
        DatabaseMediator.addNewPlayer("Henk", "asdf");
        //Test empty and null name and password
        Assert.assertFalse("Should be false!", DatabaseMediator.checkLogin("", "asdf"));
        Assert.assertFalse("Should be false!", DatabaseMediator.checkLogin(null, "asdf"));
        Assert.assertFalse("Should be false!", DatabaseMediator.checkLogin("Henk", ""));
        Assert.assertFalse("Should be false!", DatabaseMediator.checkLogin("Henk", null));
        
        //Check wrong data
        Assert.assertFalse("Should be false!", DatabaseMediator.checkLogin("Henk", "addf"));
        Assert.assertFalse("Should be false!", DatabaseMediator.checkLogin("henk", "assf"));
        
        //Check correct data
        Assert.assertTrue("Should be true!", DatabaseMediator.checkLogin("Henk", "asdf"));
        Assert.assertTrue("Should be true!", DatabaseMediator.checkLogin("henk", "asdf"));
    }
}

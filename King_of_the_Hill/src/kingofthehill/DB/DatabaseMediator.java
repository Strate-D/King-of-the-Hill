/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kingofthehill.domain.*;

/**
 *
 * @author Bas Koch
 */
public class DatabaseMediator {

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    /*
     ip: 94.211.145.73:3306
     username: kingofthehill
     password: proftaak
     */
    String url = "jdbc:mysql://94.211.145.73:3306";
    String user = "kingofthehill";
    String password = "proftaak";

    public DatabaseMediator() {
        Connect();
    }

    /**
     * Opens the connection to the database
     */
    private void Connect() {
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Login a player
     *
     * @param username
     * @param password
     * @return true if logged in
     */
    public boolean DBLogin(String username, String password) {
        try {
            if (getPlayer(username, password) != null) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the player whose username and password are in the DB
     *
     * @return Player
     * @throws SQLException
     */
    private IPlayer getPlayer(String name, String password) throws SQLException {
        IPlayer p = null;
        PreparedStatement pst = con.prepareStatement("SELECT name, score FROM player WHERE name =" + name + "AND password =" + password);
        rs = pst.executeQuery();

        p = (IPlayer) rs.getObject(0);

        return p;
    }
    /**
     * Change the score of the player in the DB
     * @param player
     * @param score
     * @throws SQLException 
     */
    public void ChangeScore(String name, int score) throws SQLException {
        PreparedStatement pst1 = con.prepareStatement("SELECT score FROM Player WHERE name = " + name);
        rs = pst1.executeQuery();
        
        PreparedStatement pst = con.prepareStatement("UPDATE Player SET score = " + rs.getInt(0) + score + 
                "WHERE name = " + name);
        pst.executeQuery();
    }
    /**
     * Gets the score of the player in the DB
     * @param name
     * @return
     * @throws SQLException 
     */
    public int GetScore(String name) throws SQLException {
        PreparedStatement pst = con.prepareStatement("SELECT score FROM Player WHERE name = " + name);
        rs = pst.executeQuery();
        
        return rs.getInt(0);
    }
    /**
     * Gets the 10 best scoring players in the DB
     * @return 10 best scoring players
     * @throws SQLException 
     */
    public List<IPlayer> getHighscores() throws SQLException {
        PreparedStatement pst = con.prepareStatement("SELECT name, score FROM Player ORDER BY score DESC lmit 10");
        rs = pst.executeQuery();
        
        List<IPlayer> highscores = new ArrayList<IPlayer>(10);
        
        while(rs.next()) {
            highscores.add((IPlayer)rs.getObject(0));
        }
        return highscores; 
    }
    /**
     * Add a new player to the DB
     * @param newName
     * @param newPassword
     * @throws SQLException 
     */
    public void AddNewPlayer(String newName, String newPassword) throws SQLException{
        PreparedStatement pst = con.prepareStatement("INSERT INTO Player (id, name, password, score) values (player_seq.nextval," + newName + "," + newPassword + ", 0)");
        pst.executeQuery();
    }
    
    

}

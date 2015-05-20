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
import kingofthehill.domain.*;

/**
 *
 * @author Bas Koch
 */
public class DatabaseMediator {

    private static Connection con = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    private static String query = "";

    /*
     ip: 94.211.145.73:3306
     username: kingofthehill
     password: proftaak
     */
    private static String url = "jdbc:mysql://94.211.145.73:3306";
    private static String user = "kingofthehill";
    private static String password = "proftaak";

    /**
     * Opens the connection to the database.
     */
    private static boolean openConnection() {
        try {
            con = DriverManager.getConnection(url, user, password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Closes the connection to the database if open.
     */
    private static void closeConnection() {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the user information that is entered is correct.
     *
     * @param username The username, not null or empty.
     * @param password The encrypted password of the user.
     * @return True if information is correct
     */
    public static boolean checkLogin(String username, String password) {
        //Check input
        if (username == null || password == null) {
            return false;
        }
        boolean result = false;
        //Open connection
        if (openConnection()) {
            try {
                //Check if user is in db
                st = con.createStatement();
                query = "SELECT name, score FROM player WHERE name =" + username + "AND password =" + password;
                rs = st.executeQuery(query);
                if(rs.next()) {
                    result = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection();
        }
        return result;
    }

    /**
     * Increase the score of the player on the database
     *
     * @param name The name of the player, not null
     * @param score The score that has to be added, higher than 0.
     * @throws SQLException
     */
    public static void increaseScore(String name, int score) throws SQLException {
        //Open connection
        if(openConnection()) {
            //Get old score
            st = con.createStatement();
            query = "SELECT score FROM player WHERE name ="+name+";";
            rs = st.executeQuery(query);
            int scoreOld;
            if(rs.next()) {
                scoreOld = rs.getInt(1);
            } else {
                //User doesn't excist
                return;
            }
            //Set new score
            st = con.createStatement();
            query = "UPDATE ";
        }
    }

    /**
     * Gets the score of the player in the DB
     *
     * @param name
     * @return Score of player
     * @throws SQLException
     */
    public static int getScore(String name) throws SQLException {
        //connect();
        PreparedStatement pst = con.prepareStatement("SELECT score FROM Player WHERE name = " + name);
        rs = pst.executeQuery();

        return rs.getInt(0);
    }

    /**
     * Gets the 10 best scoring players in the DB
     *
     * @return 10 best scoring players
     * @throws SQLException
     */
    public static List<IPlayer> getHighscores() throws SQLException {
        //connect();
        PreparedStatement pst = con.prepareStatement("SELECT name, score FROM Player ORDER BY score DESC limit 10");
        rs = pst.executeQuery();

        List<IPlayer> highscores = new ArrayList<IPlayer>(10);

        while (rs.next()) {
            highscores.add((IPlayer) rs.getObject(0));
        }
        return highscores;
    }

    /**
     * Add a new player to the DB
     *
     * @param newName
     * @param newPassword
     * @return True if added
     * @throws SQLException
     */
    //public static boolean addNewPlayer(String newName, String newPassword) throws SQLException {
        //connect();
        //if (getPlayer(newName, newPassword) != null) {
        //    return false;
        //} else {
        //    PreparedStatement pst = con.prepareStatement("INSERT INTO Player (id, name, password, score) values (player_seq.nextval," + newName + "," + newPassword + ", 0)");
        //    pst.executeQuery();
        //    return true;
        //}

    //}

}

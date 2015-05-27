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
    private static String url = "jdbc:mysql://94.211.145.73:3306/KingOfTheHill";
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
    }

    /**
     * Check if the user information that is entered is correct.
     *
     * @param username The username, not null or empty.
     * @param password The encrypted password of the user, not null or empty.
     * @return True if information is correct
     */
    public static boolean checkLogin(String username, String password) {
        //Check input
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        boolean result = false;
        //Open connection
        if (openConnection()) {
            try {
                //Check if user is in db
                st = con.createStatement();
                query = "SELECT name, score FROM Player WHERE name='" + username + "' AND password='" + password + "';";
                rs = st.executeQuery(query);
                if (rs.next()) {
                    result = true;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            closeConnection();
        }
        return result;
    }

    /**
     * Increase the score of the player on the database
     *
     * @param name The name of the player
     * @param score The score that has to be added, higher than 0.
     */
    public static void increaseScore(String name, int score) {
        //Open connection
        if (score > 0) {
            if (openConnection()) {
                try {
                    //Get old score
                    st = con.createStatement();
                    query = "SELECT score FROM Player WHERE name='" + name + "';";
                    rs = st.executeQuery(query);
                    int scoreOld;
                    if (rs.next()) {
                        scoreOld = rs.getInt(1);
                    } else {
                        //User doesn't exists
                        return;
                    }
                    //Set new score
                    st = con.createStatement();
                    query = "UPDATE Player SET score=" + (scoreOld + score) + " WHERE name='" + name + "';";
                    st.executeUpdate(query);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            closeConnection();
        }
    }

    /**
     * Gets the score of the player in the DB
     *
     * @param name Name of the player
     * @return Score of player -1 if not found
     */
    public static int getScore(String name) {
        //Open connection
        if (openConnection()) {
            //Get score
            try {
                st = con.createStatement();
                query = "SELECT score FROM Player WHERE name='" + name + "';";
                rs = st.executeQuery(query);
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            closeConnection();
        }
        return -1;
    }

    /**
     * Gets the 10 best scoring players in the DB
     *
     * @return 10 best scoring players, empty if nothing found
     */
    public static List<IPlayer> getHighscores() {
        ArrayList<IPlayer> highscores = new ArrayList<IPlayer>();
        //Open connection
        if (openConnection()) {
            //Get all the players
            try {
                st = con.createStatement();
                query = "SELECT name, score FROM Player ORDER BY score DESC limit 10;";
                rs = st.executeQuery(query);
                while (rs.next()) {
                    highscores.add(new Player(rs.getString(1), rs.getInt(2)));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            closeConnection();
        }

        return highscores;
    }

    /**
     * Add a new player to the DB
     *
     * @param newName Name of the player, not empty
     * @param newPassword Encrypted password of the player, not empty
     * @return True if added, false if data is incorrect or name is taken
     */
    public static boolean addNewPlayer(String newName, String newPassword) {
        //Check input
        if (newName.trim().isEmpty() || newPassword.trim().isEmpty()) {
            return false;
        }
        //Check if player exists
        if (getScore(newName.trim()) == -1) {
            //Open connection
            if (openConnection()) {
                try {
                    st = con.createStatement();
                    query = "INSERT INTO Player (name, password, score) values ('" + newName.trim() + "','" + newPassword.trim() + "',0);";
                    if (st.executeUpdate(query) == 1) {
                        closeConnection();
                        return true;
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                //Close connection
                closeConnection();
            }
        }
        return false;
    }
}

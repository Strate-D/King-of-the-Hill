/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.UI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Object that keepss all het info about the application
 * @author Jur
 */
class ApplicationContext {

    private Properties props;

    /**
     * Creates a new applicationcontext and tries to load the properties.
     * If failes creates a new properties file
     */
    public ApplicationContext() {
        FileInputStream in;
        props = new Properties();
        try {
            in = new FileInputStream("gamesettings.properties");
            props.load(in);
        } catch (FileNotFoundException ex) {
            props.setProperty("name", "Player");
            props.setProperty("url", "0.0.0.0");
            props.setProperty("game", "null");
            
        } catch (IOException ex) {
            props.setProperty("name", "Player");
            props.setProperty("url", "0.0.0.0");
            props.setProperty("game", "null");
        }
    }
    
    /**
     * Save the properties to a file
     */
    private void saveProps() {
        FileOutputStream out;
        
        try {
            out = new FileOutputStream("gamesettings.properties");
            props.store(out, null);
        } catch (FileNotFoundException ex) {
            try {
                out = new FileOutputStream(new File("gamesettings.properties"));
                props.store(out, null);
            } catch (FileNotFoundException ex1) {
                System.out.println("Saving failed!");
            } catch (IOException ex1) {
                System.out.println("Saving failed!");
            }
        } catch (IOException ex) {
            try {
                out = new FileOutputStream(new File("gamesettings.properties"));
                props.store(out, null);
            } catch (FileNotFoundException ex1) {
                System.out.println("Saving failed!");
            } catch (IOException ex1) {
                System.out.println("Saving failed!");
            }
        }
    }
    
    /**
     * Gets the name of the player from the properties
     * @return A string
     */
    public String getPlayerName() {
        return props.getProperty("name");
    }
    
    /**
     * Sets the name of the player and saves it to the properties file
     * @param newName The new name of the player, cannot be null!
     */
    public void setPlayerName(String newName) {
        if(newName == null) {
            return;
        }
        props.setProperty("name", newName);
        saveProps();
    }
    

    /**
     * Gets the url of the server from the properties
     * @return a string containing the server url
     */
    public String getServerUrl() {
        return props.getProperty("url");
    }
    
    /**
     * Sets the url of the server in the properties and saves it to the properties file
     * @param newUrl the new url of the server, cannot be null!
     */
    public void setServerUrl(String newUrl) {
        if(newUrl == null){
            return;
        }
        props.setProperty("url", newUrl);
        saveProps();
    }
    
    /**
     * Gets the name of the game from the properties
     * @return name of game
     */
    public String getGameName() {
        return props.getProperty("game");
    }
    
    /**
     * Sets the name of the game in the properties and saves it to the properties file
     * @param newName new name of the game
     */
    public void setGameName(String newName) {
        if(newName == null){
            return;
        }
        props.setProperty("game", newName);
        saveProps();
    }
}

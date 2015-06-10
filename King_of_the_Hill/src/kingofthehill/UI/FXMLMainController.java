/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.UI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import kingofthehill.DB.DatabaseMediator;
import kingofthehill.Encryption.AES;
import kingofthehill.client.ClientManager;

/**
 * FXML Controller class
 *
 * @author Jur
 */
public class FXMLMainController implements Initializable {

    @FXML
    private AnchorPane content;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField playerName;

    @FXML
    private PasswordField playerPassword;

    @FXML
    private TextField serverUrl;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Collect garbage
        System.gc();
        playerName.setText(King_of_the_Hill.context.getPlayerName());
        serverUrl.setText(King_of_the_Hill.context.getServerUrl());
        errorLabel.setVisible(false);
    }

    /**
     * Go to the gameview when the play button is pressed
     *
     * @param e
     */
    public void handlePlayButton(ActionEvent e) {
        //Set the player name
        King_of_the_Hill.context.setPlayerName(playerName.getText());
        try {
            //Load next window
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("FXMLGameView.fxml"));
            King_of_the_Hill.currentStage.getScene().setRoot(window1);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Go to the gameview when the login button is pressed
     *
     * @param e
     * @throws java.lang.Exception
     */
    public void handleLoginButton(ActionEvent e) {
        //Check login data
        if (DatabaseMediator.checkLogin(playerName.getText(), playerPassword.getText())) {
            //Set player name
            King_of_the_Hill.context.setPlayerName(playerName.getText());
            //Locate server
            ClientManager cm = new ClientManager(serverUrl.getText());
            if (cm.locate()) {
                //Set server url
                King_of_the_Hill.context.setServerUrl(serverUrl.getText());
                //Open lobby
                try {
                    ClientManager.setupAudioChat(cm.getServerUrl(), 9090, playerName.getText());
                    Parent window1;
                    window1 = FXMLLoader.load(getClass().getResource("FXMLLobbyListView.fxml"));
                    King_of_the_Hill.currentStage.getScene().setRoot(window1);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                errorLabel.setText("Server could not be found");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Login failed!");
            errorLabel.setVisible(true);
        }
    }

    /**
     * Exit game when pressed on the button
     *
     * @param e
     */
    public void handleQuitButton(ActionEvent e) {
        System.exit(1);
    }

    /**
     * Register the player
     *
     * @param e
     */
    public void handleRegisterButton(ActionEvent e) {
        if (DatabaseMediator.addNewPlayer(playerName.getText(), playerPassword.getText())) {
            handleLoginButton(e);
        } else {
            errorLabel.setText("Registration failed!");
            errorLabel.setVisible(true);
        }
    }

}

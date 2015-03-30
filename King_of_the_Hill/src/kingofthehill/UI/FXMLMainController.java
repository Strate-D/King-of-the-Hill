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
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Jur
 */
public class FXMLMainController implements Initializable {

    @FXML
    private AnchorPane content;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Collect garbage
        System.gc();
    }

    /**
     * Go to the gameview when the play button is pressed
     *
     * @param e
     */
    public void handlePlayButton(ActionEvent e) {
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
     * Exit game when pressed on the button
     *
     * @param e
     */
    public void handleQuitButton(ActionEvent e) {
        System.exit(1);
    }

}

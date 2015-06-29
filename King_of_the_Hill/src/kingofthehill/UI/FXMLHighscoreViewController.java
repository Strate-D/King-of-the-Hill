/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.UI;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import kingofthehill.DB.DatabaseMediator;
import kingofthehill.domain.IPlayer;

/**
 * FXML Controller class
 *
 * @author Dennis
 */
public class FXMLHighscoreViewController implements Initializable {
    @FXML
    private AnchorPane content;
    
    @FXML
    private Label lblScore;
    
    @FXML
    private Label lblName;
    
    @FXML
    private ListView<String> lvHighscore;
    
    @FXML
    private ListView<String> lvHighscoreNum;
    
    ObservableList<String> highscores;

    ObservableList<String> highscoresNum;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        highscores = FXCollections.observableArrayList();
        highscoresNum = FXCollections.observableArrayList();
        lvHighscore.setItems(highscores);
        lvHighscoreNum.setItems(highscoresNum);

        int num = 1;
        List<IPlayer> players = DatabaseMediator.getHighscores();
        for(IPlayer p : players){
            highscores.add(num + ". "+ p.getName());
            highscoresNum.add("" + p.getExp());
            /*
            // Single List view option
            highscores.add(num + ". " + p.getName() + "\t" p.getScore());
            
            */
            num++;

        }
    }    

    @FXML
    private void handleQuitButton(ActionEvent event) {
        try {
            //Load next window
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
            King_of_the_Hill.currentStage.getScene().setRoot(window1);

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
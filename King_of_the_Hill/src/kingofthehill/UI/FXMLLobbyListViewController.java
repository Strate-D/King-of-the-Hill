/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.UI;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import kingofthehill.client.ClientManager;
import kingofthehill.domain.GameManager;
import kingofthehill.domain.IGameManager;
import kingofthehill.lobby.ILobby;

/**
 *
 * @author Bas
 */
public class FXMLLobbyListViewController implements Initializable {

    @FXML
    private ListView<String> gamesList;

    @FXML
    private Button buttonJoin;
    
    @FXML
    private Button buttonNewButton;

    @FXML
    private Label labelPlayer1;

    @FXML
    private Label labelPlayer2;

    @FXML
    private Label labelPlayer3;

    @FXML
    private Label labelPlayer4;

    ObservableList<String> games;

    ILobby lobby;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        games = FXCollections.observableArrayList();
        gamesList.setItems(games);
        buttonJoin.setDisable(true);

        String ipAddress = King_of_the_Hill.context.getServerUrl();
        ClientManager cm = new ClientManager(ipAddress);

        if (cm.locate()) {
            lobby = cm.getLobby();
        }
    }

    public void handleNewGameButton(ActionEvent e) {
        try {
            lobby.createGame(King_of_the_Hill.context.getPlayerName() + "'s lobby");
            games.addAll(lobby.getGames());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleGamesListOnClick(MouseEvent e) {
        buttonJoin.setDisable(false);

        //labelPlayer1.setText(games.get(gamesList.getSelectionModel().getSelectedIndex()));
    }

    public void handleJoinButton(ActionEvent e) {
        try {
            lobby.joinGame(gamesList.getSelectionModel().getSelectedIndex(), King_of_the_Hill.context.getPlayerName());
            IGameManager gm = lobby.getGame(gamesList.getSelectionModel().getSelectedIndex());
            King_of_the_Hill.context.setGameName(gm.getName());

            try {
                //Load next window
                Parent window1;
                window1 = FXMLLoader.load(getClass().getResource("FXMLLobbyView.fxml"));
                King_of_the_Hill.currentStage.getScene().setRoot(window1);

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleHomeButton(ActionEvent e) {
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

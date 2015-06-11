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
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import kingofthehill.client.ClientManager;
import kingofthehill.domain.IGameManager;
import kingofthehill.lobby.ILobby;
import kingofthehill.rmimultiplayer.IGameInfo;

/**
 *
 * @author Bas
 */
public class FXMLLobbyListViewController implements Initializable {

    @FXML
    private AnchorPane content;

    @FXML
    private ListView<String> gamesList;

    @FXML
    private Button buttonJoin;

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
    IGameManager gm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        games = FXCollections.observableArrayList();
        buttonJoin.setDisable(true);
        gamesList.setItems(games);

        String ipAddress = King_of_the_Hill.context.getServerUrl();
        ClientManager cm = new ClientManager(ipAddress);

        if (cm.locate()) {
            lobby = cm.getLobby();

//            Executors.newSingleThreadExecutor().execute(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        try {
//                            games.addAll(lobby.getGames());
//                        } catch (RemoteException ex) {
//                            Logger.getLogger(FXMLLobbyListViewController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(FXMLLobbyListViewController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            });
        }

//        try {
//            gamesList.setItems(lobby.getGames());
//        } catch (RemoteException ex) {
//            Logger.getLogger(FXMLLobbyListViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @FXML
    public void handleNewGameButton(ActionEvent e) {
        try {
            lobby.createGame(King_of_the_Hill.context.getPlayerName() + "'s lobby");
            games.addAll(lobby.getGames());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleGamesListOnClick(MouseEvent e) {
        try {
            gm = lobby.getGame(gamesList.getSelectionModel().getSelectedIndex());
            IGameInfo gameInfo = gm.getGameInfo();

            if (gameInfo.getActivePlayerCount() < 4) {
                buttonJoin.setDisable(false);
            } else {
                buttonJoin.setDisable(true);
            }

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (gameInfo.getPlayerName(0) != null) {
                            labelPlayer1.setText(gameInfo.getPlayerName(0));
                        } else {
                            labelPlayer1.setText("Nog geen speler");
                        }

                        if (gameInfo.getPlayerName(1) != null) {
                            labelPlayer2.setText(gameInfo.getPlayerName(1));
                        } else {
                            labelPlayer2.setText("Nog geen speler");
                        }

                        if (gameInfo.getPlayerName(2) != null) {
                            labelPlayer3.setText(gameInfo.getPlayerName(2));
                        } else {
                            labelPlayer3.setText("Nog geen speler");
                        }

                        if (gameInfo.getPlayerName(3) != null) {
                            labelPlayer4.setText(gameInfo.getPlayerName(3));
                        } else {
                            labelPlayer4.setText("Nog geen speler");
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(FXMLLobbyListViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleJoinButton(ActionEvent e) {
        try {
            if (gm != null) {
                lobby.joinGame(gamesList.getSelectionModel().getSelectedIndex(), King_of_the_Hill.context.getPlayerName());
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

            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListViewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
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

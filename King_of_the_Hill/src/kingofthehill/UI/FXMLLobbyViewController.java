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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import kingofthehill.client.ClientManager;
import kingofthehill.domain.GameMode;
import kingofthehill.lobby.ILobby;
import kingofthehill.rmimultiplayer.IGameInfo;
import kingofthehill.rmimultiplayer.TextMessage;

/**
 * The lobby of a created game
 *
 * @author Bas
 */
public class FXMLLobbyViewController implements Initializable {

    @FXML
    private AnchorPane content;

    @FXML
    private TextField chatInput;

    @FXML
    private ListView messagesOutput;

    @FXML
    private Button buttonReady;

    @FXML
    private Label labelPlayer1;

    @FXML
    private Label labelPlayer2;

    @FXML
    private Label labelPlayer3;

    @FXML
    private Label labelPlayer4;

    @FXML
    private ComboBox choiceBoxGameMode;

    @FXML
    private Label lblStartRes;

    @FXML
    private Slider moneySlider;

    ObservableList<String> messages;

    ClientManager cm = new ClientManager(King_of_the_Hill.context.getServerUrl());
    String gameName = King_of_the_Hill.context.getGameName();
    ILobby lobby;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (cm.locate()) {
            lobby = cm.getLobby();
        }

        ClientManager.AudioChat.setParent(this);

        //Fill combobox
        choiceBoxGameMode.getItems().add("2 versus 2");
        choiceBoxGameMode.getItems().add("Free for all");

        try {
            ClientManager.AudioChat.start();
            System.out.println("VoiceChat: Voice client started");
        } catch (Exception ex) {
            System.out.println("VoiceChat: Cannot start voice client");
            System.out.println("VoiceChat: Exception: " + ex.getMessage());
            return;
        }

        messages = FXCollections.observableArrayList();
        messagesOutput.setItems(messages);

        chatInput.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent E) -> {
            switch (E.getCode()) {
                case ENTER: {
                    handleSendButton();
                    break;
                }
            }
        });

        content.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent E) -> {
            switch (E.getCode()) {
                case F3: {
                    if (!ClientManager.AudioChat.isAudioCaptureStarted()) {
                        ClientManager.AudioChat.startAudioCapture();
                    }
                    break;
                }
            }
        });
        content.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent E) -> {
            switch (E.getCode()) {
                case F3: {
                    ClientManager.AudioChat.stopAudioCapture();
                    break;
                }
            }
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            IGameInfo gameInfo;

            @Override
            public void run() {
                try {
                    while (!lobby.getGame(gameName).readyGame()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    gameInfo = lobby.getGame(gameName).getGameInfo();

                                    if (gameInfo.getPlayerName(0) != null) {
                                        labelPlayer1.setText(gameInfo.getPlayerName(0) + readyString(lobby.getGame(gameName).getPlayerReady(gameInfo.getPlayerName(0))));
                                    } else {
                                        labelPlayer1.setText("Wachten op nieuwe speler...");
                                    }

                                    if (gameInfo.getPlayerName(1) != null) {
                                        labelPlayer2.setText(gameInfo.getPlayerName(1) + readyString(lobby.getGame(gameName).getPlayerReady(gameInfo.getPlayerName(1))));
                                    } else {
                                        labelPlayer2.setText("Wachten op nieuwe speler...");
                                    }

                                    if (gameInfo.getPlayerName(2) != null) {
                                        labelPlayer3.setText(gameInfo.getPlayerName(2) + readyString(lobby.getGame(gameName).getPlayerReady(gameInfo.getPlayerName(2))));
                                    } else {
                                        labelPlayer3.setText("Wachten op nieuwe speler...");
                                    }
                                    if (gameInfo.getPlayerName(3) != null) {
                                        labelPlayer4.setText(gameInfo.getPlayerName(3) + readyString(lobby.getGame(gameName).getPlayerReady(gameInfo.getPlayerName(3))));
                                    } else {
                                        labelPlayer4.setText("Wachten op nieuwe speler...");
                                    }
                                } catch (RemoteException ex) {
                                    Logger.getLogger(FXMLLobbyViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                //Check for UI values
                                try {
                                    if (lobby.getGame(gameName).getGameMode() == GameMode.COOP) {
                                        if (!choiceBoxGameMode.getSelectionModel().isSelected(0)) {
                                            choiceBoxGameMode.getSelectionModel().selectFirst();
                                            System.out.println("Coop gamemode set!");
                                        }
                                    } else {
                                        if (!choiceBoxGameMode.getSelectionModel().isSelected(1)) {
                                            choiceBoxGameMode.getSelectionModel().selectLast();
                                            System.out.println("F4a gamemode set!");
                                        }
                                    }
                                    
                                    if (lobby.getGame(gameName).getStartMoney() != (int)moneySlider.getValue()) {
                                        moneySlider.setValue(lobby.getGame(gameName).getStartMoney());
                                        lblStartRes.setText(lobby.getGame(gameName).getStartMoney() + "");
                                    }
                                } catch (RemoteException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        });
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            System.out.println("kingofthehill.UI.FXMLLobbyViewController initialize(): " + ex.getMessage());
                        }
                    }
                } catch (RemoteException ex) {
                    System.out.println("kingofthehill.UI.FXMLLobbyViewController initialize(): " + ex.getMessage());
                }
            }
        });
        
        /**
         * Add value property listener
         */
        moneySlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    lblStartRes.setText(new_val.intValue() + "");
                try {
                    lobby.getGame(gameName).setStartMoney(new_val.intValue());
                } catch (RemoteException ex) {
                    Logger.getLogger(FXMLLobbyViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private String readyString(boolean ready) {
        if (ready) {
            return " (Ready)";
        } else {
            return " (Unready)";
        }
    }

    @FXML
    public void handleReadyButton() {
        try {
            if (cm.locate()) {
                /**
                 * Set player ready, if player already was ready unset ready
                 */
                if (lobby.getGame(gameName).setPlayerReady(King_of_the_Hill.context.getPlayerName())) {
                    buttonReady.setText("Unready");
                } else {
                    buttonReady.setText("Ready");
                }
            }

            //Todo thread control
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (lobby.getGame(gameName).getPlayerReady(King_of_the_Hill.context.getPlayerName())) {
                            try {
                                if (lobby.getGame(gameName).readyGame()) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                //Load next window
                                                Parent window1;
                                                window1 = FXMLLoader.load(getClass().getResource("FXMLMultiPlayerView.fxml"));
                                                King_of_the_Hill.currentStage.getScene().setRoot(window1);
                                            } catch (IOException ex) {
                                                System.out.println("Loading new window failed!");
                                            }
                                        }
                                    });
                                    break;
                                }
                                System.out.println("Checking for game start");
                            } catch (RemoteException ex) {
                                System.out.println("kingofthehill.UI.FXMLLobbyViewController handleReadyButton(): " + ex.getMessage());
                            }

                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                System.out.println("kingofthehill.UI.FXMLLobbyViewController handleReadyButton(): " + ex.getMessage());
                            }

                        }
                    } catch (RemoteException ex) {
                        System.out.println("kingofthehill.UI.FXMLLobbyViewController handleReadyButton(): " + ex.getMessage());
                    }
                }
            });

        } catch (IOException ex) {
            System.out.println("kingofthehill.UI.FXMLLobbyViewController handleReadyButton(): " + ex.getMessage());
        }
    }

    @FXML
    public void handleSendButton() {
        if (!chatInput.getText().equals("")) {
            if (ClientManager.isAudioChatRunning()) {
                ClientManager.AudioChat.sendMessage(new TextMessage(ClientManager.AudioChat.getClientId(), chatInput.getText()));
            }
        }
        chatInput.setText("");
    }

    @FXML
    public void handleQuitButton() {
        if (cm.locate()) {
            try {
                lobby.getGame(gameName).removePlayer(King_of_the_Hill.context.getPlayerName());

            } catch (RemoteException ex) {
                System.out.println("kingofthehill.UI.FXMLLobbyViewController handleQuitButton(): " + ex.getMessage());
            }
        }

        try {
            //Load next window
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("FXMLLobbyListView.fxml"));
            King_of_the_Hill.currentStage.getScene().setRoot(window1);

        } catch (IOException ex) {
            System.out.println("kingofthehill.UI.FXMLLobbyViewController handleQuitButton(): " + ex.getMessage());
        }
    }

    @FXML
    public void handleTextFieldKeyPress() {

    }

    public void printMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messages.add(message);
                messagesOutput.scrollTo(messages.size() - 1);
            }
        });
    }

    /**
     * Handle when game mode button changed
     *
     * @param e
     */
    public void handleGameModeChanged(ActionEvent e) {
        try {
            if (choiceBoxGameMode.getSelectionModel().isSelected(0)) {
                lobby.getGame(gameName).setGameMode(GameMode.COOP);
            } else {
                lobby.getGame(gameName).setGameMode(GameMode.F4A);
            }
        } catch (RemoteException ex) {
            System.out.println("Changing gamemode failed!");
            System.out.println(ex.getMessage());
        }
    }
}

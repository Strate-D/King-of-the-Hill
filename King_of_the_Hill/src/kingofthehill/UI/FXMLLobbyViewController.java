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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import kingofthehill.client.ClientManager;
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

    ObservableList<String> messages;

    ClientManager cm = new ClientManager(King_of_the_Hill.context.getServerUrl());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClientManager.AudioChat.setParent(this);

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

        if (cm.locate()) {
            try {
                cm.getGameManager().addPlayer(King_of_the_Hill.context.getPlayerName(), false);
            } catch (RemoteException ex) {
                System.out.println("kingofthehill.UI.FXMLLobbyViewController initialize(): " + ex.getMessage());
            }
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            IGameInfo gameInfo;

            @Override
            public void run() {
                try {
                    while (!cm.getGameManager().readyGame()) {
                        gameInfo = cm.getGameManager().getGameInfo();

                        for (int i = 0; i < gameInfo.getPlayers().size(); i++) {
                            final String playerName = gameInfo.getPlayers().get(i).getName();

                            String ready = "";

                            if (cm.getGameManager().getPlayerReady(gameInfo.getPlayers().get(i).getName())) {
                                ready = " (Ready)";
                            } else {
                                ready = " (Unready)";
                            }

                            final Integer id = i;
                            final String readyString = ready;

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    switch (id) {
                                        case 0:
                                            labelPlayer1.setText(playerName + readyString);
                                            break;
                                        case 1:
                                            labelPlayer2.setText(playerName + readyString);
                                            break;
                                        case 2:
                                            labelPlayer3.setText(playerName + readyString);
                                            break;
                                        case 3:
                                            labelPlayer4.setText(playerName + readyString);
                                            break;
                                    }
                                }
                            });
                        }
                    }
                } catch (RemoteException ex) {
                    System.out.println("kingofthehill.UI.FXMLLobbyViewController initialize(): " + ex.getMessage());
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println("kingofthehill.UI.FXMLLobbyViewController initialize(): " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    public void handleReadyButton() {
        try {
            if (cm.locate()) {
                /**
                 * Set player ready, if player already was ready unset ready
                 */
                if (cm.getGameManager().setPlayerReady(King_of_the_Hill.context.getPlayerName())) {
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
                        while (cm.getGameManager().getPlayerReady(King_of_the_Hill.context.getPlayerName())) {
                            try {
                                if (cm.getGameManager().readyGame()) {
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
        if (ClientManager.isAudioChatRunning()) {
            ClientManager.AudioChat.stop();
        }

        if (cm.locate()) {
            try {
                cm.getGameManager().removePlayer(King_of_the_Hill.context.getPlayerName());

            } catch (RemoteException ex) {
                System.out.println("kingofthehill.UI.FXMLLobbyViewController handleQuitButton(): " + ex.getMessage());
            }
        }

        try {
            //Load next window
            Parent window1;
            window1 = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
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
                if (messages.size() > 40) {
                    messages.remove(0);
                }
                messagesOutput.scrollTo(messages.size() - 1);
            }
        });
    }

    public void leaveLobby() {
        Platform.runLater(() -> {
            handleQuitButton();
        });
    }
}

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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import kingofthehill.client.ClientManager;
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
                        
                        int amount = (int) moneySlider.valueProperty().get() - 100; // Amount of money added to starting resources
                        lblStartRes.setText("" + amount);
                        gameInfo = lobby.getGame(gameName).getGameInfo();
                        //gameInfo.getPlayers().get(i).addMoney(amount);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {                                
                                try {
                                    //gameInfo = lobby.getGame(gameName).getGameInfo();
                                                                     
                                    if (gameInfo.getPlayerName(0) != null) {
                                        labelPlayer1.setText(gameInfo.getPlayerName(0) + lobby.getGame(gameName).getPlayerReady(gameInfo.getPlayerName(0)));
                                    } else {
                                        labelPlayer1.setText("Wachten op nieuwe speler...");
                                    }
                                    
                                    if (gameInfo.getPlayerName(1) != null) {
                                        labelPlayer2.setText(gameInfo.getPlayerName(1) + lobby.getGame(gameName).getPlayerReady(gameInfo.getPlayerName(1)));
                                    } else {
                                        labelPlayer2.setText("Wachten op nieuwe speler...");
                                    }
                                    
                                    if (gameInfo.getPlayerName(2) != null) {
                                        labelPlayer3.setText(gameInfo.getPlayerName(2) + lobby.getGame(gameName).getPlayerReady(gameInfo.getPlayerName(2)));
                                    } else {
                                        labelPlayer3.setText("Wachten op nieuwe speler...");
                                    }
                                    
                                    if (gameInfo.getPlayerName(3) != null) {
                                        labelPlayer4.setText(gameInfo.getPlayerName(3) + lobby.getGame(gameName).getPlayerReady(gameInfo.getPlayerName(3)));
                                    } else {
                                        labelPlayer4.setText("Wachten op nieuwe speler...");
                                    }
                                } catch (RemoteException ex) {
                                    Logger.getLogger(FXMLLobbyViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
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
                        while (lobby.getGame(gameName).getPlayerReady(King_of_the_Hill.context.getPlayerName()).equals(" (Unready)")) {
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
}

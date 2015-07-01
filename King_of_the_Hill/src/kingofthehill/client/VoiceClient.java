/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.client;

import java.io.EOFException;
import kingofthehill.rmimultiplayer.AudioMessage;
import kingofthehill.rmimultiplayer.InfoMessage;
import kingofthehill.rmimultiplayer.Message;
import kingofthehill.rmimultiplayer.TextMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kingofthehill.UI.FXMLLobbyViewController;

/**
 * The VoiceClient recieves audio and text messages from the server
 *
 * @author Bas
 */
public class VoiceClient {

    private Socket socket;
    private ObjectOutputStream sender;
    private ObjectInputStream reader;

    private AudioPlayer audioPlayer;
    private AudioCapture audioCapturer;

    private String ip;
    private int port;
    private String nickname;

    private boolean clientStarted;
    private FXMLLobbyViewController parent;

    private int clientID = -1;
    private Thread thr;

    public VoiceClient(String ip, int port, String nickname) {
        audioPlayer = new AudioPlayer(this);
        audioCapturer = new AudioCapture(this);

        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
        this.clientStarted = false;
    }

    /**
     * Starts the VoiceServer threads for receiving and sending messages
     * @param lobbyname
     */
    public void start(String lobbyname) {
        String serverAddress = ip;
        try {
            socket = new Socket(serverAddress, port);
            sender = new ObjectOutputStream(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("kingofthehill.client.VoiceClient start(): " + ex.getMessage());
        }

        String name = this.nickname;

        /**
         * Create an audio player
         */
        try {
            audioPlayer.play();
        } catch (Exception ex) {
            this.printMessage(ex.getMessage());
        }

        startMessageReader();

        try {
            sender.writeObject(new InfoMessage(name, "CLIENT_NAME", lobbyname));
        } catch (IOException ex) {
            System.out.println("kingofthehill.client.VoiceClient start(): " + ex.getMessage());
        }

        this.clientStarted = true;
    }

    /**
     * Starts the thread for the messages reader
     */
    private void startMessageReader() {
        Thread t = new Thread(() -> {
            OUTER:
            while (true) {
                Message object;
                try {
                    //object = (Message) reader.readObject();
                    Object message = reader.readObject();
                    if (message instanceof Message) {
                        object = (Message) message;
                    } else {
                        throw new ClassNotFoundException("Wrong message type was send to the client from the server");
                    }
                } catch (SocketException | EOFException ex) {
                    break;
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("kingofthehill.client.VoiceClient startMessageReader()(r. 110): " + ex.getMessage());
                    continue;
                }
                if (object instanceof InfoMessage) {
                    InfoMessage mess = (InfoMessage) object;
                    switch (mess.getDefine()) {
                        case "CLIENT_ID":
                            this.clientID = (int) mess.getData();
                            this.printMessage("<< ClientID received from server >>");
                            try {
                                sender.writeObject(new InfoMessage(this.clientID, "GET_LAST_MESSAGES"));
                            } catch (IOException ex) {
                                System.out.println("kingofthehill.client.VoiceClient startMessageReader()(r. 117): " + ex.getMessage());
                            }
                            continue;
                        case "KICK":
                            this.sendMessage(new InfoMessage(this.clientID, "LEAVE_PARTY"));
                            this.printMessage("<< The host kicked you >>");
                            break OUTER;
                        case "KICK_REPLY":
                            this.printMessage("<< " + mess.getData() + " >>");
                            break;
                        case "SEND_LAST_MESSAGES":
                            this.printMessage("<< Printing previous messages from server >>");
                            for (Message m : (List<Message>) mess.getData()) {
                                try {
                                    this.printMessage(
                                            (m.getTime() == null ? "never" : m.getTime()) + ": "
                                            + m.getSenderName() + " says: " + m.getData());
                                } catch (Exception ex) {
                                    this.printMessage(ex.getMessage());
                                }
                            }
                            continue;
                    }
                } else if (object instanceof AudioMessage) {
                    /**
                     * Send the audio message to the speakers
                     */
                    audioPlayer.addAudioMessage((AudioMessage) object);
                    continue;
                }
                if (this.parent != null && !(object instanceof InfoMessage)) {
                    this.printMessage(object.getTime() + ": " + object.getSenderName() + " says: " + object.getData());
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    this.printMessage(ex.getMessage());
                }
            }

            audioPlayer.stopPlayback();
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
            }
            this.parent.leaveLobby();
            
        });
        t.start();
        thr = t;
    }

    /**
     * Sends a message to the server
     *
     * @param message The message to send
     */
    public void sendMessage(Message message) {

        if (message instanceof TextMessage) {
            TextMessage tmessage = (TextMessage) message;
            try {
                if (tmessage.getData().toString().startsWith("/kick ")) {
                    //if (this.clientID == 1) {
                    sender.writeObject(new InfoMessage(tmessage.getData().toString().replace("/kick ", ""), "KICK_CLIENT"));
                    //} else {
                    //    this.printMessage("<< You are not allowed to do that >>");
                    //}
                    return;
                }// else if (tmessage.getData().toString().startsWith("/leave")) {
                //    sender.writeObject(new InfoMessage(this.clientID, "LEAVE_PARTY"));
                 //   this.parent.leaveLobby();
                //    return;
                //}
                 else if (tmessage.getData().toString().startsWith("/start")) {
                    this.audioCapturer.startCapture();
                    return;
                } else if (tmessage.getData().toString().startsWith("/stop")) {
                    this.audioCapturer.stopCapture();
                    return;
                }

                sender.writeObject(message);
            } catch (SocketException ex) {
                this.parent.printMessage("<< Connection to the server was lost >>");
            } catch (Exception ex) {
                System.out.println("kingofthehill.rmimultiplayer.VoiceServer sendMessage(): " + ex.getMessage());
            }
        } else {
            try {
                sender.writeObject(message);
            } catch (SocketException ex) {
            } catch (IOException ex) {
                System.out.println("kingofthehill.rmimultiplayer.VoiceServer sendMessage(): " + ex.getMessage());
            }

        }
    }

    /**
     * Stop the VoiceClient
     */
    public void stop() {
        this.sendMessage(new InfoMessage(this.clientID, "LEAVE_PARTY"));
        this.stopAudioCapture();
        this.audioPlayer.stopPlayback();

        if (thr != null) {
            thr.interrupt();
        }
    }

    /**
     * Returns the current clientID
     *
     * @return The clientID
     */
    public int getClientId() {
        return this.clientID;
    }

    /**
     * Returns if the client is started recieving messages
     *
     * @return true if the client started; otherwise false
     */
    public boolean isStarted() {
        return this.clientStarted;
    }

    /**
     * The FXML application for printing the output messages
     *
     * @param value The FXML Controller for the window
     */
    public void setParent(FXMLLobbyViewController value) {
        this.parent = value;
    }

    /**
     * Prints a message to the FXML application
     *
     * @param message The message to print
     */
    void printMessage(String message) {
        if (this.parent != null) {
            this.parent.printMessage(message);
        }
    }

    /**
     * Checks if the microphone is started
     *
     * @return true if the microphone is started; otherwise false
     */
    public boolean isAudioCaptureStarted() {
        return this.audioCapturer.isRunning();
    }

    /**
     * Enable the microphone for capturing audio
     */
    public void startAudioCapture() {
        try {
            this.audioCapturer.startCapture();
        } catch (Exception ex) {
        }
    }

    /**
     * Disable the microphone for audio capture
     */
    public void stopAudioCapture() {
        this.audioCapturer.stopCapture();
    }
}

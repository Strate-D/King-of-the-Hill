/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.client;

import kingofthehill.rmimultiplayer.AudioMessage;
import kingofthehill.rmimultiplayer.InfoMessage;
import kingofthehill.rmimultiplayer.Message;
import kingofthehill.rmimultiplayer.TextMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import kingofthehill.UI.FXMLLobbyViewController;

/**
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

    public VoiceClient(String ip, int port, String nickname) {
        audioPlayer = new AudioPlayer();
        audioCapturer = new AudioCapture(this);

        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
        this.clientStarted = false;
    }

    public void start() throws IOException {
        //Scanner input = new Scanner(System.in);
        //System.out.print("Client: Enter IP address of server: ");
        String serverAddress = ip; //= input.nextLine();

        socket = new Socket(serverAddress, port);
        sender = new ObjectOutputStream(this.socket.getOutputStream());
        reader = new ObjectInputStream(this.socket.getInputStream());

        //System.out.print("Client: Enter a nickname for the chat: ");
        String name = this.nickname; //= input.nextLine();
        if (name.equals("")) {
            name = "Ballenzuiger";
        }

        // Create an audio player
        try {
            audioPlayer.play();
        } catch (Exception ex) {
            Logger.getLogger(VoiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        startMessageReader();
        //startMessageWriter();

        sender.writeObject(new InfoMessage(name, "CLIENT_NAME"));

        this.clientStarted = true;
    }

    private void startMessageReader() {
        Thread t = new Thread(() -> {
            while (true) {
                Message object = null;
                try {
                    object = (Message) reader.readObject();
                } catch (IOException | ClassNotFoundException ex) {
                    continue;
                }

                if (object instanceof InfoMessage) {
                    InfoMessage mess = (InfoMessage) object;
                    if (mess.getDefine().equals("CLIENT_ID")) {
                        this.clientID = (int) mess.getData();
                        System.out.print("\r<< ClientID received from server >>\n>");
                        try {
                            sender.writeObject(new InfoMessage(this.clientID, "GET_LAST_MESSAGES"));
                        } catch (IOException ex) {
                        }
                        continue;
                    } else if (mess.getDefine().equals("KICK")) {
                        System.out.print("\r<< The host kicked you >>\n>");
                        break;
                    } else if (mess.getDefine().equals("SEND_LAST_MESSAGES")) {
                        System.out.print("\r<< Printing previous messages from server >>\n");
                        for (Message m : (List<Message>) mess.getData()) {
                            try {
                                this.parent.printMessage(
                                        (m.getTime() == null ? "never" : m.getTime()) + ": "
                                        + m.getSenderName() + " says: " + m.getData());
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                        continue;
                    }
                } else if (object instanceof AudioMessage) {
                    //Send the audio message to the speakers
                    audioPlayer.addAudioMessage((AudioMessage) object);
                    continue;
                }

                if (this.parent != null) {
                    this.parent.printMessage(object.getTime() + ": " + object.getSenderName() + " says: " + object.getData());
                }
                System.out.print("\r" + object.getTime() + ": " + object.getSenderName() + " says: ");
                System.out.println(object.getData());
                System.out.print(">");

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VoiceClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            audioPlayer.stopPlayback();

            System.exit(0);
        });
        t.start();
    }

    private void startMessageWriter() {
//        Thread t = new Thread(() -> {
//            while (true) {
//                try {
//                    Scanner input = new Scanner(System.in);
//                    String newMessage = input.nextLine();
//
//                    if (newMessage.startsWith("/kick ")) {
//                        if (this.clientID == 1) {
//                            sender.writeObject(new InfoMessage(Integer.parseInt(newMessage.replace("/kick ", "")), "KICK_CLIENT"));
//                        } else {
//                            System.out.println("<< You are not allowed to do that >>");
//                        }
//                    } else if (newMessage.startsWith("/start")) {
//                        this.audioCapturer.startCapture();
//                    } else if (newMessage.startsWith("/stop")) {
//                        this.audioCapturer.stopCapture();
//
//                    } else {
//                        sender.writeObject(new TextMessage(this.clientID, newMessage));
//                    }
//
//                    System.out.print(">");
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                }
//
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(VoiceClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        t.start();
    }

    public void sendMessage(Message message) {

        if (message instanceof TextMessage) {
            TextMessage tmessage = (TextMessage) message;
            try {
                if (tmessage.getData().toString().startsWith("/kick ")) {
                    if (this.clientID == 1) {
                        sender.writeObject(new InfoMessage(Integer.parseInt(tmessage.getData().toString().replace("/kick ", "")), "KICK_CLIENT"));
                    } else {
                        this.parent.printMessage("<< You are not allowed to do that >>");
                    }
                    return;
                } else if (tmessage.getData().toString().startsWith("/start")) {
                    this.audioCapturer.startCapture();
                    return;
                } else if (tmessage.getData().toString().startsWith("/stop")) {
                    this.audioCapturer.stopCapture();
                    return;
                } 

                sender.writeObject(message);
            } catch (Exception ex) {

            }
        } else {
            try {
                sender.writeObject(message);
            } catch (IOException ex) {
            }
        }
    }

    public int getClientId() {
        return this.clientID;
    }

    public boolean isStarted() {
        return this.clientStarted;
    }

    public void setParent(FXMLLobbyViewController value) {
        this.parent = value;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.UI;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import kingofthehill.client.ClientManager;
import kingofthehill.domain.AI;
import kingofthehill.domain.AIState;
import kingofthehill.domain.Defence;
import kingofthehill.rmimultiplayer.IGameInfo;
import kingofthehill.domain.IGameManager;
import kingofthehill.domain.IPlayer;
import kingofthehill.domain.Melee;
import kingofthehill.domain.Player;
import kingofthehill.domain.Ranged;
import kingofthehill.domain.Unit;
import kingofthehill.domain.UnitType;
import kingofthehill.unitinfo.UnitInfo;

/**
 * FXML Controller class
 *
 * @author Jur
 */
public class FXMLMultiPlayerViewController implements Initializable {

    @FXML
    private Canvas canvas;

    //Player unit sprites
    Image meleeBlueR, meleeBlueB, rangedBlueR, rangedBlueB;
    Image meleeGreenL, meleeGreenB, rangedGreenL, rangedGreenB;
    Image meleePurpleT, meleePurpleL, rangedPurpleT, rangedPurpleL;
    Image meleeRedT, meleeRedR, rangedRedT, rangedRedR;
    Image defenceSide, defenceUpDown;
    Image resource;
    //Castle sprites
    Image castle1, castle2, castle3, castle4;
    Image castle_destroyed1, castle_destroyed2, castle_destroyed3, castle_destroyed4;
    //Field sprites
    Image dirtField1, dirtField2;
    Image defenceSpots;
    Image corner1, corner2, corner3, corner4;
    Image side1, side2, side3, side4;
    Image background;
    // Selector sprite and cooldown sprite
    Image selector, cooldown;
    //Button sprites
    Image buttonMelee, buttonRanged, buttonDefence, buttonResource;
    //Mysterybox sprites
    Image mysterybox;

    IGameManager gm;
    IGameInfo gameInfo;
    AnimationTimer antimer;
    boolean isMouseOnCanvas;
    boolean isMouseClicked = false;

    int meleeCooldown, rangedCooldown, defenceCooldown, resourceCooldown;

    double scrollPosX, scrollPosY, lastMousePosx, lastMousePosy, lastRealMousePosx, lastRealMousePosy;

    UnitInfo selectedUnit;

    String mysteryboxWinner = "";
    String mysteryboxContent = "";
    int gameInfoTimer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Create the game
        IPlayer p = new Player(King_of_the_Hill.context.getPlayerName(), 10);

        // Welcome message
        System.out.println("CLIENT USING REGISTRY");

        // Get ip address of server
        String ipAddress = King_of_the_Hill.context.getServerUrl();

        ClientManager cm = new ClientManager(ipAddress);

        if (cm.locate()) {
            gm = cm.getGameManager();

            try {
                getGameInfo();
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLMultiPlayerViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            isMouseOnCanvas = false;
            selectedUnit = null;

            //Load all sprites
            meleeBlueR = new Image("kingofthehill/UI/Units/Blue/BlueKnightNewR.png");
            meleeBlueB = new Image("kingofthehill/UI/Units/Blue/BlueKnightNewF.png");
            meleeGreenL = new Image("kingofthehill/UI/Units/Green/GreenKnightNewL.png");
            meleeGreenB = new Image("kingofthehill/UI/Units/Green/GreenKnightNewF.png");
            meleePurpleL = new Image("kingofthehill/UI/Units/Purple/PurpleKnightNewL.png");
            meleePurpleT = new Image("kingofthehill/UI/Units/Purple/PurpleKnightNewB.png");
            meleeRedR = new Image("kingofthehill/UI/Units/Red/RedKnightNewR.png");
            meleeRedT = new Image("kingofthehill/UI/Units/Red/RedKnightNewB.png");
            rangedBlueR = new Image("kingofthehill/UI/Units/Blue/BlueArcherNewR.png");
            rangedBlueB = new Image("kingofthehill/UI/Units/Blue/BlueArcherNewF.png");
            rangedGreenL = new Image("kingofthehill/UI/Units/Green/GreenArcherNewL.png");
            rangedGreenB = new Image("kingofthehill/UI/Units/Green/GreenArcherNewF.png");
            rangedPurpleL = new Image("kingofthehill/UI/Units/Purple/PurpleArcherNewL.png");
            rangedPurpleT = new Image("kingofthehill/UI/Units/Purple/PurpleArcherNewB.png");
            rangedRedR = new Image("kingofthehill/UI/Units/Red/RedArcherNewR.png");
            rangedRedT = new Image("kingofthehill/UI/Units/Red/RedArcherNewB.png");
            defenceSide = new Image("kingofthehill/UI/Units/Defence/SideWall.png");
            defenceUpDown = new Image("kingofthehill/UI/Units/Defence/UpDownWall.png");
            castle1 = new Image("kingofthehill/UI/field/castle1.png");
            castle2 = new Image("kingofthehill/UI/field/castle2.png");
            castle3 = new Image("kingofthehill/UI/field/castle3.png");
            castle4 = new Image("kingofthehill/UI/field/castle4.png");
            castle_destroyed1 = new Image("kingofthehill/UI/field/castle_destroyed1.png");
            castle_destroyed2 = new Image("kingofthehill/UI/field/castle_destroyed2.png");
            castle_destroyed3 = new Image("kingofthehill/UI/field/castle_destroyed3.png");
            castle_destroyed4 = new Image("kingofthehill/UI/field/castle_destroyed4.png");
            dirtField1 = new Image("kingofthehill/UI/field/dirt1.png");
            dirtField2 = new Image("kingofthehill/UI/field/dirt2.png");
            defenceSpots = new Image("kingofthehill/UI/field/grass_final.png");
            background = new Image("kingofthehill/UI/field/background/desert.jpg");
            corner1 = new Image("kingofthehill/UI/field/background/corner1.png");
            corner2 = new Image("kingofthehill/UI/field/background/corner2.png");
            corner3 = new Image("kingofthehill/UI/field/background/corner3.png");
            corner4 = new Image("kingofthehill/UI/field/background/corner4.png");
            side1 = new Image("kingofthehill/UI/field/background/side1.png");
            side2 = new Image("kingofthehill/UI/field/background/side2.png");
            side3 = new Image("kingofthehill/UI/field/background/side3.png");
            side4 = new Image("kingofthehill/UI/field/background/side4.png");
            selector = new Image("kingofthehill/UI/field/selector.png");
            cooldown = new Image("kingofthehill/UI/field/cooldown.png");
            buttonMelee = new Image("kingofthehill/UI/field/button-melee.png");
            buttonRanged = new Image("kingofthehill/UI/field/button-ranged.png");
            buttonDefence = new Image("kingofthehill/UI/field/button-defence.png");
            buttonResource = new Image("kingofthehill/UI/field/button-munnie.png");
            mysterybox = new Image("kingofthehill/UI/field/mysterybox.png");
            resource = new Image("kingofthehill/UI/Units/Resource/house.png");
            gameInfoTimer = 0;
            //Draw field
            drawBackground();
            drawField();

            //Start animation timer
            antimer = new AnimationTimer() {

                @Override
                public void handle(long now) {
                    //Check if data has te be updated from the server
                    if (gameInfoTimer > 10) {
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getGameInfo();
                                } catch (RemoteException ex) {
                                    Logger.getLogger(FXMLMultiPlayerViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        gameInfoTimer = 0;
                    }
                    //Check if to do step
                    if (gameInfoTimer % 2 == 0) {
                        gameInfo.doStep();
                    }
                    /**
                     * Keep track of timer
                     */
                    gameInfoTimer++;

                    drawBackground();
                    drawField();
                    drawUnits();

                    try {
                        // Send heartbeat to server
                        gm.sendPlayerSignal(King_of_the_Hill.context.getPlayerName());
                    } catch (RemoteException ex) {
                        System.out.println("Failed to inform server!");
                    }

                    // Check and handle mouse scrolling
                    if (isMouseOnCanvas) {
                        if (lastMousePosx > 800) {
                            if (scrollPosX < -443) {
                                scrollPosX = -450;
                            } else {
                                scrollPosX = scrollPosX - 7;
                            }
                        } else if (lastMousePosx < 100) {
                            if (scrollPosX > -7) {
                                scrollPosX = 0;
                            } else {
                                scrollPosX = scrollPosX + 7;
                            }
                        }
                        if (lastMousePosy > 800) {
                            if (scrollPosY < -443) {
                                scrollPosY = -450;
                            } else {
                                scrollPosY = scrollPosY - 7;
                            }
                        } else if (lastMousePosy < 100) {
                            if (scrollPosY > -7) {
                                scrollPosY = 0;
                            } else {
                                scrollPosY = scrollPosY + 7;
                            }
                        }
                    }
                    //If mouse on canvas, zoom in
                    if (isMouseOnCanvas) {
                        canvas.getGraphicsContext2D().setTransform(1.5, 0, 0, 1.5, scrollPosX, scrollPosY);
                    } else {
                        canvas.getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
                    }

                    //Check if game ended
                    if (gameInfo.getPlayers().get(0).getBase().getHealthPoints() == 0 && gameInfo.getPlayers().get(2).getBase().getHealthPoints() == 0) {
                        canvas.getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
                        drawBackground();
                        drawField();
                        drawUnits();
                        canvas.getGraphicsContext2D().fillText("Team blue won!", 450, 450);
                        this.stop();
                    } else if (gameInfo.getPlayers().get(1).getBase().getHealthPoints() == 0 && gameInfo.getPlayers().get(3).getBase().getHealthPoints() == 0) {
                        canvas.getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
                        drawBackground();
                        drawField();
                        drawUnits();
                        canvas.getGraphicsContext2D().fillText("Team red won!", 450, 450);
                        this.stop();
                    }

                    //Handle cooldown of units
                    if (meleeCooldown > 0) {
                        meleeCooldown--;
                    }
                    if (rangedCooldown > 0) {
                        rangedCooldown--;
                    }
                    if (defenceCooldown > 0) {
                        defenceCooldown--;
                    }
                    if (resourceCooldown > 0) {
                        resourceCooldown--;
                    }
                }

                @Override
                public void start() {
                    super.start();
                }
            };
            antimer.start();
        }
    }

    private void getGameInfo() throws RemoteException {
        gameInfo = gm.getGameInfo();
        gameInfo.setFirstPlayer(King_of_the_Hill.context.getPlayerName());
    }

    /**
     * Go back to the main menu when the quit button is pressed
     *
     * @param e
     */
    public void handleQuitButton(ActionEvent e) {
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

    /**
     * Check for mouse movement on the game canvas
     *
     * @param e
     */
    public void handleMouseOver(MouseEvent e) {
        isMouseOnCanvas = true;
        lastMousePosx = e.getX();
        lastMousePosy = e.getY();

        lastRealMousePosx = (e.getX() - scrollPosX) / 1.5;
        lastRealMousePosy = (e.getY() - scrollPosY) / 1.5;

        //check if mouse is floating over mysterybox
        if (lastRealMousePosx >= 325 && lastRealMousePosx <= 700
                && lastRealMousePosy >= 325 && lastRealMousePosy <= 700 && mysterybox == null) {
            Cursor c = Cursor.HAND;
            canvas.setCursor(c);
        } else {
            canvas.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Create event when mouse is off the canvas
     *
     * @param e
     */
    public void handleMouseOffCanvas(MouseEvent e) {
        isMouseOnCanvas = false;
    }

    /**
     * Draws the background of the game
     */
    private void drawBackground() {
        //Draw sides
        canvas.getGraphicsContext2D().drawImage(side1, 0, 0, 900, 8);
        canvas.getGraphicsContext2D().drawImage(side2, 892, 0, 8, 900);
        canvas.getGraphicsContext2D().drawImage(side3, 0, 892, 900, 8);
        canvas.getGraphicsContext2D().drawImage(side4, 0, 0, 8, 900);
        //Draw corners
        canvas.getGraphicsContext2D().drawImage(corner1, 0, 0, 30, 30);
        canvas.getGraphicsContext2D().drawImage(corner2, 870, 0, 30, 30);
        canvas.getGraphicsContext2D().drawImage(corner3, 870, 870, 30, 30);
        canvas.getGraphicsContext2D().drawImage(corner4, 0, 870, 30, 30);
        //Draw background
        canvas.getGraphicsContext2D().drawImage(background, 8, 8, 884, 884);
    }

    /**
     * Draws the field of the game
     */
    private void drawField() {
        //Check hp, then draw correct sprite for base
        if (gameInfo.getPlayers().get(0).getBase().getHealthPoints() > 0) {
            canvas.getGraphicsContext2D().drawImage(castle1, 20, 20, 200, 200);
        } else {
            canvas.getGraphicsContext2D().drawImage(castle_destroyed1, 20, 20, 200, 200);
        }
        if (gameInfo.getPlayers().get(1).getBase().getHealthPoints() > 0) {
            canvas.getGraphicsContext2D().drawImage(castle2, 680, 20, 200, 200);
        } else {
            canvas.getGraphicsContext2D().drawImage(castle_destroyed2, 680, 20, 200, 200);
        }
        if (gameInfo.getPlayers().get(2).getBase().getHealthPoints() > 0) {
            canvas.getGraphicsContext2D().drawImage(castle3, 680, 680, 200, 200);
        } else {
            canvas.getGraphicsContext2D().drawImage(castle_destroyed3, 680, 680, 200, 200);
        }
        if (gameInfo.getPlayers().get(3).getBase().getHealthPoints() > 0) {
            canvas.getGraphicsContext2D().drawImage(castle4, 20, 680, 200, 200);
        } else {
            canvas.getGraphicsContext2D().drawImage(castle_destroyed4, 20, 680, 200, 200);
        }
        //Draw health castles
        canvas.getGraphicsContext2D().setFill(Color.RED);
        canvas.getGraphicsContext2D().fillRect(55, 70, 100, 5);
        canvas.getGraphicsContext2D().fillRect(745, 70, 100, 5);
        canvas.getGraphicsContext2D().fillRect(745, 760, 100, 5);
        canvas.getGraphicsContext2D().fillRect(55, 760, 100, 5);
        canvas.getGraphicsContext2D().setFill(Color.GREEN);
        canvas.getGraphicsContext2D().fillRect(55, 70, (float) gameInfo.getPlayers().get(0).getBase().getHealthPoints(), 5);
        canvas.getGraphicsContext2D().fillRect(745, 70, (float) gameInfo.getPlayers().get(1).getBase().getHealthPoints(), 5);
        canvas.getGraphicsContext2D().fillRect(745, 760, (float) gameInfo.getPlayers().get(2).getBase().getHealthPoints(), 5);
        canvas.getGraphicsContext2D().fillRect(55, 760, (float) gameInfo.getPlayers().get(3).getBase().getHealthPoints(), 5);
        //Draw name
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().setFont(Font.font(null, FontWeight.BOLD, 12));
        canvas.getGraphicsContext2D().fillText(gameInfo.getPlayers().get(0).getName(), 70, 65);
        canvas.getGraphicsContext2D().fillText(gameInfo.getPlayers().get(1).getName(), 760, 65);
        canvas.getGraphicsContext2D().fillText(gameInfo.getPlayers().get(2).getName(), 760, 755);
        canvas.getGraphicsContext2D().fillText(gameInfo.getPlayers().get(3).getName(), 70, 755);
        //Draw money
        canvas.getGraphicsContext2D().setFill(Color.GOLD);
        canvas.getGraphicsContext2D().fillText("# gold: " + gameInfo.getPlayers().get(0).getMoney(), 55, 100);
        //Draw score
        canvas.getGraphicsContext2D().setFill(Color.AQUA);
        canvas.getGraphicsContext2D().fillRect(55, 80, 100, 5);
        canvas.getGraphicsContext2D().fillRect(745, 80, 100, 5);
        canvas.getGraphicsContext2D().fillRect(745, 770, 100, 5);
        canvas.getGraphicsContext2D().fillRect(55, 770, 100, 5);
        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        canvas.getGraphicsContext2D().fillRect(105, 80, (float) gameInfo.getPlayers().get(0).getScore() / 2, 5);
        canvas.getGraphicsContext2D().fillRect(795, 80, (float) gameInfo.getPlayers().get(1).getScore() / 2, 5);
        canvas.getGraphicsContext2D().fillRect(795, 770, (float) gameInfo.getPlayers().get(2).getScore() / 2, 5);
        canvas.getGraphicsContext2D().fillRect(105, 770, (float) gameInfo.getPlayers().get(3).getScore() / 2, 5);
        //Set color back
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().setFont(Font.font(null, FontWeight.NORMAL, 12));
        //Draw lanes
        canvas.getGraphicsContext2D().drawImage(dirtField1, 318, 52, 265, 105);
        canvas.getGraphicsContext2D().drawImage(dirtField1, 318, 742, 265, 105);
        canvas.getGraphicsContext2D().drawImage(dirtField2, 52, 317, 105, 265);
        canvas.getGraphicsContext2D().drawImage(dirtField2, 742, 317, 105, 265);
        //Draw defence spots
        canvas.getGraphicsContext2D().drawImage(defenceSpots, 215, 52, 105, 105);
        canvas.getGraphicsContext2D().drawImage(defenceSpots, 580, 52, 105, 105);
        canvas.getGraphicsContext2D().drawImage(defenceSpots, 742, 215, 105, 105);
        canvas.getGraphicsContext2D().drawImage(defenceSpots, 742, 580, 105, 105);
        canvas.getGraphicsContext2D().drawImage(defenceSpots, 215, 742, 105, 105);
        canvas.getGraphicsContext2D().drawImage(defenceSpots, 580, 742, 105, 105);
        canvas.getGraphicsContext2D().drawImage(defenceSpots, 52, 215, 105, 105);
        canvas.getGraphicsContext2D().drawImage(defenceSpots, 52, 580, 105, 105);
        //Draw buttons for player
        canvas.getGraphicsContext2D().drawImage(buttonMelee, 55, 105, 30, 30);
        canvas.getGraphicsContext2D().drawImage(buttonRanged, 90, 105, 30, 30);
        canvas.getGraphicsContext2D().drawImage(buttonDefence, 125, 90, 30, 30);
        canvas.getGraphicsContext2D().drawImage(buttonResource, 125, 125, 30, 30);

        //Draw selector for buttons
        //Melee button
        if (lastRealMousePosx >= 55 && lastRealMousePosx <= 85
                && lastRealMousePosy >= 105 && lastRealMousePosy <= 135 && meleeCooldown <= 0) {
            canvas.getGraphicsContext2D().drawImage(selector, 55, 105, 30, 30);
        }
        //Ranged button
        if (lastRealMousePosx >= 90 && lastRealMousePosx <= 120
                && lastRealMousePosy >= 105 && lastRealMousePosy <= 135 && rangedCooldown <= 0) {
            canvas.getGraphicsContext2D().drawImage(selector, 90, 105, 30, 30);
        }
        //Defence button
        if (lastRealMousePosx >= 125 && lastRealMousePosx <= 155
                && lastRealMousePosy >= 90 && lastRealMousePosy <= 120 && defenceCooldown <= 0) {
            canvas.getGraphicsContext2D().drawImage(selector, 125, 90, 30, 30);
        }
        //Resource button
        if (lastRealMousePosx >= 125 && lastRealMousePosx <= 155
                && lastRealMousePosy >= 125 && lastRealMousePosy <= 155 && resourceCooldown <= 0) {
            canvas.getGraphicsContext2D().drawImage(selector, 125, 125, 30, 30);
        }
        //Draw selector for selected unit
        if (selectedUnit != null) {
            if (selectedUnit.getUnitType() == UnitType.MELEE) {
                canvas.getGraphicsContext2D().drawImage(selector, 55, 105, 30, 30);
            } else if (selectedUnit.getUnitType() == UnitType.RANGED) {
                canvas.getGraphicsContext2D().drawImage(selector, 90, 105, 30, 30);
            } else if (selectedUnit.getUnitType() == UnitType.DEFENCE) {
                canvas.getGraphicsContext2D().drawImage(selector, 125, 90, 30, 30);
            } else if (selectedUnit.getUnitType() == UnitType.RESOURCE) {
                canvas.getGraphicsContext2D().drawImage(selector, 125, 125, 30, 30);
            }
        }
        //Draw cooldown for units
        if (meleeCooldown > 0) {
            canvas.getGraphicsContext2D().drawImage(cooldown, 55, 105, 30, 30);
        }
        if (rangedCooldown > 0) {
            canvas.getGraphicsContext2D().drawImage(cooldown, 90, 105, 30, 30);
        }
        if (defenceCooldown > 0) {
            canvas.getGraphicsContext2D().drawImage(cooldown, 125, 90, 30, 30);
        }
        if (resourceCooldown > 0) {
            canvas.getGraphicsContext2D().drawImage(cooldown, 125, 125, 30, 30);
        }
        //SpotsLanes when unit is selected
        if (selectedUnit != null) {
            //Lane 0 to 3
            if (lastRealMousePosx >= 215 && lastRealMousePosx <= 320
                    && lastRealMousePosy >= 52 && lastRealMousePosy <= 157) {
                int posX = ((int) lastRealMousePosx - 215) / 27;
                int posY = ((int) lastRealMousePosy - 52) / 27;
                canvas.getGraphicsContext2D().drawImage(selector, 218 + posX * 25, 55 + posY * 25, 25, 25);
            } else //Lane 4 to 7
            if (lastRealMousePosx >= 52 && lastRealMousePosx <= 157
                    && lastRealMousePosy >= 215 && lastRealMousePosy <= 320) {
                int posX = ((int) lastRealMousePosx - 52) / 27;
                int posY = ((int) lastRealMousePosy - 215) / 27;
                canvas.getGraphicsContext2D().drawImage(selector, 55 + posX * 26, 218 + posY * 26, 25, 25);
            }
        }

        //Draw mysterybox and text when mysterybox is available
        if (gameInfo.getMysterybox() != null) {
            canvas.getGraphicsContext2D().drawImage(mysterybox, (canvas.getWidth() - mysterybox.getWidth()) / 2, (canvas.getHeight() - mysterybox.getHeight()) / 2.2);

            //canvas.getGraphicsContext2D().setFill(Color);
            canvas.getGraphicsContext2D().setFont(Font.font(null, FontWeight.BOLD, 12));
            if (gameInfo.getMysterybox().getHigestBidder() != null) {
                canvas.getGraphicsContext2D().fillText("Hoogste bieder: " + gameInfo.getMysterybox().getHigestBidder().getName(), (canvas.getWidth() - mysterybox.getWidth()) / 2, (canvas.getHeight() - mysterybox.getHeight()) / 2.2 + 325);
            }
            canvas.getGraphicsContext2D().fillText("Volgend bod: " + gameInfo.getMysterybox().getNewHighestBid() + " resources", (canvas.getWidth() - mysterybox.getWidth()) / 2, (canvas.getHeight() - mysterybox.getHeight()) / 2.2 + 350);

            if (gameInfo.getMysterybox().getHigestBidder() != null) {
                mysteryboxWinner = "Winnaar mysterybox: " + gameInfo.getMysterybox().getHigestBidder().getName();

                if (gameInfo.getMysterybox().getResourceAmount() != 0) {
                    mysteryboxContent = "Inhoud: " + gameInfo.getMysterybox().getResourceAmount() + " resources";
                } else if (gameInfo.getMysterybox().getUpgrade() != null) {
                    mysteryboxContent = "Inhoud: " + gameInfo.getMysterybox().getUpgrade().toString();
                }
            } else {
                mysteryboxWinner = "";
                mysteryboxContent = "";
            }
        } else {
            canvas.getGraphicsContext2D().setFont(Font.font(null, FontWeight.BOLD, 12));
            canvas.getGraphicsContext2D().fillText(mysteryboxWinner, 250, 595);
            canvas.getGraphicsContext2D().fillText(mysteryboxContent, 250, 620);
        }
    }

    /**
     * Selects a unit to be placed, or a lane to place the selected unit at
     *
     * @param e
     * @throws java.rmi.RemoteException
     */
    public void handleMouseClick(MouseEvent e) throws RemoteException {
        //Change selected unit
        if (lastRealMousePosx >= 55 && lastRealMousePosx <= 85
                && lastRealMousePosy >= 105 && lastRealMousePosy <= 135 && meleeCooldown <= 0) {
            selectedUnit = UnitInfo.getMeleeUnit(gameInfo.getPlayers().get(0));
        } else if (lastRealMousePosx >= 90 && lastRealMousePosx <= 120
                && lastRealMousePosy >= 105 && lastRealMousePosy <= 135 && rangedCooldown <= 0) {
            selectedUnit = UnitInfo.getRangedUnit(gameInfo.getPlayers().get(0));
        } else if (lastRealMousePosx >= 125 && lastRealMousePosx <= 155
                && lastRealMousePosy >= 90 && lastRealMousePosy <= 120 && defenceCooldown <= 0) {
            selectedUnit = UnitInfo.getDefenceUnit(gameInfo.getPlayers().get(0));
        } else if (lastRealMousePosx >= 125 && lastRealMousePosx <= 155
                && lastRealMousePosy >= 125 && lastRealMousePosy <= 155 && defenceCooldown <= 0) {
            selectedUnit = UnitInfo.getResourceUnit(gameInfo.getPlayers().get(0));
        }
        //Check if unit has to be placed
        if (selectedUnit != null) {
            //Place unit
            //Lane 0 to 3
            if (lastRealMousePosx >= 215 && lastRealMousePosx <= 320
                    && lastRealMousePosy >= 52 && lastRealMousePosy <= 157) {
                int posX = ((int) lastRealMousePosx - 215) / 26;
                int posY = ((int) lastRealMousePosy - 52) / 26;
                if (gm.placeUnitAtBaseMulti(gameInfo.getPlayers().get(0).getName(), selectedUnit.getUnit(), posY * 4 + posX, selectedUnit.getCost())) {
                    setCooldown(selectedUnit.getUnitType(), selectedUnit.getCooldown());
                    selectedUnit = null;
                }
            } else //Lane 4 to 7
            if (lastRealMousePosx >= 52 && lastRealMousePosx <= 157
                    && lastRealMousePosy >= 215 && lastRealMousePosy <= 320) {
                int posY = ((int) lastRealMousePosx - 52) / 26;
                int posX = ((int) lastRealMousePosy - 215) / 26;
                if (gm.placeUnitAtBaseMulti(gameInfo.getPlayers().get(0).getName(), selectedUnit.getUnit(), 16 + posY * 4 + posX, selectedUnit.getCost())) {
                    setCooldown(selectedUnit.getUnitType(), selectedUnit.getCooldown());
                    selectedUnit = null;
                }
            }
        }

        //Handle mouseclick on mysterybox when mysterybox is available
        if (lastRealMousePosx >= 325 && lastRealMousePosx <= 700 && lastRealMousePosy >= 325 && lastRealMousePosy <= 700) {
            if (gameInfo.getMysterybox() != null) {
                //gameInfo.getMysterybox().bid(gameInfo.getPlayers().get(0), gameInfo.getMysterybox().getNewHighestBid());
                IPlayer player = gameInfo.getPlayers().get(0);

                gm.bidMysteryboxMulti(player.getName(), gameInfo.getMysterybox().getNewHighestBid());
            }
        }
    }

    /**
     * Draws all the units on the field
     */
    private void drawUnits() {
        Image drawingImage = null;
        Iterator<Unit> i = gameInfo.getLaneUnits();
        while (i.hasNext()) {
            Unit u = i.next();
            double x = 0, y = 0;
            ///////////////////////////Draw for player 0///////////////////////////
            if (u.getOwner() == gameInfo.getPlayers().get(0)) {
                //Check which sprite to use
                if (u instanceof Melee) {
                    if (gameInfo.getPlayers().get(0).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = meleeBlueR;
                    } else {
                        drawingImage = meleeBlueB;
                    }
                } else if (u instanceof Ranged) {
                    if (gameInfo.getPlayers().get(0).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = rangedBlueR;
                    } else {
                        drawingImage = rangedBlueB;
                    }
                }
                //Draw unit
                //Lane 0
                if (u.getLane() == gameInfo.getPlayers().get(0).getBase().getLane(0)) {
                    x = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                    y = 53;
                }
                //Lane 1
                if (u.getLane() == gameInfo.getPlayers().get(0).getBase().getLane(1)) {
                    x = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                    y = 77;
                }
                //Lane 2
                if (u.getLane() == gameInfo.getPlayers().get(0).getBase().getLane(2)) {
                    x = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                    y = 102;
                }
                //Lane 3
                if (u.getLane() == gameInfo.getPlayers().get(0).getBase().getLane(3)) {
                    x = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                    y = 128;
                }
                //Lane 4
                if (u.getLane() == gameInfo.getPlayers().get(0).getBase().getLane(4)) {
                    x = 53;
                    y = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                }
                //Lane 5
                if (u.getLane() == gameInfo.getPlayers().get(0).getBase().getLane(5)) {
                    x = 77;
                    y = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                }
                //Lane 6
                if (u.getLane() == gameInfo.getPlayers().get(0).getBase().getLane(6)) {
                    x = 102;
                    y = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                }
                //Lane 7
                if (u.getLane() == gameInfo.getPlayers().get(0).getBase().getLane(7)) {
                    x = 128;
                    y = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                }
            }
            ///////////////////////////Draw for player 1///////////////////////////
            if (u.getOwner() == gameInfo.getPlayers().get(1)) {
                //Check which sprite to use
                if (u instanceof Melee) {
                    if (gameInfo.getPlayers().get(1).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = meleeGreenB;
                    } else {
                        drawingImage = meleeGreenL;
                    }
                } else if (u instanceof Ranged) {
                    if (gameInfo.getPlayers().get(1).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = rangedGreenB;
                    } else {
                        drawingImage = rangedGreenL;
                    }
                }
                //Draw unit
                //Lane 0
                if (u.getLane() == gameInfo.getPlayers().get(1).getBase().getLane(0)) {
                    x = 743;
                    y = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                }
                //Lane 1
                if (u.getLane() == gameInfo.getPlayers().get(1).getBase().getLane(1)) {
                    x = 768;
                    y = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                }
                //Lane 2
                if (u.getLane() == gameInfo.getPlayers().get(1).getBase().getLane(2)) {
                    x = 793;
                    y = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                }
                //Lane 3
                if (u.getLane() == gameInfo.getPlayers().get(1).getBase().getLane(3)) {
                    x = 818;
                    y = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                }
                //Lane 4
                if (u.getLane() == gameInfo.getPlayers().get(1).getBase().getLane(4)) {
                    x = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                    y = 53;
                }
                //Lane 5
                if (u.getLane() == gameInfo.getPlayers().get(1).getBase().getLane(5)) {
                    x = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                    y = 77;
                }
                //Lane 6
                if (u.getLane() == gameInfo.getPlayers().get(1).getBase().getLane(6)) {
                    x = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                    y = 102;
                }
                //Lane 7
                if (u.getLane() == gameInfo.getPlayers().get(1).getBase().getLane(7)) {
                    x = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                    y = 128;
                }
            }
            ///////////////////////////Draw for player 2///////////////////////////
            if (u.getOwner() == gameInfo.getPlayers().get(2)) {
                //Check which sprite to use
                if (u instanceof Melee) {
                    if (gameInfo.getPlayers().get(2).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = meleePurpleL;
                    } else {
                        drawingImage = meleePurpleT;
                    }
                } else if (u instanceof Ranged) {
                    if (gameInfo.getPlayers().get(2).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = rangedPurpleL;
                    } else {
                        drawingImage = rangedPurpleT;
                    }
                }
                //Draw unit
                //Lane 0
                if (u.getLane() == gameInfo.getPlayers().get(2).getBase().getLane(0)) {
                    x = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                    y = 743;
                }
                //Lane 1
                if (u.getLane() == gameInfo.getPlayers().get(2).getBase().getLane(1)) {
                    x = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                    y = 768;
                }
                //Lane 2
                if (u.getLane() == gameInfo.getPlayers().get(2).getBase().getLane(2)) {
                    x = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                    y = 793;
                }
                //Lane 3
                if (u.getLane() == gameInfo.getPlayers().get(2).getBase().getLane(3)) {
                    x = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                    y = 818;
                }
                //Lane 4
                if (u.getLane() == gameInfo.getPlayers().get(2).getBase().getLane(4)) {
                    x = 743;
                    y = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                }
                //Lane 5
                if (u.getLane() == gameInfo.getPlayers().get(2).getBase().getLane(5)) {
                    x = 768;
                    y = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                }
                //Lane 6
                if (u.getLane() == gameInfo.getPlayers().get(2).getBase().getLane(6)) {
                    x = 793;
                    y = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                }
                //Lane 7
                if (u.getLane() == gameInfo.getPlayers().get(2).getBase().getLane(7)) {
                    x = 818;
                    y = (float) u.getPosition() / (float) 1000 * (float) 470 + (float) 215;
                }
            }
            ///////////////////////////Draw for player 3///////////////////////////
            if (u.getOwner() == gameInfo.getPlayers().get(3)) {
                //Check which sprite to use
                if (u instanceof Melee) {
                    if (gameInfo.getPlayers().get(3).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = meleeRedT;
                    } else {
                        drawingImage = meleeRedR;
                    }
                } else if (u instanceof Ranged) {
                    if (gameInfo.getPlayers().get(3).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = rangedRedT;
                    } else {
                        drawingImage = rangedRedR;
                    }
                }
                //Draw unit
                //Lane 0
                if (u.getLane() == gameInfo.getPlayers().get(3).getBase().getLane(0)) {
                    x = 53;
                    y = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                }
                //Lane 1
                if (u.getLane() == gameInfo.getPlayers().get(3).getBase().getLane(1)) {
                    x = 77;
                    y = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                }
                //Lane 2
                if (u.getLane() == gameInfo.getPlayers().get(3).getBase().getLane(2)) {
                    x = 102;
                    y = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                }
                //Lane 3
                if (u.getLane() == gameInfo.getPlayers().get(3).getBase().getLane(3)) {
                    x = 128;
                    y = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                }
                //Lane 4
                if (u.getLane() == gameInfo.getPlayers().get(3).getBase().getLane(4)) {
                    x = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                    y = 743;
                }
                //Lane 5
                if (u.getLane() == gameInfo.getPlayers().get(3).getBase().getLane(5)) {
                    x = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                    y = 768;
                }
                //Lane 6
                if (u.getLane() == gameInfo.getPlayers().get(3).getBase().getLane(6)) {
                    x = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                    y = 793;
                }
                //Lane 7
                if (u.getLane() == gameInfo.getPlayers().get(3).getBase().getLane(7)) {
                    x = (float) 685 - (float) u.getPosition() / (float) 1000 * (float) 470;
                    y = 818;
                }
            }
            //Draw unit
            canvas.getGraphicsContext2D().drawImage(drawingImage, x, y, 30, 30);
            canvas.getGraphicsContext2D().setFill(Color.RED);
            canvas.getGraphicsContext2D().fillRect(x, y - 5, 30, 4);
            canvas.getGraphicsContext2D().setFill(Color.GREEN);
            canvas.getGraphicsContext2D().fillRect(x, y - 5, 30 - (double) u.getDamage() / (double) u.getHealth() * 30, 4);
            canvas.getGraphicsContext2D().setFill(Color.BLACK);
        }
        ////////////////// Draw defence units //////////////////////
        for (int j = 0; j < 4; j++) {
            IPlayer p = gameInfo.getPlayers().get(j);
            //Set start values
            double x1, y1, x2, y2;
            if (j == 0) {
                x1 = 215;
                y1 = 52;
                x2 = 52;
                y2 = 215;
            } else if (j == 1) {
                x1 = 742;
                y1 = 215;
                x2 = 580;
                y2 = 52;
            } else if (j == 2) {
                x1 = 580;
                y1 = 742;
                x2 = 742;
                y2 = 580;
            } else {
                x1 = 52;
                y1 = 580;
                x2 = 215;
                y2 = 742;
            }
            for (Unit u : p.getBase().getUnits()) {
                //Get the position of the unit
                int index = u.getBase().getUnitIndex(u);
                int posLane = index % 4;
                int laneIndex = index / 4;
                Image image = null;
                if (u instanceof Defence) {
                    image = defenceSide;
                } else {
                    image = resource;
                }

                if (j == 0) {
                    if (laneIndex < 4) {
                        canvas.getGraphicsContext2D().drawImage(image,
                                x1 + posLane * 26,
                                y1 + laneIndex * 26,
                                30,
                                30);
                    } else {
                        laneIndex -= 4;
                        canvas.getGraphicsContext2D().drawImage(image,
                                x2 + laneIndex * 26,
                                y2 + posLane * 26,
                                30,
                                30);
                    }
                } else if (j == 1) {
                    if (laneIndex < 4) {
                        canvas.getGraphicsContext2D().drawImage(image,
                                x1 + laneIndex * 26,
                                y1 + posLane * 26,
                                30,
                                30);
                    } else {
                        laneIndex -= 4;
                        canvas.getGraphicsContext2D().drawImage(image,
                                x2 + 78 - posLane * 26,
                                y2 + laneIndex * 26,
                                30,
                                30);
                    }
                } else if (j == 2) {
                    if (laneIndex < 4) {
                        canvas.getGraphicsContext2D().drawImage(image,
                                x1 + 78 - posLane * 26,
                                y1 + laneIndex * 26,
                                30,
                                30);
                    } else {
                        laneIndex -= 4;
                        canvas.getGraphicsContext2D().drawImage(image,
                                x2 + laneIndex * 26,
                                y2 + 78 - posLane * 26,
                                30,
                                30);
                    }
                } else if (j == 3) {
                    if (laneIndex < 4) {
                        canvas.getGraphicsContext2D().drawImage(image,
                                x1 + laneIndex * 26,
                                y1 + 78 - posLane * 26,
                                30,
                                30);
                    } else {
                        laneIndex -= 4;
                        canvas.getGraphicsContext2D().drawImage(image,
                                x2 + posLane * 26,
                                y2 + laneIndex * 26,
                                30,
                                30);
                    }
                }
            }
        }
    }

    /**
     * Sets the unit's cooldown to the given time
     *
     * @param unittype Unit that has to be cooldown set. Must be not null
     * @param cooldown Time of the cooldown (60 is 1 second). Must be > 0
     */
    private void setCooldown(UnitType unittype, int cooldown) {
        if (unittype == null || cooldown <= 0) {
            return;
        } else {
            if (unittype == UnitType.MELEE) {
                meleeCooldown = cooldown;
            } else if (unittype == UnitType.RANGED) {
                rangedCooldown = cooldown;
            } else if (unittype == UnitType.DEFENCE) {
                defenceCooldown = cooldown;
            } else if (unittype == UnitType.RESOURCE) {
                resourceCooldown = cooldown;
            } else if (unittype == UnitType.ALL) {
                meleeCooldown = cooldown;
                rangedCooldown = cooldown;
                defenceCooldown = cooldown;
                resourceCooldown = cooldown;
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.UI;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import kingofthehill.domain.AI;
import kingofthehill.domain.AIState;
import kingofthehill.domain.GameManager;
import kingofthehill.domain.IPlayer;
import kingofthehill.domain.Melee;
import kingofthehill.domain.Player;
import kingofthehill.domain.Ranged;
import kingofthehill.domain.Unit;
import kingofthehill.unitinfo.UnitInfo;

/**
 * FXML Controller class
 *
 * @author Jur
 */
public class FXMLGameViewController implements Initializable {

    @FXML
    private Canvas canvas;

    //Player unit sprites
    Image meleeBlueR, meleeBlueB, rangedBlueR, rangedBlueB;
    Image meleeGreenL, meleeGreenB, rangedGreenL, rangedGreenB;
    Image meleePurpleT, meleePurpleL, rangedPurpleT, rangedPurpleL;
    Image meleeRedT, meleeRedR, rangedRedT, rangedRedR;
    //Castle sprites
    Image castle1, castle2, castle3, castle4;
    Image castle_destroyed1, castle_destroyed2, castle_destroyed3, castle_destroyed4;
    //Field sprites
    Image dirtField1, dirtField2;
    Image corner1, corner2, corner3, corner4;
    Image side1, side2, side3, side4;
    Image background;
    // Selector sprite
    Image selector;
    //Button sprites
    Image buttonMelee;
    Image buttonRanged;

    GameManager gm;
    AnimationTimer antimer;
    boolean isMouseOnCanvas;
    boolean isMouseClicked = false;

    double scrollPosX, scrollPosY, lastMousePosx, lastMousePosy, lastRealMousePosx, lastRealMousePosy;

    Unit selectedUnit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Create the game
        IPlayer p = new Player("Jur", 10);
        AI a = new AI("ArtificialIntelligence0");
        a.setAIType(AIState.AGRESSIVE);
        gm = new GameManager(p);
        isMouseOnCanvas = false;
        selectedUnit = null;

        //Load all sprites
        meleeBlueR = new Image("kingofthehill/UI/Units/Blue/BlueKnightR.png");
        meleeBlueB = new Image("kingofthehill/UI/Units/Blue/BlueKnightB.png");
        meleeGreenL = new Image("kingofthehill/UI/Units/Green/GreenKnightL.png");
        meleeGreenB = new Image("kingofthehill/UI/Units/Green/GreenKnightB.png");
        meleePurpleL = new Image("kingofthehill/UI/Units/Purple/PurpleKnightL.png");
        meleePurpleT = new Image("kingofthehill/UI/Units/Purple/PurpleKnightT.png");
        meleeRedR = new Image("kingofthehill/UI/Units/Red/RedKnightR.png");
        meleeRedT = new Image("kingofthehill/UI/Units/Red/RedKnightT.png");
        rangedBlueR = new Image("kingofthehill/UI/Units/Blue/BlueArcherR.png");
        rangedBlueB = new Image("kingofthehill/UI/Units/Blue/BlueArcherB.png");
        rangedGreenL = new Image("kingofthehill/UI/Units/Green/GreenArcherL.png");
        rangedGreenB = new Image("kingofthehill/UI/Units/Green/GreenArcherB.png");
        rangedPurpleL = new Image("kingofthehill/UI/Units/Purple/PurpleArcherL.png");
        rangedPurpleT = new Image("kingofthehill/UI/Units/Purple/PurpleArcherT.png");
        rangedRedR = new Image("kingofthehill/UI/Units/Red/RedArcherR.png");
        rangedRedT = new Image("kingofthehill/UI/Units/Red/RedArcherT.png");
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
        buttonMelee = new Image("kingofthehill/UI/field/button-melee.png");
        buttonRanged = new Image("kingofthehill/UI/field/button-ranged.png");
        //Draw field
        drawBackground();
        drawField();

        //Start animation timer
        antimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                // Update JavaFX Scene Graph
                gm.doStep();
                drawBackground();
                drawField();
                drawUnits();

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
                if (gm.getPlayers().get(0).getBase().getHealthPoints() == 0 && gm.getPlayers().get(2).getBase().getHealthPoints() == 0) {
                    canvas.getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
                    drawBackground();
                    drawField();
                    drawUnits();
                    canvas.getGraphicsContext2D().fillText("Team blue won!", 450, 450);
                    this.stop();
                } else if (gm.getPlayers().get(1).getBase().getHealthPoints() == 0 && gm.getPlayers().get(3).getBase().getHealthPoints() == 0) {
                    canvas.getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
                    drawBackground();
                    drawField();
                    drawUnits();
                    canvas.getGraphicsContext2D().fillText("Team red won!", 450, 450);
                    this.stop();
                }
            }

            @Override
            public void start() {
                super.start();
            }
        };
        antimer.start();
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
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
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
        lastRealMousePosx = e.getX() / 1.5 - scrollPosX;
        lastRealMousePosy = e.getY() / 1.5 - scrollPosY;
        System.out.println(lastRealMousePosx + "    " + lastRealMousePosy);
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
        if (gm.getPlayers().get(0).getBase().getHealthPoints() > 0) {
            canvas.getGraphicsContext2D().drawImage(castle1, 20, 20, 300, 300);
        } else {
            canvas.getGraphicsContext2D().drawImage(castle_destroyed1, 20, 20, 300, 300);
        }
        if (gm.getPlayers().get(1).getBase().getHealthPoints() > 0) {
            canvas.getGraphicsContext2D().drawImage(castle2, 580, 20, 300, 300);
        } else {
            canvas.getGraphicsContext2D().drawImage(castle_destroyed2, 580, 20, 300, 300);
        }
        if (gm.getPlayers().get(2).getBase().getHealthPoints() > 0) {
            canvas.getGraphicsContext2D().drawImage(castle3, 580, 580, 300, 300);
        } else {
            canvas.getGraphicsContext2D().drawImage(castle_destroyed3, 580, 580, 300, 300);
        }
        if (gm.getPlayers().get(3).getBase().getHealthPoints() > 0) {
            canvas.getGraphicsContext2D().drawImage(castle4, 20, 580, 300, 300);
        } else {
            canvas.getGraphicsContext2D().drawImage(castle_destroyed4, 20, 580, 300, 300);
        }
        //Draw health castles
        canvas.getGraphicsContext2D().setFill(Color.RED);
        canvas.getGraphicsContext2D().fillRect(100, 75, 100, 5);
        canvas.getGraphicsContext2D().fillRect(705, 75, 100, 5);
        canvas.getGraphicsContext2D().fillRect(705, 680, 100, 5);
        canvas.getGraphicsContext2D().fillRect(100, 680, 100, 5);
        canvas.getGraphicsContext2D().setFill(Color.GREEN);
        canvas.getGraphicsContext2D().fillRect(100, 75, (float) gm.getPlayers().get(0).getBase().getHealthPoints(), 5);
        canvas.getGraphicsContext2D().fillRect(705, 75, (float) gm.getPlayers().get(1).getBase().getHealthPoints(), 5);
        canvas.getGraphicsContext2D().fillRect(705, 680, (float) gm.getPlayers().get(2).getBase().getHealthPoints(), 5);
        canvas.getGraphicsContext2D().fillRect(100, 680, (float) gm.getPlayers().get(3).getBase().getHealthPoints(), 5);
        //Draw name
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().setFont(Font.font(null, FontWeight.BOLD, 12));
        canvas.getGraphicsContext2D().fillText(gm.getPlayers().get(0).getName(), 90, 50);
        canvas.getGraphicsContext2D().fillText(gm.getPlayers().get(1).getName(), 695, 50);
        canvas.getGraphicsContext2D().fillText(gm.getPlayers().get(2).getName(), 695, 860);
        canvas.getGraphicsContext2D().fillText(gm.getPlayers().get(3).getName(), 90, 860);
        //Draw money
        canvas.getGraphicsContext2D().setFill(Color.GOLD);
        canvas.getGraphicsContext2D().fillText("" + gm.getPlayers().get(0).getMoney(), 100, 110);
        canvas.getGraphicsContext2D().fillText("" + gm.getPlayers().get(1).getMoney(), 705, 110);
        canvas.getGraphicsContext2D().fillText("" + gm.getPlayers().get(2).getMoney(), 705, 715);
        canvas.getGraphicsContext2D().fillText("" + gm.getPlayers().get(3).getMoney(), 100, 715);
        //Draw exp
        canvas.getGraphicsContext2D().setFill(Color.AQUA);
        canvas.getGraphicsContext2D().fillRect(100, 85, 100, 5);
        canvas.getGraphicsContext2D().fillRect(705, 85, 100, 5);
        canvas.getGraphicsContext2D().fillRect(705, 690, 100, 5);
        canvas.getGraphicsContext2D().fillRect(100, 690, 100, 5);
        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        canvas.getGraphicsContext2D().fillRect(100, 85, (float) gm.getPlayers().get(0).getExp(), 5);
        canvas.getGraphicsContext2D().fillRect(705, 85, (float) gm.getPlayers().get(1).getExp(), 5);
        canvas.getGraphicsContext2D().fillRect(705, 690, (float) gm.getPlayers().get(2).getExp(), 5);
        canvas.getGraphicsContext2D().fillRect(100, 690, (float) gm.getPlayers().get(3).getExp(), 5);
        //Draw score
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillText("" + gm.getPlayers().get(0).getScore(), 120, 110);
        canvas.getGraphicsContext2D().fillText("" + gm.getPlayers().get(1).getScore(), 725, 110);
        canvas.getGraphicsContext2D().fillText("" + gm.getPlayers().get(2).getScore(), 725, 715);
        canvas.getGraphicsContext2D().fillText("" + gm.getPlayers().get(3).getScore(), 120, 715);

        //Set color back
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().setFont(Font.font(null, FontWeight.NORMAL, 12));
        //Draw lanes
        canvas.getGraphicsContext2D().drawImage(dirtField1, 318, 73, 265, 150);
        canvas.getGraphicsContext2D().drawImage(dirtField1, 318, 679, 265, 150);
        canvas.getGraphicsContext2D().drawImage(dirtField2, 75, 317, 145, 265);
        canvas.getGraphicsContext2D().drawImage(dirtField2, 680, 317, 145, 265);

        //Draw buttons for player
        canvas.getGraphicsContext2D().drawImage(buttonMelee, 100, 150, 30, 30);
        canvas.getGraphicsContext2D().drawImage(buttonRanged, 150, 150, 30, 30);

        //Draw selector for buttons
        //Melee button
        if (lastRealMousePosx >= 100 && lastRealMousePosx <= 130
                && lastRealMousePosy >= 150 && lastRealMousePosy <= 180 || selectedUnit instanceof Melee) {
            canvas.getGraphicsContext2D().drawImage(selector, 100, 150, 30, 30);
        }
        //Ranged button
        if (lastRealMousePosx >= 150 && lastRealMousePosx <= 180
                && lastRealMousePosy >= 150 && lastRealMousePosy <= 180 || selectedUnit instanceof Ranged) {
            canvas.getGraphicsContext2D().drawImage(selector, 150, 150, 30, 30);
        }
        //Lanes when unit is selected
        if (selectedUnit != null) {
            //Lane 0
            if (lastRealMousePosx >= 318 && lastRealMousePosx <= 582
                    && lastRealMousePosy >= 83 && lastRealMousePosy <= 105) {
                canvas.getGraphicsContext2D().drawImage(selector, 318, 83, 264, 22);
            } else //Lane 1
            if (lastRealMousePosx >= 318 && lastRealMousePosx <= 582
                    && lastRealMousePosy >= 115 && lastRealMousePosy <= 142) {
                canvas.getGraphicsContext2D().drawImage(selector, 318, 115, 264, 22);
            } else //Lane 2
            if (lastRealMousePosx >= 318 && lastRealMousePosx <= 582
                    && lastRealMousePosy >= 154 && lastRealMousePosy <= 179) {
                canvas.getGraphicsContext2D().drawImage(selector, 318, 154, 264, 22);
            } else //Lane 3
            if (lastRealMousePosx >= 318 && lastRealMousePosx <= 582
                    && lastRealMousePosy >= 189 && lastRealMousePosy <= 213) {
                canvas.getGraphicsContext2D().drawImage(selector, 318, 189, 264, 22);
            } else //Lane 4
            if (lastRealMousePosx >= 83 && lastRealMousePosx <= 105
                    && lastRealMousePosy >= 318 && lastRealMousePosy <= 582) {
                canvas.getGraphicsContext2D().drawImage(selector, 83, 318, 22, 264);
            } else //Lane 5
            if (lastRealMousePosx >= 115 && lastRealMousePosx <= 142
                    && lastRealMousePosy >= 318 && lastRealMousePosy <= 582) {
                canvas.getGraphicsContext2D().drawImage(selector, 115, 318, 22, 264);
            } else //Lane 6
            if (lastRealMousePosx >= 154 && lastRealMousePosx <= 179
                    && lastRealMousePosy >= 318 && lastRealMousePosy <= 582) {
                canvas.getGraphicsContext2D().drawImage(selector, 154, 318, 22, 264);
            } else //Lane 7
            if (lastRealMousePosx >= 189 && lastRealMousePosx <= 213
                    && lastRealMousePosy >= 318 && lastRealMousePosy <= 582) {
                canvas.getGraphicsContext2D().drawImage(selector, 189, 318, 22, 264);
            }
        }

    }

    /**
     * Selects a unit to be placed, or a lane to place the selected unit at
     *
     * @param e
     */
    public void handleMouseClick(MouseEvent e) {
        if (selectedUnit == null) {
            //Select unit
            if (lastRealMousePosx >= 100 && lastRealMousePosx <= 130
                    && lastRealMousePosy >= 150 && lastRealMousePosy <= 180) {
                selectedUnit = UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit();
            } else if (lastRealMousePosx >= 150 && lastRealMousePosx <= 180
                    && lastRealMousePosy >= 150 && lastRealMousePosy <= 180 || selectedUnit instanceof Ranged) {
                selectedUnit = UnitInfo.getRangeUnit(gm.getPlayers().get(0)).getUnit();
            }
        } else {
            //Place unit
            //Lane 0
            if (lastRealMousePosx >= 318 && lastRealMousePosx <= 582
                    && lastRealMousePosy >= 83 && lastRealMousePosy <= 105) {
                gm.placeUnitAtLane(gm.getPlayers().get(0), selectedUnit, 0, selectedUnit.getCost());
                selectedUnit = null;
            } else //Lane 1
            if (lastRealMousePosx >= 318 && lastRealMousePosx <= 582
                    && lastRealMousePosy >= 115 && lastRealMousePosy <= 142) {
                gm.placeUnitAtLane(gm.getPlayers().get(0), selectedUnit, 1, selectedUnit.getCost());
                selectedUnit = null;
            } else //Lane 2
            if (lastRealMousePosx >= 318 && lastRealMousePosx <= 582
                    && lastRealMousePosy >= 154 && lastRealMousePosy <= 179) {
                gm.placeUnitAtLane(gm.getPlayers().get(0), selectedUnit, 2, selectedUnit.getCost());
                selectedUnit = null;
            } else //Lane 3
            if (lastRealMousePosx >= 318 && lastRealMousePosx <= 582
                    && lastRealMousePosy >= 189 && lastRealMousePosy <= 213) {
                gm.placeUnitAtLane(gm.getPlayers().get(0), selectedUnit, 1, selectedUnit.getCost());
                selectedUnit = null;
            } else //Lane 4
            if (lastRealMousePosx >= 83 && lastRealMousePosx <= 105
                    && lastRealMousePosy >= 318 && lastRealMousePosy <= 582) {
                gm.placeUnitAtLane(gm.getPlayers().get(0), selectedUnit, 4, selectedUnit.getCost());
                selectedUnit = null;
            } else //Lane 5
            if (lastRealMousePosx >= 115 && lastRealMousePosx <= 142
                    && lastRealMousePosy >= 318 && lastRealMousePosy <= 582) {
                gm.placeUnitAtLane(gm.getPlayers().get(0), selectedUnit, 5, selectedUnit.getCost());
                selectedUnit = null;
            } else //Lane 6
            if (lastRealMousePosx >= 154 && lastRealMousePosx <= 179
                    && lastRealMousePosy >= 318 && lastRealMousePosy <= 582) {
                gm.placeUnitAtLane(gm.getPlayers().get(0), selectedUnit, 6, selectedUnit.getCost());
                selectedUnit = null;
            } else //Lane 7
            if (lastRealMousePosx >= 189 && lastRealMousePosx <= 213
                    && lastRealMousePosy >= 318 && lastRealMousePosy <= 582) {
                gm.placeUnitAtLane(gm.getPlayers().get(0), selectedUnit, 7, selectedUnit.getCost());
                selectedUnit = null;
            }
        }
    }

    /**
     * Draws all the units on the field
     */
    private void drawUnits() {
        Image drawingImage = null;
        Iterator<Unit> i = gm.getUnits();
        while (i.hasNext()) {
            Unit u = i.next();
            ///////////////////////////Draw for player 0///////////////////////////
            if (u.getOwner() == gm.getPlayers().get(0)) {
                //Check which sprite to use
                if (u instanceof Melee) {
                    if (gm.getPlayers().get(0).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = meleeBlueR;
                    } else {
                        drawingImage = meleeBlueB;
                    }
                } else if (u instanceof Ranged) {
                    if (gm.getPlayers().get(0).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = rangedBlueR;
                    } else {
                        drawingImage = rangedBlueB;
                    }
                }
                //Draw unit
                //Lane 0
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(0)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 74, 40, 40);
                }
                //Lane 1
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(1)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 109, 40, 40);
                }
                //Lane 2
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(2)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 146, 40, 40);
                }
                //Lane 3
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(3)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 186, 40, 40);
                }
                //Lane 4
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(4)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 74, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 5
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(5)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 105, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 6
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(6)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 144, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 7
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(7)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 179, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
            }
            ///////////////////////////Draw for player 1///////////////////////////
            if (u.getOwner() == gm.getPlayers().get(1)) {
                //Check which sprite to use
                if (u instanceof Melee) {
                    if (gm.getPlayers().get(1).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = meleeGreenB;
                    } else {
                        drawingImage = meleeGreenL;
                    }
                } else if (u instanceof Ranged) {
                    if (gm.getPlayers().get(1).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = rangedGreenB;
                    } else {
                        drawingImage = rangedGreenL;
                    }
                }
                //Draw unit
                //Lane 0
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(0)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 679, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 1
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(1)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 711, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 2
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(2)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 749, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 3
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(3)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 782, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 4
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(4)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 74, 40, 40);
                }
                //Lane 5
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(5)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 108, 40, 40);
                }
                //Lane 6
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(6)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 146, 40, 40);
                }
                //Lane 7
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(7)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 181, 40, 40);
                }
            }
            ///////////////////////////Draw for player 2///////////////////////////
            if (u.getOwner() == gm.getPlayers().get(2)) {
                //Check which sprite to use
                if (u instanceof Melee) {
                    if (gm.getPlayers().get(2).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = meleePurpleL;
                    } else {
                        drawingImage = meleePurpleT;
                    }
                } else if (u instanceof Ranged) {
                    if (gm.getPlayers().get(2).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = rangedPurpleL;
                    } else {
                        drawingImage = rangedPurpleT;
                    }
                }
                //Draw unit
                //Lane 0
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(0)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 679, 40, 40);
                }
                //Lane 1
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(1)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 711, 40, 40);
                }
                //Lane 2
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(2)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 749, 40, 40);
                }
                //Lane 3
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(3)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 782, 40, 40);
                }
                //Lane 4
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(4)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 679, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 5
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(5)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 711, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 6
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(6)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 749, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 7
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(7)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 782, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
            }
            ///////////////////////////Draw for player 3///////////////////////////
            if (u.getOwner() == gm.getPlayers().get(3)) {
                //Check which sprite to use
                if (u instanceof Melee) {
                    if (gm.getPlayers().get(3).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = meleeRedT;
                    } else {
                        drawingImage = meleeRedR;
                    }
                } else if (u instanceof Ranged) {
                    if (gm.getPlayers().get(3).getBase().getLanes().indexOf(u.getLane()) < 4) {
                        drawingImage = rangedRedT;
                    } else {
                        drawingImage = rangedRedR;
                    }
                }
                //Draw unit
                //Lane 0
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(0)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 73, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 1
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(1)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 105, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 2
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(2)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 144, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 3
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(3)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, 179, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 4
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(4)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 679, 40, 40);
                }
                //Lane 5
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(5)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 711, 40, 40);
                }
                //Lane 6
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(6)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 749, 40, 40);
                }
                //Lane 7
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(7)) {
                    canvas.getGraphicsContext2D().drawImage(drawingImage, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 782, 40, 40);
                }
            }
        }
    }
}

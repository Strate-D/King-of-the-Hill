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

    //Player 1 unit sprites
    Image unitBlueR, unitBlueB;
    //Player 2 unit sprites
    Image unitGreenL, unitGreenB;
    //Castle sprites
    Image castle1, castle2, castle3, castle4;
    Image castle_destroyed1, castle_destroyed2, castle_destroyed3, castle_destroyed4;
    //Field sprites
    Image dirtField1, dirtField2;
    Image corner1, corner2, corner3, corner4;
    Image side1, side2, side3, side4;
    Image background;
    GameManager gm;
    AnimationTimer antimer;

    double scrollPosX, scrollPosY, lastMousePosx, lastMousePosy;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Create the game
        //Player p = new Player("Jur", 9001);
        AI a = new AI("ArtificialIntelligence0");
        a.setAIType(AIState.AGRESSIVE);
        gm = new GameManager(a);

        //Load all sprites
        unitBlueR = new Image("kingofthehill/UI/Units/Blue/BlueKnightR.png");
        unitBlueB = new Image("kingofthehill/UI/Units/Blue/BlueKnightB.png");
        unitGreenL = new Image("kingofthehill/UI/Units/Green/GreenKnightL.png");
        unitGreenB = new Image("kingofthehill/UI/Units/Green/GreenKnightB.png");
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
                if (lastMousePosx > 800) {
                    if (scrollPosX < -446) {
                        scrollPosX = -450;
                    } else {
                        scrollPosX = scrollPosX - 4;
                    }
                } else if (lastMousePosx < 100) {
                    if (scrollPosX > -4) {
                        scrollPosX = 0;
                    } else {
                        scrollPosX = scrollPosX + 4;
                    }
                }
                if (lastMousePosy > 800) {
                    if (scrollPosY < -446) {
                        scrollPosY = -450;
                    } else {
                        scrollPosY = scrollPosY - 4;
                    }
                } else if (lastMousePosy < 100) {
                    if (scrollPosY > -4) {
                        scrollPosY = 0;
                    } else {
                        scrollPosY = scrollPosY + 4;
                    }
                }
                canvas.getGraphicsContext2D().setTransform(1.5, 0, 0, 1.5, scrollPosX, scrollPosY);

                //Check if game ended
                if (gm.getPlayers().get(0).getBase().getHealthPoints() == 0 && gm.getPlayers().get(2).getBase().getHealthPoints() == 0) {
                    canvas.getGraphicsContext2D().fillText("Team blue won!", 450, 450);
                    this.stop();
                } else if (gm.getPlayers().get(1).getBase().getHealthPoints() == 0 && gm.getPlayers().get(3).getBase().getHealthPoints() == 0) {
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

    public void handleMouseOver(MouseEvent e) {
        System.out.println("Mouse over! " + e.getX() + e.getY());
        lastMousePosx = e.getX();
        lastMousePosy = e.getY();
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

    }

    /**
     * Gives the coordinates where the mouse is clicked
     *
     * @param e
     */
    public void giveCoordinates(MouseEvent e) {
        long start = System.currentTimeMillis();
        drawBackground();
        drawField();
        canvas.getGraphicsContext2D().fillText("Drawing time: " + (System.currentTimeMillis() - start) + "milliseconds", 460, 460);
        canvas.getGraphicsContext2D().fillText("x: " + e.getX() + " y: " + e.getY(), 450, 450);
    }

    /**
     * Draws all the units on the field
     */
    private void drawUnits() {
        Iterator<Unit> i = gm.getUnits();
        while (i.hasNext()) {
            Unit u = i.next();
            ///////////////////////////Draw for player 0///////////////////////////
            //Lane 0
            if (u.getOwner() == gm.getPlayers().get(0)) {
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(0)) {
                    canvas.getGraphicsContext2D().drawImage(unitBlueR, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 74, 40, 40);
                }
                //Lane 1
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(1)) {
                    canvas.getGraphicsContext2D().drawImage(unitBlueR, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 109, 40, 40);
                }
                //Lane 2
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(2)) {
                    canvas.getGraphicsContext2D().drawImage(unitBlueR, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 146, 40, 40);
                }
                //Lane 3
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(3)) {
                    canvas.getGraphicsContext2D().drawImage(unitBlueR, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 186, 40, 40);
                }
                //Lane 4
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(4)) {
                    canvas.getGraphicsContext2D().drawImage(unitBlueB, 74, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 5
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(5)) {
                    canvas.getGraphicsContext2D().drawImage(unitBlueB, 105, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 6
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(6)) {
                    canvas.getGraphicsContext2D().drawImage(unitBlueB, 144, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
                //Lane 7
                if (u.getLane() == gm.getPlayers().get(0).getBase().getLane(7)) {
                    canvas.getGraphicsContext2D().drawImage(unitBlueB, 179, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 40, 40);
                }
            }
            ///////////////////////////Draw for player 1///////////////////////////
            //Lane 0
            if (u.getOwner() == gm.getPlayers().get(1)) {
                //Set color
                canvas.getGraphicsContext2D().setFill(Color.BLUE);
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(0)) {
                    canvas.getGraphicsContext2D().drawImage(unitGreenB, 679, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 1
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(1)) {
                    canvas.getGraphicsContext2D().drawImage(unitGreenB, 711, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 2
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(2)) {
                    canvas.getGraphicsContext2D().drawImage(unitGreenB, 749, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 3
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(3)) {
                    canvas.getGraphicsContext2D().drawImage(unitGreenB, 782, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 40, 40);
                }
                //Lane 4
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(4)) {
                    canvas.getGraphicsContext2D().drawImage(unitGreenL, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 74, 40, 40);
                }
                //Lane 5
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(5)) {
                    canvas.getGraphicsContext2D().drawImage(unitGreenL, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 108, 40, 40);
                }
                //Lane 6
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(6)) {
                    canvas.getGraphicsContext2D().drawImage(unitGreenL, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 146, 40, 40);
                }
                //Lane 7
                if (u.getLane() == gm.getPlayers().get(1).getBase().getLane(7)) {
                    canvas.getGraphicsContext2D().drawImage(unitGreenL, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 181, 40, 40);
                }
            }
            ///////////////////////////Draw for player 2///////////////////////////
            //Lane 0
            if (u.getOwner() == gm.getPlayers().get(2)) {
                //Set color
                canvas.getGraphicsContext2D().setFill(Color.RED);
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(0)) {
                    canvas.getGraphicsContext2D().fillOval((float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 689, 20, 20);
                }
                //Lane 1
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(1)) {
                    canvas.getGraphicsContext2D().fillOval((float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 721, 20, 20);
                }
                //Lane 2
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(2)) {
                    canvas.getGraphicsContext2D().fillOval((float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 759, 20, 20);
                }
                //Lane 3
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(3)) {
                    canvas.getGraphicsContext2D().fillOval((float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 792, 20, 20);
                }
                //Lane 4
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(4)) {
                    canvas.getGraphicsContext2D().fillOval(689, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 20, 20);
                }
                //Lane 5
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(5)) {
                    canvas.getGraphicsContext2D().fillOval(721, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 20, 20);
                }
                //Lane 6
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(6)) {
                    canvas.getGraphicsContext2D().fillOval(759, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 20, 20);
                }
                //Lane 7
                if (u.getLane() == gm.getPlayers().get(2).getBase().getLane(7)) {
                    canvas.getGraphicsContext2D().fillOval(792, (float) u.getPosition() / (float) 100 * (float) 262 + (float) 308, 20, 20);
                }
                //Set color
                canvas.getGraphicsContext2D().setFill(Color.BLACK);
            }
            ///////////////////////////Draw for player 3///////////////////////////
            //Lane 0
            if (u.getOwner() == gm.getPlayers().get(3)) {
                //Set color
                canvas.getGraphicsContext2D().setFill(Color.BLUE);
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(0)) {
                    canvas.getGraphicsContext2D().fillOval(83, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 20, 20);
                }
                //Lane 1
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(1)) {
                    canvas.getGraphicsContext2D().fillOval(115, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 20, 20);
                }
                //Lane 2
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(2)) {
                    canvas.getGraphicsContext2D().fillOval(154, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 20, 20);
                }
                //Lane 3
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(3)) {
                    canvas.getGraphicsContext2D().fillOval(189, (float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 20, 20);
                }
                //Lane 4
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(4)) {
                    canvas.getGraphicsContext2D().fillOval((float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 689, 20, 20);
                }
                //Lane 5
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(5)) {
                    canvas.getGraphicsContext2D().fillOval((float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 721, 20, 20);
                }
                //Lane 6
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(6)) {
                    canvas.getGraphicsContext2D().fillOval((float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 759, 20, 20);
                }
                //Lane 7
                if (u.getLane() == gm.getPlayers().get(3).getBase().getLane(7)) {
                    canvas.getGraphicsContext2D().fillOval((float) 570 - (float) u.getPosition() / (float) 100 * (float) 262, 792, 20, 20);
                }
                //Set color
                canvas.getGraphicsContext2D().setFill(Color.BLACK);
            }
        }
    }

    /**
     * Test all drawing by placing units in all lanes
     */
    public void placeTestUnit0() {
        gm.placeUnitAtLane(gm.getPlayers().get(0),
                UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit(), 0, 1);
    }

    public void placeTestUnit1() {
        gm.placeUnitAtLane(gm.getPlayers().get(0),
                UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit(), 1, 1);
    }

    public void placeTestUnit2() {
        gm.placeUnitAtLane(gm.getPlayers().get(0),
                UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit(), 2, 1);
    }

    public void placeTestUnit3() {
        gm.placeUnitAtLane(gm.getPlayers().get(0),
                UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit(), 3, 1);
    }

    public void placeTestUnit4() {
        gm.placeUnitAtLane(gm.getPlayers().get(0),
                UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit(), 4, 1);
    }

    public void placeTestUnit5() {
        gm.placeUnitAtLane(gm.getPlayers().get(0),
                UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit(), 5, 1);
    }

    public void placeTestUnit6() {
        gm.placeUnitAtLane(gm.getPlayers().get(0),
                UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit(), 6, 1);
    }

    public void placeTestUnit7() {
        gm.placeUnitAtLane(gm.getPlayers().get(0),
                UnitInfo.getMeleeUnit(gm.getPlayers().get(0)).getUnit(), 7, 1);
    }
}

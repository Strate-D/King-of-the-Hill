/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.UI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Jur
 */
public class FXMLGameViewController implements Initializable {
    
    @FXML
    private Canvas canvas;
    
    Image castle1, castle2, castle3, castle4;
    Image dirtField1, dirtField2;
    Image corner1, corner2, corner3, corner4;
    Image side1, side2, side3, side4;
    Image background;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Load all sprites
        castle1 = new Image("kingofthehill/UI/field/castle1.png");
        castle2 = new Image("kingofthehill/UI/field/castle2.png");
        castle3 = new Image("kingofthehill/UI/field/castle3.png");
        castle4 = new Image("kingofthehill/UI/field/castle4.png");
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
    }    
    
    /**
     * Go back to the main menu when the quit button is pressed
     * @param e 
     */
    public void handleQuitButton(ActionEvent e){
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
    private void drawField(){
        //Draw castles
        canvas.getGraphicsContext2D().drawImage(castle1, 20, 20, 300, 300);
        canvas.getGraphicsContext2D().drawImage(castle2, 580, 20, 300, 300);
        canvas.getGraphicsContext2D().drawImage(castle3, 580, 580, 300, 300);
        canvas.getGraphicsContext2D().drawImage(castle4, 20, 580, 300, 300);
        //Draw lanes
        canvas.getGraphicsContext2D().drawImage(dirtField1, 318, 73, 265, 150);
        canvas.getGraphicsContext2D().drawImage(dirtField1, 318, 679, 265, 150);
        canvas.getGraphicsContext2D().drawImage(dirtField2, 75, 317, 145, 265);
        canvas.getGraphicsContext2D().drawImage(dirtField2, 680, 317, 145, 265);
    }
    
    /**
     * Gives the coordinates where the mouse is clicked
     * @param e 
     */
    public void giveCoordinates(MouseEvent e) {
        drawBackground();
        drawField();
        canvas.getGraphicsContext2D().fillText("x: " + e.getX() + " y: " + e.getY(), 450, 450);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.UI;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jur
 */
public class King_of_the_Hill extends Application {
    public static Stage currentStage;
    public static ApplicationContext context;

    @Override
    public void start(Stage stage) throws IOException {
        currentStage = stage;
        context = new ApplicationContext();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

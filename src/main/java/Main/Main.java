package Main;

import GUI.GUIController;
import Logging.MyLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;

public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MyLogger.log(Level.DEBUG, "Starting GUI");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainWindow.fxml"));
        Parent root = (Parent) loader.load();
        stage.getIcons().add(new Image("/Pictures/logo.jpg"));
        stage.setTitle("WordToEA_Converter");
        stage.setScene(new Scene(root, 400, 250));
        stage.show();

        GUIController controller = (GUIController) loader.getController();
        controller.setStage(stage);
        MyLogger.log(Level.DEBUG, "GUI running..");
    }
}

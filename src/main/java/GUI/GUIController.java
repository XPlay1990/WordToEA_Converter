/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package GUI;

import Convert.Converter;
import Logging.MyLogger;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;

/**
 * FXML Controller class
 *
 * @author jan.adamczyk
 */
public class GUIController implements Initializable {

    private Stage stage;
    private final File file = new File("C:\\Users\\jan.adamczyk\\Documents\\NetBeansProjects\\WordToEA_Converter\\src\\main\\resources\\REQ\\req.docx");

    /**
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     *
     */
    @FXML
    public void selectFile() {
        MyLogger.log(Level.DEBUG, "Choosing File");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("Word files (*.docx)", "*.docx");
        fileChooser.getExtensionFilters().add(extentionFilter);
        File defaultDirectory = new File("./");
        fileChooser.setInitialDirectory(defaultDirectory);
        fileChooser.showOpenDialog(stage);

        MyLogger.log(Level.DEBUG, "File chosen: " + file);
    }

    /**
     *
     */
    @FXML
    public void convert() {
        MyLogger.log(Level.DEBUG, "Start converting File: " + file);
        try {
            Converter converter = new Converter(file);
            converter.convert();
            showAlert(AlertType.INFORMATION, "Converting finished!");
        } catch (IOException | OpenXML4JException ex) {
            MyLogger.log(Level.ERROR, ExceptionUtils.getStackTrace(ex));
            showAlert(AlertType.ERROR, ex.getMessage());
        }
        MyLogger.log(Level.DEBUG, "Finished converting");

    }

    private void showAlert(AlertType type, String text) {
        Alert alert = new Alert(type);
        alert.initOwner(stage);
        alert.setTitle("Task finished");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}

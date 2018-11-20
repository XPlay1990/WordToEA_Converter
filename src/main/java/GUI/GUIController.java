/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package GUI;

import Convert.Converter;
import Logging.MyLogger;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;

/**
 * FXML Controller class
 *
 * @author jan.adamczyk
 */
public class GUIController implements Initializable {

    private Stage stage;
    private File file = new File("C:\\Users\\jan.adamczyk\\Documents\\NetBeansProjects\\WordToEA_Converter\\src\\main\\resources\\REQ\\req.docx");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

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

    @FXML
    public void convert() {
        MyLogger.log(Level.DEBUG, "Start converting");

        Converter converter = new Converter(file);
        converter.convert();

        MyLogger.log(Level.DEBUG, "Finished converting");
    }
}

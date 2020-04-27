package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.MainApp;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsViewController {

    @FXML
    public Button refreshButton;
    public ComboBox<String> locationMenu;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private MainApp mainApp;
    public AnchorPane settingsViewController;

    /**
     * List refresh
     *
     * @param actionEvent
     */
    @FXML
    public void Refresh(ActionEvent actionEvent) {
        mainApp.updateRestaurantsFromDb();
        mainApp.getMainViewControl().updateMainView(mainApp.getRestaurants());
    }

    /*
     * Change Buttons for  language selection
     */
    @FXML
    public void changeFI() {
        updateBundle("fi-FI");
    }

    @FXML
    public void changeENG() {
        updateBundle("en-EN");
    }

    @FXML
    public void changeSWE() {
        updateBundle("se-SE");
    }

    private void updateBundle(String locale) {
        mainApp.setBundle(ResourceBundle.getBundle("TextResources", Locale.forLanguageTag(locale)));
        try (OutputStream output = new FileOutputStream("./src/main/resources/TextResources_default.properties")) {
            Properties prop = new Properties();
            // set the properties value
            prop.setProperty("Default", locale);
            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    /**
     *  set the location for map when program starts
     */
    @FXML
    private void mapLocation() {
        String city = this.locationMenu.getValue();
        String value = mainApp.getCityBundle().getString(city);
        try (OutputStream output = new FileOutputStream("./src/main/resources/Location_default.properties")) {
            Properties prop = new Properties();
            // set the properties value
            prop.setProperty("Default", value);
            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void setTexts(ResourceBundle bundle) {
        refreshButton.setText(bundle.getString("refreshbutton"));
    }
}

package view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import main.MainApp;

import java.util.ResourceBundle;

/**
 * OptionsBarController controls connection and help functions, which are used by connection button and help button.
 */
public class OptionsBarController {

    /**
     * RestaurantsDAO is the database service class Connect and Help buttons.
     */
    private MainApp mainApp;

    @FXML
    private Button helpButton;
    @FXML
    private Button settingsButton;

    /**
     * Used to give a reference to the mainApp for this controller.
     * Should be done after controller initialisation, before using any of its functions.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void handleSettingsButton(ActionEvent actionEvent) {
        mainApp.sidebarOff();
        if (MainApp.MAIN_SCREEN.getCenter() == MainApp.VIEW_MAIN) {
            mainApp.loadMainView(MainApp.VIEW_SETTINGS);
        } else mainApp.loadMainView(MainApp.VIEW_MAIN);
    }

    @FXML
    public void handleHelpButton() {
        popupInfo(mainApp.getBundle().getString("help"), mainApp.getBundle().getString("helpTitle"));
    }

    /**
     * Pop-up template for help/information dialogs
     *
     * @param text
     * @param title
     */
    private void popupInfo(String text, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
    }

    public void setTexts(ResourceBundle bundle) {
        helpButton.setText(bundle.getString("helpbutton"));
        settingsButton.setText(bundle.getString("settings"));
    }
}

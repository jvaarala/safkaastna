package view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import main.MainApp;

import java.util.ResourceBundle;

/**
 * OptionsBarController controls connection and help functions, which are used by connection button and help button.
 */
public class OptionsBarViewController {

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
        if (MainApp.main_screen.getCenter() == MainApp.view_main) {
            mainApp.loadMainView(MainApp.view_settings);
        } else mainApp.loadMainView(MainApp.view_main);
    }

    @FXML
    public void handleHelpButton() {
        popupInfo(mainApp.getTextResourcesBundle().getString("help"), mainApp.getTextResourcesBundle().getString("helpTitle"));
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

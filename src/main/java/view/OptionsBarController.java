package view;


import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.MainApp;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * OptionsBarController controls connection and help functions, which are used by connection button and help button.
 */
public class OptionsBarController {

    /**
     * RestaurantsDAO is the database service class Connect and Help buttons.
     */
    private MainApp mainApp;

    /**
     *  set the location for map when program starts
     */
    @FXML
    private void mapLocation() {

    }

    /**
     * Used to give a reference to the mainApp for this controller.
     * Should be done after controller initialisation, before using any of its functions.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Pop-up template for help/information dialogs
     *
     * @param text
     * @param title
     */
    private void popupInfo(String text, String title) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
    }


    /**
     * List refresh
     * @param actionEvent
     */
    @FXML
    public void Refresh(ActionEvent actionEvent) {
        mainApp.updateRestaurantsFromDb();
        mainApp.getMainViewControl().updateView(mainApp.getRestaurants());

    }

    /**
     * Change Buttons for  language selection
     */
    @FXML
    public void changeFI() {
        updateBundle("fi-FI");
        mainApp.updateAllViews();
    }

    @FXML
    public void changeENG() {
        updateBundle("en-EN");
        mainApp.updateAllViews();
    }

    @FXML
    public void changeSWE() {
        updateBundle("se-SE");
        mainApp.updateAllViews();
    }

    public void updateBundle(String locale) {
        mainApp.setBundle(ResourceBundle.getBundle("TextResources", Locale.forLanguageTag(locale)));
    }

    @FXML
    public void Help() {
    }
}

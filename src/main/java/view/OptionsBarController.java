package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.MainApp;

/**
 * OptionsBarController controls connection and help functions, which are used by connection button and help button.
 */
public class OptionsBarController {


    private MainApp mainApp;

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
     * Help button function that pops help dialog with instructions.
     */
    @FXML
    public void Help() {
        String helpText = "SafkaaSTNA is a Java Desktop application desingned for students searching for student restaurants. \n"
                + " \n"
                + "With the this appliaction you can:\n"
                + "- See all available student restaurants in Finland:\n"
                + "		- Default view on app start\n"
                + "- Search for restaurants near your or any address\n"
                + "		- Turn \"Restaurant Filter\" off (default)\n"
                + "		- Write address, then press \"Find\" \n"
                + "- Search for restaurants by name \n"
                + "		- Turn \"Restaurant Filter\" on\n"
                + "		- Write restaurant name\n"
                + "- See additional information on each restaurant \n"
                + "		- Click a restaurants red marker";
        popupInfo(helpText, "SafkaaSTNA Help");
    }

    @FXML
    public void Refresh(ActionEvent actionEvent) {
        mainApp.updateRestaurantsFromDb();
        mainApp.getMainViewControl().updateView(mainApp.getRestaurants());
    }
}

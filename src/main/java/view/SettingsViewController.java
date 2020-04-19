package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.MainApp;

import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsViewController {

    @FXML
    public Button refreshButton;

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
    }

    public void mapLocation(MouseEvent mouseEvent) {
    }

    public void setTexts(ResourceBundle bundle) {
        refreshButton.setText(bundle.getString("refreshbutton"));
    }
}

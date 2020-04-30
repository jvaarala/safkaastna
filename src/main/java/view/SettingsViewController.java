package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import main.MainApp;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsViewController {

    @FXML
    public Button refreshButton;
    public ComboBox<String> locationMenu;
    public Button saveSettingsButton;
    public Text selectLangText;
    public Text selectDefLocText;
    public Text refreshRestText;
    @FXML
    public ImageView closeIcon;

    private MainApp mainApp;
    public AnchorPane settingsView;

    private String locale;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setLocationMenuItems(List<String> cityNames) {

        ObservableList<String> cities = FXCollections.observableArrayList();
        for (String city : cityNames) {
            cities.add(city);
        }
        locationMenu.setItems(cities);
        locationMenu.getSelectionModel().select(mainApp.getDefaultCityName());
    }

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
        this.locale = "fi-FI";
    }

    @FXML
    public void changeENG() {
        this.locale = "en-EN";
    }

    @FXML
    public void changeSWE() {
        this.locale = "se-SE";
    }

    public void setTexts(ResourceBundle bundle) {
        refreshButton.setText(bundle.getString("refreshbutton"));
        saveSettingsButton.setText(bundle.getString("saveandapplysettings"));
        selectLangText.setText(bundle.getString("selectLangText"));
        selectDefLocText.setText(bundle.getString("selectDefLocText"));
        refreshRestText.setText(bundle.getString("refreshRestText"));
    }

    public void saveSettings(ActionEvent actionEvent) {
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

        String city = this.locationMenu.getValue();
        System.out.println("city = " + city);
        String value = mainApp.getCityBundle().getString(city);
        try (OutputStream output = new FileOutputStream("./src/main/resources/Location_default.properties")) {
            Properties prop = new Properties();
            // set the properties value
            prop.setProperty("Default", value);
            prop.setProperty("cityName", city);
            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    public void closeSettings(MouseEvent mouseEvent) {
        mainApp.loadMainView(MainApp.VIEW_MAIN);
    }
}

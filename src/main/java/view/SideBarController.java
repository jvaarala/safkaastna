package view;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import main.MainApp;
import model.Restaurant;


public class SideBarController {

    private MainApp mainApp;

    @FXML private Text restaurantName;

    /**
     * Used to give a reference to the mainApp for this controller.
     * Should be done after controller initialisation, before using any of its functions.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) { this.mainApp = mainApp; }

    public void showRestaurantInfo(Restaurant restaurant) {
        this.restaurantName.setText(restaurant.getName());
    }
}

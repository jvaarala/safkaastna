package view;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import main.MainApp;
import model.Restaurant;


public class SideBarController {

    private MainApp mainApp;

    @FXML
    private Text restaurantName;
    @FXML
    private Text restaurantAddr;
    @FXML
    private Hyperlink restaurantUrl;

    /**
     * Used to give a reference to the mainApp for this controller.
     * Should be done after controller initialisation, before using any of its functions.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void showRestaurantInfo(Restaurant restaurant) {
        this.restaurantName.setText(restaurant.getName());
        this.restaurantName.setVisible(true);

        this.restaurantAddr.setText(restaurant.getAddress() + ", " + restaurant.getPostal_code() + " " + restaurant.getCity());
        this.restaurantAddr.setVisible(true);

        // if there is a url address set it to open in default web browser
        if (restaurant.getWww().contentEquals("")) {
            restaurantUrl.setVisible(false);
        } else {
            restaurantUrl.setVisible(true);
            this.restaurantUrl.setOnAction(event -> {
                if (!java.awt.Desktop.isDesktopSupported()) {
                    System.err.println("Desktop is not supported (fatal)");
                    System.exit(1);
                }

                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

                if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    System.err.println("Desktop doesn't support the browse action (fatal)");
                    System.exit(1);
                }

                try {
                    java.net.URI uri = new java.net.URI(restaurant.getWww());
                    desktop.browse(uri);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            });
        }
    }
}


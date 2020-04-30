package view;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.MainApp;
import model.Restaurant;
import model.SearchLogic;

import java.util.ResourceBundle;


public class SideBarController {

    @FXML
    public ImageView closeIcon;
    @FXML
    private ImageView headerIcon;
    @FXML
    private Text headerText;
    @FXML
    private ImageView topParagraphIcon;
    @FXML
    private Text topParagraph;
    @FXML
    private ImageView restaurantUrlIcon;
    @FXML
    private Hyperlink bottomParagraph;
    @FXML
    private Text userLocationText;
    @FXML
    private Text distanceToRestaurantText;

    private MainApp mainApp;
    private Restaurant lastSelectedRestaurant;

    /**
     * Used to give a reference to the mainApp for this controller.
     * Should be done after controller initialisation, before using any of its functions.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    void showRestaurantInfo(Restaurant restaurant) {
        mainApp.sidebarOff();
        lastSelectedRestaurant = restaurant;
        closeIcon.setVisible(true);
        closeIcon.setOnMouseClicked((MouseEvent e) -> {
            mainApp.sidebarOff();
        });

        headerIcon.setImage(new Image("icons/png/073-house-3.png"));
        if (restaurant.getName() != null) {
            headerText.setText(restaurant.getName());
        }
        topParagraph.setText(restaurant.getAddress() + ", " + restaurant.getPostal_code() + " " + restaurant.getCity());
        topParagraphIcon.setImage(new Image("icons/png/316-placeholder-2.png"));

        headerIcon.setVisible(true);
        headerText.setVisible(true);
        topParagraphIcon.setVisible(true);
        topParagraph.setVisible(true);

        // if there is a url address set it to open in default web browser
        if (restaurant.getWww().contentEquals("")) {
            bottomParagraph.setVisible(false);
            restaurantUrlIcon.setVisible(false);
        } else {

            bottomParagraph.setVisible(true);
            restaurantUrlIcon.setImage(new Image("icons/png/106-link-1.png"));
            restaurantUrlIcon.setVisible(true);

            this.bottomParagraph.setOnAction(event -> {
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
        setDistanceToRestaurantText(restaurant);
        setUserLocationText(mainApp.getMainViewControl().getTextInSearchField());
        mainApp.sidebarOn();
    }

    /**
     * Used to set user location text to sidebar
     * @param userLocationText - user input filled to search bar & used to search places from google maps api
     */
    void setUserLocationText(String userLocationText) {

        this.userLocationText.setText(mainApp.getBundle().getString("location") + userLocationText);
        setDistanceToRestaurantText(lastSelectedRestaurant);
        this.userLocationText.setOnMouseClicked(event -> {
            mainApp.getMainViewControl().focusMapOnLocation(mainApp.getUserLocation());
        });
    }

    /**
     * Used to set text to show distance from user location to chosen restaurant
     *
     * @param restaurant - chosen restaurant
     */
    private void setDistanceToRestaurantText(Restaurant restaurant) {
        if (mainApp.getUserLocation() != null && restaurant != null) {
            double dist = SearchLogic.calculateDistanceToNearest(restaurant, mainApp.getUserLocation()) / 1000;
            dist = Math.round(dist * 100.0) / 100.0;
            if (dist == 0) {
                distanceToRestaurantText.setText("");
            } else {
                distanceToRestaurantText.setText(mainApp.getBundle().getString("distance") + dist + "km");
            }
        }
    }

    /**
     * Used to handle language bundle texts
     *
     * @param bundle
     */
    public void setTexts(ResourceBundle bundle) {
        bottomParagraph.setText(bundle.getString("restaurantUrl"));
        System.out.println(lastSelectedRestaurant);
        System.out.println();
        if (this.lastSelectedRestaurant != null && MainApp.MAIN_SCREEN.getRight() != null) {
            this.showRestaurantInfo(lastSelectedRestaurant);
        }
    }
}


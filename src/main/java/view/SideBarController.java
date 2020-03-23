package view;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.MainApp;
import model.Restaurant;


public class SideBarController {

    private MainApp mainApp;

    @FXML
    private AnchorPane sidebarContainer;
    @FXML
    private GridPane sidebarInfoContainer;

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

    /**
     * Used to give a reference to the mainApp for this controller.
     * Should be done after controller initialisation, before using any of its functions.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void showDefaultView() {
        headerIcon.setImage(new Image("icons/png/309-heart.png"));
        headerText.setText("Nice to see you here!");
        topParagraph.setText("Feel free to look at restaurant locations, look up an address and filter visible results");

        headerIcon.setVisible(true);
        headerText.setVisible(true);
        topParagraph.setVisible(true);
    }

    public void showRestaurantInfo(Restaurant restaurant) {

        headerIcon.setImage(new Image("icons/png/073-house-3.png"));
        headerText.setText(restaurant.getName());
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


    }
}


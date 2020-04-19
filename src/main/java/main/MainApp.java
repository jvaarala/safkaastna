package main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.javascript.object.LatLong;

import controllers.RestaurantDatabase;
import controllers.RestaurantMongoDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Restaurant;
import view.MainViewController;
import view.OptionsBarController;
import view.SideBarController;


public class MainApp extends Application {

    private ResourceBundle bundle;
    private List<Restaurant> restaurantsFromDb;
    private Stage primaryStage;

    private SideBarController sidebarControl;
    private OptionsBarController optionsControl;
    private MainViewController mainViewControl;

    private BorderPane mainScreen;
    private AnchorPane sidebar;
    private ToolBar optionsBar;
    private LatLong userLocation;


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Controls the javafx components initialisation order.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SafkaaSTNA");
        String languageSelection = "en-EN";
        this.bundle = ResourceBundle.getBundle("TextResources", Locale.forLanguageTag(languageSelection));

        updateRestaurantsFromDb();

        initRootLayout();
        initSideBar();
        initOptionsBar();
        initMainView();

        this.setBundle(this.bundle);
    }

    /**
     * Initiates the root layout of the application.
     */
    public void initRootLayout() {
        mainScreen = new BorderPane();
        primaryStage.setWidth(1200);
        primaryStage.setHeight(768);
        Scene scene = new Scene(mainScreen);
        scene.getStylesheets().add("Styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Initialises the optionsbar.
     */
    private void initOptionsBar() {
        FXMLLoader loader = new FXMLLoader();
        URL connector = getClass().getResource("/OptionsBar.fxml");

        loader.setLocation(connector);

        try {
            optionsBar = (ToolBar) loader.load();
            mainScreen.setBottom(optionsBar);

            this.optionsControl = loader.getController();
            this.optionsControl.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises the sidebar.
     */
    public void initSideBar() {
        FXMLLoader loader = new FXMLLoader();
        URL connector = getClass().getResource("/SideBar.fxml");

        loader.setLocation(connector);

        try {
            sidebar = (AnchorPane) loader.load();
            mainScreen.setRight(null);

            this.sidebarControl = loader.getController();
            this.sidebarControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises the mainView with google maps.
     */
    public void initMainView() {
        FXMLLoader loader = new FXMLLoader();
        URL centerMap = getClass().getResource("/MainView.fxml");
        loader.setLocation(centerMap);
        try {
            AnchorPane mapPane = (AnchorPane) loader.load();
            mainScreen.setCenter(mapPane);
            this.mainViewControl = loader.getController();
            this.mainViewControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this to fetch new restaurants from database.
     */
    public void updateRestaurantsFromDb() {
        RestaurantDatabase database = new RestaurantMongoDB();
        try {
            this.restaurantsFromDb = database.loadRestaurants();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SideBarController getSidebarControl() {
        return sidebarControl;
    }

    public OptionsBarController getOptionsControl() {
        return optionsControl;
    }

    public MainViewController getMainViewControl() {
        return mainViewControl;
    }

    /**
     * Returns restaurants currently stored in memory
     */
    public List<Restaurant> getRestaurants() {
        return this.restaurantsFromDb;
    }

    public void setRestaurants(List<Restaurant> restaurantsDB) {
        this.restaurantsFromDb = restaurantsDB;
    }

    public LatLong getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLong userLocation) {
        this.userLocation = userLocation;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void sidebarOn() {
        mainScreen.setRight(sidebar);
    }

    public void sidebarOff() {
        mainScreen.setRight(null);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        this.mainViewControl.setTexts(this.bundle);
        this.sidebarControl.setTexts(this.bundle);
        this.optionsControl.setTexts(this.bundle);
    }
}

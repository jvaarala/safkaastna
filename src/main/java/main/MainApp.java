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
import view.SettingsViewController;
import view.SideBarController;


public class MainApp extends Application {

    private ResourceBundle bundle;
    private List<Restaurant> restaurantsFromDb;
    private LatLong userLocation;

    private Stage primaryStage;
    private AnchorPane sidebar;

    private SideBarController sidebarControl;
    private OptionsBarController optionsControl;
    private MainViewController mainViewControl;
    private SettingsViewController settingsViewController;


    public static BorderPane MAIN_SCREEN;
    public static AnchorPane VIEW_MAIN;
    public static AnchorPane VIEW_SETTINGS;

    // private String languageSelection;
    private ResourceBundle bundleCities;
    private ResourceBundle defaultCity;
    private ResourceBundle defaultLanguage;

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

        // this.languageSelection = "en-EN";

        this.defaultLanguage = ResourceBundle.getBundle("TextResources", Locale.forLanguageTag("default"));
        this.bundle = ResourceBundle.getBundle("TextResources", Locale.forLanguageTag(this.defaultLanguage.getString("Default")));
        this.bundleCities = ResourceBundle.getBundle("Location", Locale.forLanguageTag("cities"));
        this.defaultCity = ResourceBundle.getBundle("Location", Locale.forLanguageTag("default"));

        updateRestaurantsFromDb();

        initRootLayout();
        initSideBar();
        initOptionsBar();
        initMainView();
        initSettingsView();

        this.setBundle(this.bundle);
        this.loadMainView(VIEW_MAIN);
    }

    /**
     * Initiates the root layout of the application.
     */
    public void initRootLayout() {
        MAIN_SCREEN = new BorderPane();
        primaryStage.setWidth(1200);
        primaryStage.setHeight(768);
        Scene scene = new Scene(MAIN_SCREEN);
        scene.getStylesheets().add("Styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Initialises the optionsbar.
     */
    private void initOptionsBar() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/OptionsBar.fxml"));

        try {
            ToolBar optionsBar = loader.load();
            MAIN_SCREEN.setBottom(optionsBar);

            this.optionsControl = loader.getController();
            this.optionsControl.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises the sidebar.
     */
    private void initSideBar() {
        FXMLLoader loader = new FXMLLoader();
        URL connector = getClass().getResource("/SideBar.fxml");

        loader.setLocation(connector);

        try {
            sidebar = loader.load();
            MAIN_SCREEN.setRight(null);

            this.sidebarControl = loader.getController();
            this.sidebarControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the mainView with google maps to mainScreen.
     */
    public void initMainView() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MainView.fxml"));
        try {
            VIEW_MAIN = loader.load();
            this.mainViewControl = loader.getController();
            this.mainViewControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the settingsView to mainScreen.
     */
    public void initSettingsView() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SettingsView.fxml"));
        try {
            VIEW_SETTINGS = loader.load();
            this.settingsViewController = loader.getController();
            this.settingsViewController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the settingsView to mainScreen.
     */
    public void loadMainView(AnchorPane anchorPane) {
        MAIN_SCREEN.setCenter(anchorPane);
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
        MAIN_SCREEN.setRight(sidebar);
    }

    public void sidebarOff() {
        MAIN_SCREEN.setRight(null);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public ResourceBundle getCityBundle() {
        return bundleCities;
    }

    public ResourceBundle getDefaultBundle() {
        return defaultCity;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        this.mainViewControl.setTexts(this.bundle);
        this.sidebarControl.setTexts(this.bundle);
        this.optionsControl.setTexts(this.bundle);
        this.settingsViewController.setTexts(this.bundle);
    }
}

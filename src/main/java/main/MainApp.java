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
import view.OptionsBarViewController;
import view.SettingsViewController;
import view.SideBarViewController;


public class MainApp extends Application {

    private List<Restaurant> restaurantsFromDb;
    private LatLong userLocation;

    private Stage primaryStage;
    private AnchorPane sidebar;

    private SideBarViewController sidebarControl;
    private OptionsBarViewController optionsControl;
    private MainViewController mainViewControl;
    private SettingsViewController settingsViewControl;

    public static BorderPane main_screen;
    public static AnchorPane view_main;
    public static AnchorPane view_settings;

    private ResourceBundle textResourcesBundle;
    private ResourceBundle cityCoordsBundle;
    private ResourceBundle defaultCityBundle;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Controls the javafx components initialisation order.
     * Calls @see #loadMainView(AnchorPane anchorPane)
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SafkaaSTNA");

        this.textResourcesBundle = ResourceBundle.getBundle("TextResources", Locale.forLanguageTag(
                ResourceBundle.getBundle("DefaultLanguage").getString("Default"))
        );
        this.cityCoordsBundle = ResourceBundle.getBundle("CityCoords");
        this.defaultCityBundle = ResourceBundle.getBundle("DefaultCity");

        updateRestaurantsFromDb();

        initRootLayout();
        initSideBar();
        initOptionsBar();
        initMainView();
        initSettingsView();

        this.applyLanguageBundle(this.textResourcesBundle);
        this.loadMainView(view_main);
    }

    /**
     * Initializes the root layout of the application.
     */
    public void initRootLayout() {
        main_screen = new BorderPane();
        primaryStage.setWidth(1200);
        primaryStage.setHeight(768);
        Scene scene = new Scene(main_screen);
        scene.getStylesheets().add("Styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Initialises the optionsbar.
     */
    private void initOptionsBar() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/OptionsBarView.fxml"));

        try {
            ToolBar optionsBar = loader.load();
            main_screen.setBottom(optionsBar);

            this.optionsControl = loader.getController();
            this.optionsControl.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the sidebar.
     */
    private void initSideBar() {
        FXMLLoader loader = new FXMLLoader();
        URL connector = getClass().getResource("/SideBarView.fxml");

        loader.setLocation(connector);

        try {
            sidebar = loader.load();
            main_screen.setRight(null);

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
            view_main = loader.load();
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
            view_settings = loader.load();
            this.settingsViewControl = loader.getController();
            this.settingsViewControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the settingsView to mainScreen.
     */
    public void loadMainView(AnchorPane anchorPane) {
        main_screen.setCenter(anchorPane);
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

    public SideBarViewController getSidebarControl() {
        return sidebarControl;
    }

    public OptionsBarViewController getOptionsControl() {
        return optionsControl;
    }

    public MainViewController getMainViewControl() {
        return mainViewControl;
    }

    public SettingsViewController getSettingsViewControl() { return settingsViewControl; }

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
        main_screen.setRight(sidebar);
    }

    public void sidebarOff() {
        main_screen.setRight(null);
    }

    public ResourceBundle getTextResourcesBundle() {
        return textResourcesBundle;
    }

    public ResourceBundle getCityBundle() {
        return cityCoordsBundle;
    }

    public ResourceBundle getDefaultBundle() {
        return defaultCityBundle;
    }

    public String getDefaultCityName() {
        return defaultCityBundle.getString("cityName");
    }

    public void applyLanguageBundle(ResourceBundle bundle) {
        this.textResourcesBundle = bundle;
        this.mainViewControl.setTexts(this.textResourcesBundle);
        this.sidebarControl.setTexts(this.textResourcesBundle);
        this.optionsControl.setTexts(this.textResourcesBundle);
        this.settingsViewControl.setTexts(this.textResourcesBundle);
    }
}

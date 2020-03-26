package main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.lynden.gmapsfx.javascript.object.LatLong;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Restaurant;
import model.RestaurantDAO;
import view.MainViewController;
import view.OptionsBarController;
import view.SideBarController;


public class MainApp extends Application {

    private Stage primaryStage;

    private BorderPane mainScreen;
    private AnchorPane sidebar;

    private List<Restaurant> restaurantsFromDb;
    private OptionsBarController optionsControl;
    private MainViewController mapControl;
    private SideBarController sidebarControl;
    private Scene scene;

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

        updateRestaurantsFromDb();

        initRootLayout();

        initSideBar(this.mainScreen);
        initOptionsBar(this.mainScreen);
        initMainView(this.mainScreen);
    }

    /**
     * Initiates the root layout of the application.
     */
    public void initRootLayout() {
        mainScreen = new BorderPane();
        primaryStage.setWidth(1200);
        primaryStage.setHeight(768);
        scene = new Scene(mainScreen);
        scene.getStylesheets().add("Styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Initialises the optionsbar.
     *
     * @param mainScreen
     */
    public void initOptionsBar(BorderPane mainScreen) {
        FXMLLoader loader = new FXMLLoader();
        URL connector = getClass().getResource("/OptionsBar.fxml");

        loader.setLocation(connector);

        try {
            ToolBar connectBar = (ToolBar) loader.load();
            mainScreen.setBottom(connectBar);

            this.optionsControl = loader.getController();
            this.optionsControl.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises the sidebar.
     *
     * @param mainScreen
     */

    public void initSideBar(BorderPane mainScreen) {
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
     *
     * @param mainScreen
     */
    public void initMainView(BorderPane mainScreen) {
        FXMLLoader loader = new FXMLLoader();
        URL centerMap = getClass().getResource("/MainView.fxml");
        loader.setLocation(centerMap);
        try {
            AnchorPane mapPane = (AnchorPane) loader.load();
            mainScreen.setCenter(mapPane);
            this.mapControl = loader.getController();
            this.mapControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this to fetch new restaurants from database.
     */
    public void updateRestaurantsFromDb() {
        RestaurantDAO dao = new RestaurantDAO();
        this.restaurantsFromDb = dao.readRestaurants();
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

    public List<Restaurant> getRestaurants() {
        return this.restaurantsFromDb;
    }

    public SideBarController getSidebarControl() {
        return sidebarControl;
    }

    public MainViewController getMapControl() {
        return mapControl;
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

}

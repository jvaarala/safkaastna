package main;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Restaurant;
import view.MainViewController;
import view.OptionsBarController;


public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane mainScreen;
	
	private List<Restaurant> restaurantsFromDb;
	private OptionsBarController optionsControl;
	private MainViewController mapControl;
	private Scene scene;


	/**
	 * Controls the javafx components initialisation order.
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SafkaaSTNA");
		
		initRootLayout();

		initConnection(this.mainScreen);

		initMap(this.mainScreen);
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
	 * Initialises the connection buttons and functions.
	 * @param mainScreen
	 */
	public void initConnection(BorderPane mainScreen) {
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
	 * Initialises the google map view.
	 * @param mainScreen
	 */
	public void initMap(BorderPane mainScreen) {
		FXMLLoader loader = new FXMLLoader();
		URL centerMap = getClass().getResource("/MainViewController.fxml");
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
	 * Used to re-render the maps data points. Call this when new restaurants are fetched from database.
	 */
	public void updateMap() {
		if(this.restaurantsFromDb == null) {
			boolean success = this.optionsControl.getRestaurants();
			if(success) {
				mapControl.updateView(this.restaurantsFromDb);
			}
			return;
		}
		mapControl.updateView(this.restaurantsFromDb);
	}

    public void setRestaurants(List<Restaurant> restaurantsDB) {
    	this.restaurantsFromDb = restaurantsDB;
    }
    public List<Restaurant> getRestaurants() {
    	return this.restaurantsFromDb;
    }
    public static void main(String[] args) {
		launch(args);
	}

}

package main;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Restaurant;
import model.RestaurantDAO;
import view.MainViewController;
import view.OptionsBarController;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane mainScreen;

	private List<Restaurant> restaurantsFromDb;
	private OptionsBarController optionsControl;
	private MainViewController mapControl;
	private Scene scene;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SafkaaSTNA");

		initRootLayout();

		initConnection(this.mainScreen);

		initMap(this.mainScreen);

//		boolean success = fixCoordinates();
//		System.out.println("Onnistuiko: " + success);
	}

	public void initRootLayout() {
		mainScreen = new BorderPane();
		primaryStage.setWidth(1200);
		primaryStage.setHeight(768);
		scene = new Scene(mainScreen);
		scene.getStylesheets().add("Styles.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void initConnection(BorderPane mainScreen) {
		FXMLLoader loader = new FXMLLoader();
		URL connector = getClass().getResource("/OptionsBar.fxml");

		loader.setLocation(connector);

		try {
			ToolBar connectBar = (ToolBar) loader.load();
			mainScreen.setBottom(connectBar); // change to work with correct element

			this.optionsControl = loader.getController();
			this.optionsControl.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initMap(BorderPane mainScreen) {
		FXMLLoader loader = new FXMLLoader();
		URL centerMap = getClass().getResource("/MainViewController.fxml");
		loader.setLocation(centerMap);
		try {
			AnchorPane mapPane = (AnchorPane) loader.load();
			mainScreen.setCenter(mapPane); // change to work with correct element
			this.mapControl = loader.getController();
			this.mapControl.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void firstMapUpdate() {
		class TestThread extends Thread {
			private MainApp mainApp;

			TestThread(MainApp mainApp) {
				this.mainApp = mainApp;
			}

			@Override
			public void run() {
				try {
				if (mainApp.getRestaurants() == null) {
					boolean success = mainApp.optionsControl.getRestaurants();
					if (!success) {
						return;
					}
				}
				
					mapControl.updateView(mainApp.getRestaurants());
				} catch (Exception e) {
					Platform.runLater(this);
				}

			}
		}
		TestThread fx = new TestThread(this);
		// Platform.runLater(fx);
		fx.start();

	}

	public void updateMap() {
		if (this.restaurantsFromDb == null) {
			boolean success = this.optionsControl.getRestaurants();
			if (!success) {
				return;
			}
		}
		mapControl.updateView(this.restaurantsFromDb);
	}

	/**
	 * method to fix restaurant coordinates to database, only used when necessary
	 * 
	 * @return true, is successful
	 */
	private boolean fixCoordinates() {
		RestaurantDAO dao = new RestaurantDAO();

		// 63.673905, 22.705228
		Restaurant r = new Restaurant(105, 63.673905, 22.705228);
		boolean success = dao.updateRestaurant(r);

		return success;
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

	public Scene getScene() {
		return scene;
	}

}

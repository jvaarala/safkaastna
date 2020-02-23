package main;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Restaurant;
import model.RestaurantDAO;
import view.FXMLExampleController;
import view.OptionsBarController;


public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane mainScreen;
	
	private List<Restaurant> restaurantsFromDb;
	private OptionsBarController optionsControl;
	private FXMLExampleController mapControl;


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SafkaaSaatana");
		
		initRootLayout();
		System.out.println("perkele1");
		initConnection();
		System.out.println("perkele2");
		initMap();
		System.out.println("perkele3");

	}
	
	public void initRootLayout() {
		/*
		FXMLLoader loader = new FXMLLoader();
		URL testa =  getClass().getResource("SafkaaSaatana.fxml");
		loader.setLocation(testa);
		try {
			mainScreen = (BorderPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		mainScreen = new BorderPane();
		primaryStage.setScene(new Scene(mainScreen));
        primaryStage.show();
	}
	
	
	public void initConnection() {
		FXMLLoader loader = new FXMLLoader();
		URL connector = getClass().getResource("/view/OptionsBar.fxml");
		System.out.println("a1 "+connector);
		loader.setLocation(connector);
		try {
			ToolBar connectBar = (ToolBar) loader.load();
			mainScreen.setTop(connectBar); //change to work with correct element
			
	        this.optionsControl = loader.getController();
	        this.optionsControl.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initMap() {
		FXMLLoader loader = new FXMLLoader();
		URL centerMap = getClass().getResource("/view/fxml_example.fxml");
		System.out.println("aa "+centerMap);
		loader.setLocation(centerMap);
		try {
			System.out.println("jjj");
			GridPane mapPane = (GridPane) loader.load();
			mainScreen.setCenter(mapPane); //change to work with correct element
			System.out.println("bb");
			this.mapControl = loader.getController();
			System.out.println("Perkele "+mapControl);
			this.mapControl.setMainApp(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateMap() {
		mapControl.updateListView(this.restaurantsFromDb);
	}
	
    private boolean fixCoordinates() {
        boolean success = false;
        RestaurantDAO dao = new RestaurantDAO();

        // korjattavat id 49, 106, 110, 142, 161, 285
        Restaurant r = new Restaurant(285, 22.279710);
        dao.updateRestaurant(r);
        
        return success;
    }
    
    public void setRestaurants(List<Restaurant> restaurantsDB) {
    	this.restaurantsFromDb = restaurantsDB;
    }
    public List<Restaurant> getRestaurants() {
    	return this.restaurantsFromDb;
    }
    

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	
}

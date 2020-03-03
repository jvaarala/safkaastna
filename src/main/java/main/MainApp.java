package main;
import java.io.IOException;
import java.net.URL;
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
import view.MapController;
import view.OptionsBarController;


public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane mainScreen;
	
	private List<Restaurant> restaurantsFromDb;
	private OptionsBarController optionsControl;
	private MapController mapControl;


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SafkaaSaatana");
		
		initRootLayout();
		
		initConnection(this.mainScreen);

		initMap(this.mainScreen);
		
	}
	
	public void initRootLayout() {
		mainScreen = new BorderPane();
		primaryStage.setWidth(1024);
		primaryStage.setHeight(768);
		primaryStage.setScene(new Scene(mainScreen));
        primaryStage.show();
	}
	
	
	public void initConnection(BorderPane mainScreen) {
		FXMLLoader loader = new FXMLLoader();
		URL connector = getClass().getResource("/OptionsBar.fxml");

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
	
	public void initMap(BorderPane mainScreen) {
		FXMLLoader loader = new FXMLLoader();
		URL centerMap = getClass().getResource("/fxml_example.fxml");
		loader.setLocation(centerMap);
		try {
			GridPane mapPane = (GridPane) loader.load();
			mainScreen.setCenter(mapPane); //change to work with correct element
			this.mapControl = loader.getController();
			this.mapControl.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

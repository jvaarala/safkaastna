package view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.MainApp;
import model.RestaurantDAO;
import model.Restaurant;
import view.FXMLExampleController;


public class OptionsBarController {
	
	private RestaurantDAO db_data;
	private List<Restaurant> restaurantsFromDb;
	private String helpText = "ApuaApua";
	private FXMLExampleController mapController;
	
	
	private MainApp mainApp;
	
	
	@FXML
	private Button connect;
	
	public void Connect() {
		System.out.println("perkele");
		db_data = new RestaurantDAO();
		try {
			restaurantsFromDb = db_data.readRestaurants();
			System.out.println("perkele Saatana "+restaurantsFromDb.size());
			mainApp.setRestaurants(restaurantsFromDb);
			mainApp.initMap();
			//mapController.updateListView(restaurantsFromDb);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection failed and no restaurants available");
		}

	}
	public void setMapController(FXMLExampleController mapController) {
		this.mapController = mapController;
	}
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void Help() {
		//check what the popup window thing was
		System.out.println(helpText);
	}
}

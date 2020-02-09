package view;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.RestaurantDAO;
import model.Restaurant;


public class OptionsBarController {
	
	private RestaurantDAO db_data;
	private List<Restaurant> restaurantsFromDb;
	private Restaurant template;
	private String helpText = "ApuaApua";
	
	/*
	private MainApp mainApp;
	
	public OptionsBarController(MainApp mainApp) {
		this.mainApp = mainApp;
		template = new Restaurant(0, "Placeholder", "ExampleStreet", 00000, "Helsinki", "www.www.www", "Placeholder", "", 000, 000);

	}
	*/
	public OptionsBarController() {
		template = new Restaurant(0, "Placeholder", "ExampleStreet", 00000, "Helsinki", "www.www.www", "Placeholder", "", 000, 000);
	}
	
	@FXML
	private Button connect;
	
	public void Connect() {
		System.out.println("perkele");
		db_data = new RestaurantDAO();
		try {
			restaurantsFromDb = db_data.readRestaurants();
			//mainapp.setRestaurants(restaurantsFromDb);
		} catch (NullPointerException e) {
			System.out.println("Connection failed and no restaurands available");
			restaurantsFromDb = new ArrayList<Restaurant>();
			restaurantsFromDb.add(template);
			/*
			 if(mainapp.getRestaurants().size() < 1) {
			 	mainapp.setRestaurants(restaurantsFromDb);
			 }
			*/
		}

	}
	
	public void Help() {
		//check what the popup window thing was
		System.out.println(helpText);
	}
}

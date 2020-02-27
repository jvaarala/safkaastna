package view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.MainApp;
import model.RestaurantDAO;
import model.Restaurant;


public class OptionsBarController {
	
	private RestaurantDAO db_data;
	private List<Restaurant> restaurantsFromDb;
	private String helpText = "ApuaApua";
	private MainApp mainApp;
	
	
	private void popupAlert(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("CONNECTION ERROR");
		alert.setHeaderText("Error occured while fetching data");
		alert.setContentText(text);
		alert.show();
	}
	
	private void initConnection() {
		this.db_data = new RestaurantDAO();
	}
	
	@FXML
	public boolean getRestaurants() {
		initConnection();

		try {
			restaurantsFromDb = db_data.readRestaurants();
			System.out.println("perkele Saatana "+restaurantsFromDb.size());
			mainApp.setRestaurants(restaurantsFromDb);
		} catch (NullPointerException e) {
			popupAlert("NO CONNECTION TO DATABASE, FAILED TO FETCH RESTAURANTS");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			String errorText = e.toString();
			popupAlert(errorText);
			return false;
		}
		return true;

	}
	
	@FXML
	/*
	 * Initiates new connection and updates rendered map
	 */
	private void Refresh() {
		boolean gotRestaurants = getRestaurants();
		if(!gotRestaurants) {
			return;
		}
		
		mainApp.updateMap();
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	public void Help() {
		//check what the popup window thing was
		System.out.println(helpText);
	}
}

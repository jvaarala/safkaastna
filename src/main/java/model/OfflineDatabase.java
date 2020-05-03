package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class OfflineDatabase implements RestaurantDatabase {

	private String restaurantsFile = "RestaurantsJsonStrings.txt";
	
	public OfflineDatabase() {
	}
	
	
	public OfflineDatabase(String env) {
		if(env.equals("test")) {
			restaurantsFile = "RestaurantsJsonStrings_test.txt";
		}
	}
	
	
	/**
	 * Store restaurant array into a file
	 * @param restaurants
	 * @return boolean - true if success, false if IOException & writing failed
	 */
	public boolean storeRestaurants(List<Restaurant> restaurants) {

		List<String> jsons = createRestaurantsJson(restaurants);

		try {
			File file = new File(restaurantsFile);
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");
			}
			
			FileWriter writer = new FileWriter(file);
			for(String row : jsons) {
				writer.write(row);
				writer.write("\n");
			}
			writer.close();
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * Reads potential stored restaurants json string file.
	 * @return List<Restaurant> if successful, null if no file found
	 */
	public List<Restaurant> loadRestaurants() throws IOException {
		
		try {
			Path filePath = Paths.get(restaurantsFile);
			List<String> jsonStringLines = Files.readAllLines(filePath);
			
			List<Restaurant> restaurants = createRestaurantsList(jsonStringLines);

			return restaurants;
		} catch (IOException e) {
			System.out.println("No local restaurants file stored.");
			throw(e);
		}
	}
	
	/**
	 * Uses BSON document classes toJson ability to create json strings
	 * 
	 * @param List<Restaurant>
	 * @return List<String> restaurants in json string
	 */
	public List<Restaurant> createRestaurantsList(List<String> restaurantJsonStrings) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		for(String s : restaurantJsonStrings) {
			Document restaurantDoc = Document.parse(s);
			Restaurant r = new Restaurant(restaurantDoc);
			
			restaurants.add(r);
		}

		return restaurants;
	}
	
	/**
	 * Uses BSON document classes toJson ability to create json strings
	 * 
	 * @param List<Restaurant>
	 * @return List<String> restaurants in json string
	 */
	public List<String> createRestaurantsJson(List<Restaurant> restaurants) {
		List<String> restaurantsJSON = new ArrayList<String>();

		for (Restaurant res : restaurants) {
			Document restaurantDoc = res.restaurantToDoc();
			restaurantsJSON.add(restaurantDoc.toJson());
		}

		return restaurantsJSON;
	}

}

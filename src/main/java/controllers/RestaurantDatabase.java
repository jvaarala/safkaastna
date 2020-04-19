package controllers;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import model.Restaurant;

/**
 * Takes care of Database connection - application needs ability download and upload List<Restaurant> data.
 */
public interface RestaurantDatabase {

	/**
	 * Used to upload restaurant array to database
	 * 
	 * @param restaurantsFromDb
	 * @return boolean - true if successful upload, false if partial or complete
	 *         failure
	 */
	public boolean storeRestaurants(List<Restaurant> restaurantsFromDb) throws Exception;

	/**
	 * Load all Restaurants from database
	 * throws exception when no data available or when data is in invalid format
	 * 
	 * @return ArrayList<Restaurant>
	 */
	public List<Restaurant> loadRestaurants() throws Exception;
}

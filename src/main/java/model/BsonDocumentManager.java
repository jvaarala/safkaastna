package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public abstract class BsonDocumentManager {

	/**
	 * Converts a single restaurant object to mongodb document
	 * 
	 * @param Restaurant object
	 * @return mongoDB document version of restaurant
	 */
	protected Document restaurantToDoc(Restaurant res) {
		Document resObj = new Document("id", res.getId());
		resObj.append("name", res.getName());
		resObj.append("address", res.getAddress());
		resObj.append("postal_code", res.getPostal_code());
		resObj.append("city", res.getCity());
		resObj.append("www", res.getWww());
		resObj.append("admin", res.getAdmin());
		resObj.append("admin_www", res.getAdmin_www());
		resObj.append("lat", res.getLat());
		resObj.append("lng", res.getLng());
		return resObj;
	}
	
	
	/**
	 * Converts single mongodb document to restaurant object
	 * 
	 * @param Document
	 * @return Restaurant
	 */
	protected Restaurant docToRestaurant(Document doc) {
		Restaurant res = new Restaurant();
		res.setId(doc.getInteger("id"));
		res.setName(doc.getString("name"));
		res.setAddress(doc.getString("address"));
		res.setPostal_code(doc.getString("postal_code"));
		res.setCity(doc.getString("city"));
		res.setWww(doc.getString("www"));
		res.setAdmin(doc.getString("admin"));
		res.setAdmin_www(doc.getString("admin_www"));
		res.setLat(doc.getDouble("lat"));
		res.setLng(doc.getDouble("lng"));
		return res;
	}
	
	/**
	 * Converts single mongodb document to restaurant object
	 * 
	 * @param Document
	 * @return Restaurant
	 */
	protected void setRestaurantFromDoc(Document doc, Restaurant res) {
		res.setId(doc.getInteger("id"));
		res.setName(doc.getString("name"));
		res.setAddress(doc.getString("address"));
		res.setPostal_code(doc.getString("postal_code"));
		res.setCity(doc.getString("city"));
		res.setWww(doc.getString("www"));
		res.setAdmin(doc.getString("admin"));
		res.setAdmin_www(doc.getString("admin_www"));
		res.setLat(doc.getDouble("lat"));
		res.setLng(doc.getDouble("lng"));
	}
	
	/**
	 * Uses BSON document class to parse its toJson output into documents
	 * Then returns the documents as restaurant objects
	 * @param List<String> BSON document toJson output strings list
	 * @return List<Restaurant> normal restaurant array
	 */
	public List<Restaurant> createRestaurantsList(List<String> restaurantJsonStrings) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		for(String s : restaurantJsonStrings) {
			Document restaurantDoc = Document.parse(s);
			Restaurant r = docToRestaurant(restaurantDoc);
			
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
			Document restaurantDoc = restaurantToDoc(res);
			restaurantsJSON.add(restaurantDoc.toJson());
		}

		return restaurantsJSON;
	}
}

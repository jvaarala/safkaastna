package model;

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
		setRestaurantFromDoc(doc, res);
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
}

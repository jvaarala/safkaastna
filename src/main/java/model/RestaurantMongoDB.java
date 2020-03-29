package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class RestaurantMongoDB {

	/*Java Driver 3.4 or later connection string from MongoDB Atlas.
	 *Newer one didnt work for some reason.
	 *ALWAYS start new connection and close connection on proccess completion,
	 * We have limited number of simultaneous connections.
	*/
	
	private String MongoURI;
	private String dbName = "SafkaaSTNA";
	private String collectionName = "Restaurants";
	private boolean isAdmin;

	public RestaurantMongoDB() {
		this("user");
	}

	public RestaurantMongoDB(String userType) {
		String userENV;
		if (userType.contains("admin")) {
			userENV = "MONGO_ADMIN";
			this.isAdmin = true;
		} else {
			userENV = "MONGO_USER";
			this.isAdmin = false;
		}
		String passENV = userENV + "_PASS";

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		String user = dotenv.get(userENV);
		String pass = dotenv.get(passENV);

		this.MongoURI = createMongoURI(user, pass);
	}

	private String createMongoURI(String user, String pass) {
		String uri = "mongodb://" + user + ":" + pass
				+ "@cluster0-shard-00-00-hd5gg.mongodb.net:27017,cluster0-shard-00-01-hd5gg.mongodb.net:27017,"
				+ "cluster0-shard-00-02-hd5gg.mongodb.net:27017/"
				+ "test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";
		return uri;
	}
	

	/**
	 * Converts a single restaurant object to mongodb document
	 * @param Restaurant object
	 * @return mongoDB document version of restaurant
	 */
	private Document restaurantToDoc (Restaurant res) {
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
	 * @param Document
	 * @return Restaurant
	 */
	private Restaurant docToRestaurant (Document doc) {
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
	 * Used to upload restaurant array to MongoDB
	 * @param restaurantsFromDb
	 * @return boolean - true if successful upload, false if partial or complete failure
	 */
	public boolean uploadRestaurants(List<Restaurant> restaurantsFromDb) throws Exception {
		if(!this.isAdmin) {
			System.out.println("REQUIRES ADMIN USER");
			return false;
		}
		
		
		MongoClient mongoClient = MongoClients.create(MongoURI);
		MongoDatabase database = mongoClient.getDatabase(dbName);

		MongoCollection<Document> restaurantsCollection = database.getCollection(collectionName);
		
		//delete old data
		restaurantsCollection.drop();

		//create mongoDB document of each restaurant
		List<Document> restaurantsDocList = new ArrayList<Document>();
		for(Restaurant res : restaurantsFromDb) {
			Document restaurantDoc = restaurantToDoc(res);
			restaurantsDocList.add(restaurantDoc);
		}
		
		//add all to database
		restaurantsCollection.insertMany(restaurantsDocList);
		
		long docCount = restaurantsCollection.countDocuments();
	
		mongoClient.close();
		
		if(docCount != restaurantsFromDb.size()) {
			System.out.println("INCOMPLETE UPLOAD "+ docCount + " out of "+ restaurantsFromDb.size());
			return false;
		}
		System.out.println("UPLOAD SUCCESSFUL: " + docCount + " UPLOADS");
		return true;
	}
	
	/**
	 * Downloads all Restaurants in MongoDB
	 * Probably throws exception when no Internet
	 * or when data is in invalid format
	 * @return ArrayList<Restaurant>
	 */
	public List<Restaurant> downloadRestaurants() throws Exception {
		
		MongoClient mongoClient = MongoClients.create(MongoURI);
		MongoDatabase database = mongoClient.getDatabase(dbName);

		MongoCollection<Document> restaurantsCollection = database.getCollection(collectionName);
		
		long docCount = restaurantsCollection.countDocuments();
		//gets all documents
		FindIterable<Document> allDocs = restaurantsCollection.find();
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		for(Document doc : allDocs) {
			Restaurant res = docToRestaurant(doc);
			restaurants.add(res);
		}
		
		mongoClient.close();
		
		
		//check all counts match		
		if(docCount != restaurants.size()) {
			System.out.println("INCOMPLETE DOWNLOAD "+ docCount + " out of "+ restaurants.size());
		} else {
			System.out.println("DOWNLOAD SUCCESSFUL: " + docCount + " RESTAURANTS");
		}
		return restaurants;
	}
}

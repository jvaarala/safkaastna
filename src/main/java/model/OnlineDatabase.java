package model;

import java.util.List;

public abstract class OnlineDatabase implements RestaurantDatabase {

	private OfflineDatabase local = new OfflineDatabase();
	
	@Override
	public boolean storeRestaurants(List<Restaurant> restaurants) throws Exception {
		// TODO Auto-generated method stub
		return uploadRestaurants(restaurants);
	}
	
	//attempt to download from online database and store locally, if it fails try local storage
	@Override
	public List<Restaurant> loadRestaurants() throws Exception {
		try {
			List<Restaurant> restaurants = downloadRestaurants();
			local.storeRestaurants(restaurants);
			return downloadRestaurants();
		} catch (Exception e) {
			System.out.println("Online load failed, trying local storage");
			return local.loadRestaurants();
		}
	}
	
	protected abstract List<Restaurant> downloadRestaurants() throws Exception;
	protected abstract boolean uploadRestaurants(List<Restaurant> restaurants) throws Exception;
	


}

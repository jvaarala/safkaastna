package main;

import model.Restaurant;
import model.RestaurantDAO;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class Update {

    private static void readJSON() throws Exception {

        // HOX HIBERNATE UPDATE VS. CREATE & AUTH

        RestaurantDAO dao = new RestaurantDAO();
        File file = new File("restaurantRawData210320.json");
        String content = FileUtils.readFileToString(file, "utf-8");

        // Convert JSON string to JSONObject
        JSONObject restJSON = new JSONObject(content);

        JSONArray restArray = restJSON.getJSONArray("restaurants");
        for (int i = 0; i < restArray.length(); i++) {
            Restaurant restaurant = new Restaurant();
            JSONObject obj = restArray.getJSONObject(i);
            restaurant.setName(obj.getString("name"));
            restaurant.setAddress(obj.getString("address"));
            restaurant.setPostal_code(obj.getString("postal_code"));
            restaurant.setCity(obj.getString("city"));
            restaurant.setWww(obj.getString("www"));
            restaurant.setAdmin(obj.getString("admin"));
            restaurant.setAdmin_www(obj.getString("admin_www"));
            try {
                restaurant.setLat(obj.getDouble("lat"));
                restaurant.setLng(obj.getDouble("lng"));
            } catch (Exception e) {
                System.out.println(e);
            }

            dao.createRestaurant(restaurant);

            System.out.println(restaurant);
        }

    }


    public static void main(String args[]) throws Exception  //static method
    {
        readJSON();
    }
}

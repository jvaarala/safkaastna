package model;

import com.lynden.gmapsfx.javascript.object.LatLong;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import main.MainApp;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * SearchLogic class controls data filtering logic functions.
 */
public class SearchLogic {

    public SearchLogic() { }

    /**
     * Google maps api key is written on a separate, local file
     * Dotenv handles retrieving apikey and it is stored on a variable
     */
    private Dotenv dotenv = Dotenv
            .configure()
            .ignoreIfMissing()
            .load();

    private String api = dotenv.get("APIKEY");

    /**
     * Search restaurant list for names that match search word.
     *
     * @param restaurantList all the known restaurants.
     * @param searchWord the word that is written in the search field.
     * @return returns a list with the restaurant that matched the search word.
     * If search word is empty or contains only whitespace the whole list is returned.
     */
    public List<Restaurant> filter(List<Restaurant> restaurantList, String searchWord) {

        // Splits search word and removes all whitespace from the beginning.
        int indexOfFirstLetter = 0;
        String[] stringBlocks = searchWord.split("");
        for (int i = 0; i < stringBlocks.length; i++) {
            if (stringBlocks[i].matches("\\S")) {
                indexOfFirstLetter = i;
                i = stringBlocks.length;
            }
        }

        String newSearchWord = "";
        for (int i = indexOfFirstLetter; i < stringBlocks.length; i++) {
            newSearchWord = newSearchWord + stringBlocks[i];
        }

        //Splits search word and removes all whitespace from the end.
        int indexOfLastLetter = 0;

        stringBlocks = newSearchWord.split("");

        for (int i = stringBlocks.length - 1; i > 0; i--) {
            if (stringBlocks[i].matches("\\S")) {
                indexOfLastLetter = i;
                i = 0;
            }
        }

        String searchWordwithoutWhitespace = "";
        for (int i = 0; i <= indexOfLastLetter; i++) {
            searchWordwithoutWhitespace = searchWordwithoutWhitespace + stringBlocks[i];
        }

        // Check if the search field is empty or contains only whitespace.
        if (searchWordwithoutWhitespace.equals(" ") || searchWordwithoutWhitespace.equals("")) return restaurantList;

        // Check if the search word match any of the restaurants.
        List<Restaurant> copy = new ArrayList<>();
        searchWordwithoutWhitespace = searchWordwithoutWhitespace.toUpperCase();

        for (Restaurant restaurant : restaurantList) {
            String restaurantName = restaurant.getName().toUpperCase();
            String restaurantCity = restaurant.getCity().toUpperCase();
            String restaurantAddress = restaurant.getAddress().toUpperCase();


            if (restaurantName.contains(searchWordwithoutWhitespace) ||
                    restaurantCity.contains(searchWordwithoutWhitespace) ||
                    restaurantAddress.contains(searchWordwithoutWhitespace)) {
                copy.add(restaurant);
            }

        }
        return copy;
    }

    /**
     * uses haversine formula to calculate which restaurant is nearest to user input address
     * @param restaurants list of all restaurants
     * @param userLocation LatLong object for location given by user
     * @return Restaurant object that is nearest to user provided address
     */
    public Restaurant findNearestRestaurant(List<Restaurant> restaurants, LatLong userLocation) {
        Restaurant nearest = new Restaurant();
        double distance = 1300000;      // some big value to start with (Finlands maximum length is lower than this) in meters!

        for( Restaurant restaurant : restaurants) {
            final int R = 6371; // Radius of the earth
            double latDistance = Math.toRadians(restaurant.getLat() - userLocation.getLatitude());
            double lonDistance = Math.toRadians(restaurant.getLng() - userLocation.getLongitude());
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(restaurant.getLat())) * Math.cos(Math.toRadians(userLocation.getLatitude()))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distanceFromRestaurant = R * c * 1000; // convert to meters
//            System.out.printf(distanceFromRestaurant + "\n");
            if (distanceFromRestaurant < distance) {
                distance = distanceFromRestaurant;
                nearest = restaurant;
            }
        }

        return nearest;
    }

    /**
     * Search Google Maps api for coordinates with address
     *
     * @param s User input String (address) to be searched from Google maps api
     * @return LatLong object to be placed on map
     */
    public LatLong fetchGoogleCoordinates(String s) {

        if (s.equals("")) {
            return null;
        }
        // Format string to be usable as a part of search url
        String sWithoutSpaces = s
                .replace(",", "")
                .replace(" ", "+");
        String httpsUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + sWithoutSpaces +
                "&key=" + api;
        URL url;
        HttpsURLConnection con = null;
        StringBuilder result = new StringBuilder();
        InputStream in = null;
        BufferedReader reader = null;

        // Create String from api response
        try {
            url = new URL(httpsUrl);
            con = (HttpsURLConnection) url.openConnection();
            in = new BufferedInputStream(con.getInputStream());
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (con != null) {
                con.disconnect();
            }
        }

        // Modify api response String to a LatLong Object
        LatLong ll = null;
        JSONObject resultJSON = new JSONObject(result.toString());
        JSONArray resultArray = resultJSON.getJSONArray("results");
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject results = resultArray.getJSONObject(i);
            JSONObject geometry = results.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            ll = new LatLong(location.getDouble("lat"), location.getDouble("lng"));
        }
        return ll;
    }

}

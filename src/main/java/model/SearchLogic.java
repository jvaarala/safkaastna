package model;

import com.lynden.gmapsfx.javascript.object.LatLong;

import java.util.ArrayList;
import java.util.List;


/**
 * SearchLogic class controls data filtering logic functions.
 */
public class SearchLogic {

    public SearchLogic() { }

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

}

package model;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchLogic {

    public SearchLogic() {

    }


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
            if (restaurantName.contains(searchWordwithoutWhitespace) || restaurantCity.contains(searchWordwithoutWhitespace)) {
                copy.add(restaurant);
            }

        }
        return copy;
    }
}

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


    public List<Restaurant> Search(List<Restaurant> restaurantList, String searchWord) {


            int indexOfFirstLetter = 0;
            String[] stringPalat = searchWord.split("");
            for (int i = 0; i < stringPalat.length; i++) {
                if (stringPalat[i].matches("\\S")) {
                    indexOfFirstLetter = i;
                    i = stringPalat.length;
                }
            }
            String newSearchWord = "";
            for (int i = indexOfFirstLetter; i < stringPalat.length; i++) {
                newSearchWord = newSearchWord + stringPalat[i];
            }

            int indexOfLastLetter = 0;

            stringPalat = newSearchWord.split("");

            for (int i = stringPalat.length - 1; i > 0; i--) {
                if (stringPalat[i].matches("\\S")) {
                    indexOfLastLetter = i;
                    i = 0;
                }
            }

            String searchWordwithoutWhitespace = "";
            for (int i = 0; i <= indexOfLastLetter; i++) {
                searchWordwithoutWhitespace = searchWordwithoutWhitespace + stringPalat[i];
            }
            if (searchWordwithoutWhitespace.equals("")) return restaurantList;

            List<Restaurant> copy = new ArrayList<>();
            searchWordwithoutWhitespace = searchWordwithoutWhitespace.toUpperCase();

            for (Restaurant restaurant : restaurantList) {
                if (restaurant.getName().contains(searchWordwithoutWhitespace)) {
                    copy.add(restaurant);
                }
            }
            return copy;
        }
}

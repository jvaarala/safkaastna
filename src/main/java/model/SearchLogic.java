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

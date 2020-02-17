package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SearchLogic {

    

    public SearchLogic() {

    }


    public List<Restaurant> Search(List<Restaurant> restaurantList, String searchWord) {


        List<Restaurant> copy = new ArrayList<>();

        searchWord = searchWord.toUpperCase();
        String newsearchWord = "\\S+" + searchWord;

//        System.out.println(searchWord);
//        System.out.println(newsearchWord);

        for (Restaurant restaurant : restaurantList) {

            if (restaurant.getName().contains(searchWord)) {
            //if(Pattern.matches(searchWord, restaurant.getName())) {
             copy.add(restaurant);
            }
        }
        if(searchWord.equals("")) {
            return restaurantList;
        }
        return copy;


    }
}

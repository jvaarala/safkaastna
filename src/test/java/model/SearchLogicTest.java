package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchLogicTest {
    private Restaurant r;
    private Restaurant d;
    private Restaurant t;
    private SearchLogic search = new SearchLogic();
    private List<Restaurant> listOfRestaurants = new ArrayList<>();
    private List<Restaurant> empty = new ArrayList<>();
    private List<Restaurant> listWithOneRestaurant = new ArrayList<>();
    private List<Restaurant> listWithFastfoodRestaurants = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        listOfRestaurants.clear();
        r = new Restaurant(
                666, "FASTFOOD",
                "address",
                00000,
                "city",
                "www",
                "admin",
                "adminwww",
                62.8787878,
                23.3876387);

        d = new Restaurant(
                666, "fastfood",
                "address",
                00000,
                "city",
                "www",
                "admin",
                "adminwww",
                62.8787878,
                23.3876387);

        t = new Restaurant(
                666, "burgerplace",
                "address",
                00000,
                "city",
                "www",
                "admin",
                "adminwww",
                62.8787878,
                23.3876387);

        listOfRestaurants.add(r);
        listOfRestaurants.add(d);
        listOfRestaurants.add(t);

        listWithOneRestaurant.add(t);

        listWithFastfoodRestaurants.add(r);
        listWithFastfoodRestaurants.add(d);
    }

    @Test
    void searchFoundNothing() {

        assertEquals(empty,search.Search(listOfRestaurants,"kissa"),
                "restaurant name including kissa was found but doesn´t exist.");
    }

    @Test
    void searchLowerCase() {

        assertEquals(listWithFastfoodRestaurants,search.Search(listOfRestaurants,"fo"),
                "the two restaurants were not found including fo or FO.");
    }

    @Test
    void searchUpperCase() {

        assertEquals(listWithFastfoodRestaurants,search.Search(listOfRestaurants,"FO"),
                "the two restaurants were not found including fo or FO.");
    }

    @Test
    void searchUpperCaseWithWhitespace() {

        assertEquals(listWithFastfoodRestaurants,search.Search(listOfRestaurants,"   FO"),
                "the two restaurants were not found including fo or FO.");
    }

    @Test
    void searchLowerCaseWithWhitespace() {

        assertEquals(listWithFastfoodRestaurants,search.Search(listOfRestaurants,"   fo      "),
                "the two restaurants were not found including fo or FO.");
    }

    @Test
    void searchOnlyWhitespace() {
        assertEquals(listOfRestaurants,search.Search(listOfRestaurants,"         "),
                "whitespace search didn't return all the restaurants");
    }

    @Test
    void searchWithNoSpace() {
        assertEquals(listOfRestaurants,search.Search(listOfRestaurants,""),
                "empty search didn't return all the restaurants");
    }

    @Test
    void searchForOneRestaurant() {
        assertEquals(listWithOneRestaurant,search.Search(listOfRestaurants,"burg"),
                "The one restaurant matching burg was not found");
    }

}
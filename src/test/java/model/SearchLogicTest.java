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
    private SearchLogic search = new SearchLogic();
    List<Restaurant> listRes = new ArrayList<>();
    List<Restaurant> empty = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        listRes.clear();
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


        listRes.add(r);
    }

    @Test
    void searchFoundNothing() {

        assertEquals(empty,search.Search(listRes,"kissa"),
                "restaurant name including kissa was found but doesn´t exist.");
    }

    @Test
    void searchOneFound() {

        assertEquals(listRes,search.Search(listRes,"fo"),
                "the one restaurant name including fo was not found");
    }

}
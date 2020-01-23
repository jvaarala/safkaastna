import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Restaurant;
import model.RestaurantDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HelloWorld extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            readJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");


            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private static void readJSON() throws Exception {
        RestaurantDAO dao = new RestaurantDAO();
        File file = new File("/Users/katriaho/IdeaProjects/safkaastna/restaurantRawData230120.json");
        String content = FileUtils.readFileToString(file, "utf-8");

        // Convert JSON string to JSONObject
        JSONObject restJSON = new JSONObject(content);

        JSONArray restArray = restJSON.getJSONArray("restaurants");
        for (int i = 0; i < restArray.length(); i++) {
            Restaurant restaurant = new Restaurant();
            JSONObject obj = restArray.getJSONObject(i);
            restaurant.setName(obj.getString("name"));
            restaurant.setAddress(obj.getString("address"));
            restaurant.setPostal_code(obj.getInt("postal_code"));
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
}



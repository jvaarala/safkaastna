package main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Restaurant;
import view.MapController;
import view.OptionsBarController;

class MainAppTest extends Application  {
	private MainApp mainApp;
	private List<Restaurant> tempList;
	

	private BorderPane mainScreen;
	
	private List<Restaurant> restaurantsFromDb;
	private OptionsBarController optionsControl;
	private MapController mapControl;
	
	
	@BeforeAll
	public static void initJFX() {
        Thread fx = new Thread("JavaFX Init Thread") {
            @Override
            public void run() {
                Application.launch(MainAppTest.class, new String[0]);
            }
        };
        fx.setDaemon(true);
        fx.start();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	}
	

	@BeforeEach
	void beforeEach() {
		Restaurant temp = new Restaurant(
                666, "name",
                "address",
                00000,
                "city",
                "www",
                "admin",
                "adminwww",
                62.8787878,
                23.3876387);
		tempList = new ArrayList<Restaurant>();
		tempList.add(temp);
		mainApp = new MainApp();
		mainScreen = new BorderPane();

	}
	
	@Test
	void getRestaurants() {
		mainApp.setRestaurants(tempList);
		assertEquals(1, mainApp.getRestaurants().size(), "getRestaurants not working for mainapp");
	}
	
	@Test
	void initConnection() {
		mainApp.initConnection(this.mainScreen);
	}
	
}

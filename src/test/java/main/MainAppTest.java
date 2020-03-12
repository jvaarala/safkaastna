package main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Restaurant;



class MainAppTest extends Application  {
	static MainApp mainApp;
	private List<Restaurant> tempList;

	private BorderPane mainScreen;
	static Thread fx;
	
	@BeforeAll
	public static void initJFX() {

			fx = new Thread("JavaFX Init Thread") {
	            @Override
	            public void run() {
	            	try {
	            		Application.launch(MainAppTest.class, new String[0]);
	            	} catch (Exception e) {
	            	}

	            }
	        };
	        
	        fx.setDaemon(true);
	        fx.start();
	}
	
	@AfterAll
	public static void stopJFX() {
		fx.interrupt();
	}

	
	@Override
	public void start(Stage primaryStage)  {
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
		mainScreen = new BorderPane();
		mainApp = new MainApp();
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

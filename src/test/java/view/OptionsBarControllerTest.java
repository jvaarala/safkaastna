package view;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OptionsBarControllerTest {
	private static OptionsBarController controller;
	private static main.MainApp pseudoMain;
	private static MainViewController pseudoMapControl;

	@BeforeAll
	static void BeforeAll() {
		controller = new OptionsBarController();
		pseudoMain = new main.MainApp();
		controller.setMainApp(pseudoMain);
	}

	@Test
	void getDataNoConnection() {
		assertEquals(false, controller.getData(), "getData does not work, returns true without connection");
	}

	@Test
	void getDataConnected() {
		controller.initConnection();
		assertEquals(true, controller.getData(), "getData does not work, check connection");
	}
	
	@Test
	void getRestaurants() {
		assertEquals(true, controller.getRestaurants(), "getRestaurants not working, check connection");
	}
	
}

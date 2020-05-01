package viewcontroller;


class OptionsBarViewControllerTest {
	static OptionsBarViewController controller;
	static MainViewController pseudoMapControl;

/*	@BeforeEach
	void BeforeEach() {
		OptionsBarController controllerTemp = new OptionsBarController();
		controller = spy(controllerTemp);
		doNothing().when(controller).initConnection();

		main.MainApp pseudoMain = new main.MainApp();
		pseudoMain = spy(pseudoMain);
		doNothing().when(pseudoMain).updateRestaurantsFromDb();
		
		controller.setMainApp(pseudoMain);
		
    	RestaurantDAO db_data = mock(RestaurantDAO.class);
        controller.setDB_DATA(db_data);
	}

	@Test
	void getDataNoConnection() {
		controller.setDB_DATA(null);
		assertEquals(false, controller.getData(), "getData does not work, should return false when no connection");
	}

	@Test
	void getDataConnected() {
		assertEquals(true, controller.getData(), "getData does not work, check return conditions");
	}
	
	@Test
	void getRestaurants() {
		assertEquals(true, controller.getRestaurants(), "getRestaurants not working, check return conditions");
	}
	
	
	@Test
	void Refresh() {
		assertEquals(true, controller.refresh(), "Refresh not working, should return true on success");
	}
	
	@Test
	void RefreshFail() {
		OptionsBarController controllerTemp = mock(OptionsBarController.class);
		when(controllerTemp.getRestaurants()).thenReturn(false);
<<<<<<< HEAD
		assertEquals(false, controllerTemp.refresh(), "Refresh not working, should return false on failure");
	}
=======
		assertEquals(false, controllerTemp.Refresh(), "Refresh not working, should return false on failure");
	}*/
}

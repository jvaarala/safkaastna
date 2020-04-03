package view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.*;
import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.ParentMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static javafx.application.Application.launch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ApplicationExtension.class)
class MainViewControllerTest {
    private BorderPane mainScreen;
    private Stage primaryStage;
    private Scene scene;

    private MainApp mainAppMock = mock(MainApp.class);

    private MainViewController mainViewControl;
    private SideBarController sideBarController;

//    private MainViewController mvMock;
//    private SideBarController sbMock;

    private AnchorPane mapPane, sidebar;
    private int i = 0;

    @BeforeAll
    private void init() {
        mainScreen = new BorderPane();
        mainScreen = new BorderPane();
        scene = new Scene(mainScreen);
        scene.getStylesheets().add("Styles.css");
    }

    // This works like beforeEach
    @Start
    private void start(Stage ps) {
        System.out.println("start " + i);
        i++;
        primaryStage = ps;
        primaryStage.setWidth(300);
        primaryStage.setHeight(300);
        primaryStage.setScene(scene);

        FXMLLoader loader1 = new FXMLLoader();
        URL centerMap = getClass().getResource("/MainView.fxml");
        loader1.setLocation(centerMap);
        System.out.println(loader1.getLocation());
        try {
            mapPane = (AnchorPane) loader1.load();
            mainViewControl = loader1.getController();
            mainViewControl.setGoogleMapStuff(mock(GoogleMapView.class), mock(GoogleMap.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FXMLLoader loader2 = new FXMLLoader();
        URL connector = getClass().getResource("/SideBar.fxml");
        loader2.setLocation(connector);
        try {
            AnchorPane sidebar = (AnchorPane) loader2.load();
            sideBarController = loader2.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainScreen.setCenter(mapPane);
        mainScreen.setRight(sidebar);

        primaryStage.show();
        primaryStage.toFront();
        System.out.println("start valmis");

    }

    @DisplayName("map_fills_whole_window")
    @Test
    void map_fills_whole_window() {

        AnchorPane ap = (AnchorPane)scene.lookup("#main");
        AnchorPane map = (AnchorPane) scene.lookup("#mapContainer");
        Double top = ap.getTopAnchor(map);
        assertEquals(0.0, top, "Top Anchor wrong");
        Double bottom = ap.getBottomAnchor(map);
        assertEquals(0.0, bottom, "Bottom anchor wrong");
        Double left = ap.getLeftAnchor(map);
        assertEquals(0.0, left, "Left Anchor wrong");
        Double right = ap.getRightAnchor(map);
        assertEquals(0.0, right, "Left Anchor wrong");
        System.out.println("map_fills_whole_window done");

    }

    @DisplayName("should_contain_find_button")
    @Test
    void should_contain_find_button(FxRobot robot) {
        System.out.println("should_contain_find_button START");
        FxAssert.verifyThat("#searchButton", LabeledMatchers.hasText("Find"));
        System.out.println("should_contain_find_button done");
    }

    @Disabled
    @DisplayName("should_contain_nearest_button")
    @Test
    void should_contain_nearest_button() {
        System.err.println("should_contain_nearest_button start");
        FxAssert.verifyThat("#locateNearestButton", LabeledMatchers.hasText("DEBUG NEAREST"));

//        Assertions.assertThat(robot.lookup("#locateNearestButton").queryAs(Button.class)).hasText("DEBUG NEAREST");

//        robot.clickOn("#locateNearestButton");
//        FxAssert.verifyThat("#locateNearestButton", LabeledMatchers.hasText("DEBUG NEAREST"));
        System.err.println("should_contain_nearest_button done");
    }

    @Disabled
    @DisplayName("should_contain_search_box")
    @Test
    void should_contain_search_box(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#searchField").queryAs(TextField.class)).hasText("");
        System.out.println("should_contain_search_box done");
    }

    @Disabled
    @DisplayName("search_box_can_be_filled")
    @Test
    void search_box_can_be_filled() {
        TextField t = (TextField)scene.lookup("#searchField");
        t.setText("test");
        assertEquals("test", t.getText(), "Text setting does not work");
        System.out.println("search_box_can_be_filled done");
    }

    @Disabled
    @DisplayName("should_contain_filter_button")
    @Test
    void should_contain_filter_button() {
        FxAssert.verifyThat("#filterToggleButton", LabeledMatchers.hasText("Filter restaurants"));
        System.out.println("should_contain_filter_button done");
    }

    @DisplayName("should_contain_listView")
    @Test
    void should_contain_listView() {
        FxAssert.verifyThat("#listViewNames", ParentMatchers.hasChildren(1));
        System.out.println("should_contain_listView done");
    }

    @Disabled
    @Test
    void fill_observableList() {
        List<Restaurant> mockRestaurants;
        Restaurant rMock = mock(Restaurant.class);
        Restaurant r1 = new Restaurant(666, "name", "address", "zip", "city", "www", "admin", "adminwww", 66.66, 66.66 );
        Restaurant r2 = new Restaurant(667, "name2", "address2", "zip2", "city2", "www2", "admin2", "adminwww2", 22.22, 47.77 );
        mockRestaurants = Arrays.asList(r1, r2);
        // never goes here
        when(mainAppMock.getRestaurants())
                .thenReturn(mockRestaurants);

        ListView lv = (ListView) scene.lookup("#listViewNames");
        assertEquals(lv.getItems().size(), mockRestaurants.size(), "Restaurant list size does not match");
        System.out.println("fill_observableList done");
    }
    @Disabled
    @Test
    void should_set_userLocation(FxRobot robot) {
        TextField textField = (TextField) scene.lookup("#searchField");
        Button sb = (Button) scene.lookup("#searchButton");
        FxAssert.verifyThat(sb, LabeledMatchers.hasText("Find"));

        textField.setText("Myllypurontie 1 Helsinki");
        robot.clickOn(sb);
        robot.clickOn("#locateNearestButton");
        WaitForAsyncUtils.waitForFxEvents();
        ActionEvent e = mock(ActionEvent.class);
//        verify(mvMock, times(1)).handleLocateNearestButton(e);
//        verify(mvMock, times(1)).findNearestAndFitBounds(mainAppMock.getUserLocation());
        System.out.println("should_set_userLocation");
    }

    @DisplayName("string_format_all_lower_case")
    @Test
    void string_format_all_lower_case() {
        MainViewController mvc = new MainViewController();
        String s = mvc.formatString("testitie 5 helsinki");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_lower_case done");
    }

    @DisplayName("string_format_all_upper_case")
    @Test
    void string_format_all_upper_case() {
        MainViewController mvc = new MainViewController();
        String s = mvc.formatString("TESTITIE 5 HELSINKI");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_upper_case done");
    }

    @DisplayName("string_format_all_messed_up")
    @Test
    void string_format_all_messed_up() {
        MainViewController mvc = new MainViewController();
        String s = mvc.formatString("tesTItie 5 helsINKI");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_messed_up done");
    }

    // Does not work, since GMapsFX problems
    // netscape.javascript.JSException: ReferenceError: Can't find variable: loadMapLibrary
    @Disabled
    @Test
    void marker_test() {
        MainViewController mvc = new MainViewController();
        when(mainAppMock.getUserLocation()).thenReturn(new LatLong(62, 24));
        Marker marker = mvc.createUserLocationMarker();
        Marker test = new Marker(new MarkerOptions()
                .position(new LatLong(62, 24))
                .icon("https://users.metropolia.fi/~katriras/OTP1/map-marker.png"));
        assertEquals(marker, test, "Marker not the same");
    }
}

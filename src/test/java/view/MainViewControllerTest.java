package view;

import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.MainApp;
import model.Restaurant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.ParentMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(ApplicationExtension.class)
class MainViewControllerTest {
    private BorderPane mainScreen;
    private Stage primaryStage;
    private Scene scene;
    private MainViewController mainViewControl;
    MainViewController mvMock;
    MainApp mainAppMock = mock(MainApp.class);
    SideBarController sideBarController;
    SideBarController sbMock;


//    @BeforeAll
//    void init() {
//
//    }
    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param ps - Will be injected by the test runner.
     */
    @Start
    private void start(Stage ps) {
        primaryStage = ps;
        mainScreen = new BorderPane();
        mainScreen = new BorderPane();
        primaryStage.setWidth(1200);
        primaryStage.setHeight(768);
        scene = new Scene(mainScreen);
        scene.getStylesheets().add("Styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        FXMLLoader loader = new FXMLLoader();
        URL centerMap = getClass().getResource("/MainView.fxml");
        loader.setLocation(centerMap);
        try {
            AnchorPane mapPane = (AnchorPane) loader.load();
            mainScreen.setCenter(mapPane);
            mvMock = mock(MainViewController.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FXMLLoader loader1 = new FXMLLoader();
        URL connector = getClass().getResource("/SideBar.fxml");

        loader1.setLocation(connector);

        try {
            AnchorPane sidebar = (AnchorPane) loader1.load();
            mainScreen.setRight(sidebar);
            // sbMock = mock(SideBarController.class);
            sideBarController = loader1.getController();
//            this.sidebarControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    void should_contain_nearest_button(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#locateNearestButton").queryAs(Button.class)).hasText("DEBUG NEAREST");

//        robot.clickOn("#locateNearestButton");
//        FxAssert.verifyThat("#locateNearestButton", LabeledMatchers.hasText("DEBUG NEAREST"));
    }

    @Test
    void should_contain_find_button(FxRobot robot) {
        FxAssert.verifyThat("#searchButton", LabeledMatchers.hasText("Find"));
    }

    @Test
    void should_contain_search_box(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#searchField").queryAs(TextField.class)).hasText("");
    }

    @Disabled // GMapsFX fails this
    @Test
    void search_box_can_be_filled() {
        TextField t = (TextField)scene.lookup("#searchField");
        t.setText("test");
        assertEquals("test", t.getText(), "Text setting does not work");
    }

    @Test
    void should_contain_filter_button() {
        FxAssert.verifyThat("#filterToggleButton", LabeledMatchers.hasText("Filter restaurants"));
    }

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

    }

    @Test
    void should_contain_listView() {
        FxAssert.verifyThat("#listViewNames", ParentMatchers.hasChildren(1));
    }

    @Disabled
    @Test
    void fill_observableList() {
        List<Restaurant> mockRestaurants = new ArrayList<>();
        Restaurant rMock = mock(Restaurant.class);
        Restaurant r1 = new Restaurant(666, "name", "address", "zip", "city", "www", "admin", "adminwww", 66.66, 66.66 );
//        Restaurant r2 = new Restaurant(667, "name2", "address2", "zip2", "city2", "www2", "admin2", "adminwww2", 22.22, 47.77 );
        mockRestaurants = Arrays.asList(r1);
        // ei mee tänne ikinä
        when(mainAppMock.getRestaurants())
                .thenReturn(mockRestaurants);

        ListView lv = (ListView) scene.lookup("#listViewNames");
        assertEquals(lv.getItems().size(), mockRestaurants.size(), "Restaurant list size does not match");

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
        verify(mvMock, times(1)).handleLocateNearestButton(e);
        verify(mvMock, times(1)).findNearestAndFitBounds(mainAppMock.getUserLocation());
    }

}

/*
MainViewControllerissa :
initialize
mapInitialized
updateView
updateListView
updateMarkers
 */
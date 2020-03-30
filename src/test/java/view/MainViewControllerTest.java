package view;

import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.MainApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class MainViewControllerTest {
    private BorderPane mainScreen;
    private Stage primaryStage;
    private Scene scene;
//    private MainViewController mainViewControl;
    MainViewController mvMock;
//    MainApp mainAppMock = mock(MainApp.class);
    SideBarController sbMock;


    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param ps - Will be injected by the test runner.
     */
    @Start
    private void start(Stage ps) {
//        Parent mainNode = FXMLLoader.load(com.javafx.main.Main.class.getResource("/MainView.fxml"));
//        primaryStage.setScene(new Scene(mainNode));
//        primaryStage.show();
//        primaryStage.toFront();
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
//            this.mainViewControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FXMLLoader loader1 = new FXMLLoader();
        URL connector = getClass().getResource("/SideBar.fxml");

        loader1.setLocation(connector);

        try {
            AnchorPane sidebar = (AnchorPane) loader1.load();
            mainScreen.setRight(sidebar);
            sbMock = mock(SideBarController.class);
//            this.sidebarControl.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void should_contain_nearest_button(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#locateNearestButton").queryAs(Button.class)).hasText("DEBUG NEAREST");

        robot.clickOn("#locateNearestButton");
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

    @Test
    void should_set_userLocation(FxRobot robot) {
        TextField textField = (TextField) scene.lookup("#searchField");
        Button b = (Button) scene.lookup("#searchButton");
        FxAssert.verifyThat(b, LabeledMatchers.hasText("Find"));

        textField.setText("Myllypurontie 1 Helsinki");
        robot.clickOn(b);
        Text userLocation = (Text)scene.lookup("#userLocationText");
//        assertEquals("Myllypurontie 1 Helsinki", userLocation.getText(), "UserLocation not set");
    }


}
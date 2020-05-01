package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.MainViewController;
import view.OptionsBarViewController;
import view.SideBarViewController;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@Disabled
@ExtendWith(ApplicationExtension.class)
class MainAppTest {

    private Button button;
    MainApp mainApp;
    Stage stage;
    Scene scene;

    BorderPane mainScreen;
    AnchorPane mapPane;
    AnchorPane sidebar;
    ToolBar optionBar;

    MainViewController mMainViewCtrl = mock(MainViewController.class);
    SideBarViewController mSidebarCtrl = mock(SideBarViewController.class);
    OptionsBarViewController mOptionbarCtrl = mock(OptionsBarViewController.class);

    /**
     * Called before each test
     * @param stage Java framework Stage passed as a parameter
     */
    @Start
    private void start(Stage stage) {
        mainApp = mock(MainApp.class);

        mainScreen = new BorderPane();
        scene = new Scene(mainScreen);

        // SIDEBAR
        FXMLLoader loader1 = new FXMLLoader();
        URL connector = getClass().getResource("/SideBarView.fxml");
        loader1.setLocation(connector);
        try {
            sidebar = (AnchorPane) loader1.load();
            mainScreen.setRight(null);
//            mSidebarCtrl = loader.getController();
            mSidebarCtrl.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // MAIN VIEW
        FXMLLoader loader2 = new FXMLLoader();
        URL centerMap = getClass().getResource("/MainView.fxml");
        loader2.setLocation(centerMap);
        try {
            AnchorPane mapPane = (AnchorPane) loader2.load();
            mainScreen.setCenter(mapPane);
//            mMainViewCtrl = loader.getController();
            mMainViewCtrl.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // OPTIONBAR
        FXMLLoader loader3 = new FXMLLoader();
        URL toolbarBottom = getClass().getResource("/OptionsBarView.fxml");
        loader3.setLocation(toolbarBottom);
        try {
            optionBar = (ToolBar) loader3.load();
            mainScreen.setBottom(optionBar);
//            mOptionbarCtrl = loader.getController();
            mOptionbarCtrl.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(scene);
        stage.show();

    }

    /**
     * Test to check if AnchorPane housing MapView is set to center of main screen (BorderPane)
     */
    /*
    Disabled because:
    java.lang.RuntimeException: netscape.javascript.JSException: ReferenceError: Can't find variable: loadMapLibrary
    Caused by: netscape.javascript.JSException: ReferenceError: Can't find variable: loadMapLibrary
     */
    @Disabled
    @Test
    void mapPane_at_center() {
        BorderPane main = (BorderPane)scene.lookup(".root");
        AnchorPane map = (AnchorPane)scene.lookup("#main");
        assertEquals(main.getCenter(), map);
    }

    /**
     * Test to check if AnchorPane housing sidebar is set to right of main screen (BorderPane)
     */
    @Test
    void sideBar_at_right() {
        BorderPane main = (BorderPane)scene.lookup(".root");
        AnchorPane side = (AnchorPane)scene.lookup(".sidebarContainer");
        assertEquals(main.getRight(), side);
    }

    /**
     * Test to check if AnchorPane housing option bar is set to bottom of main screen (BorderPane)
     */
    /*
    Disabled because:
    java.lang.RuntimeException: netscape.javascript.JSException: ReferenceError: Can't find variable: loadMapLibrary
    Caused by: netscape.javascript.JSException: ReferenceError: Can't find variable: loadMapLibrary
     */
    @Disabled
    @Test
    void optionBar_at_bottom() {
        BorderPane main = (BorderPane)scene.lookup(".root");
        ToolBar option = (ToolBar)scene.lookup(".optionsbarContainer");
        assertEquals(main.getBottom(), option);
    }

}
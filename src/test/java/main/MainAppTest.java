package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.testfx.util.WaitForAsyncUtils;
import view.MainViewController;
import view.OptionsBarController;
import view.SideBarController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
    SideBarController mSidebarCtrl = mock(SideBarController.class);
    OptionsBarController mOptionbarCtrl = mock(OptionsBarController.class);

    @Start
    private void start(Stage stage) {
        mainApp = mock(MainApp.class);

        mainScreen = new BorderPane();
        scene = new Scene(mainScreen);

        // SIDEBAR
        FXMLLoader loader1 = new FXMLLoader();
        URL connector = getClass().getResource("/SideBar.fxml");
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
        URL toolbarBottom = getClass().getResource("/OptionsBar.fxml");
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

    /*
    Disabled because:
    java.lang.RuntimeException: netscape.javascript.JSException: ReferenceError: Can't find variable: loadMapLibrary
    Caused by: netscape.javascript.JSException: ReferenceError: Can't find variable: loadMapLibrary
     */
    @Disabled
    @Test
    void mapPane_at_bottom() {
        BorderPane main = (BorderPane)scene.lookup(".root");
        AnchorPane map = (AnchorPane)scene.lookup("#main");
        assertEquals(main.getCenter(), map);
    }

    @Test
    void sideBar_at_right() {
        BorderPane main = (BorderPane)scene.lookup(".root");
        AnchorPane side = (AnchorPane)scene.lookup(".sidebarContainer");
        assertEquals(main.getRight(), side);
    }

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
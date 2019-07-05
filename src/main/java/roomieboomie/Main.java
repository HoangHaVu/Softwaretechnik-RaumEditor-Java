package roomieboomie;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.controller.LayoutEditorController;
import roomieboomie.controller.RootController;

public class Main extends Application {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;

    public Main() {
        this.roomieBoomieManager = new RoomieBoomieManager();
        this.switcher = new RootController();
    }

    public static void main(String[] args) {
        //TestClass.run();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        RoomEditor testEditor = new RoomEditor();
        testEditor.loadNewRoom("test", false);
        LayoutEditorController editRoom = new LayoutEditorController(testEditor);
        
        Scene scene = new Scene(editRoom.getView());
        primaryStage.setTitle("RoomieBoomie");
        primaryStage.setScene(scene);
        
        switcher.setPrimaryStage(primaryStage);
        switcher.setRoomieBoomieManager(roomieBoomieManager);
        switcher.switchView("Login");
        
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        //primaryStage.setFullScreen(true);
        //primaryStage.setResizable(false);
        
        primaryStage.show();
        
        testEditor.addItem(new LayoutItem(LayoutItemType.WALL, 5, 5, 4, 1, Orientation.LEFT));
        testEditor.addItem(new LayoutItem(LayoutItemType.WALL, 4, 5, 6, 1, Orientation.TOP));
        testEditor.addItem(new LayoutItem(LayoutItemType.WINDOW, 6, 5, 2, 1, Orientation.RIGHT));
        editRoom.refreshView();
        
        //testEditor.editItem((byte)-3);
        //testEditor.editItem((byte)1);
    }
}



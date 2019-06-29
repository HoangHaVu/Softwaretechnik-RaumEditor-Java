package roomieboomie;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.user.User;
import roomieboomie.business.user.UserMap;
import roomieboomie.gui.controller.RootController;
import roomieboomie.persistence.*;

import java.util.HashMap;

public class Main extends Application {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;

    public Main(){
        this.roomieBoomieManager = new RoomieBoomieManager();
        this.switcher = new RootController();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RoomieBoomie");
        switcher.setPrimaryStage(primaryStage);
        switcher.setRoomieBoomieManager(roomieBoomieManager);
        switcher.switchView("Login");
        primaryStage.show();
    }
}

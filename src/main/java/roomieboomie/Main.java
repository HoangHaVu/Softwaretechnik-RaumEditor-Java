package roomieboomie;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.Scene;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("RooomieBoomie");
        Scene scene = new Scene(label);
        primaryStage.setTitle("RoomieBoomie");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

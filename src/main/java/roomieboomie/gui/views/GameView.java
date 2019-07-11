package roomieboomie.gui.views;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class GameView extends Pane {
    private Button refreshBtn;
    private Button startBtn;
    private Button timeLeftBtn;
    private Pane timeLeftPane;

    public GameView(){
        refreshBtn = new Button("refresh");
        refreshBtn.setLayoutX(50);

        startBtn = new Button("start");

        timeLeftBtn = new Button();
        timeLeftPane = new Pane();
        timeLeftPane.setLayoutY(50);
        timeLeftPane.setPrefWidth(500);
        timeLeftPane.setPrefHeight(10);

        timeLeftPane.setStyle("-fx-background-color: #ff0001;");

        this.getChildren().addAll(refreshBtn, startBtn, timeLeftPane);
    }

    public Button getRefreshBtn(){
        return refreshBtn;
    }

    public Button getStartBtn() {
        return startBtn;
    }

    public Pane getTimeLeftPane(){
        return timeLeftPane;
    }
}

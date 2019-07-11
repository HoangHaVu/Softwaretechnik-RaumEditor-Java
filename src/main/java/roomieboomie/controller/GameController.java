package roomieboomie.controller;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import roomieboomie.business.game.Game;
import roomieboomie.business.room.Room;
import roomieboomie.gui.views.GameView;

public class GameController {
    private RootController switcher;
    private GameView gameview;
    private Button startBtn;
    private Button refreshBtn;
    private Pane timeLeftPane;
    private Game game;

    public GameController(RootController switcher, Room room){
        this.switcher = switcher;

        this.game = new Game(room);

        game.getTimerRunning().addListener((observable, oldvar, newvar) -> {
            if (newvar == false){
                System.out.println("VERLOREN");
            }
        });

        this.gameview = new GameView();

        startBtn = gameview.getStartBtn();
        startBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            System.out.println("start");
            game.startTimer();
        });

        refreshBtn = gameview.getRefreshBtn();
        refreshBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            game.resetTime();
        });

        timeLeftPane = gameview.getTimeLeftPane();
        timeLeftPane. prefWidthProperty().bind(game.getCountProperty().multiply(1));
    }

    public GameView getGameview(){
        return gameview;
    }
}

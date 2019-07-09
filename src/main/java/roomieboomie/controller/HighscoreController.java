package roomieboomie.controller;

import javafx.fxml.FXML;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;


public class HighscoreController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;
    private HighscoreList highscoreList;

    @FXML
    private TableView scoreTableView;

    @FXML
    private TableColumn placeColumn;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn scoreColumn;

    public void init(){
        setHighscoreList(roomieBoomieManager.getOverallHighscore());
    }

    public void backToMenu(){
        switcher.switchView("MainMenu");
    }

    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }

    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
        init();
    }

    public void setHighscoreList(HighscoreList highscoreList){
        this.highscoreList = highscoreList;
    }

}

package roomieboomie.controller;

import javafx.fxml.FXML;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.highscore.HighscoreList;

import javax.swing.text.TableView;

public class HighscoreController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;
    private HighscoreList highscoreList;

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

    public void init(){
        setHighscoreList(roomieBoomieManager.getHighscoreListRanked());
    }
    public void setHighscoreList(HighscoreList highscoreList){
        this.highscoreList = highscoreList;
    }

    @FXML TableView tableView;
}

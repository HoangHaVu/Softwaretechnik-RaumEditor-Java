package roomieboomie.controller;

import javafx.fxml.FXML;
import roomieboomie.business.RoomieBoomieManager;

public class MainMenuController {
    private RootController switcher;
    private RoomieBoomieManager roomieBoomieManager;

    @FXML
    public void choosePlay(){
        switcher.switchView("ChoosePlay");
    }
    @FXML
    public void toTutorial(){
        switcher.switchView("Tutorial");
    }
    @FXML
    public void chooseEdit(){
        switcher.switchView("ChooseEdit");
    }
    @FXML
    public void toHighscore(){
        switcher.switchView("Highscore");
    }

    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }

}

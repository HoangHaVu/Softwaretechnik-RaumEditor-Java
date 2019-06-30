package roomieboomie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import roomieboomie.business.RoomieBoomieManager;



public class LoginController {
    private RootController switcher;
    private boolean adminValue;
    private RoomieBoomieManager roomieBoomieManager;

    @FXML
    private TextField username;

    public void chooseWhichUser(String username){
    }

    @FXML
    public void toMenu(){
        switcher.switchView("MainMenu");
    }

    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }
}

package roomieboomie.controller;

import javafx.fxml.FXML;
import roomieboomie.business.RoomieBoomieManager;

public class ChoosePlayController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;

    @FXML
    public void creativePlayMod() {
        switcher.setCreative(true);
        switcher.switchView("SelectPlayRoom");
    }

    @FXML
    public void levelPlayMod() {
        switcher.setCreative(false);
        switcher.switchView("SelectPlayRoom");
    }

    public void setSwitcher(RootController rootController) {
        this.switcher = rootController;
    }

    public void setRoomieBoomieManager(RoomieBoomieManager roomieBoomieManager) {
        this.roomieBoomieManager = roomieBoomieManager;
    }

    @FXML
    public void backToMenu() {
        switcher.switchView("MainMenu");
    }
}
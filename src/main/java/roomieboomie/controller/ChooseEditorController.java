package roomieboomie.controller;

import javafx.fxml.FXML;
import roomieboomie.business.RoomieBoomieManager;

public class ChooseEditorController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;

    @FXML
    public void createNewRoom() {
        switcher.switchView("LayoutEditor");
    }

    @FXML
    public void loadRoom() {
        switcher.setCreative(true);
        switcher.switchView("SelectRoom");
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
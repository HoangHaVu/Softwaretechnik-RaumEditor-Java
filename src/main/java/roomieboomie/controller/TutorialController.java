package roomieboomie.controller;


import javafx.fxml.FXML;

public class TutorialController {
    private RootController switcher;

    @FXML
    public void backToMenu() {
        switcher.switchView("MainMenu");
    }

    public void setSwitcher(RootController rootController) {
        this.switcher = rootController;
    }

}

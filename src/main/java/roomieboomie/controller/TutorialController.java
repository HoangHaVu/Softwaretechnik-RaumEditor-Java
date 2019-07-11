package roomieboomie.controller;


import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class TutorialController {
    private RootController switcher;

    @FXML
    private ImageView imageView;

    public TutorialController() {
    }

    @FXML
    public void backToMenu() {
        switcher.switchView("MainMenu");
    }

    public void setSwitcher(RootController rootController) {
        this.switcher = rootController;
    }

    public void init(){
        imageView.setImage(new Image("img/tutorial/layout.png"));
    }

}

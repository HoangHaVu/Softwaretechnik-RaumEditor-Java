package roomieboomie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import roomieboomie.persistence.Config;

/**
 * Controller fuer Tutorial View. Zeigt nach Auswahl ueber ComboBox verschiebene Bilder an.
 */
public class TutorialController {
    private RootController switcher;

    @FXML
    private BorderPane root;

    @FXML
    private ImageView imageView;

    @FXML
    private ComboBox<String> choiceBox;

    private String path;
    private Image layoutImage;
    private Image placeableImage;

    public TutorialController() {
    }

    @FXML
    public void backToMenu() {
        switcher.switchView("MainMenu");
    }

    public void setSwitcher(RootController rootController) {
        this.switcher = rootController;
    }

    public void init() {
        path = Config.get().TUTORIALIMAGEPATH();
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        layoutImage = new Image(path + "layout.png");
        placeableImage = new Image(path + "placeable.png");
        imageView.setImage(layoutImage);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((selected, oldValue, newValue) -> {
            if (newValue.equals("Grundriss-Editor")){
                imageView.setImage(layoutImage);
            } else if(newValue.equals("Gegenst\u00e4nde-Editor")){
                imageView.setImage(placeableImage);
            }
        });

        imageView.fitWidthProperty().bind(root.widthProperty().multiply(0.8));
        imageView.fitHeightProperty().bind(root.heightProperty().multiply(0.7));
    }

}

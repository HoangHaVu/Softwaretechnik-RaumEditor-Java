package roomieboomie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.user.User;
import roomieboomie.business.user.UserException;
import roomieboomie.persistence.exception.JsonWritingException;


public class LoginController {
    private RootController switcher;
    private boolean adminValue;
    private RoomieBoomieManager roomieBoomieManager;

    @FXML
    private TextField usernameField;

    public void chooseWhichUser(String username){ //TODO brauchen wir das?

    }

    @FXML
    public void toMenu() {

        if (usernameField.getText().isEmpty()) { // kein Name angegeben
            showAlert("Fehler", "Bitte gib einen Namen ein.");
        } else { // Name angegeben
            String username = usernameField.getText();
            User user;

            if (roomieBoomieManager.getUserMap().containsUsername(username)) { // User bereits vorhanden
                user = roomieBoomieManager.getUserMap().getUser(username);
            } else { // User anlegen und speichern
                user = new User(username);
                roomieBoomieManager.getUserMap().addUser(user);
                try {
                    roomieBoomieManager.getJsonHandler().saveUser(user);
                } catch (JsonWritingException e) {
                    showAlert("Ups!", "Leider konnte dein Profil nicht gespeichert werden. Versuche es erneut.");
                    return;
                }
                showAlert("Hallo!", "Willkommen bei deinem ersten Spiel!");
            }

            roomieBoomieManager.setCurrentUser(user);
            switcher.switchView("MainMenu");
        }

    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            toMenu();
        }
    }

    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }

    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

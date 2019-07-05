package roomieboomie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.user.User;
import roomieboomie.business.user.UserException;
import roomieboomie.persistence.JsonWritingException;


public class LoginController {
    private RootController switcher;
    private boolean adminValue;
    private RoomieBoomieManager roomieBoomieManager;

    @FXML
    private TextField usernameField;

    public void chooseWhichUser(String username){

    }

    @FXML
    public void toMenu(){
        System.out.println();
        if(usernameField.getText().isEmpty()){
            showAlert("Fehler", "Bitte gib einen Namen ein.");
        } else{
            String username = usernameField.getText();
            User user = roomieBoomieManager.getUserMap().getUser(username);
            if (user == null){
                user = new User(username);

                try { //TODO wird eigentlich schon ueberprueft, Exception weglassen?
                    roomieBoomieManager.getUserMap().addUser(user);
                } catch (UserException e) {
                    e.printStackTrace();
                }

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

package roomieboomie.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.persistence.exception.JsonLoadingException;
import roomieboomie.persistence.exception.JsonValidatingException;

import java.io.IOException;

/**
 * RootController ist dafür zuständig die jeweiligen Controller die aufgerufen werden mit Attributen zu befüllen
 */
public class RootController {
    private Stage primaryStage;
    private Scene scene;
    private RoomieBoomieManager roomieBoomieManager;
    private boolean creative;
    private RoomPreview selectedRoom;

    /**
     * SwitchView Methode ist für den Wechsel zwischen den Views zuständig
     *
     * @param view
     */
    public void switchView(String view) {
        switch (view) {
            case "Login":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/LoginView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    scene.getStylesheets().add("application.css");
                    LoginController loginController = loader.getController();
                    loginController.setSwitcher(this);
                    loginController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "MainMenu":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/MainMenuView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    MainMenuController mainMenuController = loader.getController();
                    mainMenuController.setSwitcher(this);
                    mainMenuController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Tutorial":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/TutorialView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    TutorialController tutorialController = loader.getController();
                    tutorialController.setSwitcher(this);
                    tutorialController.init();
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "ChooseEdit":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/ChooseEditView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    ChooseEditorController chooseEditorController = loader.getController();
                    chooseEditorController.setSwitcher(this);
                    chooseEditorController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    break;
                }
                break;
            case "ChoosePlay":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/ChoosePlayView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    ChoosePlayController choosePlayController = loader.getController();
                    choosePlayController.setSwitcher(this);
                    choosePlayController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "highscore":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/HighscoreView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 600);
                    HighscoreController highscoreController = loader.getController();
                    highscoreController.setSwitcher(this);
                    highscoreController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "SelectEditRoom":
                setSelectRoomScene("LayoutEditor_load");
                break;
            case "SelectPlayRoom":
                setSelectRoomScene("Play");
                break;
            case "LayoutEditor":
                LayoutEditorController layoutEditorController = new LayoutEditorController(roomieBoomieManager.getRoomEditor());
                this.scene = new Scene(layoutEditorController.getView(), 1000, 600);
                scene.getStylesheets().add("application.css");
                layoutEditorController.setSwitcher(this);
                primaryStage.setScene(scene);
                primaryStage.show();
                layoutEditorController.refreshView();
                break;

            case "LayoutEditor_load":
                try {
                    roomieBoomieManager.getRoomEditor().loadRoom(selectedRoom, true);
                } catch (JsonValidatingException e) {
                    showAlert("Fehler!", "Oh nein! Der Raum konnte nicht erfolgreich validiert werden.");
                    break;
                } catch (JsonLoadingException e) {
                    showAlert("Fehler!", "Oh nein! Der Raum konnte nicht geladen werden.");
                    break;
                }
                LayoutEditorController layoutEditorController_load = new LayoutEditorController(this.roomieBoomieManager.getRoomEditor());
                this.scene = new Scene(layoutEditorController_load.getView(), 1000, 600);
                scene.getStylesheets().add("application.css");
                layoutEditorController_load.setSwitcher(this);
                primaryStage.setScene(scene);
                primaryStage.show();
                layoutEditorController_load.refreshView();

                break;
            case "PlaceableEditor":
                PlaceableEditorController placeableEditorController = new PlaceableEditorController(roomieBoomieManager.getRoomEditor());
                placeableEditorController.setSwitcher(this);
                Scene scene1 = new Scene(placeableEditorController.getView(), 1000, 600);
                primaryStage.setScene(scene1);
                primaryStage.show();
                placeableEditorController.refreshLayoutView();

                break;
            case "Play":
                try {
                    GameController gameController = new GameController(this, selectedRoom.getFullRoom());
                    scene = new Scene(gameController.getGameview());
                } catch (JsonValidatingException e) {
                    showAlert("Fehler!", "Oh nein! Der Raum konnte nicht erfolgreich validiert werden.");
                    break;
                } catch (JsonLoadingException e) {
                    showAlert("Fehler!", "Oh nein! Der Raum konnte nicht geladen werden.");
                    break;
                }
                roomieBoomieManager.initGame();
                primaryStage.setScene(scene);
                primaryStage.show();
                break;
            case "GameOver":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/GameOverView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    GameOverController gameOverController = loader.getController();
                    gameOverController.setSwitcher(this);
                    gameOverController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "WinLevel":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/WinLevelView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    WinLevelController winLevelController = loader.getController();
                    winLevelController.setSwitcher(this);
                    winLevelController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Zeigt die Raumauswahl-Szene an, wobei das Ziel der Raumauswahl mitgegeben wird
     * @param target Ziel beim Auswaehlen eines Raums ("Play" (Game) oder "LayoutEditor_load" (LayoutEditor))
     */
    private void setSelectRoomScene(String target) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/SelectRoomView.fxml"));
            this.scene = new Scene((Parent) loader.load(), 700, 500);
            SelectRoomController selectRoomController = loader.getController();
            selectRoomController.setSwitcher(this);
            selectRoomController.setSoomselectTarget(target);
            selectRoomController.setCreative(creative);
            selectRoomController.setRoomieBoomieManager(roomieBoomieManager);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Setter-Methode für das Attribut Stage
     *
     * @param stage
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Setter-Methode für das Objekt RoomieBoomieManager
     *
     * @param roomieBoomieManager
     */
    public void setRoomieBoomieManager(RoomieBoomieManager roomieBoomieManager) {
        this.roomieBoomieManager = roomieBoomieManager;
    }

    /**
     * Setter-Methode für den Boolean Wert
     *
     * @param value
     */
    public void setCreative(boolean value) {
        this.creative = value;
    }

    public void setSelectedRoom(RoomPreview roomPreview) {
        this.selectedRoom = roomPreview;
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

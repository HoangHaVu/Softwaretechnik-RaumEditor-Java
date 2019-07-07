package roomieboomie.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.editor.RoomEditor;
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
     * @param view
     */
    public void switchView(String view){
        switch (view){
            case "Login":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/LoginView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
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
                    MainMenuController mainMenuController= loader.getController();
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
                    e.printStackTrace();
                }
                break;
            case "ChoosePlay":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/ChoosePlayView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    ChoosePlayController choosePlayController= loader.getController();
                    choosePlayController.setSwitcher(this);
                    choosePlayController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Highscore":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/HighscoreView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 600);
                    HighscoreController highscoreController= loader.getController();
                    highscoreController.setSwitcher(this);
                    highscoreController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "SelectRoom":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/SelectRoomView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    SelectRoomController selectRoomController= loader.getController();
                    selectRoomController.setSwitcher(this);
                    selectRoomController.setCreative(creative);
                    selectRoomController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                LayoutEditorController layoutEditorController_load = new LayoutEditorController(roomieBoomieManager.getRoomEditor());
                this.scene = new Scene(layoutEditorController_load.getView(), 1000, 600);
                layoutEditorController_load.setSwitcher(this);
                try {
                    roomieBoomieManager.getRoomEditor().loadRoom(selectedRoom,true);
                } catch (JsonValidatingException e) {
                    e.printStackTrace();
                } catch (JsonLoadingException e) {
                    e.printStackTrace();
                }
                primaryStage.setScene(scene);
                primaryStage.show();
                layoutEditorController_load.refreshView();
                break;
            case "PlaceableEditor":
                PlaceableEditorController placeableEditorController=new PlaceableEditorController(roomieBoomieManager.getRoomEditor());
                Scene scene1 = new Scene(placeableEditorController.getView(), 1000, 600);
                primaryStage.setScene(scene1);
                primaryStage.show();
                placeableEditorController.refreshView();

                break;
            case "Play":
                break;

            case "GameOver":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/GameOverView.fxml"));
                    this.scene = new Scene((Parent) loader.load(), 700, 500);
                    GameOverController gameOverController= loader.getController();
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
                    WinLevelController winLevelController= loader.getController();
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
     * Setter Methode für das Attribut Stage
     * @param stage
     */
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    /**
     * Setter Methode für das Objekt RoomieBoomieManager
     * @param roomieBoomieManager
     */
    public void setRoomieBoomieManager(RoomieBoomieManager roomieBoomieManager) {
        this.roomieBoomieManager = roomieBoomieManager;
    }

    /**
     * Setter Methode für den Boolean Wert
     * @param value
     */
    public void setCreative(boolean value){
        this.creative = value;
    }

    public void setSelectedRoom(RoomPreview roomPreview){this.selectedRoom=roomPreview;}
}

package roomieboomie.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.room.RoomPreview;

import java.io.IOException;

/**
 * RootController ist dafür zuständig die jeweiligen Controller die aufgerufen werden mit Attributen zu befüllen
 */
public class RootController {
    private Stage primaryStage;
    private RoomieBoomieManager roomieBoomieManager;
    private boolean creative;
    private RoomPreview selectedRoom;

    /**
     * SwitchView Methode ist für den Wechsel zwischen den Views zuständig
     * @param scene
     */
    public void switchView(String scene){
        switch (scene){
            case "Login":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/LoginView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 500);
                    LoginController loginController = loader.getController();
                    loginController.setSwitcher(this);
                    loginController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "MainMenu":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/MainMenuView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 500);
                    MainMenuController mainMenuController= loader.getController();
                    mainMenuController.setSwitcher(this);
                    mainMenuController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Tutorial":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/TutorialView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 500);
                    TutorialController tutorialController = loader.getController();
                    tutorialController.setSwitcher(this);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "ChooseEdit":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/ChooseEditView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 500);
                    ChooseEditorController chooseEditorController = loader.getController();
                    chooseEditorController.setSwitcher(this);
                    chooseEditorController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "ChoosePlay":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/ChoosePlayView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 500);
                    ChoosePlayController choosePlayController= loader.getController();
                    choosePlayController.setSwitcher(this);
                    choosePlayController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Highscore":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/HighscoreView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 600);
                    HighscoreController highscoreController= loader.getController();
                    highscoreController.setSwitcher(this);
                    highscoreController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "SelectRoom":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/SelectRoomView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 500);
                    SelectRoomController selectRoomController= loader.getController();
                    selectRoomController.setSwitcher(this);
                    selectRoomController.setRoomieBoomieManager(roomieBoomieManager);
                    selectRoomController.setCreative(creative);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "LayoutEditor":
                LayoutEditorController layoutEditorController = new LayoutEditorController(roomieBoomieManager.getRoomEditor());
                Scene sce = new Scene(layoutEditorController.getView(), 1000, 600);
                layoutEditorController.setSwitcher(this);
                primaryStage.setScene(sce);
                primaryStage.show();
                layoutEditorController.refreshView();
                break;
            case "LayoutEditor_load":
                LayoutEditorController layoutEditorController2 = new LayoutEditorController(roomieBoomieManager.getRoomEditor());
                Scene scen = new Scene(layoutEditorController2.getView(), 1000, 600);
                layoutEditorController2.setSwitcher(this);
                //roomieBoomieManager.getRoomEditor().loadRoom(selectedRoom);
                //layoutEditorController2.loadRoom();
                primaryStage.setScene(scen);
                primaryStage.show();
                layoutEditorController2.refreshView();
                break;
            case "PlaceableEditor":
                PlaceableEditorController2 placeableEditorController=new PlaceableEditorController2(roomieBoomieManager.getRoomEditor());
                //Scene scen = new Scene(placeableEditorController.getView(), 1000, 600); TODO
                //primaryStage.setScene(scen); TODO
                primaryStage.show();
                placeableEditorController.refreshView();
                break;
            case "Play":
                break;

            case "GameOver":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/GameOverView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 500);
                    GameOverController gameOverController= loader.getController();
                    gameOverController.setSwitcher(this);
                    gameOverController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "WinLevel":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_views/WinLevelView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 700, 500);
                    WinLevelController winLevelController= loader.getController();
                    winLevelController.setSwitcher(this);
                    winLevelController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
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

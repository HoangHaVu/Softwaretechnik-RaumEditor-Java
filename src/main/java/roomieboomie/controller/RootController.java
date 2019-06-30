package roomieboomie.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import roomieboomie.business.RoomieBoomieManager;

import java.io.IOException;

public class RootController {
    private Stage primaryStage;
    private RoomieBoomieManager roomieBoomieManager;
    private boolean creative;


    public void switchView(String scene){
        switch (scene){
            case "Login":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/LoginView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
                     LoginController loginController= loader.getController();
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
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/MainMenuView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
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
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/TutorialView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
                    TutorialController tutorialController= loader.getController();
                    tutorialController.setSwitcher(this);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "ChooseEdit":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/ChooseEditView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
                    ChooseEditorController chooseEditorController= loader.getController();
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
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/ChoosePlayView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
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
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/Highscore.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
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
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/SelectRoomView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
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
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/LayoutEditorView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
                    LayoutEditorController layoutEditorController= loader.getController();
                    layoutEditorController.setSwitcher(this);
                    layoutEditorController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "PlaceableEditor":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/PlaceableEditorEditorView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
                    PlaceableEditorController placeableEditorController= loader.getController();
                    placeableEditorController.setSwitcher(this);
                    placeableEditorController.setRoomieBoomieManager(roomieBoomieManager);
                    primaryStage.setScene(s);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Play":
                break;

            case "GameOver":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/GameOverView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
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
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/WinLevelView.fxml"));
                    Scene s = new Scene((Parent) loader.load(), 800, 500);
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
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    public void setRoomieBoomieManager(RoomieBoomieManager roomieBoomieManager) {
        this.roomieBoomieManager = roomieBoomieManager;
    }
    public void setCreative(boolean value){
        this.creative = value;
    }
}

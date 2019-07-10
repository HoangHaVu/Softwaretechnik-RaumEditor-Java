package roomieboomie.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;
import roomieboomie.business.user.User;


public class HighscoreController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;
    private HighscoreList highscoreList;

    @FXML
    private TableView<HighscoreRecord> scoreTableView;

    @FXML
    private TableColumn<HighscoreRecord, Number> place;

    @FXML
    private TableColumn<HighscoreRecord, String> username;

    @FXML
    private TableColumn<HighscoreRecord, Integer> points;

    public void init(){
        /*highscoreList = new HighscoreList(); //TODO nur zum Testen, solange es noch keine HighScores gibt
        User user1;
        User user2;
        HighscoreRecord record1;
        HighscoreRecord record2;

        user1 = new User("Joendhardt",3);
        user2 = new User("Joghurta",2);

        record1 = new HighscoreRecord(10, 1000, user1.getName());
        highscoreList.addRecord(record1);
        record2 = new HighscoreRecord(12,1500, user2.getName());
        highscoreList.addRecord(record2);

        setHighscoreList(highscoreList);*/
        setHighscoreList(roomieBoomieManager.getOverallHighscore());

        for (HighscoreRecord record : highscoreList){
            scoreTableView.getItems().add(record);
        }

        scoreTableView.setSelectionModel(null);

        place.setSortable(false);
        username.setSortable(false);
        points.setSortable(false);

        place.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(scoreTableView.getItems().indexOf(c.getValue()) + 1));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        points.setCellValueFactory(new PropertyValueFactory<>("points"));

        place.prefWidthProperty().bind(scoreTableView.widthProperty().multiply(0.196));
        username.prefWidthProperty().bind(scoreTableView.widthProperty().multiply(0.4));
        points.prefWidthProperty().bind(scoreTableView.widthProperty().multiply(0.4));
    }

    public void backToMenu(){
        switcher.switchView("MainMenu");
    }

    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }

    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
        init();
    }

    public void setHighscoreList(HighscoreList highscoreList){
        this.highscoreList = highscoreList;
    }

}

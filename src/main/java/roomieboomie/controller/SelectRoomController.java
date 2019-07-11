package roomieboomie.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.room.RoomMaps;
import roomieboomie.business.room.RoomPreview;

import java.util.Collection;

public class SelectRoomController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;
    private RoomMaps roomMaps;
    private boolean creative;
    private Collection levelRooms;
    private Collection creativeRooms;
    private Collection<RoomPreview> showRooms;
    private ObservableList<RoomPreview> roomlist;
    private RoomPreview selectedRoom;
    private String roomselectTarget;

    @FXML
    private ListView<RoomPreview> roomlistView;

    public SelectRoomController() {
    }

    @FXML
    public void backToMenu() {
        switcher.switchView("ChooseEdit");
    }

    @FXML
    public void loadRoom() {
        switcher.setSelectedRoom(selectedRoom);
        switcher.switchView(roomselectTarget);
    }

    public void setSwitcher(RootController rootController) {
        this.switcher = rootController;
    }

    public void setSoomselectTarget(String target){
        this.roomselectTarget = target;
    }

    public void setRoomieBoomieManager(RoomieBoomieManager roomieBoomieManager) {
        this.roomieBoomieManager = roomieBoomieManager;
        init();
    }

    public void setCreative(boolean value) {
        this.creative = value;
    }

    public void init() {
        this.roomlist = FXCollections.observableArrayList();
        this.selectedRoom = null;
        this.roomMaps = roomieBoomieManager.getRoomMaps();
        this.levelRooms = roomMaps.getLevelRooms();
        this.creativeRooms = roomMaps.getCreativeRooms();
        roomlistView.setOnMouseClicked(event -> {
            this.selectedRoom = roomlistView.getSelectionModel().getSelectedItem();
        });
        if (creative) {
            this.showRooms = creativeRooms;
            fillCells();
        } else {
            this.showRooms = levelRooms;
            fillCells();
        }

    }

    public void fillCells() {
        for (RoomPreview roomPreview : showRooms) {
            roomlist.add(roomPreview);
        }
        roomlistView.setItems(roomlist);
        roomlistView.setCellFactory(new Callback<ListView<RoomPreview>, ListCell<RoomPreview>>() {
            @Override
            public ListCell<RoomPreview> call(ListView<RoomPreview> param) {
                return new Itemcell();
            }
        });
    }

    public class Itemcell extends ListCell<RoomPreview> {
        private Label itemLabel = new Label();
        private HBox root = new HBox();
        private ImageView imageView = new ImageView();


        public Itemcell() {

            root.getChildren().addAll(imageView, itemLabel);
        }

        protected void updateItem(RoomPreview item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty) {
                itemLabel.setText(item.getName());
                Image image = item.getThumbnail();
                if (image.getWidth() > image.getHeight()){
                    imageView.setFitWidth(30);
                } else if (image.getWidth() < image.getHeight()){
                    imageView.setFitHeight(30);
                } else{
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(30);
                }

                imageView.setImage(image);
                imageView.setPreserveRatio(true);
                this.setGraphic(root);
            } else {
                this.setGraphic(null);
            }
        }

    }
}

package roomieboomie.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;
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

    @FXML
    private ListView rooms;

    public SelectRoomController(){
        this.roomlist = FXCollections.observableArrayList();
    }

    @FXML
    public void backToMenu(){
        switcher.switchView("MainMenu");
    }
    @FXML
    public void loadRoom(){
        switcher.switchView("LayoutEditor");

        switcher.switchView("Play");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }
    public void setRoomieBoomieManager (RoomieBoomieManager roomieBoomieManager){
        this.roomieBoomieManager = roomieBoomieManager;
        init();
    }
    public void setCreative(boolean value){
        this.creative = value;
    }
    public void init(){
        setRoomMaps(roomieBoomieManager.getRoomMaps());
        this.levelRooms = roomMaps.getLevelRooms();
        this.creativeRooms = roomMaps.getCreativeRooms();
        if(creative){
            this.showRooms = creativeRooms;
            fillCells();
        }else{
            this.showRooms = levelRooms;
            fillCells();
        }
    }
    public void fillCells(){
        for (RoomPreview roomPreview: showRooms){
            roomlist.add(roomPreview);
        }
        rooms.setItems(roomlist);
        rooms.setCellFactory(new Callback<ListView<RoomPreview>, ListCell<RoomPreview>>() {
            @Override
            public ListCell<RoomPreview> call(ListView<RoomPreview> param) {
                return new Itemcell();
            }

        });
    }

    public void setRoomMaps(RoomMaps roomMaps){
        this.roomMaps = roomMaps;
    }
    public class Itemcell extends ListCell<RoomPreview> {
        private Label itemLabel=new Label();
        private HBox root =new HBox();
        private ImageView image = new ImageView();

        public Itemcell(){
            image.setFitHeight(30);
            image.setFitWidth(30);
            root.getChildren().addAll(image,itemLabel);
        }


        protected void updateItem(RoomPreview item, boolean empty){
            super.updateItem(item, empty);

            if(!empty) {
                itemLabel.setText(item.getName());
                image.setImage(item.getThumbnail());
                this.setGraphic(root);
            } else {
                this.setGraphic(null);
            }

        }


    }
}

package roomieboomie.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.util.Callback;
import roomieboomie.business.editor.PlaceableItemEditor;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.exception.validationExceptions.ItemIsTooCloseToDoorException;
import roomieboomie.business.exception.validationExceptions.ObjectToHighInFrontOfWindowException;
import roomieboomie.business.exception.validationExceptions.PlaceItemIsNotInInteriorException;
import roomieboomie.business.exception.validationExceptions.*;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placeable.PlaceableItem;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.gui.views.PlaceableEditorView;
import roomieboomie.gui.zoompane.ZoomableScrollPane;
import roomieboomie.persistence.Config;
import roomieboomie.persistence.SoundHandler;
import roomieboomie.persistence.exception.JsonWritingException;

public class PlaceableEditorController {

    private enum Action {
        DELETE, PLACE, EDIT
    }

    PlaceableItem selectedItem;
    RootController switcher;
    PlaceableItemEditor placeableItemEditor;
    RoomEditor roomEditor;
    PlaceableEditorView view;
    GridPane raster, interactionRaster, dragRaster, placeableRaster;
    GridPane completeEditor;
    GridPane controlBox;
    GridPane itemPreview;
    Button rotate, delete, edit, finish;
    ScrollPane scrollableRaster;
    Label objectName;
    Action action;
    TextField roomName;
    ZoomableScrollPane zoomPane;
    StackPane zoomAndScroll;
    String backgroundStyle = ("-fx-background-color: black;");
    int actMouseX = 0, actMouseY = 0;
    String iconTexturePath = Config.get().ICONTEXTUREPATH();
    private RoomPreview roomPreview;
    ListView<PlaceableItem> listView;
    ObservableList<PlaceableItem> items;
    HashSet<String> currentlyActiveKeys;
    Label messageLabel;

    public PlaceableEditorController(RoomEditor roomEditor) {
        view = new PlaceableEditorView();
        this.roomEditor = roomEditor;
        this.placeableItemEditor = roomEditor.getPlaceableEditor();
        this.selectedItem = placeableItemEditor.getCurrentItem();
        this.roomPreview = null;
        this.objectName = view.objectName;
        this.raster = view.raster;
        this.completeEditor = view.completeEditor;
        this.controlBox = view.controlBox;
        this.itemPreview = view.itemPreviewGrid;
        this.rotate = view.rotate;
        this.scrollableRaster = view.scrollableRaster;
        this.action = Action.PLACE;
        this.delete = view.delete;
        this.edit = view.edit;
        this.zoomPane = view.zoomPane;
        this.finish = view.finish;
        this.interactionRaster = view.interactionRaster;
        this.zoomAndScroll = view.zoomAndScroll;
        this.dragRaster = view.dragRaster;
        this.listView = view.listView;
        this.roomName = view.roomName;
        this.placeableRaster = view.placeableRaster;
        this.messageLabel = view.messageLabel;
        this.currentlyActiveKeys = view.currentlyActiveKeys;
        initialize();
    }

    private void initialize() {
        items = FXCollections.observableArrayList();
        for (PlaceableItem placeableItem : roomEditor.getPlaceableItemList()) {
            items.add(placeableItem);
        }

        listView.setItems(items);
        listView.setOnMouseClicked(event -> {
            selectedItem = listView.getSelectionModel().getSelectedItem();
            placeableItemEditor.selectPlaceableItem(selectedItem.getType());
            refreshPreviewObject();
        });

        controlBox.prefHeightProperty().bind(view.heightProperty());
        completeEditor.prefWidthProperty().bind(view.widthProperty());
        completeEditor.prefHeightProperty().bind(view.heightProperty());
        raster.setPrefSize(1000, 1000);

        finish.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (!roomName.getText().equals(roomName.getPromptText()) && !roomName.getText().isEmpty() ){
                roomEditor.getRoom().setName(roomName.getText());
                placeableItemEditor.setRoomPlaceableItemList();

                try {
                    roomEditor.saveRoom();
                    showAlert("Super!", "Dein Raum wurde gespeichert und ist jetzt spielbar.");
                    SoundHandler.get().SUCCESSSOUND().play();
                    switcher.switchView("ChooseEdit");
                } catch (JsonWritingException ex) {
                    showAlert("Fehler!", "Raum konnte leider nicht gespeichert werden.");
                    SoundHandler.get().FAILSOUND().play();
                }
            } else{
                showAlert("Fehler!", "Bitte gib deinem Raum noch einen Namen.");
                SoundHandler.get().FAILSOUND().play();
            }

        });

        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.action = Action.EDIT;
            refreshHighlightedButton();
            refreshPlaceableLayout(placeableItemEditor.getPlaceableItemList(), placeableItemEditor.getLayout());
        });

        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (this.action == Action.DELETE) {
                this.action = Action.PLACE;
                refreshHighlightedButton();
                return;
            }
            delete.setFocusTraversable(true);
            this.action = Action.DELETE;
            refreshHighlightedButton();
            refreshPlaceableLayout(placeableItemEditor.getPlaceableItemList(), placeableItemEditor.getLayout());
        });

        rotate.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.rotateItem();
            refreshPreviewObject();
        });

        zoomPane.addEventHandler(ZoomEvent.ZOOM, e -> {

            Scale newScale = new Scale();
            newScale.setPivotX(e.getX());
            newScale.setPivotY(e.getY());
            newScale.setX(zoomPane.getScaleX() * e.getZoomFactor());
            newScale.setY(zoomPane.getScaleY() * e.getZoomFactor());
            zoomPane.getTransforms().add(newScale);

            e.consume();
        });

        view.setOnKeyPressed(e -> {
            Pane item = (Pane) getPlacedObjectPaneByRow(actMouseY, actMouseX, dragRaster);

            Pane clearPane = new Pane();
            GridPane.setConstraints(clearPane, actMouseX, actMouseY);

            if (e.getCode() == KeyCode.D) {
                placeableItemEditor.getCurrentItem().setLength(placeableItemEditor.getCurrentItem().getLength() + 1);
            } else if (e.getCode() == KeyCode.A) {
                placeableItemEditor.getCurrentItem().setLength(placeableItemEditor.getCurrentItem().getLength() - 1);
            } else if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.S) {
                placeableItemEditor.rotateCurrItem();
            }

            String codeString = e.getCode().toString();
            if (!currentlyActiveKeys.contains(codeString)) {
                currentlyActiveKeys.add(codeString);
            }

            movePreviewObject(actMouseX, actMouseY, item, clearPane, false);
            refreshPreviewObject();
        });

        view.setOnKeyReleased(e ->{
            currentlyActiveKeys.remove(e.getCode().toString());

            //scrollableRaster.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
        });

        listView.setCellFactory(new Callback<ListView<PlaceableItem>, ListCell<PlaceableItem>>() {
            @Override
            public ListCell<PlaceableItem> call(ListView<PlaceableItem> param) {
                return new Itemcell();
            }
        });

        refreshLayoutView();
        initInteraction();
        refreshHighlightedButton();
        refreshPreviewObject();
    }

    public void refreshHighlightedButton() {

        if (this.action == Action.DELETE) {
            delete.getStyleClass().add("selected-button");
            edit.getStyleClass().remove("selected-button");
        }

        if (this.action == Action.EDIT) {
            edit.getStyleClass().add("selected-button");
            delete.getStyleClass().remove("selected-button");
        }

        if (this.action == Action.PLACE) {

            edit.getStyleClass().remove("selected-button");
            delete.getStyleClass().remove("selected-button");

        }
    }

    /**
     * - das Pane vom gesetzten Objekt aus der Vorschau in die eigentliche view
     *
     * @param row
     * @param column
     * @param gridPane
     * @return
     */
    public Node getPlacedObjectPaneByRow(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    /**
     * - ist dafür zuständig das jeweilige VorschauObject in der VorschauUmgebung(DragRaster) visuell
     * anhand der x und y Koordinate der Mausposition zu veränder ( koordinaten werden mitegegeben)
     *
     * @param x
     * @param y
     * @param itemPane
     * @param clearPane
     * @param onlyDel
     */
    private void movePreviewObject(int x, int y, Pane itemPane, Pane clearPane, boolean onlyDel) {
        if (this.action != Action.PLACE) return;

        try {
            dragRaster.getChildren().remove(itemPane);
            dragRaster.add(clearPane, x, y);
        } catch (IllegalArgumentException exception) {

        }

        if (onlyDel) return;

        if (placeableItemEditor.getCurrentItem().getOrientation().isVertical()) {
            GridPane.setConstraints(itemPane, x, y, placeableItemEditor.getCurrentItem().getWidth(), placeableItemEditor.getCurrentItem().getLength());
        } else {
            GridPane.setConstraints(itemPane, x, y, placeableItemEditor.getCurrentItem().getLength(), placeableItemEditor.getCurrentItem().getWidth());
        }

        try {
            dragRaster.getChildren().remove(clearPane);
            dragRaster.getChildren().add(itemPane);
        } catch (IllegalArgumentException exception) {

        }
    }

    /**
     * - dafür zuständig wie die die view mit den Interaktionen des Nutzers sich verändert
     * - z.b. was mit dem ausgewählten vorschauObjekt passiert wenn man mit der Maus aus dem Raster rausgeht
     * - welche Methoden aufgerufen werden wenn das vorschauObjekt(dragElement) gesetzt wird / bewegt wird
     */
    public void initInteraction() {
        Pane itemPane = new Pane();
        itemPane.setStyle("-fx-background-color: rgba(0,45,150,0.3);");
        for (int i = 0; i < roomEditor.getRoom().getLayout().length; i++) {
            for (int j = 0; j < roomEditor.getRoom().getLayout()[0].length; j++) {

                int y = j;
                int x = i;
                Pane dragElement = new Pane();
                byte[][] layout = roomEditor.getRoom().getLayout();

                Pane clearPane = new Pane();

                GridPane.setConstraints(clearPane, x, y);
                dragRaster.getChildren().add(clearPane);

                dragElement.setOnMouseEntered(e -> {
                    movePreviewObject(x, y, itemPane, clearPane, false);
                    actMouseX = x;
                    actMouseY = y;
                });

                dragElement.setOnMouseExited(e -> {
                    movePreviewObject(x, y, itemPane, clearPane, true);
                });

                clearPane.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                clearPane.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                itemPane.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                itemPane.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                dragElement.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                dragElement.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));

                dragElement.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    if (this.action == Action.PLACE) {
                        try {
                            placeableItemEditor.placeCurrItem(x, y);
                        } catch (PlaceItemIsNotInInteriorException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        } catch (ObjectToHighInFrontOfWindowException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        } catch (ItemIsTooCloseToDoorException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        } catch (CarpetOnCarpetException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        } catch (PlaceIsAlreadyTakenException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        } catch (ObjectIsNotStorableException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        }
                    }
                    else if (this.action == Action.DELETE)
                        placeableItemEditor.delItem(x,y);
                    else if (this.action == Action.EDIT) {
                        placeableItemEditor.editItem(x,y);
                        this.action = Action.PLACE;
                        refreshHighlightedButton();
                        //edit.setStyle("");
                        refreshPreviewObject();
                    }
                    refreshPlaceableLayout(placeableItemEditor.getPlaceableItemList(), placeableItemEditor.getLayout());
                    e.consume();
                });

                GridPane.setConstraints(dragElement, i, j);
                interactionRaster.getChildren().add(dragElement);
            }
        }
    }

    public void setSwitcher(RootController rootController) {
        this.switcher = rootController;
    }

    /**
     * - Texture des Bildes wird nochmal geladen? TODO
     * - für die Vorschau des Objektes zuständig
     */
    public void refreshPreviewObject() {

        PlaceableItem item = placeableItemEditor.getCurrentItem();
        objectName.setText(item.getType().getName());
        int size = 17;
        Image textureImage = item.getTextureImage();

        if (item.getLength() > size) {
            return;
        }

        Pane itemPane = new Pane();

        ImageView texture = new ImageView(textureImage);

        texture.fitWidthProperty().bind(itemPane.widthProperty());
        texture.fitHeightProperty().bind(itemPane.heightProperty());
        texture.setPreserveRatio(true);

        view.itemPreviewGrid.getChildren().clear();
        /*
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Pane clearPane = new Pane();
                clearPane.prefHeightProperty().bind(view.itemPreviewGrid.heightProperty().divide(size));
                clearPane.prefWidthProperty().bind(view.itemPreviewGrid.widthProperty().divide(size));
                GridPane.setConstraints(clearPane, x, y, 1, 1);
                view.itemPreviewGrid.getChildren().add(clearPane);
            }
        }*/

        itemPane.prefHeightProperty().bind(view.itemPreviewGrid.heightProperty());
        itemPane.prefWidthProperty().bind(view.itemPreviewGrid.widthProperty());


        GridPane.setConstraints(itemPane, item.getLength() / 2, item.getWidth() / 2, item.getLength(), item.getWidth());
        if (item.getOrientation().isVertical()) {
            itemPane.setRotate(90);
        }

        itemPane.getChildren().add(texture);
        view.itemPreviewGrid.getChildren().add(itemPane);//TODO
    }

    public void refreshPlaceableLayout(ArrayList<PlaceableItem> placeableItems, byte[][]layout){
        placeableRaster.getChildren().clear();

        for (int i = 0; i < placeableItemEditor.getLayout().length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Pane element = new Pane();

                element.prefHeightProperty().bind(view.raster.widthProperty().divide(placeableItemEditor.getLayout()[0].length));
                element.prefWidthProperty().bind(view.raster.widthProperty().divide(placeableItemEditor.getLayout()[0].length));

                GridPane.setConstraints(element, i, j);

                placeableRaster.getChildren().add(element);
            }
        }

        for(PlaceableItem placeable : placeableItems){

            PlaceableItem currItem = placeable;
            int x = 0;
            int y = 0;

            do{
                Pane item = new Pane();
                Image textureImage = currItem.getTextureImage();
                item.prefHeightProperty().bind(view.placeableRaster.widthProperty().divide(layout[0].length));
                item.prefWidthProperty().bind(view.placeableRaster.widthProperty().divide(layout[0].length));
                x += currItem.getX();
                y += currItem.getY();
                if (currItem.getOrientation().isVertical()){
                    GridPane.setConstraints(item, x, y, currItem.getWidth(), currItem.getLength());
                } else{
                    GridPane.setConstraints(item ,x,y, currItem.getLength(), currItem.getWidth());
                }

                ImageView texture = new ImageView(textureImage);

                texture.fitWidthProperty().bind(item.widthProperty());
                texture.fitHeightProperty().bind(item.heightProperty());

                item.getChildren().add(texture);

                placeableRaster.getChildren().add(item);

                currItem = currItem.getNext();

            } while (currItem != null);
        }
    }

    public void updateItems(ArrayList<LayoutItem> items, byte[][]layout){

        for(LayoutItem w : items){

            Pane item = new Pane();
            Image textureImage;

            item.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
            item.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
            item.getStyleClass().add("layout-item");
            if (w.getOrientation().isVertical()){

                GridPane.setConstraints(item, w.getX(), w.getY(), w.getWidth(), w.getLength());
                if (w.getType() == LayoutItemType.WALL) textureImage = new Image(iconTexturePath + "wallTextureVertical.jpg");
                else if (w.getType() == LayoutItemType.WINDOW) textureImage = new Image(iconTexturePath + "windowTextureVertical.jpg");
                else textureImage = new Image(iconTexturePath + "doorTextureVertical.png");

            } else{

                GridPane.setConstraints(item, w.getX(), w.getY(), w.getLength(), w.getWidth());
                if (w.getType() == LayoutItemType.WALL) textureImage = new Image(iconTexturePath + "wallTextureHorizontal.jpg");
                else if (w.getType() == LayoutItemType.WINDOW) textureImage = new Image(iconTexturePath + "windowTextureHorizontal.jpg");
                else textureImage = new Image(iconTexturePath + "doorTextureHorizontal.png");

            }

            ImageView texture = new ImageView(textureImage);

            texture.fitWidthProperty().bind(item.widthProperty());
            texture.fitHeightProperty().bind(item.heightProperty());

            item.getChildren().add(texture);
            raster.getChildren().add(item);
        }
    }

    /**
     * Updatet die eigentliche View
     * Setzt jeweils für jedes Pixel die bestimmte Farbe je nachdem was im Byte Array steht
     * Ruft jeweils die Methode setItemsIntoView auf um die gesetzten Objekte anzuzeigen
     */
    public void refreshLayoutView() {
        byte[][] layout = roomEditor.getRoom().getLayout();
        view.raster.getChildren().clear();
        Image textureImage = new Image(iconTexturePath + "grassTexture.jpg");

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Pane element = new Pane();

                element.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                element.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));

                GridPane.setConstraints(element, i, j);

                if (i % 20 == 0 && j % 20 == 0) {

                    String path = iconTexturePath + "raufaserTextur.jpg";
                    element.setStyle("-fx-background-image: url('" + path + "'); " +
                            "-fx-background-position: center center; " +
                            "-fx-background-repeat: stretch;" +
                            "-fx-background-size: cover; -fx-border-color: rgba(0,0,0, .15);" +
                            "-fx-border-width: 5 5 5 5;");

                    GridPane.setConstraints(element, i, j, 20, 20);

                } else if (i % 5 == 0 && j % 5 == 0) {
                    element.setStyle("-fx-border-color: rgba(0,0,0, .2);" +
                            "-fx-border-width: 1 1 0 1;");
                    GridPane.setConstraints(element, i, j, 5, 5);
                }

                raster.getChildren().add(element);

                if (layout[j][i] == 0) {

                    Pane floor = new Pane();
                    floor.setStyle("-fx-background-color: rgba(68, 200, 155, .2)");
                    GridPane.setConstraints(floor, i, j);
                    raster.getChildren().add(floor);
                }
            }
        }


        updateItems(roomEditor.getRoom().getWalls(),roomEditor.getRoom().getLayout());
        updateItems(roomEditor.getRoom().getWindows(),roomEditor.getRoom().getLayout());
        updateItems(roomEditor.getRoom().getDoors(),roomEditor.getRoom().getLayout());

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Zeigt eine Fehlermeldung an und blendet sie nach 2 Sekunden wieder aus
     * @param message Text, der angezeigt werden soll
     */
    private void showMessageLabel(String message){
        new Thread(() -> {
            Platform.runLater( () -> messageLabel.setText(message));
            messageLabel.setVisible(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageLabel.setVisible(false);
        }).start();
    }

    public Pane getView() {
        return this.view;
    }

    private class Itemcell extends ListCell<PlaceableItem> {
        private Label itemLabel = new Label();
        private HBox root = new HBox();
        private ImageView image = new ImageView();

        public Itemcell() {
            image.setFitHeight(30);
            image.setFitWidth(30);
            root.getChildren().addAll(image, itemLabel);
        }

        protected void updateItem(PlaceableItem item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty) {
                itemLabel.setText(item.getType().getName());
                Image i = item.getTextureImage();
                if (i == null) {
                    String noDirectory = Config.get().NOPICTUREPATH();
                    try {
                        i = new Image(new FileInputStream(noDirectory));
                        image.setImage(i);
                    } catch (FileNotFoundException e) {
                        System.err.println("Anzeigebild des Objekts wurde nicht gefunden");
                    }
                } else {
                    image.setImage(i);
                }
                this.setGraphic(root);
            } else {
                this.setGraphic(null);
            }

        }
    }
}
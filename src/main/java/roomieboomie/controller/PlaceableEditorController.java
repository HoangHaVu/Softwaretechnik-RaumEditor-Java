package roomieboomie.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
import roomieboomie.business.editor.PlaceableEditor;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.gui.views.PlaceableEditorView;
import roomieboomie.gui.zoompane.ZoomableScrollPane;
import roomieboomie.persistence.Config;
import roomieboomie.persistence.exception.JsonWritingException;

public class PlaceableEditorController {

    private enum Action {
        DELETE, PLACE, EDIT
    }
    PlacableItem selectedItem;
    RootController switcher;
    PlaceableEditor placeableEditor;
    RoomEditor roomEditor;
    PlaceableEditorView view;
    GridPane raster, interactionRaster, dragRaster;
    GridPane completeEditor;
    GridPane controlBox;
    GridPane itemPreview;
    Button rotate, delete, edit, finish;
    ScrollPane scrollableRaster;
    Label objectName;
    Action action;
    ZoomableScrollPane zoomPane;
    StackPane zoomAndScroll;
    String backgroundStyle = ("-fx-background-color: black;");
    int actMouseX = 0, actMouseY = 0;
    String iconTexturePath = Config.get().ICONTEXTUREPATH();
    private RoomPreview roomPreview;
    ListView<PlacableItem> listView = new ListView<PlacableItem>();
    ObservableList<PlacableItem> items;

    public PlaceableEditorController(RoomEditor roomEditor) {
        view = new PlaceableEditorView();
        this.roomEditor = roomEditor;
        this.placeableEditor = roomEditor.getPlaceableEditor();
        this.selectedItem = placeableEditor.getCurrentItem();
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
        initialize();
    }

    private void initialize() {
        items = FXCollections.observableArrayList();
        for (PlacableItem placableItem : roomEditor.getPlacableItemList()) {
            items.add(placableItem);
        }

        listView.setItems(items);
        listView.setOnMouseClicked(event -> {
            selectedItem = listView.getSelectionModel().getSelectedItem();
            placeableEditor.selectPlaceableItem(selectedItem.getType());
            refreshPreviewObject();
        });

        controlBox.prefHeightProperty().bind(view.heightProperty());
        completeEditor.prefWidthProperty().bind(view.widthProperty());
        completeEditor.prefHeightProperty().bind(view.heightProperty());
        raster.setPrefSize(1000, 1000);

        finish.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                roomEditor.saveRoom();
            } catch (JsonWritingException ex) {
                showAlert("Fehler!", "Ups, dein Raum kommte leider nicht gespeichert werden.");
            }
            refreshView();
        });

        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.action = Action.EDIT;
            refreshHighlightedButton();
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
                placeableEditor.getCurrentItem().setLength(placeableEditor.getCurrentItem().getLength() + 1);
            } else if (e.getCode() == KeyCode.A) {
                placeableEditor.getCurrentItem().setLength(placeableEditor.getCurrentItem().getLength() - 1);
            } else if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.S) {
                placeableEditor.rotateItem();
            }

            movePreviewObject(actMouseX, actMouseY, item, clearPane, false);
            refreshPreviewObject();
        });

        listView.setCellFactory(new Callback<ListView<PlacableItem>, ListCell<PlacableItem>>() {
            @Override
            public ListCell<PlacableItem> call(ListView<PlacableItem> param) {
                return new Itemcell();
            }
        });


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

            /*
            if (roomEditor.getCurrLayoutItem().getType() == LayoutItemType.DOOR) {
                door.getStyleClass().add("selected-button");
            } else if (roomEditor.getCurrLayoutItem().getType() == LayoutItemType.WINDOW) {
                window.getStyleClass().add("selected-button");
            } else if (roomEditor.getCurrLayoutItem().getType() == LayoutItemType.WALL) {
                wall.getStyleClass().add("selected-button");
            }*/
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

        if (placeableEditor.getCurrentItem().getOrientation() == Orientation.TOP || placeableEditor.getCurrentItem().getOrientation() == Orientation.BOTTOM) {
            GridPane.setConstraints(itemPane, x, y, placeableEditor.getCurrentItem().getWidth(), placeableEditor.getCurrentItem().getLength());
        } else {
            GridPane.setConstraints(itemPane, x, y, placeableEditor.getCurrentItem().getLength(), placeableEditor.getCurrentItem().getWidth());
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
        itemPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
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
                    if (this.action == Action.PLACE) placeableEditor.placeCurrItem(x, y);
                    else if (this.action == Action.DELETE)
                        roomEditor.deleteItem(roomEditor.getRoom().getLayout()[y][x]); //FIXME TODO
                    else if (this.action == Action.EDIT) {

                        roomEditor.editItem(roomEditor.getRoom().getLayout()[y][x]);
                        this.action = Action.PLACE;
                        refreshHighlightedButton();
                        //edit.setStyle("");
                        refreshPreviewObject();
                    }
                    refreshView();
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

        PlacableItem item = placeableEditor.getCurrentItem();
        objectName.setText(item.getType().getName());
        int size = 19;
        Image textureImage;

        if (item.getLength() > size) {
            return;
        }

        Pane itemPane = new Pane();
        itemPane.setStyle("-fx-background-color: rgb(1,1,1)");
        /*if (item.getType() == LayoutItemType.WALL) {
            textureImage = new Image(iconTexturePath + "wallTextureHorizontal.jpg");
        } else if (item.getType() == LayoutItemType.WINDOW) {
            textureImage = new Image(iconTexturePath + "windowTextureHorizontal.jpg");
        } else {
            textureImage = new Image(iconTexturePath + "doorTextureHorizontal.png");
        }

        ImageView texture = new ImageView(textureImage);
        texture.fitWidthProperty().bind(itemPane.widthProperty());
        texture.fitHeightProperty().bind(itemPane.heightProperty());*/

        view.itemPreviewGrid.getChildren().clear();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Pane clearPane = new Pane();
                clearPane.prefHeightProperty().bind(view.itemPreviewGrid.heightProperty().divide(size));
                clearPane.prefWidthProperty().bind(view.itemPreviewGrid.widthProperty().divide(size));
                GridPane.setConstraints(clearPane, x, y, 1, 1);
                view.itemPreviewGrid.getChildren().add(clearPane);
            }
        }

        itemPane.prefHeightProperty().bind(view.itemPreviewGrid.heightProperty().divide(size));
        itemPane.prefWidthProperty().bind(view.itemPreviewGrid.widthProperty().divide(size));

        GridPane.setConstraints(itemPane, size / 2 - item.getLength() / 2, size / 2 - item.getWidth() / 2, item.getLength(), item.getWidth());
        if (item.getOrientation() == Orientation.TOP || item.getOrientation() == Orientation.BOTTOM) {
            itemPane.setRotate(90);
        }

        //itemPane.getChildren().add(texture);
        view.itemPreviewGrid.getChildren().add(itemPane);
    }

    /**
     * - new Method selectItemTexture!!! - zum laden der Texture TODO
     * - das gesetzte Objekt wird im backend gespeichert/ im array gesetzt
     * - bestimmt auch diese Fläche des Objektes also die Breite und die Länge des Objektes und wie das angezeigt wird
     * - und auf der richtigen View angezeigt
     *
     * @param items
     * @param layout
     */
    //public void setItemsIntoView(ArrayList<LayoutItem> items, byte[][] layout) {
    public void setItemsIntoView(ArrayList<PlacableItem> items, byte[][] layout) {
        for (PlacableItem w : items) {

            Pane item = new Pane();
            Image textureImage;

            item.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
            item.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
            item.getStyleClass().add("layout-item");
            //item.setStyle(style);
            if (w.getOrientation() == Orientation.TOP || w.getOrientation() == Orientation.BOTTOM) {

                GridPane.setConstraints(item, w.getX(), w.getY(), w.getWidth(), w.getLength());
                /*if (w.getType() == LayoutItemType.WALL)
                    textureImage = new Image(iconTexturePath + "wallTextureVertical.jpg");
                else if (w.getType() == LayoutItemType.WINDOW)
                    textureImage = new Image(iconTexturePath + "windowTextureVertical.jpg");
                else textureImage = new Image(iconTexturePath + "doorTextureVertical.png");*/

            } else {

                GridPane.setConstraints(item, w.getX(), w.getY(), w.getLength(), w.getWidth());
                /*if (w.getType() == LayoutItemType.WALL)
                    textureImage = new Image(iconTexturePath + "wallTextureHorizontal.jpg");
                else if (w.getType() == LayoutItemType.WINDOW)
                    textureImage = new Image(iconTexturePath + "windowTextureHorizontal.jpg");
                else textureImage = new Image(iconTexturePath + "doorTextureHorizontal.png");
                //textureImage = new Image(iconTexturePath + "wallTextureHorizontal.jpg");*/

            }
            /*
            ImageView texture = new ImageView(textureImage);

            texture.fitWidthProperty().bind(item.widthProperty());
            texture.fitHeightProperty().bind(item.heightProperty());*/

            //item.getChildren().add(texture);
            item.setStyle("-fx-background-color: rgb(1,1,1)");
            raster.getChildren().add(item);
        }
    }

    /**
     * Updatet die eigentliche View
     * Setzt jeweils für jedes Pixel die bestimmte Farbe je nachdem was im Byte Array steht
     * Ruft jeweils die Methode setItemsIntoView auf um die gesetzten Objekte anzuzeigen
     */
    public void refreshView() {

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

                    element.setStyle("-fx-background-image: url('" + iconTexturePath + "raufaserTextur.jpg" + "'); " +
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



        setItemsIntoView(placeableEditor.getPlacableItems(),layout);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Pane getView() {
        return this.view;
    }

    private class Itemcell extends ListCell<PlacableItem> {
        private Label itemLabel = new Label();
        private HBox root = new HBox();
        private ImageView image = new ImageView();

        public Itemcell() {
            image.setFitHeight(30);
            image.setFitWidth(30);
            root.getChildren().addAll(image, itemLabel);
        }

        protected void updateItem(PlacableItem item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty) {
                itemLabel.setText(item.getType().getName());
                Image i = item.getImage();
                if (i == null) {
                    String noDirectory = Config.get().NOPICTUREPATH();
                    try {
                        i = new Image(new FileInputStream(noDirectory));
                        image.setImage(i);
                    } catch (FileNotFoundException e) {
                        System.out.println("Anzeigebild des Objekts wurde nicht gefunden");
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
package roomieboomie.controller;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.gui.views.PlaceableEditorView;
import roomieboomie.gui.zoompane.ZoomableScrollPane;

public class PlaceableEditorController {
    private RoomieBoomieManager roomieBoomieManager;
    private RootController switcher;
    private RoomEditor roomEditor;
    private PlaceableEditorView view;

    private enum Action {
        DELETE, PLACE, EDIT
    }

    private GridPane raster, interactionRaster, dragRaster;
    private GridPane completeEditor;
    private VBox controlBox;
    private GridPane itemPreview;
    private Button rotate, delete, edit, finish;
    private ScrollPane scrollableRaster;
    private Slider sizeSlider;
    private PlaceableEditorController.Action action;
    private ZoomableScrollPane zoomPane;
    private StackPane zoomAndScroll;
    private EventHandler refreshDrag;

    public PlaceableEditorController(RoomEditor roomeditor) {
        this.view = new PlaceableEditorView();
        this.roomEditor = roomeditor;
        this.raster = view.raster;
        this.completeEditor = view.completeEditor;
        this.controlBox = view.controlBox;
        this.itemPreview = view.itemPreview;
        this.rotate = view.rotate;
        this.scrollableRaster = view.scrollableRaster;
        this.action = PlaceableEditorController.Action.PLACE;
        this.delete = view.delete;
        this.edit = view.edit;
        this.zoomPane = view.zoomPane;
        this.finish = view.finish;
        this.interactionRaster = view.interactionRaster;
        this.zoomAndScroll = view.zoomAndScroll;
        this.dragRaster = view.dragRaster;
    }

    private void initialize() {
        view.controlBox.prefHeightProperty().bind(view.heightProperty());
        view.completeEditor.prefWidthProperty().bind(view.widthProperty());
        view.completeEditor.prefHeightProperty().bind(view.heightProperty());
        view.raster.setPrefSize(1000, 1000);
        view.itemPreview.setPrefSize(200, 200);

        finish.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.saveRoom();
            refreshView();
        });

        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.action = PlaceableEditorController.Action.EDIT;
            edit.setStyle("-fx-border-color: red;");
            delete.setStyle("");
        });

        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.action = PlaceableEditorController.Action.DELETE;
            delete.setStyle("-fx-border-color: red;");
            edit.setStyle("");
        });

        rotate.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.rotateItem();
            refreshPreview();
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

        sizeSlider.valueChangingProperty().addListener((observable, wasChanging, isChanging) -> {
            if (!isChanging) {
                roomEditor.changeLength((float) sizeSlider.getValue());
                refreshPreview();
            }
        });

        view.setOnKeyPressed(e -> {

            if (e.getCode() == KeyCode.D) {
                roomEditor.getActLayoutItem().setLength(roomEditor.getActLayoutItem().getLength() + 1);
            } else if (e.getCode() == KeyCode.A) {
                roomEditor.getActLayoutItem().setLength(roomEditor.getActLayoutItem().getLength() - 1);
            } else if (e.getCode() == KeyCode.W) {
                roomEditor.rotateItem();
            }
        });

    }

    public void refreshPreview() {
        byte[][] layout = roomEditor.getPreviewLayout();

        view.itemPreview = new GridPane();

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Pane element = new Pane();

                element.prefHeightProperty().bind(itemPreview.widthProperty().divide(layout[0].length));
                element.prefWidthProperty().bind(itemPreview.widthProperty().divide(layout[0].length));

                if (layout[j][i] > 0) {
                    element.setStyle("-fx-background-color: black;");
                } else if (layout[j][i] < -2) {
                    element.setStyle("-fx-background-color: lightblue;");
                } else if (layout[j][i] == -2) {
                    element.setStyle("-fx-background-color: brown;");
                } else {
                    element.setStyle("-fx-background-color: grey;");
                }

                itemPreview.setConstraints(element, i, j);
                itemPreview.getChildren().add(element);

                initInteractionPane();
            }
        }
    }

    public void initInteractionPane() {
        Pane itemPane = new Pane();
        itemPane.setStyle("-fx-background-color: green;");

        for (int i = 0; i < roomEditor.getRoom().getLayout().length; i++) {
            for (int j = 0; j < roomEditor.getRoom().getLayout()[0].length; j++) {

                int y = j;
                int x = i;
                Pane dragElement = new Pane();
                byte[][] layout = roomEditor.getRoom().getLayout();

                Pane clearPane = new Pane();

                GridPane.setConstraints(clearPane, x, y);
                dragRaster.getChildren().add(clearPane);

                dragElement.setOnMouseMoved(e -> {
                    if (this.action != PlaceableEditorController.Action.PLACE) return;

                    if (roomEditor.getActLayoutItem().getOrientation() == Orientation.TOP || roomEditor.getActLayoutItem().getOrientation() == Orientation.BOTTOM) {
                        GridPane.setConstraints(itemPane, x, y, roomEditor.getActLayoutItem().getWidth(), roomEditor.getActLayoutItem().getLength());
                    } else {
                        GridPane.setConstraints(itemPane, x, y, roomEditor.getActLayoutItem().getLength(), roomEditor.getActLayoutItem().getWidth());
                    }

                    try {
                        dragRaster.getChildren().remove(clearPane);
                        dragRaster.getChildren().add(itemPane);
                    } catch (IllegalArgumentException exception) {

                    }
                });

                dragElement.setOnMouseExited(e -> {

                    if (this.action != PlaceableEditorController.Action.PLACE) return;

                    try {
                        dragRaster.getChildren().remove(itemPane);
                        dragRaster.add(clearPane, x, y);
                    } catch (IllegalArgumentException exception) {

                    }
                });

                clearPane.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                clearPane.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                itemPane.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                itemPane.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                dragElement.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                dragElement.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));

                dragElement.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    if (this.action == PlaceableEditorController.Action.PLACE) roomEditor.placeActItem(x, y);
                    else if (this.action == PlaceableEditorController.Action.DELETE)
                        roomEditor.deleteItem(roomEditor.getRoom().getLayout()[y][x]);
                    else if (this.action == PlaceableEditorController.Action.EDIT) {

                        roomEditor.editItem(roomEditor.getRoom().getLayout()[y][x]);
                        this.action = PlaceableEditorController.Action.PLACE;
                        edit.setStyle("");
                        refreshPreview();
                    }


                    refreshView();
                    e.consume();
                });

                GridPane.setConstraints(dragElement, i, j);
                interactionRaster.getChildren().add(dragElement);
            }
        }
    }

    public void refreshView() {

        byte[][] layout = roomEditor.getRoom().getLayout();
        view.raster.getChildren().clear();

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {

                int y = j;
                int x = i;
                int itemWidth = 1;
                int itemHeight = 1;
                Pane element = new Pane();

                element.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                element.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));

                if (layout[j][i] > 0) {
                    element.setStyle("-fx-background-color: black;");
                } else if (layout[j][i] < -2) {
                    element.setStyle("-fx-background-color: lightblue;");
                } else if (layout[j][i] == -2) {
                    element.setStyle("-fx-background-color: brown;");
                } else if (layout[j][i] == 0) {
                    element.setStyle("-fx-background-color: green;");
                } else {
                    element.setStyle("-fx-background-color: #B2B2B2;");
                }

                //dragElement.setStyle("-fx-background-color: rgba(255, 255, 255, 0);");

                GridPane.setConstraints(element, i, j);
                raster.getChildren().add(element);
            }
        }
    }

    public void saveRoom() {
        switcher.switchView("MainMenu");
    }

    public void backToLayout() {
        switcher.switchView("SelectRoom");
    }

    public void addPlaceableItem() {

    }

    public void setSwitcher(RootController rootController) {
        this.switcher = rootController;
    }

    public void setRoomieBoomieManager(RoomieBoomieManager roomieBoomieManager) {
        this.roomieBoomieManager = roomieBoomieManager;
        init();
    }

    public void init() {
        setRoomEditor(roomieBoomieManager.getRoomEditor());
    }

    public void setRoomEditor(RoomEditor roomEditor) {
        this.roomEditor = roomEditor;
    }

    public Pane getView() {
        return this.view;
    }
}

package roomieboomie.controller;

import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.exception.validationExceptions.*;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.gui.views.LayoutEditorView;
import roomieboomie.gui.zoompane.ZoomableScrollPane;
import roomieboomie.persistence.Config;
import roomieboomie.persistence.SoundHandler;

public class LayoutEditorController {

    private enum Action {
        DELETE, PLACE, EDIT
    }

    RootController switcher;
    RoomEditor roomEditor;
    LayoutEditorView view;
    GridPane raster, interactionRaster, dragRaster;
    GridPane completeEditor;
    GridPane controlBox;
    GridPane itemPreview;
    Pane wall, window, door;
    Button rotate, delete, edit, finish,backToMenu;
    ScrollPane scrollableRaster;
    Slider sizeSlider;
    Action action;
    ZoomableScrollPane zoomPane;
    StackPane zoomAndScroll;
    String backGroundStyle = ("-fx-background-color: black;");
    int currMouseX = 0, currMouseY = 0;
    String iconTexturePath = Config.get().ICONTEXTUREPATH();
    HashSet<String> currentlyActiveKeys;
    Label messageLabel;

    /**
     * Konstruktor des LayoutEditorControllers
     * die jeweiligen Variablen werden verwiesen
     * @param roomEditor
     */
    public LayoutEditorController(RoomEditor roomEditor) {
        view = new LayoutEditorView();
        this.roomEditor = roomEditor;
        this.raster = view.raster;
        this.completeEditor = view.completeEditor;
        this.controlBox = view.controlBox;
        this.itemPreview = view.itemPreviewGrid;
        this.wall = view.wall;
        this.rotate = view.rotate;
        this.scrollableRaster = view.scrollableRaster;
        this.sizeSlider = view.sizeSlider;
        this.window = view.window;
        this.door = view.door;
        this.backToMenu = view.backToMenu;
        this.action = Action.PLACE;
        this.delete = view.delete;
        this.edit = view.edit;
        this.zoomPane = view.zoomPane;
        this.finish = view.finish;
        this.interactionRaster = view.interactionRaster;
        this.zoomAndScroll = view.zoomAndScroll;
        this.dragRaster = view.dragRaster;
        this.currentlyActiveKeys = view.currentlyActiveKeys;
        this.messageLabel = view.messageLabel;
        initialize();
    }

    /**
     * Methode initialize setzt die Funktionen für die jeweiligen
     * Buttons, installiert die ganzen Listener und startet die jeweiligen Nutzer bezogenen Funktionen
     */
    private void initialize() {

        controlBox.prefHeightProperty().bind(view.heightProperty());
        completeEditor.prefWidthProperty().bind(view.widthProperty());
        completeEditor.prefHeightProperty().bind(view.heightProperty());
        raster.setPrefSize(1000, 1000);

        finish.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            refreshView();
            try {
                if (roomEditor.validateRoom()){
                    SoundHandler.get().SUCCESSSOUND().play();
                    switcher.switchView("PlaceableEditor");
                } else{
                    showAlert("Grundriss noch nicht fertig","Fenster, Tuer und geschlossener Raum benoetigt.");
                }
            } catch (MissingWindowException ex) {
                showAlert("Grundriss noch nicht fertig", ex.getMessage());
            } catch (MissingDoorException ex) {
                showAlert("Grundriss noch nicht fertig", ex.getMessage());
            } catch (getIntoRoomException ex) {
                showAlert("Grundriss noch nicht fertig", ex.getMessage());
            }
        });

        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (this.action == Action.EDIT){
                this.action = Action.PLACE;
                refreshHighlightedButton();
                return;
            }
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

        door.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            roomEditor.selectnewItem(LayoutItemType.DOOR);
            this.action = Action.PLACE;
            refreshHighlightedButton();
            refreshPreview();
        });

        window.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.selectnewItem(LayoutItemType.WINDOW);
            this.action = Action.PLACE;
            refreshHighlightedButton();
            refreshPreview();
        });

        wall.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.selectnewItem(LayoutItemType.WALL);
            this.action = Action.PLACE;
            refreshHighlightedButton();
            refreshPreview();
        });

        rotate.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.rotateItem();
            refreshPreview();
        });

        backToMenu.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            switcher.switchView("MainMenu");
        });


        sizeSlider.valueProperty().addListener((observable, oldvar, newvar) -> {

            roomEditor.changeLength(newvar.floatValue());
            refreshPreview();
        });

        view.setOnKeyPressed(e -> {
            Pane item = (Pane) getNodeByRowColumnIndex(currMouseY, currMouseX, dragRaster);

            Pane clearPane = new Pane();
            GridPane.setConstraints(clearPane, currMouseX, currMouseY);

            if (e.getCode() == KeyCode.D) {
                roomEditor.getCurrLayoutItem().setLength(roomEditor.getCurrLayoutItem().getLength() + 1);
            } else if (e.getCode() == KeyCode.A) {
                roomEditor.getCurrLayoutItem().setLength(roomEditor.getCurrLayoutItem().getLength() - 1);
            } else if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.S) {
                roomEditor.rotateItem();
            } 

            String codeString = e.getCode().toString();
            if (!currentlyActiveKeys.contains(codeString)) {
                currentlyActiveKeys.add(codeString);
            }


            refreshDragPane(currMouseX, currMouseY, item, clearPane, false);
            refreshPreview();
        });
        view.setOnKeyReleased(e ->{
            currentlyActiveKeys.remove(e.getCode().toString());

        });

        initInteractionPane();
        refreshHighlightedButton();
        refreshPreview();
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

    public void refreshHighlightedButton(){

        if (this.action == Action.DELETE){
            delete.getStyleClass().add("selected-button");
            edit.getStyleClass().remove("selected-button");
            wall.getStyleClass().remove("selected-button");
            window.getStyleClass().remove("selected-button");
            door.getStyleClass().remove("selected-button");
            updateSelectedButton();
        } 

        if (this.action == Action.EDIT){
            edit.getStyleClass().add("selected-button");
            delete.getStyleClass().remove("selected-button");
            wall.getStyleClass().remove("selected-button");
            window.getStyleClass().remove("selected-button");
            door.getStyleClass().remove("selected-button");
            updateSelectedButton();
        }

        if (this.action == Action.PLACE){

            edit.getStyleClass().remove("selected-button");
            delete.getStyleClass().remove("selected-button");
            wall.getStyleClass().remove("selected-button");
            window.getStyleClass().remove("selected-button");
            door.getStyleClass().remove("selected-button");
            updateSelectedButton();

            if (roomEditor.getCurrLayoutItem().getType() == LayoutItemType.DOOR){
                door.getStyleClass().add("selected-button");
            } else if (roomEditor.getCurrLayoutItem().getType() == LayoutItemType.WINDOW){
                window.getStyleClass().add("selected-button");
            }else if (roomEditor.getCurrLayoutItem().getType() == LayoutItemType.WALL){
                wall.getStyleClass().add("selected-button");
            }
        }
    }

    public void updateSelectedButton(){
        LayoutItem item = roomEditor.getCurrLayoutItem();
        
        wall.prefWidthProperty().unbind();
        door.prefWidthProperty().unbind();
        window.prefWidthProperty().unbind();

        if (this.action == Action.PLACE){
            if (item.getType() == LayoutItemType.WALL){

                wall.setMaxWidth(160);
                wall.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.4));
                window.setMaxWidth(100);
                window.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.25));
                door.setMaxWidth(100);
                door.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.25));
    
            } else if(item.getType() == LayoutItemType.WINDOW){
    
                window.setMaxWidth(160);
                window.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.4));
                wall.setMaxWidth(100);
                wall.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.25));
                door.setMaxWidth(100);
                door.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.25));
    
            } else if (item.getType() == LayoutItemType.DOOR){
    
                door.setMaxWidth(160);
                door.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.4));
                wall.setMaxWidth(100);
                wall.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.25));
                window.setMaxWidth(100);
                window.prefWidthProperty().bind(view.selectItemPane.widthProperty().multiply(0.25));
    
            }
        } 

    }

    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
    
        for (Node node : childrens) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    /**
     * TODO
     * @param x
     * @param y
     * @param itemPane
     * @param clearPane
     * @param onlyDel
     */
    private void refreshDragPane(int x, int y, Pane itemPane, Pane clearPane, boolean onlyDel){
        if (this.action != Action.PLACE) return;

            try{
                dragRaster.getChildren().remove(itemPane);
                dragRaster.add(clearPane, x, y);
            } catch(IllegalArgumentException exception){

            }
        
        if (onlyDel) return;
                    
        if (roomEditor.getCurrLayoutItem().getOrientation().isVertical()){
            GridPane.setConstraints(itemPane, x, y,roomEditor.getCurrLayoutItem().getWidth(),roomEditor.getCurrLayoutItem().getLength());
        } else{
            GridPane.setConstraints(itemPane, x, y, roomEditor.getCurrLayoutItem().getLength(), roomEditor.getCurrLayoutItem().getWidth());
        }

        try{
            dragRaster.getChildren().remove(clearPane);
            dragRaster.getChildren().add(itemPane);
        } catch(IllegalArgumentException exception){
            
        }
    }

    /**
     * diese Methode ist dafür zuständig die Interaktionen des Nutzers visuell anzuzeigen
     * bedeutet wenn er ein Objekt auswählt und sich entscheidet wohin er es setzt (Objekt Preview)
     * das Objekt platziert wird
     * und die jeweiligen Veränderungen im RoomEditor
     */
    public void initInteractionPane(){
        Pane itemPane = new Pane();
        itemPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        for(int i = 0; i < roomEditor.getRoom().getLayout().length; i++){
            for(int j = 0; j < roomEditor.getRoom().getLayout()[0].length; j++){

                int y = j;
                int x = i;
                Pane dragElement = new Pane();
                byte[][] layout = roomEditor.getRoom().getLayout();
                
                Pane clearPane = new Pane();
                
                GridPane.setConstraints(clearPane, x, y);
                dragRaster.getChildren().add(clearPane);

                dragElement.setOnMouseEntered(e->{
                   refreshDragPane(x, y, itemPane, clearPane, false);
                   currMouseX = x;
                   currMouseY = y;
                });

                dragElement.setOnMouseExited(e->{
                    refreshDragPane(x, y, itemPane, clearPane, true);
                });

                clearPane.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                clearPane.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                itemPane.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                itemPane.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                dragElement.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                dragElement.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));

                dragElement.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
                    if (this.action == Action.PLACE) {
                        try {
                            roomEditor.placeCurrItem(x, y);
                        } catch (WindowMissplaceException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        } catch (LayoutItemMissplaceException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        } catch (DoorMissplaceException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        } catch (WallMissplaceException ex) {
                            showMessageLabel(ex.getMessage());
                            SoundHandler.get().FAILSOUND().play();
                        }
                    }
                    else if (this.action == Action.DELETE) roomEditor.deleteItem(roomEditor.getRoom().getLayout()[y][x]);
                    else if (this.action == Action.EDIT) {

                        roomEditor.editItem(roomEditor.getRoom().getLayout()[y][x]);
                        this.action = Action.PLACE;
                        refreshHighlightedButton();
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

    /**
     * RootController wird gesetzt
     * @param rootController
     */
    public void setSwitcher(RootController rootController) {
        this.switcher = rootController;
    }


    public void refreshPreview(){

        LayoutItem item = roomEditor.getCurrLayoutItem();
        int size = 19;
        Image textureImage;

        if (item.getLength() > size){
            return;
        }

        Pane itemPane = new Pane();
        if (item.getType() == LayoutItemType.WALL){
            textureImage = new Image(iconTexturePath + "wallTextureHorizontal.jpg");
        } else if(item.getType() == LayoutItemType.WINDOW){
            textureImage = new Image(iconTexturePath + "windowTextureHorizontal.jpg");
        } else {
            textureImage = new Image(iconTexturePath + "doorTextureHorizontal.png");
        }
        
        ImageView texture = new ImageView(textureImage);
        texture.fitWidthProperty().bind(itemPane.widthProperty());
        texture.fitHeightProperty().bind(itemPane.heightProperty());

        view.itemPreviewGrid.getChildren().clear();

        for (int x = 0; x < size; x++){
            for (int y = 0; y < size ; y++){
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
        if (item.getOrientation().isVertical()){
            itemPane.setRotate(90);
        }
        
        itemPane.getChildren().add(texture);
        view.itemPreviewGrid.getChildren().add(itemPane);
    }

    /**
     * Diese Methoden setzt die Items in ein Raster
     * gibt die Texturen der jeweiligen Items mit deren Texturen
     *
     * @param items
     * @param layout
     */
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
     * updated die View und befüllt das Raster mit der dementsprechenden Farbe des Pixels 
     */
    public void refreshView(){

        byte[][] layout = roomEditor.getRoom().getLayout();
        view.raster.getChildren().clear();

        for (int i = 0; i < layout.length; i++){
            for(int j = 0; j < layout[0].length; j++){
                Pane element = new Pane();
                
                element.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                element.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));


                GridPane.setConstraints(element, i, j);

                if (i % 20 == 0 && j % 20 == 0){

                    String path = iconTexturePath + "raufaserTextur.jpg";
                    element.setStyle("-fx-background-image: url('"+ path + "'); " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: stretch;" +
                    "-fx-background-size: cover; -fx-border-color: rgba(0,0,0, .15);" + 
                    "-fx-border-width: 5 5 5 5;");
                    
                    GridPane.setConstraints(element, i, j, 20, 20);
        
                } 
                else if (i % 5 == 0 && j % 5 == 0){
                    element.setStyle("-fx-border-color: rgba(0,0,0, .2);" + 
                    "-fx-border-width: 1 1 0 1;");
                    GridPane.setConstraints(element, i, j, 5, 5);
                }
              
                raster.getChildren().add(element);

                if (layout[j][i] == 0){

                    Pane floor = new Pane();
                    floor.setStyle("-fx-background-color: rgba(68, 200, 155, .2)");
                    GridPane.setConstraints(floor, i, j);
                    raster.getChildren().add(floor);
                }
            }
        }

        updateItems(roomEditor.getRoom().getWalls(), layout);
        updateItems(roomEditor.getRoom().getWindows(), layout);
        updateItems(roomEditor.getRoom().getDoors(), layout);
       
    }

    /**
     * wirft eine Warnung raus wenn der Raum nicht standardgemäß validiert werden kann
     * @param title
     * @param message
     */
    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Pane getView(){
        return this.view;
    }
}

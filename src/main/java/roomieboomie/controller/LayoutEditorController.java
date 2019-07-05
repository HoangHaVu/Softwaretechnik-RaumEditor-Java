package roomieboomie.controller;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.gui.views.LayoutEditorView;
import roomieboomie.gui.zoompane.ZoomableScrollPane;
import roomieboomie.persistence.Config;

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
    Button rotate, delete, edit, finish;
    ScrollPane scrollableRaster;
    Slider sizeSlider;
    Action action;
    ZoomableScrollPane zoomPane;
    StackPane zoomAndScroll;
    String backGroundStyle=("-fx-background-color: black;");
    int actMouseX = 0, actMouseY = 0;
    
    public LayoutEditorController(RoomEditor roomEditor){
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
        this.action = Action.PLACE;
        this.delete = view.delete;
        this.edit = view.edit;
        this.zoomPane = view.zoomPane;
        this.finish = view.finish;
        this.interactionRaster = view.interactionRaster;
        this.zoomAndScroll = view.zoomAndScroll;
        this.dragRaster = view.dragRaster;
        initialize();
    }

    private void initialize(){

        controlBox.prefHeightProperty().bind(view.heightProperty());
        completeEditor.prefWidthProperty().bind(view.widthProperty());
        completeEditor.prefHeightProperty().bind(view.heightProperty());
        raster.setPrefSize(1000, 1000);

        finish.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            roomEditor.saveRoom();
            refreshView();
        });
        
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            this.action = Action.EDIT;
            //edit.setStyle("-fx-border-color: red;");
            //delete.setStyle("");
        });

        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (this.action == Action.DELETE){
                this.action = Action.PLACE;
                //delete.setStyle("");
                return;
            }

            this.action = Action.DELETE;
            //delete.setStyle("-fx-border-color: red;");
            //edit.setStyle("");
        });

        door.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.selectnewItem(LayoutItemType.DOOR);
            //delete.setStyle("");
            //edit.setStyle("");
            this.action = Action.PLACE;
            refreshPreview();
            updateSelectedButton();
        });

        window.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.selectnewItem(LayoutItemType.WINDOW);
            //delete.setStyle("");
            //edit.setStyle("");
            this.action = Action.PLACE;
            refreshPreview();
            updateSelectedButton();
        });

        wall.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            roomEditor.selectnewItem(LayoutItemType.WALL);
            //delete.setStyle("");
            //edit.setStyle("");
            this.action = Action.PLACE;
            refreshPreview();
            updateSelectedButton();
        });

        rotate.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            roomEditor.rotateItem();
            refreshPreview();
        });

        zoomPane.addEventHandler(ZoomEvent.ZOOM, e ->{
            Scale newScale = new Scale();
            newScale.setPivotX(e.getX());
            newScale.setPivotY(e.getY());
            newScale.setX( zoomPane.getScaleX() * e.getZoomFactor() );
            newScale.setY( zoomPane.getScaleY() * e.getZoomFactor() );

            zoomPane.getTransforms().add(newScale);

            e.consume();
        });

        sizeSlider.valueProperty().addListener((observable, oldvar, newvar) -> {
           
            roomEditor.changeLength(newvar.floatValue());
            refreshPreview();
            
        });
        
        view.setOnKeyPressed(e ->{
            Pane item = (Pane) getNodeByRowColumnIndex(actMouseY, actMouseX, dragRaster);
            
            Pane clearPane = new Pane();
            GridPane.setConstraints(clearPane, actMouseX, actMouseY);

            
            if (e.getCode() == KeyCode.D){
                roomEditor.getActLayoutItem().setLength(roomEditor.getActLayoutItem().getLength() + 1);
            } else if(e.getCode() == KeyCode.A){
                roomEditor.getActLayoutItem().setLength(roomEditor.getActLayoutItem().getLength() - 1);
            } else if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.S){
                roomEditor.rotateItem();
            }
            actualizeDragPane(actMouseX, actMouseY, item, clearPane, false);
            refreshPreview();


        });

        initInteractionPane();
        updateSelectedButton();
        refreshPreview();

    }

    public void updateSelectedButton(){
        LayoutItem item = roomEditor.getActLayoutItem();
        
        wall.prefWidthProperty().unbind();
        door.prefWidthProperty().unbind();
        window.prefWidthProperty().unbind();


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

    private void actualizeDragPane(int x, int y, Pane itemPane, Pane clearPane, boolean onlyDel){

        if (this.action != Action.PLACE) return;

        
            try{
                dragRaster.getChildren().remove(itemPane);
                dragRaster.add(clearPane, x, y);
            } catch(IllegalArgumentException exception){

            }
        
        if (onlyDel) return;
                    
        if (roomEditor.getActLayoutItem().getOrientation() == Orientation.TOP || roomEditor.getActLayoutItem().getOrientation() == Orientation.BOTTOM ){
            GridPane.setConstraints(itemPane, x, y,roomEditor.getActLayoutItem().getWidth(),roomEditor.getActLayoutItem().getLength());
        } else{
            GridPane.setConstraints(itemPane, x, y, roomEditor.getActLayoutItem().getLength(), roomEditor.getActLayoutItem().getWidth());
        }

        
        try{
            dragRaster.getChildren().remove(clearPane);
            dragRaster.getChildren().add(itemPane);
        } catch(IllegalArgumentException exception){
            
        }
    }

    public void initInteractionPane(){


        Pane itemPane = new Pane();
        itemPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
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
                   actualizeDragPane(x, y, itemPane, clearPane, false);
                   actMouseX = x;
                   actMouseY = y;

                });
                

                dragElement.setOnMouseExited(e->{

                    actualizeDragPane(x, y, itemPane, clearPane, true);
                    
                });
                clearPane.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                clearPane.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                itemPane.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                itemPane.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                dragElement.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                dragElement.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));

                dragElement.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
                    if (this.action == Action.PLACE) roomEditor.placeActItem(x, y);
                    else if (this.action == Action.DELETE) roomEditor.deleteItem(roomEditor.getRoom().getLayout()[y][x]);
                    else if (this.action == Action.EDIT) {

                        roomEditor.editItem(roomEditor.getRoom().getLayout()[y][x]);
                        this.action = Action.PLACE;
                        //edit.setStyle("");
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
    public void setSwitcher(RootController rootController){
        this.switcher=rootController;
    }



    public void refreshPreview(){

        
        LayoutItem item = roomEditor.getActLayoutItem();
        int size = 19;
        Image textureImage;

        if (item.getLength() > size){
            return;
        }
        
        
        Pane itemPane = new Pane();
        if (item.getType() == LayoutItemType.WALL){
            textureImage = new Image("iconsandtextures/wallTextureHorizontal.jpg");
        } else if(item.getType() == LayoutItemType.WINDOW){
            textureImage = new Image("iconsandtextures/windowTextureHorizontal.jpg");
        } else {
            textureImage = new Image("iconsandtextures/doorTextureHorizontal.png");
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
        if (item.getOrientation() == Orientation.TOP|| item.getOrientation() == Orientation.BOTTOM){
            itemPane.setRotate(90);
            
        }
        
        itemPane.getChildren().add(texture);
        view.itemPreviewGrid.getChildren().add(itemPane);
                
    }

    public void updateItems(ArrayList<LayoutItem> items, byte[][]layout){
        
        

        for(LayoutItem w : items){

            Pane item = new Pane();
            Image textureImage;
            
            
            item.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
            item.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
            //item.setStyle(style);
            if (w.getOrientation() == Orientation.TOP || w.getOrientation() == Orientation.BOTTOM){

                GridPane.setConstraints(item, w.getX(), w.getY(), w.getWidth(), w.getLength());
                if (w.getType() == LayoutItemType.WALL) textureImage = new Image("iconsandtextures/wallTextureVertical.jpg");
                else if (w.getType() == LayoutItemType.WINDOW) textureImage = new Image("iconsandtextures/windowTextureVertical.jpg");
                else textureImage = new Image("iconsandtextures/doorTextureVertical.png");

            } else{

                GridPane.setConstraints(item, w.getX(), w.getY(), w.getLength(), w.getWidth());
                if (w.getType() == LayoutItemType.WALL) textureImage = new Image("iconsandtextures/wallTextureHorizontal.jpg");
                else if (w.getType() == LayoutItemType.WINDOW) textureImage = new Image("iconsandtextures/windowTextureHorizontal.jpg");
                else textureImage = new Image("iconsandtextures/doorTextureHorizontal.png");
                //textureImage = new Image("iconsandtextures/wallTextureHorizontal.jpg");

            }

            ImageView texture = new ImageView(textureImage);

            texture.fitWidthProperty().bind(item.widthProperty());
            texture.fitHeightProperty().bind(item.heightProperty());

            item.getChildren().add(texture);
            raster.getChildren().add(item);
        }
    }

    public void refreshView(){

        byte[][] layout = roomEditor.getRoom().getLayout();
        view.raster.getChildren().clear();
        Image textureImage = new Image("iconsandtextures/grassTexture.jpg");

        for (int i = 0; i < layout.length; i++){
            for(int j = 0; j < layout[0].length; j++){
                Pane element = new Pane();
                
                //ImageView texture = new ImageView(textureImage);
                //texture.fitWidthProperty().bind(element.widthProperty());
                //texture.fitHeightProperty().bind(element.heightProperty());
                element.prefHeightProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                element.prefWidthProperty().bind(view.raster.widthProperty().divide(layout[0].length));
                if (i % 20 == 0 && j % 20 == 0){
                    
                    element.setStyle("-fx-background-image: url('"+"iconsandtextures/concreteTexture.jpg"+ "'); " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: stretch;" +
                    "-fx-background-size: cover");


                    GridPane.setConstraints(element, i, j, 20, 20);
                    /*
                    element.getChildren().add(texture);
                    */
                } 
                else 
                GridPane.setConstraints(element, i, j);
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

    public Pane getView(){
        return this.view;
    }
}

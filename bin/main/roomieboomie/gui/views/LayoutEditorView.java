package roomieboomie.gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import roomieboomie.gui.zoompane.ZoomableScrollPane;

public class LayoutEditorView extends Pane {

    public GridPane raster = new GridPane();
    public GridPane interactionRaster = new GridPane();
    public GridPane completeEditor = new GridPane();
    public VBox controlBox = new VBox();
    public ScrollPane scrollableRaster;
    public GridPane itemPreview = new GridPane();
    public Button finish = new Button("FERTIG");
    public Slider sizeSlider = new Slider(0, 1, 0.5);
    public HBox sliderPane = new HBox();
    public Button rotate = new Button("rotate");
    public Button delete = new Button("delete");
    public Button edit = new Button("edit");
    public HBox objectInteraction = new HBox();
    public VBox buttonPane = new VBox();
    public Pane wall = new Pane();
    public Pane window = new Pane();
    public Pane door = new Pane();
    public HBox selectItemPane = new HBox();
    public ZoomableScrollPane zoomPane;
    public StackPane zoomAndScroll;
    public GridPane dragRaster = new GridPane();

    public LayoutEditorView() {

        

        zoomAndScroll = new StackPane();
        Pane lineColor = new Pane();
        lineColor.setStyle("-fx-background-color:black;");
        zoomAndScroll.getChildren().addAll(lineColor, raster, dragRaster, interactionRaster);
        zoomPane = new ZoomableScrollPane(zoomAndScroll, "-fx-background-color: #AFAFAF");

        scrollableRaster = new ScrollPane(zoomPane);
        
        
        ColumnConstraints layoutCol = new ColumnConstraints();
        ColumnConstraints controlCol = new ColumnConstraints();

        zoomPane.prefWidthProperty().bind(scrollableRaster.widthProperty());
        zoomPane.prefHeightProperty().bind(scrollableRaster.heightProperty());
        
        scrollableRaster.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollableRaster.setHbarPolicy(ScrollBarPolicy.NEVER);
        
        
        
        wall.setMinSize(60, 60);
        window.setMinSize(60, 60);
        door.setMinSize(60, 60);
        wall.setStyle("-fx-background-color:grey;");
        window.setStyle("-fx-background-color:grey;");
        door.setStyle("-fx-background-color:grey;");


        wall.getChildren().add(new Label("wall"));
        window.getChildren().add(new Label("window"));
        door.getChildren().add(new Label("door"));
        
        selectItemPane.setAlignment(Pos.CENTER);
        selectItemPane.setSpacing(30);
        buttonPane.setAlignment(Pos.CENTER);
        objectInteraction.setAlignment(Pos.CENTER);
        sliderPane.setAlignment(Pos.CENTER);
        sliderPane.setSpacing(10);
        layoutCol.setPercentWidth(70);
        controlCol.setPercentWidth(30);
        buttonPane.setSpacing(25);
        objectInteraction.setSpacing(10);
        controlBox.setPadding(new Insets(40, 20, 40, 20));
        controlBox.setSpacing(20);

        selectItemPane.getChildren().addAll(wall, window, door);
        buttonPane.getChildren().addAll(selectItemPane, sliderPane, objectInteraction, finish);
        objectInteraction.getChildren().addAll(rotate, edit, delete);
        sliderPane.getChildren().addAll(new Label("Länge"), sizeSlider);
        controlBox.getChildren().addAll(itemPreview, buttonPane);
        completeEditor.getColumnConstraints().addAll(layoutCol, controlCol);
        completeEditor.add(scrollableRaster, 0, 0);
        completeEditor.add(controlBox, 1, 0);
        this.getChildren().add(completeEditor);
        
    }



}
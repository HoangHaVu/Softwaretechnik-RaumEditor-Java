package roomieboomie.gui.views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.gui.zoompane.ZoomableScrollPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PlaceableEditorView extends Pane {

    public GridPane raster = new GridPane();
    public GridPane interactionRaster = new GridPane();
    public GridPane completeEditor = new GridPane();
    public VBox controlBox = new VBox();
    public ScrollPane scrollableRaster;
    public GridPane itemPreview = new GridPane();
    public Button finish = new Button("FERTIG");
    public Slider sizeSlider = new Slider(0, 1, 0.5);
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
    public ListView<PlacableItem> listview=new ListView<PlacableItem>();

    public PlaceableEditorView() {



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
        layoutCol.setPercentWidth(70);
        controlCol.setPercentWidth(30);
        buttonPane.setSpacing(25);
        objectInteraction.setSpacing(10);
        controlBox.setPadding(new Insets(40, 20, 40, 20));
        controlBox.setSpacing(20);







        selectItemPane.getChildren().addAll(wall, window, door);
        buttonPane.getChildren().addAll(selectItemPane,listview, objectInteraction, finish);
        objectInteraction.getChildren().addAll(rotate, edit, delete);
        controlBox.getChildren().addAll(itemPreview, buttonPane);
        completeEditor.getColumnConstraints().addAll(layoutCol, controlCol);
        completeEditor.add(scrollableRaster, 0, 0);
        completeEditor.add(controlBox, 1, 0);
        this.getChildren().add(completeEditor);

    }


}
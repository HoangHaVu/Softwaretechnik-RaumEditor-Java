package roomieboomie.gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.SVGPath;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.gui.zoompane.ZoomableScrollPane;

public class PlaceableEditorView extends Pane {

    public GridPane raster = new GridPane();
    public GridPane interactionRaster = new GridPane();
    public GridPane completeEditor = new GridPane();
    public GridPane controlBox = new GridPane();
    public ScrollPane scrollableRaster;
    public GridPane itemPreviewGrid = new GridPane();
    public Button finish = new Button("FERTIG");
    public Label objectName = new Label();
    public TextField roomName = new TextField();
    public Button rotate = new Button();
    public Button delete = new Button();
    public Button edit = new Button();
    public HBox objectInteraction = new HBox();
    public VBox buttonPane = new VBox();
    public ZoomableScrollPane zoomPane;
    public StackPane zoomAndScroll;
    public GridPane dragRaster = new GridPane();
    public HBox itemPreviewPane = new HBox();
    public HBox savePane = new HBox();
    public ListView<PlacableItem> listView=new ListView<PlacableItem>();

    public PlaceableEditorView() {
        zoomAndScroll = new StackPane();
        zoomAndScroll.getChildren().addAll(raster, dragRaster, interactionRaster);
        zoomPane = new ZoomableScrollPane(zoomAndScroll, "-fx-background-color: #cacaca");
        scrollableRaster = new ScrollPane(zoomPane);

        raster.getStyleClass().add("grid");


        rotate.setStyle("-fx-shape: \"" + LayoutItemType.svgToPath("rotate") + "\";");
        edit.setStyle("-fx-shape: \"" + LayoutItemType.svgToPath("edit") + "\";");
        delete.setStyle("-fx-shape: \"" + LayoutItemType.svgToPath("delete") + "\";");
        rotate.getStyleClass().add("action-button");
        edit.getStyleClass().add("action-button");
        delete.getStyleClass().add("action-button");

        /*
        SVGPath shape = new SVGPath();
        shape.setContent(LayoutItemType.svgToPath("delete"));
        wall.setStyle("-fx-background-image: url(\" iconsandtextures/wallTexture.jpg\"); -fx-background-repeat: no-repeat;-fx-background-size: cover;");
        wall.setShape(shape);
        */



        zoomPane.prefWidthProperty().bind(scrollableRaster.widthProperty());
        zoomPane.prefHeightProperty().bind(scrollableRaster.heightProperty());
        controlBox.prefHeightProperty().bind(this.heightProperty());
        completeEditor.prefWidthProperty().bind(this.widthProperty());
        completeEditor.prefHeightProperty().bind(this.heightProperty());
        raster.setPrefSize(1000, 1000);
        itemPreviewGrid.prefHeightProperty().bind(controlBox.widthProperty());
        itemPreviewGrid.prefWidthProperty().bind(controlBox.widthProperty());
        itemPreviewGrid.maxWidthProperty().bind(itemPreviewGrid.heightProperty());
        itemPreviewGrid.setAlignment(Pos.CENTER);
        itemPreviewPane.setAlignment(Pos.CENTER);


        controlBox.setAlignment(Pos.CENTER);
        ColumnConstraints layoutCol = new ColumnConstraints();
        ColumnConstraints controlCol = new ColumnConstraints();
        RowConstraints previewRow = new RowConstraints();
        RowConstraints interactionRow = new RowConstraints();

        /*

        zoomPane.prefWidthProperty().bind(scrollableRaster.widthProperty());
        zoomPane.prefHeightProperty().bind(scrollableRaster.heightProperty());
        */
        scrollableRaster.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollableRaster.setHbarPolicy(ScrollBarPolicy.NEVER);

        //wall.setStyle("-fx-background-color:grey;");
        //window.setStyle("-fx-background-color:grey;");
        //door.setStyle("-fx-background-color:grey;");






        rotate.minHeightProperty().bind(rotate.widthProperty());
        rotate.maxHeightProperty().bind(rotate.widthProperty());
        edit.minHeightProperty().bind(edit.widthProperty());
        edit.maxHeightProperty().bind(edit.widthProperty());
        delete.minHeightProperty().bind(delete.widthProperty());
        delete.maxHeightProperty().bind(delete.widthProperty());

        rotate.prefWidthProperty().bind(objectInteraction.widthProperty().multiply(0.2));
        edit.prefWidthProperty().bind(objectInteraction.widthProperty().multiply(0.2));
        delete.prefWidthProperty().bind(objectInteraction.widthProperty().multiply(0.2));

        rotate.setMaxWidth(50);
        edit.setMaxWidth(50);
        delete.setMaxWidth(50);


        roomName.setPromptText("Raum Name");
        finish.setMinWidth(120);
        finish.prefWidthProperty().bind(buttonPane.widthProperty().multiply(0.7));
        finish.minHeightProperty().bind(finish.widthProperty().divide(5));
        finish.maxHeightProperty().bind(finish.widthProperty().divide(5));
        finish.getStyleClass().add("submit-button");

        buttonPane.setAlignment(Pos.CENTER);
        objectInteraction.setAlignment(Pos.CENTER);

        layoutCol.setPercentWidth(70);
        controlCol.setPercentWidth(30);
        interactionRow.setPercentHeight(75);
        previewRow.setPercentHeight(25);

        buttonPane.setSpacing(10);
        objectInteraction.setSpacing(30);
        controlBox.setPadding(new Insets(40, 20, 40, 20));
        //controlBox.setSpacing(20);

        VBox regionVerticalForButtons0, regionVerticalForButtons1, regionVerticalForButtons2, regionVerticalForButtons3, regionVerticalForButtons4;
        regionVerticalForButtons0 = new VBox();
        regionVerticalForButtons1 = new VBox();
        regionVerticalForButtons2 = new VBox();
        regionVerticalForButtons3 = new VBox();
        regionVerticalForButtons4 = new VBox();



        VBox.setVgrow(regionVerticalForButtons0, Priority.ALWAYS);
        VBox.setVgrow(regionVerticalForButtons1, Priority.ALWAYS);
        VBox.setVgrow(regionVerticalForButtons2, Priority.ALWAYS);
        VBox.setVgrow(regionVerticalForButtons3, Priority.ALWAYS);
        VBox.setVgrow(regionVerticalForButtons4, Priority.ALWAYS);


        completeEditor.getColumnConstraints().addAll(layoutCol, controlCol);
        controlBox.getRowConstraints().addAll(previewRow, interactionRow);

        savePane.getChildren().addAll(roomName,finish);
        itemPreviewPane.getChildren().add(itemPreviewGrid);
        buttonPane.getChildren().addAll(objectName,regionVerticalForButtons0, listView, regionVerticalForButtons1, regionVerticalForButtons2,
                objectInteraction, regionVerticalForButtons3,savePane, regionVerticalForButtons4);
        objectInteraction.getChildren().addAll(rotate, edit, delete);
        //controlBox.getChildren().addAll(itemPreviewGrid, buttonPane);
        controlBox.add(itemPreviewPane, 0, 0);
        controlBox.add(buttonPane, 0, 1);
        completeEditor.add(scrollableRaster, 0, 0);
        completeEditor.add(controlBox, 1, 0);
        this.getChildren().add(completeEditor);


    }





}
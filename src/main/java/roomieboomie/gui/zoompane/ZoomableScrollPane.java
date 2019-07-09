package roomieboomie.gui.zoompane;

import java.util.HashSet;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ZoomableScrollPane extends ScrollPane {
    private double scaleValue = 0.7;
    private double zoomIntensity = 0.02;
    private Node target;
    private Node zoomNode;
    private HashSet<String> currentlyActiveKeys;

    public ZoomableScrollPane(Node target) {
        super();
        this.target = target;
        
        this.zoomNode = new Group(target);
        
        Node outerNode = outerNode(zoomNode);
        //outerNode.setStyle("-fx-background-color: black;");
        setContent(outerNode);

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //center
        setFitToWidth(true); //center

        updateScale();
    }

    public ZoomableScrollPane(Node target, HashSet<String> currentlyActiveKeys, String style){
        super();
        this.target = target;
        this.currentlyActiveKeys = currentlyActiveKeys;
        this.zoomNode = new Group(target);
        
        Node outerNode = outerNode(zoomNode);
        outerNode.setStyle(style);
        setContent(outerNode);

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //center
        setFitToWidth(true); //center

        updateScale();
    }

    
    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnZoom(e -> {
            e.consume();
            
            onZoom(e.getZoomFactor(), new Point2D(e.getX(), e.getY()));
        });
        outerNode.setOnScroll(e->{
            if (currentlyActiveKeys.contains("ALT"))
                ifScrolled(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
            
        });
        return outerNode;
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void updateScale() {
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }
    public void ifScrolled(double wheelDelta, Point2D mousePoint){
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);
        onZoom(zoomFactor, mousePoint);
    }

    private void onZoom(double zoomFactor, Point2D mousePoint) {

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();
        // calculate pixel offsets from [0, 1] range
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        scaleValue = scaleValue * zoomFactor;
        updateScale();
        this.layout(); // refresh ScrollPane scroll positions & target bounds

        // convert target coordinates to zoomTarget coordinates
        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

        // calculate adjustment of scroll position (pixels)
        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }
}
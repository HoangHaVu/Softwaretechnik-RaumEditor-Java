package persitence;

import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.persistence.ImageHandler;

public class ImageHandlerTest {
    @Before
    public void init(){

    }

    @Test
    public void testPlacableImage(){
        PlacableItem item = new PlacableItem(0,0, Orientation.TOP, PlacableItemType.DINO);
        Image img = item.getTextureImage();
        //TODO einfach assert if kein fileerror
    }
}

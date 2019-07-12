import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.exception.validationExceptions.DoorMissplaceException;
import roomieboomie.business.exception.validationExceptions.LayoutItemMissplaceException;
import roomieboomie.business.exception.validationExceptions.WallMissplaceException;
import roomieboomie.business.exception.validationExceptions.WindowMissplaceException;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placeable.PlaceableItem;
import roomieboomie.business.item.placeable.PlaceableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.validation.Validator;
import roomieboomie.persistence.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorTest {
    LayoutItem wall;
    LayoutItem window;
    LayoutItem door;
    Validator validator;
    PlaceableItem dino;
    byte[][] layout;

    @Before
    public void init(){
        Room r= new Room(60,60,null);
        validator = new Validator();
        layout= r.getLayout();
        wall=new LayoutItem(LayoutItemType.WALL,1,1,10,1, Orientation.TOP);
        window=new LayoutItem(LayoutItemType.WINDOW,2,2,5,1, Orientation.TOP);
        door=new LayoutItem(LayoutItemType.DOOR,3,3,2,1, Orientation.TOP);
        for (int i=0;i<10;i++){
            for(int j =0;j<10;j++){
                layout[i][j]= Config.get().LAYOUTEXTERIORVALUE();
            }
        }


    }
    @Test
    public void validateLayoutTests() throws LayoutItemMissplaceException, WallMissplaceException, DoorMissplaceException, WindowMissplaceException {
        boolean success = true;

        assertTrue(validator.validateLayoutPlacement(wall, layout));
        try {
            validator.validateLayoutPlacement(window, layout);
        } catch (WallMissplaceException e) {
            success = false;
        }
        try {
            validator.validateLayoutPlacement(door, layout);
        } catch (WindowMissplaceException e) {
            success = false;
        }
        assert success;
    }

    @Test
    public void validateInFrontDoor_and_setInInterior_Test(){
        boolean success = true;
        byte[][] layout = new byte[][]{
                { 1, 1, 1, 1, 1, 2, 2, 2, 1, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 3, 0, 1, 1, 1, 0, 0, 0, 0, 1 },
                { 3, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 3, 3, 1, 1, 1, 1 }
        };
        PlaceableItem dino = new PlaceableItem(PlaceableItemType.DINO);

    }
}

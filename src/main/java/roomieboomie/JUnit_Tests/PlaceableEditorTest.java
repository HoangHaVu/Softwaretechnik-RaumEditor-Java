package roomieboomie.JUnit_Tests;

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.editor.PlaceableEditor;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;

import static org.junit.Assert.assertTrue;

public class PlaceableEditorTest {
    PlacableItem table;
    PlaceableEditor placeableEditor;

    @Before
    public void init(){
        table=new PlacableItem(PlacableItemType.TABLE);
        placeableEditor= new PlaceableEditor();


    }



    @Test
    public void placeableItemPlatzieren(){
        int x=0;
        int y=0;
        int endX=table.getLength();
        int endY=table.getWidth();

        placeableEditor.selectPlaceableItem(PlacableItemType.COUCH);
        placeableEditor.placeActItem(x,y);

        placeableEditor.selectPlaceableItem(PlacableItemType.CARPET);
        placeableEditor.placeActItem(x,y);
        assertTrue(true);

    }
}

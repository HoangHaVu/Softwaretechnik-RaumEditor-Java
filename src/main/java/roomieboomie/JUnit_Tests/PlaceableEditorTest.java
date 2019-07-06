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
        int x=1;
        int y=1;
        int endX=table.getLength();
        int endY=table.getWidth();

        placeableEditor.selectPlaceableItem(PlacableItemType.TABLE);
        placeableEditor.placeActItem(x,y);

        assertTrue(true);

    }
}

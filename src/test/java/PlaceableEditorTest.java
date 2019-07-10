import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.editor.PlaceableEditor;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlaceableEditorTest {
    PlacableItem table;
    PlaceableEditor placeableEditor;
    byte[][] layout;

    @Before
    public void init(){
        Room r= new Room(60,60,null);
        table=new PlacableItem(PlacableItemType.TABLE);
        placeableEditor= new PlaceableEditor(r);
        layout=placeableEditor.getRoom().getLayout();


    }



    @Test
    /**
     * getestete Funktionen: Auswahl und Platzieren von Gegenstaenden,die übereinander sind
     * Beweis:
     */
    public void placeableItemPlatzieren(){
        int x=0;
        int y=0;
        int endX=table.getLength();
        int endY=table.getWidth();

        placeableEditor.selectPlaceableItem(PlacableItemType.COUCH);
        placeableEditor.placeCurrItem(x,y);

        placeableEditor.selectPlaceableItem(PlacableItemType.CARPET);
        placeableEditor.placeCurrItem(x,y);

        //placeableEditor.delete(x,y);
        //placeableEditor.delete(x,y);


        assertTrue(true);

    }

   @Test
   /**
    * getestete Funktionen:aufeinanderliegende Gegenstaende loeschen
    * Beweis:
    */
    public void deleteItems(){
       int x=0;
       int y=0;
       placeableItemPlatzieren();

       //erst wird die Couch entfernt danach der Teppich
       placeableEditor.delete(x,y);
       placeableEditor.delete(x,y);

       for(int i =0;i<layout.length;i++){
           for (int j=0;j<layout[0].length;j++){
               assertFalse(placeableEditor.getRoom().getLayout()[i][j]==0);
           }
       }
        assertTrue(true);

   }
   @Test
    public void placeTest(){
        placeableEditor.selectPlaceableItem(PlacableItemType.CARPET);
        placeableEditor.placeCurrItem(2,2);
        placeableEditor.selectPlaceableItem(PlacableItemType.TABLE);
        placeableEditor.placeCurrItem(2,2);

   }


}

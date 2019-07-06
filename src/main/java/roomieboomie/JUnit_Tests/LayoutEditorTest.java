package roomieboomie.JUnit_Tests;

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.persistence.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class LayoutEditorTest {
    RoomEditor testEditor;
    LayoutItem wall;
    LayoutItem window;
    byte[][] musterLayout;
    byte maxWindow = Config.get().EDITORMAXWINDOWVALUE();
    byte minWall = Config.get().EDITORMINWALLVALUE();

    @Before
    public void init() {
        wall=new LayoutItem(LayoutItemType.WALL, 10, 1, Orientation.TOP);
        window=new LayoutItem(LayoutItemType.WINDOW, 4, 1, Orientation.RIGHT);

        testEditor = new RoomEditor("test", false, new ArrayList<LayoutItem>(), new ArrayList<PlacableItem>());
        musterLayout =testEditor.getRoom().getLayout();
    }


    @Test
    /**
     * getestete Funktionen: Auswahl und Platzieren einer Wand
     * Beweis: byte [][] wird mit 1 befüllt von [1][1] bis [10][1] wegen der Länge der senkrechten Wand
     */
    public void wandPlatzieren(){
        int x= 1;
        int y=1;
        testEditor.selectnewItem(LayoutItemType.WALL);
        testEditor.placeActItem(x,y);

        for(int i =1;i<=wall.getLength();i++){
            musterLayout[i][x]=minWall;
        }
        //Vergleich vom Musterraum mit dem Raum im Editor nach der Platzierung
        assertArrayEquals(musterLayout,testEditor.getRoom().getLayout());
    }

    @Test
    /**
     * getestete Funktionen: Auswah, Rotieren und Platzieren eines Fensters
     * Beweis: byte [][] wird mit -3 an den jeweiligen Koordinaten befüllt
     */
    public void fensterPlatzieren(){
        int x= 1;
        int y=5;
        int endY=y+ window.getLength();
        testEditor.selectnewItem(window.getType());
        testEditor.rotateItem();
        testEditor.placeActItem(x,y);

        for(int i=y;i<endY;i++){
            musterLayout[i][x]=maxWindow;
        }

        //Vergleich vom Musterraum mit dem Raum im Editor nach der Platzierung
        assertArrayEquals(musterLayout,testEditor.getRoom().getLayout());

    }


}

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.exception.validationExceptions.DoorMissplaceException;
import roomieboomie.business.exception.validationExceptions.LayoutItemMissplaceException;
import roomieboomie.business.exception.validationExceptions.WallMissplaceException;
import roomieboomie.business.exception.validationExceptions.WindowMissplaceException;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.persistence.Config;
import roomieboomie.persistence.JsonHandler;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LayoutEditorTest {
    RoomEditor testEditor;
    LayoutItem wall;
    LayoutItem window;
    byte[][] musterLayout;
    byte maxWindow = Config.get().EDITORMAXWINDOWVALUE();
    byte minWall = Config.get().EDITORMINWALLVALUE();

    @Before
    public void init() {
        wall = new LayoutItem(LayoutItemType.WALL, 10, 1, Orientation.TOP);
        window = new LayoutItem(LayoutItemType.WINDOW, 4, 1, Orientation.RIGHT);

        testEditor = new RoomEditor("test", false, new ArrayList<PlacableItem>(), new JsonHandler());
        musterLayout = testEditor.getRoom().getLayout();
    }


    @Test
    /**
     * getestete Funktionen: Auswahl und Platzieren einer Wand
     * Beweis: byte [][] wird mit 1 befüllt von [1][1] bis [10][1] wegen der Länge der senkrechten Wand
     */
    public void placeWall() throws LayoutItemMissplaceException, WindowMissplaceException, DoorMissplaceException, WallMissplaceException {
        int x = 1;
        int y = 1;
        testEditor.selectnewItem(LayoutItemType.WALL);
        testEditor.placeCurrItem(x, y);

        for (int i = 1; i <= wall.getLength(); i++) {
            musterLayout[i][x] = minWall;
        }
        //Vergleich vom Musterraum mit dem Raum im Editor nach der Platzierung
        assertArrayEquals(musterLayout, testEditor.getRoom().getLayout());
    }

    @Test
    /**
     * getestete Funktionen: Auswahl, Rotieren und Platzieren eines Fensters
     * Beweis: byte [][] wird mit -3 an den jeweiligen Koordinaten befüllt
     */
    public void placeWindow() throws LayoutItemMissplaceException, WindowMissplaceException, DoorMissplaceException, WallMissplaceException {
        int x = 1;
        int y = 5;
        int endY = y + window.getLength();
        testEditor.selectnewItem(window.getType());
        testEditor.rotateItem();
        testEditor.placeCurrItem(x, y);

        for (int i = y; i < endY; i++) {
            musterLayout[i][x] = maxWindow;
        }

        //Vergleich vom Musterraum mit dem Raum im Editor nach der Platzierung
        assertArrayEquals(musterLayout, testEditor.getRoom().getLayout());

    }

    @Test
    /**
     * getestete Funktionen: loeschen von platzierten Objekten
     * Beweis: das byte[][] wird durchiteriert und schaut ob es noch eine Wand gibt(im byte[][] der Zahl 1 gekennzeichnet
     */
    public void deleteItem() throws LayoutItemMissplaceException, WindowMissplaceException, DoorMissplaceException, WallMissplaceException {
        int x = 1;
        int y = 1;
        int layoutHeight = testEditor.getRoom().getLayout().length;
        int layoutWidth = testEditor.getRoom().getLayout()[1].length;

        testEditor.selectnewItem(LayoutItemType.WALL);
        testEditor.placeCurrItem(x, y);

        for (int i = 1; i <= wall.getLength(); i++) {
            musterLayout[i][x] = minWall;
        }
        //Vergleich vom Musterraum mit dem Raum im Editor nach der Platzierung
        assertArrayEquals(musterLayout, testEditor.getRoom().getLayout());

        byte b = minWall;
        testEditor.deleteItem(b);

        //iteriert durch byte[][] und schaut ob es noch eine Wand gibt (als 1 im byte Array gekennzeichnet)
        for (int i = 0; i < layoutHeight; i++) {
            for (int j = 0; j < layoutWidth; j++) {
                assertFalse(testEditor.getRoom().getLayout()[i][j] == minWall);
            }
        }

    }


}

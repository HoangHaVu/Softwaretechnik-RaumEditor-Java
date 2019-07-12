package item;

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.item.Orientation;

public class OrientationTest {

    private Orientation testOrientation;

    @Before
    public void init(){
        testOrientation = Orientation.TOP;
    }

    @Test
    public void testTurnRight(){
        testOrientation = testOrientation.getNext();
        assert testOrientation == Orientation.RIGHT;
        testOrientation = testOrientation.getNext();
        assert testOrientation == Orientation.BOTTOM;
        testOrientation = testOrientation.getNext();
        assert testOrientation == Orientation.LEFT;
        testOrientation = testOrientation.getNext();
        assert testOrientation == Orientation.TOP;
    }

    @Test
    public void testTurnLeft(){
        testOrientation = testOrientation.getPrev();
        assert testOrientation == Orientation.LEFT;
        testOrientation = testOrientation.getPrev();
        assert testOrientation == Orientation.BOTTOM;
        testOrientation = testOrientation.getPrev();
        assert testOrientation == Orientation.RIGHT;
        testOrientation = testOrientation.getPrev();
        assert testOrientation == Orientation.TOP;
    }
}

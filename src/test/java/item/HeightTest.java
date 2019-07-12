package item;

import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.item.placeable.Height;

public class HeightTest {

    Height flat;
    Height small;
    Height medium;
    Height high;

    @Before
    public void init(){
        flat = Height.FLAT;
        small = Height.SMALL;
        medium = Height.MEDIUM;
        high = Height.HIGH;
    }

    @Test
    /**
     * Testet, ob fitsBeneath korrekt zurueckgbibt, ob Hoehen untereinander passen
     */
    public void testFitsBeneath(){
        assert !flat.fitsBeneath(flat);
        assert flat.fitsBeneath(small);
        assert flat.fitsBeneath(medium);
        assert flat.fitsBeneath(high);

        assert !small.fitsBeneath(flat);
        assert !small.fitsBeneath(small);
        assert small.fitsBeneath(medium);
        assert small.fitsBeneath(high);

        assert !medium.fitsBeneath(flat);
        assert !medium.fitsBeneath(small);
        assert !medium.fitsBeneath(medium);
        assert medium.fitsBeneath(high);

        assert !high.fitsBeneath(flat);
        assert !high.fitsBeneath(small);
        assert !high.fitsBeneath(medium);
        assert !high.fitsBeneath(high);
    }
}

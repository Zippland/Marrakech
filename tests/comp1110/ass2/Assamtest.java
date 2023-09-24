package comp1110.ass2;

import comp1110.ass2.gui.Viewer;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class AssamTest {
    @Test
    void parseAssam() {
        Viewer viewer = new Viewer();
        Assam assam = viewer.parseAssam("23N");
        assertEquals(2, assam.getX());
        assertEquals(3, assam.getY());
        assertEquals('N', assam.getDirection());
    }

}

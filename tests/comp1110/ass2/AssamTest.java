package comp1110.ass2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssamTest {

    @Test
    public void testAssamConstructor() {
        Assam assam = new Assam(1, 2, 'N');

        Assertions.assertEquals(1, assam.getX());
        Assertions.assertEquals(2, assam.getY());
        Assertions.assertEquals('N', assam.getDirection());
    }

    @Test
    public void testGetX() {
        Assam assam = new Assam(3, 4, 'S');

        Assertions.assertEquals(3, assam.getX());
    }

    @Test
    public void testGetY() {
        Assam assam = new Assam(5, 6, 'E');

        Assertions.assertEquals(6, assam.getY());
    }

    @Test
    public void testGetDirection() {
        Assam assam = new Assam(7, 8, 'W');

        Assertions.assertEquals('W', assam.getDirection());
    }

}

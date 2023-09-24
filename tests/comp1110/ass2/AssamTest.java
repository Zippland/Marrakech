package comp1110.ass2;
import org.junit.Assert;
import org.junit.Test;

public class AssamTest {

    @Test
    public void testAssamConstructor() {
        Assam assam = new Assam(1, 2, 'N');

        Assert.assertEquals(1, assam.getX());
        Assert.assertEquals(2, assam.getY());
        Assert.assertEquals('N', assam.getDirection());
    }

    @Test
    public void testGetX() {
        Assam assam = new Assam(3, 4, 'S');

        Assert.assertEquals(3, assam.getX());
    }

    @Test
    public void testGetY() {
        Assam assam = new Assam(5, 6, 'E');

        Assert.assertEquals(6, assam.getY());
    }

    @Test
    public void testGetDirection() {
        Assam assam = new Assam(7, 8, 'W');

        Assert.assertEquals('W', assam.getDirection());
    }

}

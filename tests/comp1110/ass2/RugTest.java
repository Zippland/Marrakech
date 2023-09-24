package comp1110.ass2;

import org.junit.Assert;
import org.junit.Test;

public class RugTest {

    @Test
    public void testRugConstructor() {
        Rug rug = new Rug('B', 1, 2, 3, 4, 5);

        Assert.assertEquals('B', rug.getColor());
        Assert.assertEquals(1, rug.getId());
        Assert.assertEquals(2, rug.getX1());
        Assert.assertEquals(3, rug.getY1());
        Assert.assertEquals(4, rug.getX2());
        Assert.assertEquals(5, rug.getY2());
    }

    @Test
    public void testGetColor() {
        Rug rug = new Rug('R', 6, 7, 8, 9, 10);

        Assert.assertEquals('R', rug.getColor());
    }

    @Test
    public void testGetX1() {
        Rug rug = new Rug('G', 11, 12, 13, 14, 15);

        Assert.assertEquals(12, rug.getX1());
    }

    @Test
    public void testGetY1() {
        Rug rug = new Rug('Y', 16, 17, 18, 19, 20);

        Assert.assertEquals(18, rug.getY1());
    }

    @Test
    public void testGetId() {
        Rug rug = new Rug('B', 21, 22, 23, 24, 25);

        Assert.assertEquals(21, rug.getId());
    }

    @Test
    public void testGetX2() {
        Rug rug = new Rug('R', 26, 27, 28, 29, 30);

        Assert.assertEquals(29, rug.getX2());
    }

    @Test
    public void testGetY2() {
        Rug rug = new Rug('G', 31, 32, 33, 34, 35);

        Assert.assertEquals(35, rug.getY2());
    }
}


package comp1110.ass2;
import org.junit.Assert;
import org.junit.Test;

public class PlayerTest {

    @Test
    public void testPlayerConstructor() {
        Player player = new Player('R', 10, 5, true);

        Assert.assertEquals('R', player.getColor());
        Assert.assertEquals(10, player.getDirhams());
        Assert.assertEquals(5, player.getRugs());
        Assert.assertTrue(player.isInGame());
    }

    @Test
    public void testGetColor() {
        Player player = new Player('B', 20, 7, false);

        Assert.assertEquals('B', player.getColor());
    }

    @Test
    public void testGetDirhams() {
        Player player = new Player('G', 15, 6, true);

        Assert.assertEquals(15, player.getDirhams());
    }

    @Test
    public void testGetRugs() {
        Player player = new Player('Y', 25, 8, false);

        Assert.assertEquals(8, player.getRugs());
    }

    @Test
    public void testIsInGame() {
        Player player = new Player('R', 30, 9, true);

        Assert.assertTrue(player.isInGame());
    }
}

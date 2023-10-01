package comp1110.ass2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void testPlayerConstructor() {
        Player player = new Player('R', 10, 5, true);

        Assertions.assertEquals('R', player.getColor());
        Assertions.assertEquals(10, player.getDirhams());
        Assertions.assertEquals(5, player.getRugs());
        Assertions.assertTrue(player.isInGame());
    }

    @Test
    public void testGetColor() {
        Player player = new Player('B', 20, 7, false);

        Assertions.assertEquals('B', player.getColor());
    }

    @Test
    public void testGetDirhams() {
        Player player = new Player('G', 15, 6, true);

        Assertions.assertEquals(15, player.getDirhams());
    }

    @Test
    public void testGetRugs() {
        Player player = new Player('Y', 25, 8, false);

        Assertions.assertEquals(8, player.getRugs());
    }

    @Test
    public void testIsInGame() {
        Player player = new Player('R', 30, 9, true);

        Assertions.assertTrue(player.isInGame());
    }
}

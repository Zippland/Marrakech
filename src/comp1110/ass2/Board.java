package comp1110.ass2;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board {
    public static final int TILE_SIZE = 72; // you can adjust this value
    private static final int WIDTH = 7;
    private static final int HEIGHT = 7;
    public Rug[][] board;

    private GridPane gridPane = new GridPane();

    public Board() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill(Color.TRANSPARENT); // default color
                gridPane.add(tile, x, y);
            }
        }
    }

    public void fromString(String boardString) {
        for (int i = 0; i < boardString.length(); i += 3) {
            String rug = boardString.substring(i, i + 3);
            int x = i / 21;
            int y = (i - x * 21) / 3;
            if (!rug.equals("n00")) {
                Color color;
                switch (rug.charAt(0)) {
                    case 'c':
                        color = Color.CYAN;
                        break;
                    case 'y':
                        color = Color.YELLOW;
                        break;
                    case 'r':
                        color = Color.RED;
                        break;
                    case 'p':
                        color = Color.PURPLE;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + rug.charAt(0));
                }
                setTileColor(x, y, color);
            } else {
                setTileColor(x, y, Color.TRANSPARENT);
            }
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setTileColor(int x, int y, Color color) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                ((Rectangle) node).setFill(color);
                break;
            }
        }
    }
}

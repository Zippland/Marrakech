package comp1110.ass2;

import comp1110.ass2.gui.Game;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;




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

        // Set the size constraints for each row and column
        for (int i = 0; i < WIDTH; i++) {
            ColumnConstraints column = new ColumnConstraints(TILE_SIZE);
            gridPane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < HEIGHT; i++) {
            RowConstraints row = new RowConstraints(TILE_SIZE);
            gridPane.getRowConstraints().add(row);
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

    public boolean addRug(Rug rug, boolean rugIsHorizontal, Game game) {
        // Create a new ImageView for the first part of the rug
        ImageView rugView = new ImageView(rug.getImage());
        rugView.setFitWidth(rugIsHorizontal ? 2*Board.TILE_SIZE : Board.TILE_SIZE);
        rugView.setFitHeight(rugIsHorizontal ? Board.TILE_SIZE : 2*Board.TILE_SIZE);

        // Create a new Pane and add the rugView to it
        Pane pane = new Pane(rugView);
        pane.setPrefSize(rugView.getFitWidth(), rugView.getFitHeight());

        // Center the rugView in the pane
        rugView.setX((pane.getPrefWidth() - rugView.getFitWidth()) / 2);
        rugView.setY((pane.getPrefHeight() - rugView.getFitHeight()) / 2);

        // Add the pane to the grid
        gridPane.add(pane, rug.x1, rug.y1);

        String newGameCode = updateBoardCode(game.getGameCode(), rug, rug.x1, rug.y1, rug.x2, rug.y2, game.getRugId());
        game.updateRugId();
        game.updateGameCode(newGameCode);
        return false;
    }

    public String updateBoardCode(String gameCode, Rug rug, int x1, int y1, int x2, int y2, String RugId) {
        String boardState = gameCode.split("A")[1].substring(4);
        String newBoardState = "";

        for (int i = 0; i < boardState.length(); i += 3) {
            int currentX = i / 21;
            int currentY = (i - currentX * 21) / 3;
            if ((currentX == x1 && currentY == y1) || (currentX == x2 && currentY == y2)) {
                // Replace the tile state with the new rug color and id
                newBoardState += String.valueOf(rug.getColor()) + RugId;
            } else {
                // Keep the original tile state
                newBoardState += boardState.substring(i, i + 3);
            }
        }

        // Replace the board state in the game code with the new board state
        String newGameCode = gameCode.split("A")[0] + "A" + gameCode.split("A")[1].substring(0, 4) + newBoardState;

        return newGameCode;
    }

}

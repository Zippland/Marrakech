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

    public void addRug(Rug rug, boolean rugIsHorizontal, Game game) {
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
        int x1 = rugIsHorizontal ? rug.getX1()-2:rug.getX1()-1;
        int y1 = rugIsHorizontal ? rug.getY1()-1:rug.getY1()-2;
        gridPane.add(pane, x1, y1);

        int x2 = rugIsHorizontal ? x1+1:x1;
        int y2 = rugIsHorizontal ? y1:y1+1;

        // Update the game code
        String newGameCode = updateBoardCode(game.getGameCode(), rug, x1, y1, x2, y2, game.getRugId());
        game.updateRugId();
        System.out.println(newGameCode);
        game.updateGameCode(newGameCode);
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

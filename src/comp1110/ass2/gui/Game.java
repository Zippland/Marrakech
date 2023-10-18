package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;

import static comp1110.ass2.Board.TILE_SIZE;
import comp1110.ass2.Assam;

public class Game extends Application {

    private final Group root = new Group();
    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;
    private TextField boardTextField;
    private ImageView assamImageView;
    private Polygon assamArrow;
    private Player[] players = new Player[4];
    private Board board;
    public final Group playerInfo = new Group();
    public void updatePlayerInfo(String[] players) {
        playerInfo.getChildren().clear(); // 清除旧的标签

        int yOffset = 0; // 用于垂直定位标签的偏移量
        for (String player : players) {
            if (!player.isEmpty()) {
                char color = player.charAt(0);
                int dirhams = Integer.parseInt(player.substring(1, 4));
                int rugs = Integer.parseInt(player.substring(4, 6));
                char status = player.charAt(6);
                String info = "Player < " + (color == 'r' ? "  Red " : color == 'c' ? "  Clay " : color == 'y' ? "Yellow" : "Purple" ) + " >   " + dirhams + " dirhams  |  " + rugs + " rugs  |   " + (status == 'i' ? "InGame" : "Out");

                Label playerLabel = new Label(info);
                playerLabel.setLayoutX(780); // 设置标签的水平位置
                playerLabel.setLayoutY(50 + yOffset); // 设置标签的垂直位置
                playerInfo.getChildren().add(playerLabel);

                yOffset += 32; // 更新偏移量以便下一个标签在下方
            }
        }
    }
    public Assam parseAssam(String assamString) {
        int x = Character.getNumericValue(assamString.charAt(0));
        int y = Character.getNumericValue(assamString.charAt(1));
        char direction = assamString.charAt(2);
        return new Assam(x, y, direction);
    }
    /**
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state
     */
    public void displayState(String state) {
        // Split the state string into its components
        String[] components = state.split("A");

        // Extract and print the player information
        String[] players = components[0].split("P");
        updatePlayerInfo(players);
        for (String player : players) {
            if (!player.isEmpty()) {
                char color = player.charAt(0);
                int dirhams = Integer.parseInt(player.substring(1, 4));
                int rugs = Integer.parseInt(player.substring(4, 6));
                boolean inGame = player.charAt(6) == 'i';
                // Create a new Player object with the extracted information
                Player newPlayer = new Player(color, dirhams, rugs, inGame);
                System.out.println("Player " + color + " has " + dirhams + " dirhams, " + rugs + " rugs, and is " + (inGame ? "in" : "out of") + " the game.");
            }
        }

        // Extract and print Assam's information
        String assamInfo = components[1].substring(0, 3);
        Assam assam = parseAssam(assamInfo);
        int x = Character.getNumericValue(assamInfo.charAt(0));
        int y = Character.getNumericValue(assamInfo.charAt(1));
        char direction = assamInfo.charAt(2);
        System.out.println("Assam is at (" + x + ", " + y + "), facing " + direction + ".");

        // Set Assam's position
        assamImageView.setX(board.getGridPane().getTranslateX() + x * TILE_SIZE + TILE_SIZE / 2 - assamImageView.getFitWidth() / 2);
        assamImageView.setY(board.getGridPane().getTranslateY() + y * TILE_SIZE + TILE_SIZE / 2 - assamImageView.getFitHeight() / 2);

        // Set Assam's direction
        switch (direction) {
            case 'N':
                assamImageView.setRotate(0);
                break;
            case 'E':
                assamImageView.setRotate(90);
                break;
            case 'S':
                assamImageView.setRotate(180);
                break;
            case 'W':
                assamImageView.setRotate(-90);
                break;
        }

        // Extract and print the board information
        String board = components[1].substring(4);
        this.board.fromString(board);
        for (int i = 0; i < board.length(); i += 3) {
            String rug = board.substring(i, i + 3);
            if (!rug.equals("n00")) {
                int rug_x = i / 21;
                int rug_y = (i - rug_x*21) / 3;
                System.out.println("Rug " + rug + " is at (" + rug_x + ", " + rug_y + ").");

                // Get the color of the rug
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

                // Set the color of the tile
                this.board.setTileColor(rug_x, rug_y, color);
            } else {
                // If there's no rug, set the color to TRANSPARENT
                int rug_x = i / 21;
                int rug_y = (i - rug_x*21) / 3;
                this.board.setTileColor(rug_x, rug_y, Color.TRANSPARENT);
            }
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Marrakech Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        // Initialize the picture and add it to the root group
        Image image = new Image("file:src/comp1110/ass2/gui/img/board.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(VIEWER_WIDTH);
        imageView.setFitHeight(VIEWER_HEIGHT);
        // imageView.setImage(image);
        root.getChildren().add(imageView);


        // Initialize the board and add it to the root group
        board = new Board();
        board.getGridPane().setTranslateX(103);
        board.getGridPane().setTranslateY(99);
        root.getChildren().add(board.getGridPane());
        // player info
        root.getChildren().add(playerInfo);

        // Initialize the Assam image and add it to the root group
        Image assamImage = new Image("file:src/comp1110/ass2/gui/img/assam.png");
        assamImageView = new ImageView(assamImage);
        assamImageView.setFitWidth(72);
        assamImageView.setFitHeight(72);
        root.getChildren().add(assamImageView);

        stage.setScene(scene);
        stage.show();
        String testcode = "Pr03111iPc03212iPy03313oPp03414iA03NBp01p02n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00c02c01";
        displayState(testcode);
    }
}

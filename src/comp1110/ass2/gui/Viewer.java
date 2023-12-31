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

/*
test sample:
Pr03111iPc03212iPy03313oPp03414iA23NBp01p02n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00c02c01
*/


public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField boardTextField;
    private ImageView assamImageView;

    private Polygon assamArrow;

    private Player[] players = new Player[4];

    private Board board;

    // Add a new Group element to hold the player info tag
    public final Group playerInfo = new Group();

    // Methods for updating player information
    public void updatePlayerInfo(String[] players) {
        playerInfo.getChildren().clear(); // Clearing old labels

        int yOffset = 0; // Offset for vertically positioning labels
        for (String player : players) {
            if (!player.isEmpty()) {
                char color = player.charAt(0);
                int dirhams = Integer.parseInt(player.substring(1, 4));
                int rugs = Integer.parseInt(player.substring(4, 6));
                char status = player.charAt(6);
                String info = "Player < " + (color == 'r' ? "  Red " : color == 'c' ? "  Clay " : color == 'y' ? "Yellow" : "Purple" ) + " >   " + dirhams + " dirhams  |  " + rugs + " rugs  |   " + (status == 'i' ? "InGame" : "Out");

                Label playerLabel = new Label(info);
                playerLabel.setLayoutX(780); /// Set the horizontal position of the label
                playerLabel.setLayoutY(50 + yOffset); // Set the vertical position of the label
                playerInfo.getChildren().add(playerLabel);

                yOffset += 32; // Update the offset so that the next label is below it
            }
        }
    }

    public Assam parseAssam(String assamString) {
        // Parse the x-coordinate from the string
        int x = Character.getNumericValue(assamString.charAt(0));
        // Parse the y-coordinate from the string
        int y = Character.getNumericValue(assamString.charAt(1));
        // Parse the direction from the string
        char direction = assamString.charAt(2);
        // Create a new Assam object with the parsed values and return it
        return new Assam(null, x, y, direction, board);
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

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label boardLabel = new Label("Game State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(800);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                displayState(boardTextField.getText());
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(boardLabel,
                boardTextField, button);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    private void createAssamArrow() {
        // Create a new Polygon object for the arrow
        assamArrow = new Polygon();
        // Add points to the polygon to form an arrow shape
        assamArrow.getPoints().addAll(new Double[]{
                -10.0, -5.0,  // Left point
                10.0, 0.0,    // Top point
                -10.0, 5.0    // Right point
        });
        // Set the color of the arrow to black
        assamArrow.setFill(Color.BLACK);
        // Add the arrow to the game root
        root.getChildren().add(assamArrow);
    }



    private void drawBoard() {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (i < 7 && j < 7) {
                    Rectangle rect = new Rectangle(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    if ((i + j) % 2 == 0) {
                        rect.setFill(Color.TRANSPARENT);
                    } else {
                        rect.setFill(Color.LIGHTGRAY);
                    }
                    root.getChildren().add(rect);
                }

                // 横线
                if (j <= 7) {
                    javafx.scene.shape.Line hLine = new javafx.scene.shape.Line(0, j * TILE_SIZE, 7 * TILE_SIZE, j * TILE_SIZE);
                    hLine.setStroke(Color.BLACK);
                    root.getChildren().add(hLine);
                }

                // 竖线
                if (i <= 7) {
                    javafx.scene.shape.Line vLine = new javafx.scene.shape.Line(i * TILE_SIZE, 0, i * TILE_SIZE, 7 * TILE_SIZE);
                    vLine.setStroke(Color.BLACK);
                    root.getChildren().add(vLine);
                }
            }
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Marrakech Viewer");
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

        root.getChildren().add(controls);
        // player info
        root.getChildren().add(playerInfo);

        // Initialize the Assam image and add it to the root group
        Image assamImage = new Image("file:src/comp1110/ass2/gui/img/assam.png");
        assamImageView = new ImageView(assamImage);
        assamImageView.setFitWidth(72);
        assamImageView.setFitHeight(72);
        root.getChildren().add(assamImageView);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
        createAssamArrow();
        String testcode = "Pc03015iPy03015iPp03015iPr03015iA03WBn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00";
        displayState(testcode);
    }

}

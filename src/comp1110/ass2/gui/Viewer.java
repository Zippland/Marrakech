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
Pr03111iPc03212iPy03313oPp03414iA23Np01p02n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00c02c01
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

    // 添加一个新的Group元素来保存玩家信息标签
    private final Group playerInfo = new Group();

    // 更新玩家信息的方法
    void updatePlayerInfo(String[] players) {
        playerInfo.getChildren().clear(); // 清除旧的标签

        int yOffset = 0; // 用于垂直定位标签的偏移量
        for (String player : players) {
            if (!player.isEmpty()) {
                char color = player.charAt(0);
                int dirhams = Integer.parseInt(player.substring(1, 4));
                int rugs = Integer.parseInt(player.substring(4, 6));
                char status = player.charAt(6);
                String info = "Player " + color + " has " + dirhams + " dirhams, " + rugs + " rugs remaining, and is " + (status == 'i' ? "in" : "out of") + " the game.";

                Label playerLabel = new Label(info);
                playerLabel.setLayoutX(800); // 设置标签的水平位置
                playerLabel.setLayoutY(50 + yOffset); // 设置标签的垂直位置
                playerInfo.getChildren().add(playerLabel);

                yOffset += 30; // 更新偏移量以便下一个标签在下方
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
    void displayState(String state) {
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
                System.out.println("Player " + color + " has " + dirhams + " dirhams, " + rugs + " rugs remaining, and is " + (inGame ? "in" : "out of") + " the game.");
            }
        }

        // Extract and print Assam's information
        String assamInfo = components[1].substring(0, 3);
        Assam assam = parseAssam(assamInfo);
        System.out.println("Assam is at (" + assam.getX() + ", " + assam.getY() + "), facing " + assam.getDirection() + ".");
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

        // After setting Assam's position and direction
        double arrowOffset = TILE_SIZE / 2.0;

        // Set Assam's arrow position
        assamArrow.setLayoutX(assamImageView.getX() + arrowOffset);
        assamArrow.setLayoutY(assamImageView.getY() + arrowOffset);

        // Set Assam's arrow direction
        switch (direction) {
            case 'N':
                assamArrow.setRotate(-90);
                break;
            case 'E':
                assamArrow.setRotate(0);
                break;
            case 'S':
                assamArrow.setRotate(90);
                break;
            case 'W':
                assamArrow.setRotate(180);
                break;
        }

        // Extract and print the board information
        String board = components[1].substring(3);
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
        assamArrow = new Polygon();
        assamArrow.getPoints().addAll(new Double[]{
                -10.0, -5.0,
                10.0, 0.0,
                -10.0, 5.0
        });
        assamArrow.setFill(Color.BLACK);
        root.getChildren().add(assamArrow);
    }



    private void drawBoard() {
        for (int i = 0; i <= 7; i++) {  // 注意这里改成了i <= 7
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
        Image image = new Image("file:../../../src/ass2/img/board.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(VIEWER_WIDTH);
        imageView.setFitHeight(VIEWER_HEIGHT);
        root.getChildren().add(imageView);

        // Draw the board grid lines
        drawBoard();

        // Initialize the board and add it to the root group
        board = new Board();
        board.getGridPane().setTranslateX(0);
        board.getGridPane().setTranslateY(0);
        root.getChildren().add(board.getGridPane());

        root.getChildren().add(controls);
        // player info
        root.getChildren().add(playerInfo);

        // Initialize the Assam image and add it to the root group
        Image assamImage = new Image("file:../../../src/ass2/img/assam.png");
        assamImageView = new ImageView(assamImage);
        assamImageView.setFitWidth(50);
        assamImageView.setFitHeight(50);
        root.getChildren().add(assamImageView);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
        createAssamArrow();

    }

}

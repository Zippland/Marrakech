package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.Glow;

import comp1110.ass2.Assam;

public class Game extends Application {
    public String Gamecode;
    public void updateGameCode(String newGameCode) {
        this.Gamecode = newGameCode;
        displayState(this.Gamecode);
    }
    private String RugId = "00";
    public void updateRugId() {
        int number = Integer.parseInt(this.RugId);
        number += 1;
        this.RugId = String.format("%02d", number);
    }
    public String getRugId() {
        return this.RugId;
    }

    public String getGameCode() {
        return this.Gamecode;
    }

    public final char[] colors = {'r', 'p', 'c', 'y'};
    public String[] colors2 = {"Red", "Purple", "Cyan", "Yellow"};
    public int colorIndex = 0;
    public boolean isRollingDice = false;
    public boolean isRotatingAssam = true;
    private final Group root = new Group();
    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;
    public ImageView assamImageView;
    private Player[] players;
    private int currentPlayerIndex = 0;
    public Board board;
    public Assam assam;
    public final Group playerInfo = new Group();
    private ImageView rugImageView;
    public boolean rugIsHorizontal = true;
    private Glow glowEffect = new Glow(0.8);

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public void displayState(String state) {
        this.Gamecode = state;
        // Split the state string into its components
        String[] components = state.split("A");

        // Extract and print the player information
        Player[] players = Player.parsePlayers(components[0]);
        Player.updatePlayerInfo(playerInfo, players);

        // Extract and print Assam's information
        String assamInfo = components[1].substring(0, 3);
        assam = Assam.parseAssam(assamInfo);

        // Set Assam's position and direction
        assam.setAssamPosition(board, assamImageView);
        assam.setAssamDirection(assamImageView);

        // Extract and print the board information
        String board = components[1].substring(4);
        this.board.fromString(board);
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

        // Initialize the rug image and add it to the root group
        Image rugImage = new Image("file:src/comp1110/ass2/gui/img/RedRug.png");
        rugImageView = new ImageView(rugImage);
        rugImageView.setFitWidth(2 * Board.TILE_SIZE);
        rugImageView.setFitHeight(Board.TILE_SIZE);
        rugImageView.setVisible(false); // Set the rug invisible initially
        root.getChildren().add(rugImageView);


        // Mouse Actions
        MouseActions mouseActions = new MouseActions(this, rugImageView);
        mouseActions.handleMouseMoved(scene);
        mouseActions.handleMouseScroll(scene);
        mouseActions.handleMouseClicked(scene);

        stage.setScene(scene);
        stage.show();
        String Gamecode = "Pr03015iPc03015iPy03015iPp03015iA33eBn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00";
        displayState(Gamecode);
    }
}

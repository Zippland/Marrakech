package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.Glow;
import javafx.util.Duration;

public class Game extends Application {
    //Game code modification
    public String Gamecode;
    public comp1110.ass2.Player Player;

    public void updateGameCode(String newGameCode) {
        this.Gamecode = newGameCode;
        displayState(this.Gamecode);
    }
    public String getGameCode() {
        return this.Gamecode;
    }

    //Rug Id modification
    private String RugId = "01";
    public void updateRugId() {
        int number = Integer.parseInt(this.RugId);
        number += 1;
        this.RugId = String.format("%02d", number);
    }
    public String getRugId() {
        return this.RugId;
    }

    //color Index modification
    public int colorIndex = 0;
    public final char[] colors = {'r', 'p', 'c', 'y'};
    public String[] colors2 = {"Red", "Purple", "Cyan", "Yellow"};
    public char assamdirec = 'N';
    public boolean isRollingDice = false;
    public boolean isRotatingAssam = true;
    public boolean rugIsHorizontal = true;
    public final Group root = new Group();
    public final Group playerInfo = new Group();
    public static final int VIEWER_WIDTH = 1200;
    public static final int VIEWER_HEIGHT = 700;
    public ImageView assamImageView;
    public ImageView diceImageView;
    private ImageView rugImageView;
    public Player[] players;
    public Board board;
    public Assam assam;
    private Glow glowEffect = new Glow(0.8);
    public String lastAssam = "A33N";
    public Player getCurrentPlayer() {
        return players[colorIndex];
    }

    public void displayState(String state) {

        this.Gamecode = state;
        System.out.println(Gamecode);
        // Split the state string into its components
        String[] components = state.split("A");

        // Extract the player information
        Player.updatePlayerInfo(playerInfo, players);


        // Set Assam's position and direction
        this.assam.setAssamPosition(board, assamImageView);
        this.assam.setAssamDirection(assamImageView);

        // Extract and print the board information
        String board = components[1].substring(4);
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
        this.assam = new Assam(this,3, 3, 'N', board);
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

        // Initialize the dice image and add it to the root group
        Image diceImage = new Image("file:src/comp1110/ass2/gui/img/3.png");
        diceImageView = new ImageView(diceImage);
        diceImageView.setFitWidth(100);
        diceImageView.setFitHeight(100);
        diceImageView.setVisible(false);
        root.getChildren().add(diceImageView);


        // Initialize the players
        players = new Player[4];
        players[0] = new Player('r', 30, 15, true, this);
        players[1] = new Player('p', 30, 15, true, this);
        players[2] = new Player('c', 30, 15, true, this);
        players[3] = new Player('y', 30, 15, true, this);

        // Mouse Actions
        MouseActions mouseActions = new MouseActions(this, rugImageView, this.assam, this.root);
        mouseActions.handleMouseMoved(scene);
        mouseActions.handleMouseScroll(scene);
        mouseActions.handleMouseClicked(scene, assamImageView);
        mouseActions.handleMouseMoved(scene);


        stage.setScene(scene);
        stage.show();
        String Gamecode = "Pr03015iPc03015iPy03015iPp03015iA33NBn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00";
        displayState(Gamecode);
    }
}

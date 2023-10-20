package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.scene.control.ChoiceDialog;
import java.util.Optional;
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
/**
 * Main Game class that manages the GUI representation of the game.
 *
 * @author Zihan Jian, Xinyue Fei
 */
public class Game extends Application {
    // Variables related to game state management

    //Game code modification
    // Variable to hold the game code
    public String Gamecode;
    // Variable to hold the player object
    public comp1110.ass2.Player Player;
    // Method to update the game code
    public void updateGameCode(String newGameCode) {
        // Assigning the new game code to the Gamecode variable
        this.Gamecode = newGameCode;
        // Display the current state of the game
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
    public ImageView RollImageView;
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

        // Initialize the dice image and add it to the root group
        Image RollImage = new Image("file:src/comp1110/ass2/gui/img/roll.png");
        RollImageView = new ImageView(RollImage);
        RollImageView.setFitWidth(100);
        RollImageView.setFitHeight(100);
        RollImageView.setVisible(true);
        root.getChildren().add(RollImageView);


        // Create a dialog for player selection
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, 2, 3, 4);
        dialog.setTitle("Player Selection");
        dialog.setHeaderText("Please select the number of human players:");
        Optional<Integer> result = dialog.showAndWait();

        // Initialize the players based on the user's choice
        if (result.isPresent()) {
            int humanPlayerCount = result.get();
            players = new Player[4];
            for (int i = 0; i < 4; i++) {
                if (i < humanPlayerCount) {
                    players[i] = new Player(colors[i], 5, 15, true, this,false);
                } else {
                    players[i] = new Player(colors[i], 5, 15, true, this,true);
                }
            }
        }


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

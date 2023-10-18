package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import comp1110.ass2.Assam;

public class Game extends Application {
    private final Group root = new Group();
    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;
    private ImageView assamImageView;
    private Board board;
    private Assam assam;
    public final Group playerInfo = new Group();

    public void displayState(String state) {
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

        stage.setScene(scene);
        stage.show();
        String testcode = "Pr03111iPc03212iPy03313oPp03414iA03NBp01p02n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00c02c01";
        displayState(testcode);
    }
}

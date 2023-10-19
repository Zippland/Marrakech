package comp1110.ass2;

import comp1110.ass2.gui.Game;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import static comp1110.ass2.gui.Game.VIEWER_HEIGHT;
import static comp1110.ass2.gui.Game.VIEWER_WIDTH;
//This class is responsible for implementing the Artificial Intelligence (AI) in the game.
//The AI is designed to play the game independently, making its own decisions based on the game state.
// Authors: [Xinyue Fei]
//This code was written by the above-mentioned authors as part of their coursework in [Your Course Name].
//The ideas and algorithms implemented in this code are the original work of the authors.
public class AI {
    // Variable to keep track of the total number of rotations performed by the AI
    int totalRotation = 0;
    // Variable to store the winning string or condition
    String winString = "";
    // Variable for the game instance which the AI is playing
    Game game;

    public void aiTurn(Scene scene, ImageView rugImageView,ImageView assamImageView, Game game2) {
        this.game = game2;
        // Step 1: Move Assam
        // For simplicity, we can move Assam in a random direction
        int rotation = new Random().nextInt(4) * 90;
        // Generate a random rotation (0, 90, 180, or 270 degrees)
        // Get the current Assam state
        String currentAssam = "A" + game.assam.getX() + game.assam.getY() + game.assam.getDirection();
        // Rotate Assam by the random rotation
        String newAssam = Marrakech.rotateAssam(currentAssam, rotation);
        // Update Assam's direction in the game state
        game.assam.setDirection(newAssam.charAt(3));
        // Update Assam's direction in the game's image view
        game.assam.setAssamDirection(game.assamImageView);
        // The AI has finished rotating Assam and is now ready to roll the dice
        game.isRotatingAssam = false;
        game.isRollingDice = true;
        // Make the dice image view visible
        game.diceImageView.setVisible(true);
        // Reset the total rotation to 0 for the next turn
        this.totalRotation = 0;

        // Step 2: Roll the dice
        // Start the animation for dice rolling
        game.isRollingDice = false;
        game.diceImageView.setFitWidth(400);
        game.diceImageView.setFitHeight(400);
        game.diceImageView.setX(VIEWER_WIDTH / 2 -200);
        game.diceImageView.setY(VIEWER_HEIGHT / 2 -200);
        // Create a rotation transition for the dice roll animation
        RotateTransition rt = new RotateTransition(Duration.seconds(1.5), game.diceImageView);
        rt.setByAngle(1440); // Rotate by 1440 degrees
        rt.setCycleCount(1);// Do this rotation once
        rt.setAutoReverse(false); // Do not reverse the rotation
        // Set an action to perform when the rotation animation finishes
        rt.setOnFinished(event3 -> {
            // Roll the dice and get the result
            int roll = Marrakech.rollDie();
            // Display the result of the dice roll
            ImageView diceImageView = new ImageView(new Image("file:src/comp1110/ass2/gui/img/"+ roll +".png"));
            diceImageView.setX(VIEWER_WIDTH / 2 - 200);  // Display the result in the center of the GUI
            diceImageView.setY(VIEWER_HEIGHT / 2 - 200);  // Display the result in the center of the GUI
            diceImageView.setFitWidth(400);
            diceImageView.setFitHeight(400);
            game.root.getChildren().add(diceImageView);

            // Add the dice result image to the game root
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(1500),
                    ae -> game.root.getChildren().remove(diceImageView)// Remove the dice image from the game root
            ));
            timeline.play();
            // Create a timeline to move Assam based on the dice roll result
            Timeline timeline2 = new Timeline();
            String lastAssamString = game.assam.getString();
            timeline2.setCycleCount(roll); // Set the cycle count to the dice roll result
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(500), event2 -> {
                // Move Assam
                game.assam.moveAssam(1, assamImageView);
                game.lastAssam = game.assam.getString();// Update the last Assam state
            }));
            timeline2.setOnFinished(event2 -> {
                // Update the game code with the new Assam state
                game.Gamecode = game.Gamecode.replace(lastAssamString, game.assam.toString());
                // Create a new timeline for the next part of the game
                Timeline timeline4 = new Timeline();
                timeline4.setCycleCount(roll); // Set the cycle count to the dice roll result
                timeline4.getKeyFrames().add(new KeyFrame(Duration.millis(300), event4 -> {
                    // The AI has finished rolling the dice
                    game.isRollingDice = false;
                    // Make the rug image view visible
                    rugImageView.setVisible(true);
                    // Hide the dice image view
                    game.diceImageView.setVisible(false);

                    int addims = Marrakech.getPaymentAmount(game.Gamecode);
                    //System.out.println(addims+"       dirhams");
                    //System.out.println(game.Gamecode.charAt(36+1+3*(7*(game.assam.getX())+game.assam.getY()))+"       recieved");
                    // Update the players' dirhams and game code based on the payment amount
                    // The color character in the game code determines which player receives the payment
                    switch (game.Gamecode.charAt(36+1+3*(7*(game.assam.getX())+game.assam.getY()))){
                        case 'r':
                            // The red player receives the payment
                            game.players[game.colorIndex].dirhams -= addims;
                            game.players[game.colorIndex].updateGameCode();
                            game.players[0].dirhams += addims;
                            game.players[0].updateGameCode();
                            break;
                        case 'c':
                            // The cyan player receives the payment
                            game.players[game.colorIndex].dirhams -= addims;
                            game.players[game.colorIndex].updateGameCode();
                            game.players[1].dirhams += addims;
                            game.players[1].updateGameCode();
                            break;
                        case 'y':
                            // The yellow player receives the payment
                            game.players[game.colorIndex].dirhams -= addims;
                            game.players[game.colorIndex].updateGameCode();
                            game.players[2].dirhams += addims;
                            game.players[2].updateGameCode();
                            break;
                        case 'p':
                            // The purple player receives the payment
                            game.players[game.colorIndex].dirhams -= addims;
                            game.players[game.colorIndex].updateGameCode();
                            game.players[3].dirhams += addims;
                            game.players[3].updateGameCode();
                            break;
                        default:
                            // No player receives the payment
                            break;
                    }
                    // Update the player information in the game
                    game.Player.updatePlayerInfo(game.playerInfo, game.players);
                }));
                timeline4.setOnFinished(event4 -> {
                    // Create a random generator
                    Random random = new Random();
                    // Generate a random x and y coordinate for the rug placement
                    int x = random.nextInt(7);
                    int y = random.nextInt(7);
                    // Create a new rug with the generated coordinates and the current player's color
                    Rug rug = new Rug(game.colors[game.colorIndex], Integer.parseInt(game.getRugId()), x, y, game.rugIsHorizontal ? x + 1 : x, game.rugIsHorizontal ? y : y + 1);
                    // Determine the other end of the rug based on whether it's horizontal or vertical
                    rug.x2 = game.rugIsHorizontal ? rug.x1 + 1 : rug.x1;
                    rug.y2 = game.rugIsHorizontal ? rug.y1 : rug.y1 + 1;
                    // Keep generating new coordinates until a valid rug placement is found
                    while (!(Marrakech.isRugValid(game.Gamecode, rug.getRugString())&&Marrakech.isPlacementValid(game.Gamecode, rug.getRugString()))) {
                        x = random.nextInt(7);
                        y = random.nextInt(7);
                        rug = new Rug(game.colors[game.colorIndex], Integer.parseInt(game.getRugId()), x, y, game.rugIsHorizontal ? x + 1 : x, game.rugIsHorizontal ? y : y + 1);

                        rug.x2 = game.rugIsHorizontal ? rug.x1 + 1 : rug.x1;
                        rug.y2 = game.rugIsHorizontal ? rug.y1 : rug.y1 + 1;
                    }
                    if (Marrakech.isRugValid(game.Gamecode, rug.getRugString()) && Marrakech.isPlacementValid(game.Gamecode, rug.getRugString())) {
                        // Add the rug to the game board
                        game.board.addRug(rug, game.rugIsHorizontal, game);
                        // Update the game code with the new rug placement
                        game.getCurrentPlayer().updateGameCode(rug);
                        // Update the player information in the game
                        Player.updatePlayerInfo(game.playerInfo, game.players);
                        // Start the next turn
                        // Update the current player index
                        game.colorIndex = (game.colorIndex + 1) % game.colors.length;
                        // If the game is over
                        if (Marrakech.isGameOver(game.Gamecode)) {
                            // Get the winner's color character
                            char Winner = Marrakech.getWinner(game.Gamecode);
                            // Convert the color character to a string
                            String winString = "";
                            switch (Winner) {
                                case 'r':
                                    winString = "RED";
                                    break;
                                case 'c':
                                    winString = "CYAN";
                                    break;
                                case 'y':
                                    winString = "YELLOW";
                                    break;
                                case 'p':
                                    winString = "PURPLE";
                                    break;
                                default:
                                    break;
                            }
                            // Create a new dialog box to announce the game result
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Over");
                            alert.setHeaderText(null);
                            // If the game ended in a tie
                            if (Winner == 't') {
                                alert.setContentText("Game Over! It is a TIE!\nWould you like to play again?");
                            } else {
                                // If there is a winner
                                alert.setContentText("Game Over! Winner is " + winString + "! \nWould you like to play again?");
                            }


                            // Add buttons to the dialog box for the user to choose whether to play again
                            alert.getButtonTypes().clear();
                            ButtonType buttonTypeOne = new ButtonType("Yes");
                            ButtonType buttonTypeCancel = new ButtonType("No");
                            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

                            // Show the dialog box and wait for the user's response
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == buttonTypeOne) {
                                // If the user clicks the "Yes" button, restart the game
                                // Add code here to restart the game...
                            } else {
                                // If the user clicks the "No" button, exit the game
                                Platform.exit();
                            }

                        }
                        // Create a new rug image for the next player's turn
                        Image newRugImage = new Image("file:src/comp1110/ass2/gui/img/" + game.colors2[game.colorIndex] + "Rug.png");
                        // Create an image view for the new rug image
                        ImageView newRugImageView = new ImageView(newRugImage);
                        newRugImageView.setFitWidth(200);  // Set the width
                        newRugImageView.setFitHeight(80);  // Set the height
                        newRugImageView.setX(VIEWER_WIDTH / 2 - 350);  // Display the rug image in the center of the GUI
                        newRugImageView.setY(VIEWER_HEIGHT / 2 - 340);  // Display the rug image in the center of the GUI
                        // Add the new rug image view to the game root
                        game.root.getChildren().add(newRugImageView);

                        // Update the rug image view with the new rug image
                        rugImageView.setImage(newRugImage);
                        rugImageView.setVisible(false);
                        // The next player's turn starts with rotating Assam
                        game.isRotatingAssam = true;

                        // Check if the next player is AI
                        if (game.getCurrentPlayer().isAI) {
                            // If the next player is an AI, trigger the AI's turn
                            System.out.println("InAI");
                            aiTurn(scene, rugImageView, assamImageView, game);
                        }else{
                            System.out.println("ByHuman");

                        }
                    }
                });
                timeline4.play();
            });
            timeline2.play();

        });

        rt.play();

        // If the next player is a human, we do nothing and wait for their mouse events
    }

}

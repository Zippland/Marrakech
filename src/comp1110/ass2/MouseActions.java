package comp1110.ass2;

import comp1110.ass2.gui.*;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.text.Text;

import java.util.Optional;

import static comp1110.ass2.gui.Game.VIEWER_HEIGHT;
import static comp1110.ass2.gui.Game.VIEWER_WIDTH;

/**
 * Class that manages mouse actions within the game.
 *
 * @author Zihan Jian
 */
public class MouseActions {
    private AI aiPlayer;
    private ImageView rugImageView;
    private Game game;
    private Glow glowEffect = new Glow(0.8);
    Board board;
    private Assam assam;
    private Group root;
    Text diceText = new Text();
    int totalRotation = 0;

    /**
     * Constructor for the MouseActions class.
     */
    public MouseActions(Game game, ImageView rugImageView, Assam assam, Group root) {
        this.game = game;
        this.rugImageView = rugImageView;
        this.assam = assam;
        this.root = root;
        this.aiPlayer = new AI();  // Initializing an instance of the AI class
    }

    /**
     * Handle mouse movement within the game scene.
     */
    public void handleMouseMoved(Scene scene) {
        scene.setOnMouseMoved(event -> {
            if(game.getCurrentPlayer().isAI) {
                // AI related code
            } else {
                if (game.isRollingDice) {
                    game.diceImageView.setFitWidth(100);
                    game.diceImageView.setFitHeight(100);
                    game.diceImageView.setX(event.getSceneX() - game.diceImageView.getFitWidth() / 2);
                    game.diceImageView.setY(event.getSceneY() - game.diceImageView.getFitHeight() / 2);
                }

                if (game.isRotatingAssam) {
                    game.RollImageView.setVisible(true);
                    game.RollImageView.setFitWidth(100);
                    game.RollImageView.setFitHeight(100);
                    game.RollImageView.setX(event.getSceneX() - game.RollImageView.getFitWidth() / 2);
                    game.RollImageView.setY(event.getSceneY() - game.RollImageView.getFitHeight() / 2);
                }

                rugImageView.setX(event.getX() - rugImageView.getFitWidth() / 2);
                rugImageView.setY(event.getY() - rugImageView.getFitHeight() / 2);
            }
        });
    }

    /**
     * Handle mouse scrolling within the game scene.
     */
    public void handleMouseScroll(Scene scene) {
        scene.setOnScroll(event -> {
            if(game.getCurrentPlayer().isAI) {
                // AI related code
            } else {
                if (game.isRotatingAssam) {
                    int rotation = event.getDeltaY() > 0 ? 90 : -90;
                    this.totalRotation += rotation;
                    String currentAssam = "A" + game.assam.getX() + game.assam.getY() + game.assam.getDirection();
                    String newAssam = Marrakech.rotateAssam(currentAssam,rotation);
                    game.assam.setDirection(newAssam.charAt(3));
                    game.assam.setAssamDirection(game.assamImageView);
                } else if (event.getDeltaY() != 0) {
                    game.rugIsHorizontal = !game.rugIsHorizontal;
                    double temp = rugImageView.getFitWidth();
                    rugImageView.setFitWidth(rugImageView.getFitHeight());
                    rugImageView.setFitHeight(temp);
                }
            }
        });
    }

    /**
     * Handle mouse clicks within the game scene.
     */
    public void handleMouseClicked(Scene scene, ImageView assamImageView) {
        scene.setOnMouseClicked(event -> {

                if(game.isRotatingAssam) {
                    game.RollImageView.setVisible(false);
                    // Confirm Assam rotation
                    String currentAssam = Marrakech.rotateAssam(game.lastAssam,this.totalRotation);
                    game.assam.setDirection(currentAssam.charAt(3));
                    game.assam.setAssamDirection(game.assamImageView);
                    game.Gamecode =  game.Gamecode.substring(0,35) + currentAssam.charAt(3) + game.Gamecode.substring(36);
                    game.assam.setDirection(currentAssam.charAt(3));

                    game.isRotatingAssam = false;
                    game.isRollingDice = true;
                    game.diceImageView.setVisible(true);
                    this.totalRotation = 0;
                } else if(game.isRollingDice) {
                    // Play rotation animation
                    game.isRollingDice = false;
                    game.diceImageView.setFitWidth(400);
                    game.diceImageView.setFitHeight(400);
                    game.diceImageView.setX(VIEWER_WIDTH / 2 -200);
                    game.diceImageView.setY(VIEWER_HEIGHT / 2 -200);
                    RotateTransition rt = new RotateTransition(Duration.seconds(1.5), game.diceImageView);
                    rt.setByAngle(1440);
                    rt.setCycleCount(1);
                    rt.setAutoReverse(false);

                    rt.setOnFinished(event3 -> {
                        // Gain points
                        int roll = Marrakech.rollDie();
                        // Display number
                        ImageView diceImageView = new ImageView(new Image("file:src/comp1110/ass2/gui/img/"+ roll +".png"));
                        diceImageView.setX(VIEWER_WIDTH / 2 - 200);  // Displays points in the center of the GUI
                        diceImageView.setY(VIEWER_HEIGHT / 2 - 200);  // Displays points in the center of the GUI
                        diceImageView.setFitWidth(400);
                        diceImageView.setFitHeight(400);
                        root.getChildren().add(diceImageView);

                        // Create a delayed animation
                        Timeline timeline = new Timeline(new KeyFrame(
                                Duration.millis(1500),
                                ae -> game.root.getChildren().remove(diceImageView)
                        ));
                        timeline.play();

                        Timeline timeline2 = new Timeline();
                        timeline2.setCycleCount(roll);
                        timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(500), event2 -> {
                            // Mobile Assam
                            assam.moveAssam(1, assamImageView);
                            game.lastAssam = assam.getString();
                        }));
                        timeline2.setOnFinished(event2 -> {
                            game.isRollingDice = false;
                            rugImageView.setVisible(true);
                            game.diceImageView.setVisible(false);

                            int addims = Marrakech.getPaymentAmount(game.Gamecode);
                            System.out.println(addims+"       dirhams");
                            System.out.println(game.Gamecode.charAt(36+1+3*(7*(game.assam.getX())+game.assam.getY()))+"       recieved");
                            switch (game.Gamecode.charAt(36+1+3*(7*(game.assam.getX())+game.assam.getY()))){
                                case 'r':
                                    if(game.players[0].status = true) {
                                        if (game.players[game.colorIndex].dirhams > addims) {
                                            game.players[game.colorIndex].dirhams -= addims;
                                            game.players[game.colorIndex].updateGameCode();
                                            game.players[0].dirhams += addims;
                                            game.players[0].updateGameCode();
                                        } else {
                                            game.players[0].dirhams += game.players[game.colorIndex].dirhams;
                                            game.players[0].updateGameCode();
                                            game.players[game.colorIndex].dirhams = 0;
                                            game.players[game.colorIndex].status = false;
                                            game.players[game.colorIndex].updateGameCode();
                                        }
                                    }
                                    break;
                                case 'c':
                                    if(game.players[1].status = true) {
                                        if (game.players[game.colorIndex].dirhams > addims) {
                                            game.players[game.colorIndex].dirhams -= addims;
                                            game.players[game.colorIndex].updateGameCode();
                                            game.players[1].dirhams += addims;
                                            game.players[1].updateGameCode();
                                        } else {
                                            game.players[1].dirhams += game.players[game.colorIndex].dirhams;
                                            game.players[1].updateGameCode();
                                            game.players[game.colorIndex].dirhams = 0;
                                            game.players[game.colorIndex].status = false;
                                            game.players[game.colorIndex].updateGameCode();
                                        }
                                    }
                                    break;
                                case 'y':
                                    if(game.players[2].status = true) {
                                        if (game.players[game.colorIndex].dirhams > addims) {
                                            game.players[game.colorIndex].dirhams -= addims;
                                            game.players[game.colorIndex].updateGameCode();
                                            game.players[2].dirhams += addims;
                                            game.players[2].updateGameCode();
                                        } else {
                                            game.players[2].dirhams += game.players[game.colorIndex].dirhams;
                                            game.players[2].updateGameCode();
                                            game.players[game.colorIndex].dirhams = 0;
                                            game.players[game.colorIndex].status = false;
                                            game.players[game.colorIndex].updateGameCode();
                                        }
                                    }
                                    break;
                                case 'p':
                                    if(game.players[3].status = true) {
                                        if (game.players[game.colorIndex].dirhams > addims) {
                                            game.players[game.colorIndex].dirhams -= addims;
                                            game.players[game.colorIndex].updateGameCode();
                                            game.players[3].dirhams += addims;
                                            game.players[3].updateGameCode();
                                        } else {
                                            game.players[3].dirhams += game.players[game.colorIndex].dirhams;
                                            game.players[3].updateGameCode();
                                            game.players[game.colorIndex].dirhams = 0;
                                            game.players[game.colorIndex].status = false;
                                            game.players[game.colorIndex].updateGameCode();
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            game.Player.updatePlayerInfo(game.playerInfo, game.players);
                            //System.out.println(game.Gamecode +"  After update dirham");

                        });
                        timeline2.play();
                        game.root.getChildren().remove(diceText);

                    });

                    rt.play();

                } else if(rugImageView.isVisible()) {
                    game.root.getChildren().remove(diceText);
                    int x = (int) (event.getX() / Board.TILE_SIZE);
                    int y = (int) (event.getY() / Board.TILE_SIZE);
                    Rug rug = new Rug(game.colors[game.colorIndex], Integer.parseInt(game.getRugId()), x, y, game.rugIsHorizontal ? x + 1 : x, game.rugIsHorizontal ? y : y + 1);

                    rug.x1 = game.rugIsHorizontal ? rug.getX1() - 2 : rug.getX1() - 1;
                    rug.y1 = game.rugIsHorizontal ? rug.getY1() - 1 : rug.getY1() - 2;
                    rug.x2 = game.rugIsHorizontal ? rug.x1 + 1 : rug.x1;
                    rug.y2 = game.rugIsHorizontal ? rug.y1 : rug.y1 + 1;

                    if (Marrakech.isRugValid(game.Gamecode, rug.getRugString()) && Marrakech.isPlacementValid(game.Gamecode, rug.getRugString())) {
                        game.board.addRug(rug, game.rugIsHorizontal, game);

                        game.getCurrentPlayer().updateGameCode(rug);
                        Player.updatePlayerInfo(game.playerInfo, game.players);
                        // The start of the next game
                        // Update the current player index
                        game.colorIndex = (game.colorIndex + 1) % game.colors.length;
                        while(game.getCurrentPlayer().status==false){
                            game.colorIndex = (game.colorIndex + 1) % game.colors.length;
                        }
                        //System.out.println(game.colorIndex);

                        if (Marrakech.isGameOver(game.Gamecode)) {
                            char Winner = Marrakech.getWinner(game.Gamecode);
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
                            final String finalWinString = winString;
                            Platform.runLater(() -> {
                                // Create a new Alert dialog
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Game Over");
                                alert.setHeaderText(null);
                                if (Winner == 't') {
                                    alert.setContentText("Game Over! It is a TIE!\nWould you like to play again?");
                                } else {
                                    alert.setContentText("Game Over! Winner is " + finalWinString + "! \nWould you like to play again?");
                                }


                                // Add a restart game button
                                alert.getButtonTypes().clear();
                                ButtonType buttonTypeOne = new ButtonType("Yes");
                                ButtonType buttonTypeCancel = new ButtonType("No");
                                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

                                // Displays the dialog box and waits for the user's response
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == buttonTypeOne) {
                                    // The user clicks the "Yes" button and restarts the game
                                    Platform.exit();
                                } else {
                                    // The user clicks the "No" button to close the game
                                    Platform.exit();
                                }
                            });

                        }

                        Image newRugImage = new Image("file:src/comp1110/ass2/gui/img/" + game.colors2[game.colorIndex] + "Rug.png");

                        ImageView newRugImageView = new ImageView(newRugImage);
                        newRugImageView.setFitWidth(200);  // 设置你想要的宽度
                        newRugImageView.setFitHeight(80);  // 设置你想要的高度
                        newRugImageView.setX(VIEWER_WIDTH / 2 - 350);  // 在GUI的正中心显示rugImage
                        newRugImageView.setY(VIEWER_HEIGHT / 2 - 340);  // 在GUI的正中心显示rugImage
                        root.getChildren().add(newRugImageView);


                        rugImageView.setImage(newRugImage);
                        rugImageView.setVisible(false);
                        game.isRotatingAssam = true;
                        game.RollImageView.setVisible(true);

                        // Check if the next player is AI
                        if(game.getCurrentPlayer().isAI) {
                            System.out.println("InAI");
                            aiPlayer.aiTurn(scene, rugImageView, assamImageView, game);
                        }else{
                            System.out.println("ByHuman");
                        }
                        // If the next player is a human, we do nothing and wait for their mouse events
                    }
                }

        });
    }

}

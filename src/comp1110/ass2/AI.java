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
/**
 * AI class that manages the artificial intelligence behavior for the game.
 *
 * @author Xinyue Fei
 */
public class AI {
    int totalRotation = 0;
    String winString = "";
    Game game;
    /**
     * Simulates a turn for the AI.
     */
    public void aiTurn(Scene scene, ImageView rugImageView,ImageView assamImageView, Game game2) {
        this.game = game2;
        // Step 1: Move Assam
        // For simplicity, we can move Assam in a random direction
        int rotation = new Random().nextInt(4) * 90;  // 0, 90, 180, or 270
        String currentAssam = "A" + game.assam.getX() + game.assam.getY() + game.assam.getDirection();
        String newAssam = Marrakech.rotateAssam(currentAssam, rotation);
        game.assam.setDirection(newAssam.charAt(3));
        game.assam.setAssamDirection(game.assamImageView);

        game.isRotatingAssam = false;
        game.isRollingDice = true;
        game.diceImageView.setVisible(true);
        this.totalRotation = 0;

        // Step 2: Roll the dice
        // 播放旋转动画
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
            // 获取点数
            int roll = Marrakech.rollDie();
            // 显示点数
            ImageView diceImageView = new ImageView(new Image("file:src/comp1110/ass2/gui/img/"+ roll +".png"));
            diceImageView.setX(VIEWER_WIDTH / 2 - 200);  // 在GUI的正中心显示点数
            diceImageView.setY(VIEWER_HEIGHT / 2 - 200);  // 在GUI的正中心显示点数
            diceImageView.setFitWidth(400);
            diceImageView.setFitHeight(400);
            game.root.getChildren().add(diceImageView);

            // 创建一个延迟动画
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(1500),
                    ae -> game.root.getChildren().remove(diceImageView)
            ));
            timeline.play();

            Timeline timeline2 = new Timeline();
            String lastAssamString = game.assam.getString();
            timeline2.setCycleCount(roll);
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(500), event2 -> {
                // Move Assam
                game.assam.moveAssam(1, assamImageView);
                game.lastAssam = game.assam.getString();
            }));
            timeline2.setOnFinished(event2 -> {
                game.Gamecode = game.Gamecode.substring(0,32)+game.assam.getString()+game.Gamecode.substring(36);
                //System.out.println(game.assam.getString());
                System.out.println(game.Gamecode);
                Timeline timeline4 = new Timeline();
                timeline4.setCycleCount(roll);
                timeline4.getKeyFrames().add(new KeyFrame(Duration.millis(300), event4 -> {
                    game.isRollingDice = false;
                    rugImageView.setVisible(true);
                    game.diceImageView.setVisible(false);

                    int addims = Marrakech.getPaymentAmount(game.Gamecode);
                    //System.out.println(addims+"       dirhams");
                    //System.out.println(game.Gamecode.charAt(36+1+3*(7*(game.assam.getX())+game.assam.getY()))+"       recieved");
                    switch (game.Gamecode.charAt(36+1+3*(7*(game.assam.getX())+game.assam.getY()))){
                        case 'r':
                            if(game.players[game.colorIndex].dirhams > addims){
                                game.players[game.colorIndex].dirhams -= addims;
                                game.players[game.colorIndex].updateGameCode();
                                game.players[0].dirhams += addims;
                                game.players[0].updateGameCode();
                            }else{
                                game.players[0].dirhams += game.players[game.colorIndex].dirhams;
                                game.players[0].updateGameCode();
                                game.players[game.colorIndex].dirhams = 0;
                                game.players[game.colorIndex].updateGameCode();
                            }
                            break;
                        case 'c':
                            if(game.players[game.colorIndex].dirhams > addims){
                                game.players[game.colorIndex].dirhams -= addims;
                                game.players[game.colorIndex].updateGameCode();
                                game.players[1].dirhams += addims;
                                game.players[1].updateGameCode();
                            }else{
                                game.players[1].dirhams += game.players[game.colorIndex].dirhams;
                                game.players[1].updateGameCode();
                                game.players[game.colorIndex].dirhams = 0;
                                game.players[game.colorIndex].updateGameCode();
                            }
                            break;
                        case 'y':
                            if(game.players[game.colorIndex].dirhams > addims){
                                game.players[game.colorIndex].dirhams -= addims;
                                game.players[game.colorIndex].updateGameCode();
                                game.players[2].dirhams += addims;
                                game.players[2].updateGameCode();
                            }else{
                                game.players[2].dirhams += game.players[game.colorIndex].dirhams;
                                game.players[2].updateGameCode();
                                game.players[game.colorIndex].dirhams = 0;
                                game.players[game.colorIndex].updateGameCode();
                            }
                            break;
                        case 'p':
                            if(game.players[game.colorIndex].dirhams > addims){
                                game.players[game.colorIndex].dirhams -= addims;
                                game.players[game.colorIndex].updateGameCode();
                                game.players[3].dirhams += addims;
                                game.players[3].updateGameCode();
                            }else{
                                game.players[3].dirhams += game.players[game.colorIndex].dirhams;
                                game.players[3].updateGameCode();
                                game.players[game.colorIndex].dirhams = 0;
                                game.players[game.colorIndex].updateGameCode();
                            }
                            break;
                        default:
                            break;
                    }
                    game.Player.updatePlayerInfo(game.playerInfo, game.players);
                }));
                timeline4.setOnFinished(event4 -> {
                    Random random = new Random();
                    int x = random.nextInt(7);
                    int y = random.nextInt(7);
                    Rug rug = new Rug(game.colors[game.colorIndex], Integer.parseInt(game.getRugId()), x, y, game.rugIsHorizontal ? x + 1 : x, game.rugIsHorizontal ? y : y + 1);

                    rug.x2 = game.rugIsHorizontal ? rug.x1 + 1 : rug.x1;
                    rug.y2 = game.rugIsHorizontal ? rug.y1 : rug.y1 + 1;
                    while (!(Marrakech.isRugValid(game.Gamecode, rug.getRugString())&&Marrakech.isPlacementValid(game.Gamecode, rug.getRugString()))) {
                        x = random.nextInt(7);
                        y = random.nextInt(7);
                        rug = new Rug(game.colors[game.colorIndex], Integer.parseInt(game.getRugId()), x, y, game.rugIsHorizontal ? x + 1 : x, game.rugIsHorizontal ? y : y + 1);

                        rug.x2 = game.rugIsHorizontal ? rug.x1 + 1 : rug.x1;
                        rug.y2 = game.rugIsHorizontal ? rug.y1 : rug.y1 + 1;
                    }
                    if (Marrakech.isRugValid(game.Gamecode, rug.getRugString()) && Marrakech.isPlacementValid(game.Gamecode, rug.getRugString())) {
                        game.board.addRug(rug, game.rugIsHorizontal, game);

                        game.getCurrentPlayer().updateGameCode(rug);
                        Player.updatePlayerInfo(game.playerInfo, game.players);
                        // 下一局的开始
                        // Update the current player index
                        game.colorIndex = (game.colorIndex + 1) % game.colors.length;
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
                            // 创建一个新的Alert对话框
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Over");
                            alert.setHeaderText(null);
                            if (Winner == 't') {
                                alert.setContentText("Game Over! It is a TIE!\nWould you like to play again?");
                            } else {
                                alert.setContentText("Game Over! Winner is " + winString + "! \nWould you like to play again?");
                            }


                            // 添加一个重新开始游戏的按钮
                            alert.getButtonTypes().clear();
                            ButtonType buttonTypeOne = new ButtonType("Yes");
                            ButtonType buttonTypeCancel = new ButtonType("No");
                            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

                            // 显示对话框并等待用户的响应
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == buttonTypeOne) {
                                // 用户点击了"Yes"按钮，重新开始游戏

                            } else {
                                // 用户点击了"No"按钮，关闭游戏
                                Platform.exit();
                            }

                        }

                        Image newRugImage = new Image("file:src/comp1110/ass2/gui/img/" + game.colors2[game.colorIndex] + "Rug.png");

                        ImageView newRugImageView = new ImageView(newRugImage);
                        newRugImageView.setFitWidth(200);  // 设置你想要的宽度
                        newRugImageView.setFitHeight(80);  // 设置你想要的高度
                        newRugImageView.setX(VIEWER_WIDTH / 2 - 350);  // 在GUI的正中心显示rugImage
                        newRugImageView.setY(VIEWER_HEIGHT / 2 - 340);  // 在GUI的正中心显示rugImage
                        game.root.getChildren().add(newRugImageView);


                        rugImageView.setImage(newRugImage);
                        rugImageView.setVisible(false);
                        game.isRotatingAssam = true;

                        // Check if the next player is AI
                        if (game.getCurrentPlayer().isAI) {
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

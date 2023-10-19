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
import static java.lang.Thread.sleep;


public class MouseActions {
    private ImageView rugImageView;
    private Game game;
    private Glow glowEffect = new Glow(0.8);
    Board board;
    private Assam assam;
    private Group root;
    Text diceText = new Text();
    int totalRotation = 0;

    public MouseActions(Game game, ImageView rugImageView, Assam assam, Group root) {
        this.game = game;
        this.rugImageView = rugImageView;
        this.assam = assam;
        this.root = root;
    }

    public void handleMouseMoved(Scene scene) {
        scene.setOnMouseMoved(event -> {
            if(game.getCurrentPlayer().isAI) {
                // Call your AI code here
            }else {
                if (game.isRollingDice) {
                    game.diceImageView.setFitWidth(100);
                    game.diceImageView.setFitHeight(100);
                    game.diceImageView.setX(event.getSceneX() - game.diceImageView.getFitWidth() / 2);
                    game.diceImageView.setY(event.getSceneY() - game.diceImageView.getFitHeight() / 2);
                }

                rugImageView.setX(event.getX() - rugImageView.getFitWidth() / 2);
                rugImageView.setY(event.getY() - rugImageView.getFitHeight() / 2);
            }
        });
    }

    public void handleMouseScroll(Scene scene) {
        scene.setOnScroll(event -> {
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
        });
    }

    public void handleMouseClicked(Scene scene, ImageView assamImageView) {
        scene.setOnMouseClicked(event -> {
            if(game.isRotatingAssam) {
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
                    root.getChildren().add(diceImageView);

                    // 创建一个延迟动画
                    Timeline timeline = new Timeline(new KeyFrame(
                            Duration.millis(1500),
                            ae -> game.root.getChildren().remove(diceImageView)
                    ));
                    timeline.play();

                    Timeline timeline2 = new Timeline();
                    timeline2.setCycleCount(roll);
                    timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(500), event2 -> {
                        // 移动Assam
                        assam.moveAssam(1, assamImageView);
                        game.lastAssam = assam.getString();
                    }));
                    timeline2.setOnFinished(event2 -> {
                        game.isRollingDice = false;
                        rugImageView.setVisible(true);
                        game.diceImageView.setVisible(false);

                        diceText.setFont(new Font(30));
                        diceText.setFill(Color.BLACK);
                        diceText.setText(String.valueOf("Scroll the mouse wheel to control \n the orientation of the blanket."));
                        diceText.setX(VIEWER_WIDTH / 2 - 480); // Centered horizontally
                        diceText.setY(VIEWER_HEIGHT / 2 + 290); // Centered vertically
                        game.root.getChildren().add(diceText);

                        int addims = Marrakech.getPaymentAmount(game.Gamecode);
                        System.out.println(addims+"       dirhams");
                        System.out.println(game.Gamecode.charAt(36+1+3*(7*(game.assam.getX())+game.assam.getY()))+"       recieved");
                        switch (game.Gamecode.charAt(36+1+3*(7*(game.assam.getX())+game.assam.getY()))){
                            case 'r':
                                game.players[game.colorIndex].dirhams -= addims;
                                game.players[game.colorIndex].updateGameCode();
                                game.players[0].dirhams += addims;
                                game.players[0].updateGameCode();
                                break;
                            case 'c':
                                game.players[game.colorIndex].dirhams -= addims;
                                game.players[game.colorIndex].updateGameCode();
                                game.players[1].dirhams += addims;
                                game.players[1].updateGameCode();
                                break;
                            case 'y':
                                game.players[game.colorIndex].dirhams -= addims;
                                game.players[game.colorIndex].updateGameCode();
                                game.players[2].dirhams += addims;
                                game.players[2].updateGameCode();
                                break;
                            case 'p':
                                game.players[game.colorIndex].dirhams -= addims;
                                game.players[game.colorIndex].updateGameCode();
                                game.players[3].dirhams += addims;
                                game.players[3].updateGameCode();
                                break;
                            default:
                                break;
                        }
                        game.Player.updatePlayerInfo(game.playerInfo, game.players);
                        System.out.println(game.Gamecode);

                    });
                    timeline2.play();
                    game.root.getChildren().remove(diceText);

                });

                rt.play();

            } else if(rugImageView.isVisible()){
                game.root.getChildren().remove(diceText);
                int x = (int) (event.getX() / Board.TILE_SIZE);
                int y = (int) (event.getY() / Board.TILE_SIZE);
                Rug rug = new Rug(game.colors[game.colorIndex], Integer.parseInt(game.getRugId()), x, y, game.rugIsHorizontal ? x + 1 : x, game.rugIsHorizontal ? y : y + 1);

                rug.x1 = game.rugIsHorizontal ? rug.getX1()-2:rug.getX1()-1;
                rug.y1 = game.rugIsHorizontal ? rug.getY1()-1:rug.getY1()-2;
                rug.x2 = game.rugIsHorizontal ? rug.x1+1:rug.x1;
                rug.y2 = game.rugIsHorizontal ? rug.y1:rug.y1+1;

                if (Marrakech.isRugValid(game.Gamecode,rug.getRugString())&&Marrakech.isPlacementValid(game.Gamecode,rug.getRugString())) {
                    game.board.addRug(rug,game.rugIsHorizontal, game);

                    game.getCurrentPlayer().updateGameCode(rug);
                    Player.updatePlayerInfo(game.playerInfo, game.players);
                    // 下一局的开始
                    game.colorIndex = (game.colorIndex + 1) % game.colors.length;

                    if(Marrakech.isGameOver(game.Gamecode)){
                        char Winner = Marrakech.getWinner(game.Gamecode);
                        String winString = "";
                        switch(Winner){
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
                        if(Winner == 't'){
                            alert.setContentText("Game Over! It is a TIE!\nWould you like to play again?");
                        }else{
                            alert.setContentText("Game Over! Winner is "+winString+"! \nWould you like to play again?");
                        }


                        // 添加一个重新开始游戏的按钮
                        alert.getButtonTypes().clear();
                        ButtonType buttonTypeOne = new ButtonType("Yes");
                        ButtonType buttonTypeCancel = new ButtonType("No");
                        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

                        // 显示对话框并等待用户的响应
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTypeOne){
                            // 用户点击了"Yes"按钮，重新开始游戏

                        } else {
                            // 用户点击了"No"按钮，关闭游戏
                            Platform.exit();
                        }

                    }
                    System.out.println();
                    System.out.println(game.players[game.colorIndex].color+" Turn");
                    System.out.println("Status: "+game.players[game.colorIndex].status+" | Rugs:"+game.players[game.colorIndex].rugs);
                    if(Marrakech.isGameOver(game.Gamecode)){
                        System.out.println(game.Gamecode);
                        char winner = Marrakech.getWinner(game.Gamecode);
                        String winnerString = "";
                        switch(winner){
                            case 'r':
                                winnerString = "RED";
                                break;
                            case 'y':
                                winnerString = "YELLOW";
                                break;
                            case 'c':
                                winnerString = "Cyan";
                                break;
                            case 'p':
                                winnerString = "Purple";
                                break;
                            default:
                                break;
                        }
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Game Over");
                        alert.setHeaderText(null);
                        if(winner=='t'){
                            alert.setContentText("Game Over! TIE! Would you like to play again?");
                        }else{
                            alert.setContentText("Game Over! The Winner is "+ winnerString +"! Would you like to play again?");
                        }
                    }

                    Image newRugImage = new Image("file:src/comp1110/ass2/gui/img/" + game.colors2[game.colorIndex] + "Rug.png");

                    ImageView newRugImageView = new ImageView(newRugImage);
                    newRugImageView.setFitWidth(200);  // 设置你想要的宽度
                    newRugImageView.setFitHeight(80);  // 设置你想要的高度
                    newRugImageView.setX(VIEWER_WIDTH / 2 - 350);  // 在GUI的正中心显示rugImage
                    newRugImageView.setY(VIEWER_HEIGHT / 2 - 340);  // 在GUI的正中心显示rugImage
                    root.getChildren().add(newRugImageView);

                    diceText.setFont(new Font(30));
                    diceText.setFill(Color.BLACK);
                    diceText.setText(String.valueOf("Scroll the mouse wheel to control \n the orientation of Assam."));
                    diceText.setX(VIEWER_WIDTH / 2 - 480); // Centered horizontally
                    diceText.setY(VIEWER_HEIGHT / 2 + 290); // Centered vertically
                    game.root.getChildren().add(diceText);


                    rugImageView.setImage(newRugImage);
                    rugImageView.setVisible(false);
                    game.isRotatingAssam = true;
                }


            }
        });
    }

}

package comp1110.ass2;

import comp1110.ass2.*;
import comp1110.ass2.gui.*;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MouseActions {
    private ImageView rugImageView;
    private Game game;
    private Glow glowEffect = new Glow(0.8);

    public MouseActions(Game game, ImageView rugImageView) {
        this.game = game;
        this.rugImageView = rugImageView;
    }

    public void handleMouseMoved(Scene scene) {
        scene.setOnMouseMoved(event -> {
            if (game.isRollingDice) {
                game.diceImageView.setX(event.getSceneX() - game.diceImageView.getFitWidth() / 2);
                game.diceImageView.setY(event.getSceneY() - game.diceImageView.getFitHeight() / 2);
            }

            rugImageView.setX(event.getX() - rugImageView.getFitWidth() / 2);
            rugImageView.setY(event.getY() - rugImageView.getFitHeight() / 2);
        });
    }

    public void handleMouseScroll(Scene scene) {
        scene.setOnScroll(event -> {
            if (game.isRotatingAssam) {
                int rotation = event.getDeltaY() > 0 ? 90 : -90;
                String currentAssam = "A" + game.assam.getX() + game.assam.getY() + game.assam.getDirection();
                String newAssam = Marrakech.rotateAssam(currentAssam, rotation);
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

    public void handleMouseClicked(Scene scene) {
        scene.setOnMouseClicked(event -> {
            if(game.isRotatingAssam) {
                // Confirm Assam rotation
                game.isRotatingAssam = false;
                game.isRollingDice = true;
                game.diceImageView.setVisible(true);
            } else if(game.isRollingDice) {
                // 播放旋转动画
                RotateTransition rt = new RotateTransition(Duration.seconds(2), game.diceImageView);
                rt.setByAngle(1440);
                rt.setCycleCount(1);
                rt.setAutoReverse(false);
                rt.play();

                // 获取点数
                int roll = Marrakech.rollDie();
                System.out.println("Rolled a " + roll);

                // 显示点数
                Text diceText = new Text();
                diceText.setFont(new Font(200));
                diceText.setFill(Color.YELLOW);
                diceText.setText(String.valueOf(roll));
                diceText.setX(event.getSceneX());
                diceText.setY(event.getSceneY());
                game.root.getChildren().add(diceText);

                // 创建一个延迟动画
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(1000),
                        ae -> game.root.getChildren().remove(diceText)
                ));
                timeline.play();


                game.isRollingDice = false;
                rugImageView.setVisible(true);
                game.diceImageView.setVisible(false);


            } else {
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

                    game.colorIndex = (game.colorIndex + 1) % game.colors.length;

                    Image newRugImage = new Image("file:src/comp1110/ass2/gui/img/" + game.colors2[game.colorIndex] + "Rug.png");
                    rugImageView.setImage(newRugImage);
                    rugImageView.setVisible(false);
                    game.isRotatingAssam = true;
                }


            }
        });
    }

}

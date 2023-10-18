package comp1110.ass2;

import comp1110.ass2.*;
import comp1110.ass2.gui.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.effect.Glow;

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
            rugImageView.setX(event.getX() - rugImageView.getFitWidth() / 2);
            rugImageView.setY(event.getY() - rugImageView.getFitHeight() / 2);

            int x = (int) (event.getX() / Board.TILE_SIZE);
            int y = (int) (event.getY() / Board.TILE_SIZE);
            Rug rug = new Rug('r', 0, x, y, game.rugIsHorizontal ? x + 1 : x, game.rugIsHorizontal ? y : y + 1);
            if (Marrakech.isPlacementValid("","")) {
                rugImageView.setEffect(glowEffect);
            } else {
                rugImageView.setEffect(null);
            }
        });
    }

    public void handleMouseScroll(Scene scene) {
        scene.setOnScroll(event -> {
            if (game.isRotatingAssam) {
                char newDirection = game.assam.getDirection();
                game.assam.setDirection(newDirection);
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
                // Assam rotation
                System.out.println("Assam rotation");
                char newDirection = game.assam.getDirection();
                game.assam.setDirection(newDirection);
                game.assam.setAssamDirection(game.assamImageView);
                game.isRotatingAssam = false;
                game.isRollingDice = true;
            } else if(game.isRollingDice) {
                int roll = Marrakech.rollDie();
                System.out.println("Rolled a " + roll);
                game.isRollingDice = false;
                rugImageView.setVisible(true);
            } else {
                int x = (int) (event.getX() / Board.TILE_SIZE);
                int y = (int) (event.getY() / Board.TILE_SIZE);
                Rug rug = new Rug(game.colors[game.colorIndex], 0, x, y, game.rugIsHorizontal ? x + 1 : x, game.rugIsHorizontal ? y : y + 1);
                if (Marrakech.isPlacementValid("","")) {
                    game.board.addRug(rug,game.rugIsHorizontal, game);
                    game.colorIndex = (game.colorIndex + 1) % game.colors.length;

                    Image newRugImage = new Image("file:src/comp1110/ass2/gui/img/" + game.colors2[game.colorIndex] + "Rug.png");
                    rugImageView.setImage(newRugImage);
                }
                rugImageView.setVisible(false);
                game.isRotatingAssam = true;
            }
        });
    }
}

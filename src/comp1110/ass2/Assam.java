package comp1110.ass2;

import comp1110.ass2.gui.Game;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static comp1110.ass2.Board.TILE_SIZE;

public class Assam {
    private int x;
    private int y;
    private char direction;
    private Board board;


    public Assam(int x, int y, char direction, Board board) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.board = board;
    }

    public static Assam parseAssam(String assamString,Board board) {
        int x = Character.getNumericValue(assamString.charAt(0));
        int y = Character.getNumericValue(assamString.charAt(1));
        char direction = assamString.charAt(2);
        return new Assam(x, y, direction,board);
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public char getDirection() {
        return this.direction;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public void setAssamPosition(Board board, ImageView assamImageView) {
        assamImageView.setX(board.getGridPane().getTranslateX() + this.x * TILE_SIZE + TILE_SIZE / 2 - assamImageView.getFitWidth() / 2);
        assamImageView.setY(board.getGridPane().getTranslateY() + this.y * TILE_SIZE + TILE_SIZE / 2 - assamImageView.getFitHeight() / 2);
    }

    public void setAssamDirection(ImageView assamImageView) {
        switch (this.direction) {
            case 'N':
                assamImageView.setRotate(0);
                break;
            case 'E':
                assamImageView.setRotate(90);
                break;
            case 'S':
                assamImageView.setRotate(180);
                break;
            case 'W':
                assamImageView.setRotate(-90);
                break;
        }
    }

    public void moveAssam(int dieResult, Game game, Group root, ImageView assamImageView) {
        System.out.println(dieResult);
        String oldAssam = "A" + this.getX() + this.getY() + this.getDirection();
        String newAssam = Marrakech.moveAssam(oldAssam, dieResult);


        // 解析新的Assam字符串
        this.setX(Character.getNumericValue(newAssam.charAt(1)));
        this.setY(Character.getNumericValue(newAssam.charAt(2)));
        this.setDirection(newAssam.charAt(3));
        System.out.println("newAssam:"+newAssam);

        // 更新Gamecode
        System.out.println("oldAssam:"+oldAssam);
        game.Gamecode = game.Gamecode.replace(oldAssam, newAssam);


        updateAssamView(board, assamImageView);

    }

    private void updateAssamView(Board board, ImageView assamImageView) {
        this.setAssamPosition(board, assamImageView);
        this.setAssamDirection(assamImageView);
    }
}

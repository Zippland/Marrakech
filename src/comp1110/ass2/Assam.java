package comp1110.ass2;

import comp1110.ass2.gui.Game;
import javafx.scene.image.ImageView;

import static comp1110.ass2.Board.TILE_SIZE;

/**
 * The Assam class represents the Assam character in the game.
 *
 * @author Zihan jian, Xinyue Fei
 */
public class Assam {
    private int x;
    private int y;
    private char direction;
    private Board board;
    Game game;


    public Assam(Game game,int x, int y, char direction, Board board) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.board = board;
        this.game = game;
    }

    public String getString(){
        return "A"+this.x+this.y+this.direction;
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
    /**
     * Set Assam's position based on the board and the given ImageView.
     */
    public void setAssamPosition(Board board, ImageView assamImageView) {
        assamImageView.setX(board.getGridPane().getTranslateX() + this.x * TILE_SIZE + TILE_SIZE / 2 - assamImageView.getFitWidth() / 2);
        assamImageView.setY(board.getGridPane().getTranslateY() + this.y * TILE_SIZE + TILE_SIZE / 2 - assamImageView.getFitHeight() / 2);
    }
    /**
     * Set the direction of Assam based on the given ImageView.
     */
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
    /**
     * Move Assam based on the result of the dice roll.
     */
    public void moveAssam(int dieResult, ImageView assamImageView) {
        String oldAssam = "A" + this.getX() + this.getY() + this.getDirection();
        String newAssam = Marrakech.moveAssam(oldAssam, dieResult);


        // Parse the new Assam string
        this.setX(Character.getNumericValue(newAssam.charAt(1)));
        this.setY(Character.getNumericValue(newAssam.charAt(2)));
        this.setDirection(newAssam.charAt(3));

        // Update the game code
        game.Gamecode = game.Gamecode.replace(oldAssam, newAssam);
        updateAssamView(board, assamImageView);
    }
    /**
     * Update Assam's view on the board.
     */
    private void updateAssamView(Board board, ImageView assamImageView) {
        this.setAssamPosition(board, assamImageView);
        this.setAssamDirection(assamImageView);
    }
}

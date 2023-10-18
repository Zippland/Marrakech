package comp1110.ass2;

import javafx.scene.image.Image;
public class Rug {
    private Image image;
    private char color;
    private int id;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Rug(char color, int id, int x1, int y1, int x2, int y2) {
        this.color = color;
        this.id = id;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        switch (color) {
            case 'r':
                this.image = new Image("file:src/comp1110/ass2/gui/img/RedRug.png");
                break;
            case 'p':
                this.image = new Image("file:src/comp1110/ass2/gui/img/PurpleRug.png");
                break;
            case 'y':
                this.image = new Image("file:src/comp1110/ass2/gui/img/YellowRug.png");
                break;
            case 'c':
                this.image = new Image("file:src/comp1110/ass2/gui/img/CyanRug.png");
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + color);
        }
    }

    public char getColor() {
        return this.color;
    }

    public int getX1() {
        return this.x1;
    }

    public int getY1() {
        return this.y1;
    }

    // You might also want to add getters for the other fields
    public int getId() {
        return this.id;
    }

    public int getX2() {
        return this.x2;
    }

    public int getY2() {
        return this.y2;
    }

    public Image getImage() {
        return this.image;
    }
}

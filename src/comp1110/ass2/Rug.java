package comp1110.ass2;

import javafx.scene.image.Image;
public class Rug {
    private Image image;
    private char color;
    private int id;
    int x1;
    int y1;
    int x2;
    int y2;

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
    public String getRugString() {
        System.out.println(""+color+String.format("%02d", id)+x1+y1+x2+y2);
        return ""+color+String.format("%02d", id)+x1+y1+x2+y2;
    }

    public static Rug parseRug(String rugString) {
        char color = rugString.charAt(0);
        int id = Integer.parseInt(rugString.substring(1, 3));
        int x1 = Character.getNumericValue(rugString.charAt(3));
        int y1 = Character.getNumericValue(rugString.charAt(4));
        int x2 = Character.getNumericValue(rugString.charAt(5));
        int y2 = Character.getNumericValue(rugString.charAt(6));
        return new Rug(color, id, x1, y1, x2, y2);
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

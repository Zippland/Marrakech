package comp1110.ass2;
public class Rug {
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
}

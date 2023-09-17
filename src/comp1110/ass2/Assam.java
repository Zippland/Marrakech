package comp1110.ass2;

public class Assam {
    private int x;
    private int y;
    private char direction;

    public Assam(int x, int y, char direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
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
}

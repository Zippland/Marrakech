package comp1110.ass2;

public class Player {
    private char color;
    private int dirhams;
    private int rugs;
    private boolean inGame;

    public Player(char color, int dirhams, int rugs, boolean inGame) {
        this.color = color;
        this.dirhams = dirhams;
        this.rugs = rugs;
        this.inGame = inGame;
    }

    public char getColor() {
        return color;
    }

    // You should also provide getters for the other fields
    public int getDirhams() {
        return dirhams;
    }

    public int getRugs() {
        return rugs;
    }

    public boolean isInGame() {
        return inGame;
    }
}


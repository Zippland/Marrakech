package comp1110.ass2;

import javafx.scene.Group;
import javafx.scene.control.Label;

public class Player {
    private char color;
    private int dirhams;
    private int rugs;
    private boolean status;

    public Player(char color, int dirhams, int rugs, boolean status) {
        this.color = color;
        this.dirhams = dirhams;
        this.rugs = rugs;
        this.status = status;
    }


    public static Player[] parsePlayers(String playerInfo) {
        String[] playerStrings = playerInfo.split("P");
        Player[] players = new Player[playerStrings.length];
        for (int i = 0; i < playerStrings.length; i++) {
            if (!playerStrings[i].isEmpty()) {
                char color = playerStrings[i].charAt(0);
                int dirhams = Integer.parseInt(playerStrings[i].substring(1, 4));
                int rugs = Integer.parseInt(playerStrings[i].substring(4, 6));
                char status = playerStrings[i].charAt(6);
                players[i] = new Player(color, dirhams, rugs, status == 'i');
            }
        }
        return players;
    }

    public static void updatePlayerInfo(Group playerInfo, Player[] players) {
        playerInfo.getChildren().clear(); // Clear old labels

        int yOffset = 0; // The offset used to position the label vertically
        for (Player player : players) {
            if (player != null) {
                playerInfo.getChildren().add(player.getPlayerInfo(yOffset));
                yOffset += 32; // Update the offset so that the next label is below
            }
        }
    }

    public Label getPlayerInfo(int yOffset) {
        String info = "Player < " + (color == 'r' ? "  Red " : color == 'c' ? "  Clay " : color == 'y' ? "Yellow" : "Purple" ) + " >   " + dirhams + " dirhams  |  " + rugs + " rugs  |   " + (status  ? "InGame" : "Out");
        Label playerLabel = new Label(info);
        playerLabel.setLayoutX(780); // Set the horizontal position of the label
        playerLabel.setLayoutY(50 + yOffset); // Set the vertical position of the label
        return playerLabel;
    }
}

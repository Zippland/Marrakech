package comp1110.ass2;

import comp1110.ass2.gui.Game;
import javafx.scene.Group;
import javafx.scene.control.Label;

public class Player {
    char color;
    int dirhams;
    int rugs;
    boolean status;
    private Game game;

    public Player(char color, int dirhams, int rugs, boolean status, Game game) {
        this.color = color;
        this.dirhams = dirhams;
        this.rugs = rugs;
        this.status = status;
        this.game = game;
    }


    public String getPlayerString() {
        return "P" + this.color + String.format("%03d", this.dirhams) + String.format("%02d", this.rugs) + (this.status ? "i" : "o");
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
        String info = "Player < " + (color == 'r' ? "  Red " : color == 'c' ? "  Cyan " : color == 'y' ? "Yellow" : "Purple" ) + " >   " + dirhams + " dirhams  |  " + rugs + " rugs  |   " + (status  ? "InGame" : "Out");
        Label playerLabel = new Label(info);
        playerLabel.setLayoutX(780); // Set the horizontal position of the label
        playerLabel.setLayoutY(50 + yOffset); // Set the vertical position of the label using game's currentPlayerIndex
        return playerLabel;
    }

    public void updateGameCode() {
        // Split the game code into its components
        String[] components = this.game.Gamecode.split("P");
        String[] Tempcomponents = this.game.Gamecode.split("A");
        for (int i = 1; i < components.length; i++) {
            if (components[i].charAt(0) == this.color) {
                // Update the player's information in the game code
                components[i] = Character.toString(this.color) + String.format("%03d", this.dirhams) + String.format("%02d", this.rugs) + (this.status ? "i" : "o");
                break;
            }
        }

        // Reconstruct the game code
        // Reconstruct the game code
        StringBuilder newGameCode = new StringBuilder(components[0]);
        for (int i = 1; i < components.length; i++) {
            newGameCode.append("P").append(components[i]);
        }
        if(this.color=='p'){
            newGameCode.append("A").append(Tempcomponents[1]);
        }

        // Update the game code
        this.game.Gamecode = newGameCode.toString();
    }

    public void updateGameCode(Rug rug) {
        if (this.color == rug.getColor()) {
            // Decrease the number of rugs
            this.rugs -= 1;
            // Update the status
            this.status = this.rugs == 0 ? false : true;

            // Split the game code into its components
            String[] components = this.game.Gamecode.split("P");
            String[] Tempcomponents = this.game.Gamecode.split("A");
            for (int i = 1; i < components.length; i++) {
                if (components[i].charAt(0) == this.color) {
                    // Update the player's information in the game code
                    components[i] = Character.toString(this.color) + String.format("%03d", this.dirhams) + String.format("%02d", this.rugs) + (this.status ? "i" : "o");
                    break;
                }
            }

            // Reconstruct the game code
            StringBuilder newGameCode = new StringBuilder(components[0]);
            for (int i = 1; i < components.length; i++) {
                newGameCode.append("P").append(components[i]);
            }
            if(this.color=='p'){
                newGameCode.append("A").append(Tempcomponents[1]);
            }

            // Update the game code
            this.game.Gamecode = newGameCode.toString();
        }
    }




}

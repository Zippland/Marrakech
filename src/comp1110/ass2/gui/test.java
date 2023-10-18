package comp1110.ass2.gui;

import javafx.stage.Stage;

public class test {
    public static boolean isGameOver(String currentGame) {
        // Split the currentGame string into individual player strings
        String[] playerStrings = currentGame.split("P");

        for (String playerString : playerStrings) {
            if (!playerString.isEmpty()) {
                // Get the number of rugs left for this player
                int rugsLeft = Integer.parseInt(playerString.substring(4, 6));
                System.out.println(playerString);

                // Check if the player has no rugs left
                if (rugsLeft == 0) {
                    return true;
                }
            }
        }

        // If no player has 0 rugs left, the game is not over
        return false;
    }
    public static void main(String[] args){
        String currentGame = "Pc04000iPy03900iPp01101iPr03001iA10EBy21c23c23y24r13c16c20n00p16c30c30r29r21c20r18r18y11y11c29r21r20n00n00y22c26y15p15p15r05r05y22r28r28y20r15n00c28c28c13p20p20r15n00n00c05c11y12p02r10";
        System.out.println(isGameOver(currentGame));
    }
}

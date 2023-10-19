package comp1110.ass2.gui;

import comp1110.ass2.Marrakech;

public class TEST {
    public static boolean isGameOver(String currentGame) {
        // Split the game string into player strings
        String[] playerStrings = currentGame.split("P");

        // Iterate over each player string
        for (String playerString : playerStrings) {
            // Skip the first split result as it will be an empty string
            if (playerString.isEmpty()) {
                continue;
            }

            // Extract the number of rugs and the in-game status of the player
            int numRugs = Integer.parseInt(playerString.substring(4, 6));
            char inGameStatus = playerString.charAt(6);

            // If the player is still in the game and has rugs remaining, the game is not over
            if (inGameStatus == 'i' && numRugs > 0) {
                return false;
            }
        }

        // If none of the players have rugs remaining or are in the game, the game is over
        return true;
    }
    public static char getWinner(String gameState) {
        // Split the game state string into its components
        String[] components = gameState.split("A");

        // Extract the player information
        String[] playerStrings = components[0].split("P");
        int[] dirhams = new int[4];
        int[] scores = new int[4];
        char[] colors = new char[4];
        boolean[] inGame = new boolean[4];
        for (int i = 1; i < playerStrings.length; i++) {
            String playerString = playerStrings[i];
            colors[i-1] = playerString.charAt(0);
            int dirham = Integer.parseInt(playerString.substring(1, 4));
            inGame[i-1] = playerString.charAt(6) == 'i';
            dirhams[i-1] = dirham;
        }

        if(!isGameOver(gameState)){
            return 'n';
        }

        // Extract the board information
        String board = components[1].substring(4);
        for (int i = 0; i < board.length(); i += 3) {
            String rug = board.substring(i, i + 3);
            if (!rug.equals("n00")) {
                char color = rug.charAt(0);
                for (int j = 0; j < 4; j++) {
                    if (colors[j] == color) {
                        scores[j]++;
                        break;
                    }
                }
            }
        }

        // Calculate total scores and find the player with the highest score
        int[] totalScores = new int[4];
        int maxScore = -1;
        int maxScoreIndex = -1;
        for (int i = 0; i < 4; i++) {
            if (inGame[i]) {
                totalScores[i] = dirhams[i] + scores[i];
                if (totalScores[i] > maxScore) {
                    maxScore = totalScores[i];
                    maxScoreIndex = i;
                }
            }
        }

        // Check for ties
        for (int i = 0; i < 4; i++) {
            if (i != maxScoreIndex && inGame[i] && totalScores[i] == maxScore) {
                // If the tied players have the same number of dirhams, then it's a tie game
                if (dirhams[i] == dirhams[maxScoreIndex]) {
                    return 't';
                } else if (dirhams[i] > dirhams[maxScoreIndex]) {
                    // If the current player has more dirhams than the previous max, update the max
                    maxScoreIndex = i;
                }
            }
        }

        // Return the color of the player with the highest score
        return colors[maxScoreIndex];
    }
    public static void main(String[] args){
        String gameString = "Pr01600oPc01000oPy04000oPp03500oA31WBc43y56y44y44p50r49r45n00y56p26p22p50r49c15r01c23p38p42c55c39c39r01r37p38p42c55y52y36r29y60y60p34c35c47c47r33y28c59c59r53c31y32r33y28p54p58y48c31n00";
        System.out.println(getWinner(gameString));

    }
}

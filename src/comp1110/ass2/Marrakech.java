package comp1110.ass2;

import comp1110.ass2.*;
public class Marrakech {
    /**
     * Determine whether a rug String is valid.
     * For this method, you need to determine whether the rug String is valid, but do not need to determine whether it
     * can be placed on the board (you will determine that in Task 10 ). A rug is valid if and only if all the following
     * conditions apply:
     *  - The String is 7 characters long
     *  - The first character in the String corresponds to the colour character of a player present in the game
     *  - The next two characters represent a 2-digit ID number
     *  - The next 4 characters represent coordinates that are on the board
     *  - The combination of that ID number and colour is unique
     * To clarify this last point, if a rug has the same ID as a rug on the board, but a different colour to that rug,
     * then it may still be valid. Obviously multiple rugs are allowed to have the same colour as well so long as they
     * do not share an ID. So, if we already have the rug c013343 on the board, then we can have the following rugs
     *  - c023343 (Shares the colour but not the ID)
     *  - y013343 (Shares the ID but not the colour)
     * But you cannot have c014445, because this has the same colour and ID as a rug on the board already.
     * @param gameString A String representing the current state of the game as per the README
     * @param rug A String representing the rug you are checking
     * @return true if the rug is valid, and false otherwise.
     */
    public static boolean isRugValid(String gameString, String rug) {
        // Check if the rug string is 7 characters long
        if (rug.length() != 7) {
            return false;
        }

        // Check if the first character in the rug string corresponds to the color character of a player present in the game
        char color = rug.charAt(0);
        if (!gameString.contains("P" + color)) {
            return false;
        }

        // Check if the next two characters represent a 2-digit ID number
        String id = rug.substring(1, 3);
        try {
            Integer.parseInt(id); // This will throw a NumberFormatException if id is not a valid integer
        } catch (NumberFormatException e) {
            return false;
        }

        // Check if the next 4 characters represent coordinates that are on the board
        String coordinates = rug.substring(3);
        for (int i = 0; i < coordinates.length(); i++) {
            int coordinate = Character.getNumericValue(coordinates.charAt(i));
            if (coordinate < 0 || coordinate > 6) {
                return false;
            }
        }

        // Check if the combination of color and ID is unique
        String rugInfo = rug.substring(0, 3); // Get the color and ID of the rug
        String boardString = gameString.substring(gameString.indexOf('B')); // Get the board string from the game string
        if (boardString.contains(rugInfo)) {
            return false;
        }

        return true;
    }



    /**
     * Roll the special Marrakech die and return the result.
     * Note that the die in Marrakech is not a regular 6-sided die, since there
     * are no faces that show 5 or 6, and instead 2 faces that show 2 and 3. That
     * is, of the 6 faces
     *  - One shows 1
     *  - Two show 2
     *  - Two show 3
     *  - One shows 4
     * As such, in order to get full marks for this task, you will need to implement
     * a die where the distribution of results from 1 to 4 is not even, with a 2 or 3
     * being twice as likely to be returned as a 1 or 4.
     * @return The result of the roll of the die meeting the criteria above
     */
    public static int rollDie() {
        int roll = (int) (Math.random() * 6) + 1;
        return switch (roll) {
            case 1 -> 1;
            case 2, 3 -> 2;
            case 4, 5 -> 3;
            case 6 -> 4;
            default -> -1;
        };
    }

    /**
     * Determine whether a game of Marrakech is over
     * Recall from the README that a game of Marrakech is over if a Player is about to enter the rotation phase of their
     * turn, but no longer has any rugs. Note that we do not encode in the game state String whose turn it is, so you
     * will have to think about how to use the information we do encode to determine whether a game is over or not.
     * @param currentGame A String representation of the current state of the game.
     * @return true if the game is over, or false otherwise.
     */
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

    /**
     * Implement Assam's rotation.
     * Recall that Assam may only be rotated left or right, or left alone -- he cannot be rotated a full 180 degrees.
     * For example, if he is currently facing North (towards the top of the board), then he could be rotated to face
     * East or West, but not South. Assam can also only be rotated in 90 degree increments.
     * If the requested rotation is illegal, you should return Assam's current state unchanged.
     * @param currentAssam A String representing Assam's current state
     * @param rotation The requested rotation, in degrees. This degree reading is relative to the direction Assam
     *                 is currently facing, so a value of 0 for this argument will keep Assam facing in his
     *                 current orientation, 90 would be turning him to the right, etc.
     * @return A String representing Assam's state after the rotation, or the input currentAssam if the requested
     * rotation is illegal.
     */
    public static String rotateAssam(String currentAssam, int rotation) {
        rotation %= 360;
        if (rotation == 270){
            rotation = -90;
        }
        if (rotation == -270){
            rotation = 90;
        }
        if (rotation != 90 && rotation != -90 ) {
            return currentAssam; // Invalid rotation
        }

        char x = currentAssam.charAt(1);
        char y = currentAssam.charAt(2);
        char direction = currentAssam.charAt(3);

        switch (direction) {
            case 'N':
                direction = rotation == 90 ? 'E' : 'W' ;
                break;
            case 'E':
                direction = rotation == 90 ? 'S' : 'N' ;
                break;
            case 'S':
                direction = rotation == 90 ? 'W' : 'E' ;
                break;
            case 'W':
                direction = rotation == 90 ? 'N' : 'S' ;
                break;
        }
        return "A" + x + y + direction;
    }



    /**
     * Determine whether a potential new placement is valid (i.e that it describes a legal way to place a rug).
     * There are a number of rules which apply to potential new placements, which are detailed in the README but to
     * reiterate here:
     *   1. A new rug must have one edge adjacent to Assam (not counting diagonals)
     *   2. A new rug must not completely cover another rug. It is legal to partially cover an already placed rug, but
     *      the new rug must not cover the entirety of another rug that's already on the board.
     * @param gameState A game string representing the current state of the game
     * @param rug A rug string representing the candidate rug which you must check the validity of.
     * @return true if the placement is valid, and false otherwise.
     */
    public static boolean isPlacementValid(String gameState, String rug) {
        String[] components = gameState.split("A");

        // Extract and print Assam's information
        String assamInfo = components[1].substring(0, 3);
        Assam assam = Assam.parseAssam(assamInfo);
        int assamX = assam.getX();
        int assamY = assam.getY();

        // Extract the rug's information
        int rugX1 = Integer.parseInt(rug.substring(3, 4));
        int rugY1 = Integer.parseInt(rug.substring(4, 5));
        int rugX2 = Integer.parseInt(rug.substring(5, 6));
        int rugY2 = Integer.parseInt(rug.substring(6, 7));

        // Check if the rug is adjacent to Assam
        if (Math.abs(rugX1 - assamX) + Math.abs(rugY1 - assamY) > 1) {
            if (Math.abs(rugX2 - assamX) + Math.abs(rugY2 - assamY) > 1) {
                return false;
            }
        }
        // Check if the rug is covered Assam
        if ((rugX1 == assamX && rugY1 == assamY) || (rugX2 == assamX && rugY2 == assamY)) {
            return false;
        }

        // Check if the rug completely covers another rug
        // Get the board string
        String boardString = components[1].substring(4);

        // Check if the rug completely covers another rug
        int square1Index = 3 * (rugX1 * 7 + rugY1);
        int square2Index = 3 * (rugX2 * 7 + rugY2);
        String square1Rug = boardString.substring(square1Index, square1Index + 3);
        String square2Rug = boardString.substring(square2Index, square2Index + 3);

        if (!square1Rug.equals("n00") && !square2Rug.equals("n00")) {
            if (square1Rug.equals(square2Rug)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Determine the amount of payment required should another player land on a square.
     * For this method, you may assume that Assam has just landed on the square he is currently placed on, and that
     * the player who last moved Assam is not the player who owns the rug landed on (if there is a rug on his current
     * square). Recall that the payment owed to the owner of the rug is equal to the number of connected squares showing
     * on the board that are of that colour. Similarly to the placement rules, two squares are only connected if they
     * share an entire edge -- diagonals do not count.
     * @param gameString A String representation of the current state of the game.
     * @return The amount of payment due, as an integer.
     */
    // This is a very basic implementation and will depend heavily on how your gameString is formatted
    public static int getPaymentAmount(String gameString, String assamString) {
        // Convert gameString into a 2D array
        char[][] gameBoard = convertStringToBoard(gameString);

        // Parse the Assam string to get Assam's position and direction
        int[] assamPosition = parseAssamString(assamString);

        // Determine the color of the square Assam is currently on
        char squareColor = getColorOfAssamSquare(gameBoard, assamPosition);

        // Use BFS to count the number of connected squares of the same color
        int payment = countConnectedSquares(gameBoard, squareColor);

        return payment;
    }

    public static char[][] convertStringToBoard(String gameString) {
        String[] rows = gameString.split("\n");
        char[][] gameBoard = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            gameBoard[i] = rows[i].toCharArray();
        }
        return gameBoard;
    }

    public static int[] parseAssamString(String assamString) {
        int x = Character.getNumericValue(assamString.charAt(1));
        int y = Character.getNumericValue(assamString.charAt(2));
        int direction;
        switch (assamString.charAt(3)) {
            case 'N':
                direction = 0; break;
            case 'E':
                direction = 1; break;
            case 'S':
                direction = 2; break;
            case 'W':
                direction = 3; break;
            default:
                direction = -1; // Error case
        }
        return new int[] {x, y, direction};
    }

    public static char getColorOfAssamSquare(char[][] gameBoard, int[] assamPosition) {
        int x = assamPosition[0];
        int y = assamPosition[1];
        return gameBoard[x][y];
    }

    public static int countConnectedSquares(char[][] gameBoard, char color) {
        int count = 0;
        boolean[][] visited = new boolean[gameBoard.length][gameBoard[0].length];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] {assamRow, assamCol});

        while (!queue.isEmpty()) {
            int[] square = queue.remove();
            int row = square[0];
            int col = square[1];

            if (row < 0 || col < 0 || row >= gameBoard.length || col >= gameBoard[0].length) {
                continue;
            }

            if (visited[row][col] || gameBoard[row][col] != color) {
                continue;
            }

            visited[row][col] = true;
            count++;
            queue.add(new int[] {row - 1, col});
            queue.add(new int[] {row + 1, col});
            queue.add(new int[] {row, col - 1});
            queue.add(new int[] {row, col + 1});
        }

        return count;
    }





    /**
     * Determine the winner of a game of Marrakech.
     * For this task, you will be provided with a game state string and have to return a char representing the colour
     * of the winner of the game. So for example if the cyan player is the winner, then you return 'c', if the red
     * player is the winner return 'r', etc...
     * If the game is not yet over, then you should return 'n'.
     * If the game is over, but is a tie, then you should return 't'.
     * Recall that a player's total score is the sum of their number of dirhams and the number of squares showing on the
     * board that are of their colour, and that a player who is out of the game cannot win. If multiple players have the
     * same total score, the player with the largest number of dirhams wins. If multiple players have the same total
     * score and number of dirhams, then the game is a tie.
     * @param gameState A String representation of the current state of the game
     * @return A char representing the winner of the game as described above.
     */
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



    /**
     * Implement Assam's movement.
     * Assam moves a number of squares equal to the die result, provided to you by the argument dieResult. Assam moves
     * in the direction he is currently facing. If part of Assam's movement results in him leaving the board, he moves
     * according to the tracks diagrammed in the assignment README, which should be studied carefully before attempting
     * this task. For this task, you are not required to do any checking that the die result is sensible, nor whether
     * the current Assam string is sensible either -- you may assume that both of these are valid.
     * @param currentAssam A string representation of Assam's current state.
     * @param dieResult The result of the die, which determines the number of squares Assam will move.
     * @return A String representing Assam's state after the movement.
     */
    public static String moveAssam(String currentAssam, int dieResult){
        int x = Character.getNumericValue(currentAssam.charAt(1));
        int y = Character.getNumericValue(currentAssam.charAt(2));
        char direction = currentAssam.charAt(3);

        switch (direction) {
            case 'N':
                if (y > dieResult-1) {
                    y -= dieResult;
                } else {
                    if(x==1 || x==3 || x==5){
                        x-=1;
                        y = dieResult - y - 1;
                        direction = 'S';
                    }else if(x==0 || x==2 || x==4){
                        x+=1;
                        y = dieResult - y - 1;
                        direction = 'S';
                    }else{
                        x = 7 - dieResult + y;
                        y = 0;
                        direction = 'W';
                    }
                }
                break;
            case 'E':
                if (x + dieResult < 7) {
                    x += dieResult;
                } else {
                    if(y==1 || y==3 || y==5){
                        y+=1;
                        x = 13 - dieResult - x;
                        direction = 'W';
                    }else if(y==2 || y==4 || y==6){
                        y-=1;
                        x = 13 - dieResult - x;
                        direction = 'W';
                    }else{
                        y = dieResult + x - 7;
                        x = 6;
                        direction = 'S';
                    }
                }
                break;
            case 'S':
                if (y + dieResult < 7) {
                    y += dieResult;
                } else {
                    if(x==1 || x==3 || x==5){
                        x+=1;
                        y = 13 - dieResult - y;
                        direction = 'N';
                    }else if(x==2 || x==4 || x==6){
                        x-=1;
                        y = 13 - dieResult - y;
                        direction = 'N';
                    }else{
                        x = dieResult + y - 7;
                        y = 6;

                        direction = 'E';
                    }
                }
                break;
            case 'W':
                if (x > dieResult-1) {
                    x -= dieResult;
                } else {
                    if(y==1||y==3||y==5){
                        y-=1;
                        x = dieResult - x - 1;
                        direction = 'E';
                    }else if(y==0||y==2||y==4){
                        y+=1;
                        x = dieResult - x - 1;
                        direction = 'E';
                    }else{
                        y = 7 - dieResult + x;
                        x = 0;
                        direction = 'N';
                    }
                }
                break;
        }
        return "A" + x + y + direction;
    }

    /**
     * Place a rug on the board
     * This method can be assumed to be called after Assam has been rotated and moved, i.e in the placement phase of
     * a turn. A rug may only be placed if it meets the conditions listed in the isPlacementValid task. If the rug
     * placement is valid, then you should return a new game string representing the board after the placement has
     * been completed. If the placement is invalid, then you should return the existing game unchanged.
     * @param currentGame A String representation of the current state of the game.
     * @param rug A String representation of the rug that is to be placed.
     * @return A new game string representing the game following the successful placement of this rug if it is valid,
     * or the input currentGame unchanged otherwise.
     */
    public static String makePlacement(String currentGame, String rug) {
        if(isPlacementValid(currentGame, rug)){
            String boardState = currentGame.split("A")[1].substring(4);
            String newBoardState = "";
            Rug R = Rug.parseRug(rug);
            for (int i = 0; i < boardState.length(); i += 3) {
                int currentX = i / 21;
                int currentY = (i - currentX * 21) / 3;
                if ((currentX == R.x1 && currentY == R.y1) || (currentX == R.x2 && currentY == R.y2)) {
                    // Replace the tile state with the new rug color and id
                    newBoardState += String.valueOf(R.getColor()) + R.getId();
                } else {
                    // Keep the original tile state
                    newBoardState += boardState.substring(i, i + 3);
                }
            }

            // Replace the board state in the game code with the new board state
            String newGameCode = currentGame.split("A")[0] + "A" + currentGame.split("A")[1].substring(0, 4) + newBoardState;

            return newGameCode;
        }
        return currentGame;
    }
}

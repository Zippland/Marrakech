package comp1110.ass2.gui;

import comp1110.ass2.Marrakech;

public class TEST {
    public static int getPaymentAmount(String gameString) {
        // Extract Assam's position and orientation
        String assamString = gameString.split("B")[0].split("A")[1];
        int assamX = Integer.parseInt(assamString.charAt(0) + "");
        int assamY = Integer.parseInt(assamString.charAt(1) + "");

        // Extract board string
        String boardString = gameString.split("B")[1];

        // Convert board string to 2D array
        String[][] board = new String[7][7];
        for (int i = 0; i < 49; i++) {
            board[i / 7][i % 7] = boardString.substring(i*3, i*3+3);
        }

        // Get the color of the rug Assam landed on
        String assamColor = board[assamX][assamY].charAt(0) + "";

        // Initialize visited array
        boolean[][] visited = new boolean[7][7];

        // Perform DFS to find connected rugs
        return dfs(board, visited, assamX, assamY, assamColor);
    }

    private static int dfs(String[][] board, boolean[][] visited, int x, int y, String color) {
        // Check if out of bounds or already visited
        if (x < 0 || y < 0 || x >= 7 || y >= 7 || visited[x][y]) {
            return 0;
        }

        // Check if the rug is the same color
        if (!board[x][y].startsWith(color)) {
            return 0;
        }

        // Mark as visited
        visited[x][y] = true;

        // Visit all adjacent squares
        int count = 1;
        count += dfs(board, visited, x - 1, y, color);
        count += dfs(board, visited, x + 1, y, color);
        count += dfs(board, visited, x, y - 1, color);
        count += dfs(board, visited, x, y + 1, color);

        return count;
    }
    public static void main(String[] args){
        String gameString = "Pr03015iPc03015iPy03015iPp03015iA33NBn00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00n00";
        System.out.println(getPaymentAmount(gameString));

    }
}

import java.util.ArrayList;
import java.util.List;

class TicTacToeGameLogic {
    private Player[][] board;
    private static final int SIZE = 3;

    public TicTacToeGameLogic() {
        board = new Player[SIZE][SIZE];
    }

    public void makeMove(int row, int col, Player player) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == null) {
            board[row][col] = player;
        }
    }
    public void undoLastMove(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            board[row][col] = null;
        }
    }


    public Player checkWinner() {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != null && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
            if (board[0][i] != null && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }
        if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }
        return null;
    }
    public int[][] getWinningLine() {
        for (int i = 0; i < SIZE; i++) {
            // Check rows
            if (board[i][0] != null && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return new int[][]{{i, 0}, {i, 1}, {i, 2}};
            }
            // Check columns
            if (board[0][i] != null && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return new int[][]{{0, i}, {1, i}, {2, i}};
            }
        }
        // Check diagonals
        if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return new int[][]{{0, 0}, {1, 1}, {2, 2}};
        }
        if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return new int[][]{{0, 2}, {1, 1}, {2, 0}};
        }
        // No winner
        return null;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = null;
            }
        }
    }

    public int[] getBotMove() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        if (!emptyCells.isEmpty()) {
            int randomIndex = (int) (Math.random() * emptyCells.size());
            return emptyCells.get(randomIndex);
        }
        return null;
    }
}

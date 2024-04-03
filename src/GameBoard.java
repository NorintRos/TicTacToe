import javax.swing.*;
import java.awt.*;

class GameBoard {
    private JFrame frame;
    private JPanel gamePanel;
    private JPanel mainMenuPanel;
    private JLabel currentPlayerLabel;
    private JButton[][] buttons;
    private JButton rematchButton;
    private JButton returnToMainMenuButton;
    private Player currentPlayer;
    private boolean gameOver;
    private boolean playWithBot;
    private TicTacToeGameLogic gameLogic;
    private JButton backtrackButton;
    private int lastRow = -1;
    private int lastCol = -1;

    public GameBoard(JFrame frame, GameMode gameMode, JPanel mainMenuPanel) {
        this.frame = frame;
        this.playWithBot = (gameMode == GameMode.BOT);
        this.mainMenuPanel = mainMenuPanel;
        this.gameLogic = new TicTacToeGameLogic();
        initializeGameBoard();
    }
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        ImageIcon homeIcon = new ImageIcon("img/home.png");
        homeIcon = resizeIcon(homeIcon, 30, 30);
        returnToMainMenuButton = new JButton(homeIcon);
        returnToMainMenuButton.setBorder(BorderFactory.createEmptyBorder());
        returnToMainMenuButton.setContentAreaFilled(false);
        returnToMainMenuButton.addActionListener(e -> returnToMainMenu());
        topPanel.add(returnToMainMenuButton, BorderLayout.WEST);

        currentPlayerLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        topPanel.add(currentPlayerLabel, BorderLayout.CENTER);

        return topPanel;
    }
    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        buttons = new JButton[3][3];
        currentPlayer = Player.X;
        gameOver = false;

        backtrackButton = new JButton("Backtrack");
        backtrackButton.setFont(new Font("Arial", Font.BOLD, 30));
        backtrackButton.addActionListener(e -> backtrack());
        gamePanel.add(backtrackButton, BorderLayout.NORTH);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 120));
                final int row = i;
                final int col = j;
                button.addActionListener(e -> handleButtonClick(row, col, button));
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        return boardPanel;
    }
    private JButton createRematchButton() {
        JButton rematchButton = new JButton("Rematch");
        rematchButton.setFont(new Font("Arial", Font.BOLD, 30));
        rematchButton.addActionListener(e -> resetGame());
        return rematchButton;
    }
    private void initializeGameBoard() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        gamePanel.add(createTopPanel(), BorderLayout.NORTH);
        gamePanel.add(createBoardPanel(), BorderLayout.CENTER);
        rematchButton = createRematchButton();
        gamePanel.add(rematchButton, BorderLayout.SOUTH);
    }
    private void handleButtonClick(int row, int col, JButton button) {
        if (!gameOver && button.getText().isEmpty()) {
            button.setText(currentPlayer.toString());
            gameLogic.makeMove(row, col, currentPlayer);
            lastRow = row;
            lastCol = col;
            checkWinner();
            if (!gameOver) {
                switchPlayer();
                if (playWithBot) {
                    makeBotMove();
                }
            }
        }
    }
    private void backtrack() {
        if (lastRow != -1 && lastCol != -1) {
            buttons[lastRow][lastCol].setText("");
            gameLogic.undoLastMove(lastRow, lastCol);
            switchPlayer();
            lastRow = -1;
            lastCol = -1;
        }
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    public void showGameBoard() {
        frame.getContentPane().removeAll();
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer.next();
        currentPlayerLabel.setText(currentPlayer.getDisplayName() + "'s Turn");
    }


    private void checkWinner() {
        Player winner = gameLogic.checkWinner();
        if (winner != null) {
            gameOver = true;
            currentPlayerLabel.setText("Player " + winner + " wins!");

            // Highlight the winning line
            highlightWinningLine(gameLogic.getWinningLine());
        } else if (gameLogic.isBoardFull()) {
            gameOver = true;
            currentPlayerLabel.setText("It's a tie!");
        }
    }

    private void highlightWinningLine(int[][] winningLine) {
        if (winningLine != null) {
            for (int[] cell : winningLine) {
                int row = cell[0];
                int col = cell[1];
                buttons[row][col].setBackground(Color.GREEN);
            }
        }
    }

    private void resetGame() {
        gameLogic.resetBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(null);
            }
        }
        currentPlayer = Player.X;
        gameOver = false;
        currentPlayerLabel.setText("Player X's Turn");
    }

    private void makeBotMove() {
        if (gameOver || !playWithBot) {
            return;
        }

        int[] move = gameLogic.getBotMove();
        if (move != null) {
            JButton selectedButton = buttons[move[0]][move[1]];
            selectedButton.setText(currentPlayer.toString());
            gameLogic.makeMove(move[0], move[1], currentPlayer);
            checkWinner();
            if (!gameOver) {
                switchPlayer();
            }
        }
    }

    private void returnToMainMenu() {
        frame.getContentPane().removeAll();
        frame.add(mainMenuPanel);
        frame.revalidate();
        frame.repaint();
    }
}

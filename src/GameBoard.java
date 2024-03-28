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
    private Timer resetTimer;

    public GameBoard(JFrame frame, boolean playWithBot, JPanel mainMenuPanel) {
        this.frame = frame;
        this.playWithBot = playWithBot;
        this.mainMenuPanel = mainMenuPanel;
        this.gameLogic = new TicTacToeGameLogic();
        initializeGameBoard();
    }

    private void initializeGameBoard() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        ImageIcon homeIcon = new ImageIcon("img/home.png");
        homeIcon = resizeIcon(homeIcon, 30, 30); // Resize the icon
        returnToMainMenuButton = new JButton(homeIcon);
        returnToMainMenuButton.setBorder(BorderFactory.createEmptyBorder()); // Remove button border
        returnToMainMenuButton.setContentAreaFilled(false); // Remove button background
        returnToMainMenuButton.addActionListener(e -> returnToMainMenu()); // Add action listener to return to main menu
        topPanel.add(returnToMainMenuButton, BorderLayout.WEST);

        currentPlayerLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Adjust the font size as needed
        topPanel.add(currentPlayerLabel, BorderLayout.CENTER);

        gamePanel.add(topPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        buttons = new JButton[3][3];
        currentPlayer = Player.X;
        gameOver = false;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 120));
                final int row = i;
                final int col = j;
                button.addActionListener(e -> {
                    if (!gameOver && button.getText().isEmpty()) {
                        button.setText(currentPlayer.toString());
                        gameLogic.makeMove(row, col, currentPlayer);
                        checkWinner();
                        if (!gameOver) {
                            switchPlayer();
                            if (playWithBot) {
                                makeBotMove();
                            }
                        }
                    }
                });
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        gamePanel.add(boardPanel, BorderLayout.CENTER);

        rematchButton = new JButton("Rematch");
        rematchButton.setFont(new Font("Arial", Font.BOLD, 30));
        rematchButton.addActionListener(e -> resetGame());
        gamePanel.add(rematchButton, BorderLayout.SOUTH);
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
        currentPlayerLabel.setText("Player " + currentPlayer + "'s Turn");
    }

    private void checkWinner() {
        Player winner = gameLogic.checkWinner();
        if (winner != null) {
            gameOver = true;
            currentPlayerLabel.setText("Player " + winner + " wins!");
            startResetTimer();
        } else if (gameLogic.isBoardFull()) {
            gameOver = true;
            currentPlayerLabel.setText("It's a tie!");
            startResetTimer();
        }
    }

    private void startResetTimer() {
        if (resetTimer != null) {
            resetTimer.stop();
        }
        resetTimer = new Timer(2000, e -> resetGame());
        resetTimer.setRepeats(false);
        resetTimer.start();
    }

    private void resetGame() {
        if (resetTimer != null) {
            resetTimer.stop();
        }
        gameLogic.resetBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
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

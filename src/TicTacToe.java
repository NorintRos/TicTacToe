import javax.swing.*;
import java.awt.*;


public class TicTacToe {
    private JFrame frame;
    private JPanel mainMenuPanel;
    private JButton playWithFriendButton;
    private JButton playWithBotButton;

    public TicTacToe() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 650);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        playWithFriendButton = new JButton("Play with Friend");
        playWithFriendButton.setFont(new Font("Arial", Font.BOLD, 30));
        playWithFriendButton.addActionListener(e -> startGame(GameMode.FRIEND));

        playWithBotButton = new JButton("Play with Bot");
        playWithBotButton.setFont(new Font("Arial", Font.BOLD, 30));
        playWithBotButton.addActionListener(e -> startGame(GameMode.BOT));

        mainMenuPanel.add(playWithFriendButton, gbc);
        mainMenuPanel.add(playWithBotButton, gbc);
        frame.add(mainMenuPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void startGame(GameMode gameMode) {
        GameBoard gameBoard = new GameBoard(frame, gameMode, mainMenuPanel);
        gameBoard.showGameBoard();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}


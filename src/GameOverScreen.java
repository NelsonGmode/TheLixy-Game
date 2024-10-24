
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class GameOverScreen extends JPanel{
    private JFrame frame;

    public GameOverScreen(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        JLabel gameOverLabel = new JLabel("YOU ARE DONE", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 40));
        add(gameOverLabel, BorderLayout.CENTER);

        JButton restartButton = new JButton("Go again?");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame(); // restart the game
            }
        });
        add(restartButton, BorderLayout.SOUTH);
    }

    //method to restart game
    private void restartGame(){
        frame.getContentPane().removeAll(); // clear the game over screen
        Main newGamePanel = new Main(frame); // create a new game instance
        frame.add(newGamePanel); // add the new game to the frame
        frame.revalidate(); // refresh the frame
        newGamePanel.requestFocusInWindow(); //focus on the new game panel
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen {
    private JFrame frame;

    public WelcomeScreen(){
        //create the main frame (window)
        frame = new JFrame("TheLixy Game");
        frame.setSize(800, 600); // set window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close when the exit button is clicked
        frame.setLocationRelativeTo(null); // center the window
        frame.setLayout(new BorderLayout());
    }

    public void showWelcomeScreen() {
        /*create a panel to hold the welcome screen components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // VERTICAL ALIGNMENTS for elements */

        //create a custom panel for background image
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //vertical alignment for elements

        //create and style the title label
        JLabel titleLabel = new JLabel("Welcome to TheLixy Game!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE); //make the text white for better visibility
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0)); // padding around the label

        //create the start button
        JButton startButton = new JButton("Let's Fucking Go!");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //start the game when "start" is clicked
                frame.dispose(); // close the welcome screen window
                launchGame(); //switch to game screen
                //placeholder for game starting logic
                //JOptionPane.showMessageDialog(frame, "TheLixy Game is loading...(make sure you don't fuck it up!)");

            }
        });

        // create the exit button
        JButton exitButton = new JButton("Fuck off");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setFont(new Font("Arial", Font.PLAIN, 24));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //CLOSES THE WINDOW
                frame.dispose();

            }
        });

        //add components to the panel
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); //space between components
        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); //space between components
        panel.add(exitButton);

        //add panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        //make the window visible
        frame.setVisible(true);
    }

    //inner class for custom JPanel with background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            //load the background image
            backgroundImage = new ImageIcon("media/lixy-background.jpg").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // draw the background image to cover the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    //launch the main game screen
    private void launchGame() {
        //initialize the game window with the main game panel (player movement)
        JFrame gameFrame = new JFrame("TheLixy Game");
        Main gamePanel = new Main(frame); // this is our main game loop class
        gameFrame.add(gamePanel);
        gameFrame.setSize(800, 600);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
}

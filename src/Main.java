import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Main extends JPanel implements KeyListener {
    private Player player;
    private int cameraX = 0; // camera's horizontal position (will move based on player)

    private final int WORLD_WIDTH = 4000; // world width (will move based on player)
    private final int SCREEN_WIDTH = 800; // screen width
    //private final int SCREEN_HEIGHT = 600; // screen height
    private final int CAMERA_MARGIN = 300; // how far from the edges the player can go before the camera moves

    private Timer gameLoopTimer; // store reference to the game loop timer
    private  List<Platform> platforms; //lists of platforms
    private List<MovingPlatform> movingPlatforms; //list of moving platforms
    private List<DisappearingPlatform> disappearingPlatforms; //list of disappearing platforms
    private List<Enemy> enemies; // list of enemies
    private List<Coin> coins; // list of coins
    private List<PowerUp> powerUps; // list of power ups
    private List<Trap> traps; // list of traps
    private List<Projectile> projectiles;
    private LevelEnd levelEnd;


    public Main(JFrame gameFrame){
        player = new Player(100, 500, gameFrame); //start player at (100, 500)

        //initialize platforms
        platforms = new ArrayList<>();
        platforms.add(new Platform(300, 450, 200, 20)); //platform 1
        platforms.add(new Platform(600, 400, 150, 20)); //platform 2
        platforms.add(new Platform(1200, 300, 200, 20)); //platform 3
        //add more platforms to the extended world

        platforms.add(new Platform(1800, 400, 150, 20)); //platform 5
        platforms.add(new Platform(2100, 300, 200, 20)); //platform 6
        platforms.add(new Platform(2400, 450, 200, 20)); //platform 4
        platforms.add(new Platform(2700, 400, 150, 20)); //platform 5
        platforms.add(new Platform(3000, 300, 200, 20)); //platform 6

        //initialize moving platforms
        movingPlatforms = new ArrayList<>();
        movingPlatforms.add(new MovingPlatform(1600, 300, 2000, 350, 150, 20, 2, 0)); //moving horizontally
        movingPlatforms.add(new MovingPlatform(2500, 500, 2500, 350, 150, 20, 0, -2)); //moving vertically
        //initialize enemies
        enemies = new ArrayList<>();
        enemies.add(new Enemy(400, 500, 350, 500, projectiles)); //enemy 1 patrolling between 350 and 500
        enemies.add(new Enemy(1000, 500, 950, 1100, projectiles)); //enemy 2 patrolling between 950 and 500
        enemies.add(new Enemy(1800, 500, 1750, 1900, projectiles)); //enemy 1 patrolling between 350 and 500
        enemies.add(new Enemy(2800, 500, 2750, 2900, projectiles));
        enemies.add(new JumpingEnemy(3200, 500, 2750, 2900, projectiles));


        // Add projectile-shooting enemies
        projectiles = new ArrayList<>();
        enemies.add(new Enemy(800, 500, 750, 850, projectiles)); // Enemy that shoots projectiles
        enemies.add(new Enemy(1600, 500, 1550, 1650, projectiles)); // Another shooting enemy

        //initialize coins
        coins = new ArrayList<>();
        coins.add(new Coin(350, 420)); // coin1 on platform 1
        coins.add(new Coin(750, 370)); // coin2 on platform 2
        coins.add(new Coin(1250, 320)); // coin3 on platform 3
        coins.add(new Coin(1650, 270)); // coin1 on platform 4
        coins.add(new Coin(2050, 320)); // coin2 on platform 5
        coins.add(new Coin(2550, 370)); // coin3 on platform 6

        //initialize power ups
        powerUps = new ArrayList<>();
        powerUps.add(new PowerUp(400, 420, "speed")); // speed boost power up on platform 1
        powerUps.add(new PowerUp(1700, 270, "invincibility")); // speed boost power up on platform 2
        powerUps.add(new PowerUp(2100, 320, "extra_life")); // speed boost power up on platform 3

        //initialize traps (spikes, etc.)
        traps = new ArrayList<>();
        traps.add(new Trap(1400, 540, 50, 10)); // trap1 (spikes) at ground level
        traps.add(new Trap(2200, 540, 50, 10)); // trap2 (spikes)


        disappearingPlatforms = new ArrayList<>();
        //add normal and disappearing platforms
        platforms.add(new Platform(300, 450, 200, 20));
        disappearingPlatforms.add(new DisappearingPlatform(1600, 400, 200, 20)); //disappearing Platforms


        //initialize level end(the player will finish level at x- 3700)
        levelEnd = new LevelEnd(3700, 450); //add the level end at the right position

        //game loop with timer
        gameLoopTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player.isGameOver()) {// only update if the game is not over
                    player.update(); //update player position on each tick
                    player.checkPlatformCollisions(platforms); //check for platform collisions
                    player.checkEnemyCollisions(enemies); // check for enemies collisions
                    player.checkCoinCollection(coins); // check for coin collection
                    player.checkPowerUpCollection(powerUps); // check for power up collection
                    player.checkTrapCollisions(traps); // check for trap collisions
                    player.checkMovingPlatformCollisions(movingPlatforms);
                    player.checkDisappearingPlatformCollisions(disappearingPlatforms);
                    player.checkProjectileCollisions(projectiles);

                    //update disappearing platforms
                    for (DisappearingPlatform disappearingPlatform : disappearingPlatforms) {
                        disappearingPlatform.update();
                    }

                    //update moving platforms
                    for (MovingPlatform movingPlatform : movingPlatforms) {
                        movingPlatform.update();
                    }

                    for (Enemy enemy : enemies) {
                        enemy.update(); // update enemy positions
                    }
                    for (Projectile projectile : projectiles) {
                        projectile.update();
                    }

                    // check if player reaches the level end
                    if (levelEnd != null) { //make sure level end is initialized
                        levelEnd.update(player); //update the level end
                    }

                    updateCamera(); // adjust the camera based on the player's position
                    repaint(); // redraw the game panel
                }else {
                    gameLoopTimer.stop(); // stop the game loop if the game is over
                }
            }
        });
        gameLoopTimer.start();

        setFocusable(true);
        addKeyListener(this); //enable keyboard input
    }



    //adjust camera  based on the player's position
    private void updateCamera(){
        int playerX = player.getX();

        //keep the player within a margin before moving the camera
        if(playerX - cameraX > SCREEN_WIDTH - CAMERA_MARGIN){
            cameraX = playerX - (SCREEN_WIDTH - CAMERA_MARGIN); // SHIFT THE CAMERA RIGHT
        } else if (playerX - cameraX < CAMERA_MARGIN) {
            cameraX = playerX - CAMERA_MARGIN; // shift the camera left
        }

        //ensure the camera doesn't go outside the world boundaries
        if (cameraX < 0){
            cameraX = 0; // stop camera at the start of the world
        } else if (cameraX > WORLD_WIDTH - SCREEN_WIDTH){
            cameraX = WORLD_WIDTH - SCREEN_WIDTH; // stop the camera at the end of the world
        }
    }

    //public method to stop the game loop externally (called from player's game over logic)
    public void stopGameLoop(){
        if (gameLoopTimer !=null) {
            gameLoopTimer.stop(); // stop the game loop
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw background depending on player's position (adjusted with camera)
        int playerX = player.getX();
        if (playerX < 1500) {
            g.setColor(Color.CYAN); //sky blue for the starting area
        } else if (playerX < 3000) {
            g.setColor(Color.DARK_GRAY); // darker background for underground or cave section
        } else {
            g.setColor(Color.ORANGE); // warm color for castle section

        }
        g.fillRect(0, 0, getWidth(), getHeight());

        // draw ground (translate by cameraX to simulate scrolling)
        g.setColor(Color.GREEN);
        g.fillRect(0 - cameraX, 550, WORLD_WIDTH, 50); // world-wide ground

        // draw some background objects (simple trees)
        g.setColor(Color.GREEN);
        g.fillRect(100 - cameraX, 400, 40, 150); //tree trunk 1
        g.fillRect(600 - cameraX, 380, 40, 170); // tree trunk 2
        g.setColor(Color.GREEN);
        g.fillOval(80 - cameraX, 350, 80, 80); //tree leaves 1
        g.fillOval(580 - cameraX, 350, 80, 80); // tree leaves 2
        //draw the player (translated by cameraX)
        //g.translate(-cameraX, 0); // shift all drawings by the camera's x position
        player.draw(g, cameraX, SCREEN_WIDTH, false);
        // g.translate(cameraX, 0); // reset translation for future drawings

        //draw platforms
        for (Platform platform : platforms) {
            platform.draw(g,cameraX); // draw each platforms relative to camera
        }

        //draw moving platforms
        for (MovingPlatform movingPlatform : movingPlatforms){
            movingPlatform.draw(g, cameraX);// draw each platform relative to camera
        }

        //draw disappearing platforms
        for (DisappearingPlatform disappearingPlatform : disappearingPlatforms) {
            disappearingPlatform.draw(g, cameraX);
        }

        //draw enemies
        for (Enemy enemy : enemies) {
            enemy.draw(g, cameraX); // draw each enemy relative to camera
        }

        //draw projectiles
        for (Projectile projectile : projectiles){
            projectile.draw(g, cameraX);
        }

        //draw coins
        for (Coin coin : coins) {
            coin.draw(g, cameraX); //draw each coin relative to camera
        }

        //draw power-ups
        for (PowerUp powerUp: powerUps) {
            powerUp.draw(g, cameraX); // draw each coin relative to camera
        }

        //draw traps
        for(Trap trap : traps){
            trap.draw(g, cameraX); // draw each relative to camera
        }

        //draw the player's lives on screen
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Lives: " + player.getLives(), 20, 30); // display lives in the top left corner

        //draw the level end flag/door
        levelEnd.draw(g, cameraX);
    }

    // handle key presses for movement
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            player.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            player.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.jump();
        }
    }

    //handle key releases to stop moving
    @Override
    public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT){
            player.stopMoving();
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
        //not needed for now
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TheLixy Game");
        Main gamePanel = new Main(frame);
        frame.add(gamePanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); //center window
        frame.setVisible(true);
    }
}
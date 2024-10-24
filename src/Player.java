import java.awt.*;
import java.util.List;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class Player {
    private int x, y; // player's position
    private int width = 16, height =50; // player's size
    private BufferedImage playerImage; //image to represent the player
    private int velocityX, velocityY; //player's speed in X and Y directions
    private final int GRAVITY = 1; // simulate gravity by increasing velocityY

    private final int JUMP_STRENGTH = -13; // Negative for upwards movement
    private boolean isJumping = false; // to prevent double jumps

    //GROUND level where the player should land (y = 500 means the ground is at this height)
    private final int GROUND_LEVEL = 500;

    private int lives = 3; // 3 player lives
    private int invincibilityTimer = 0; //cooldown timer for collisions invincibility
    private int score = 0;

    //power-up effects
    private boolean isSpeedBoosted = false;
    private boolean isInvincible = false;
    private boolean gameOver = false; //flag for game over state
    private int powerUpTimer = 0; //timer for power-up effects
    private int trapCooldown = 0; // trap cooldown to prevent losing all lives
    private JFrame gameFrame; //reference to the game's jframe

    public Player(int startX, int startY, JFrame gameFrame) {
        this.x = startX;
        this.y = startY;
        this.width = 16;
        this.height = 50;
        this.velocityX = 0;
        this.velocityY = 0;
        this.gameFrame = gameFrame; // save reference to the game


        //load the player image
        try {
            playerImage = ImageIO.read(new File("media/lixy-date.jpg")); // Use your actual image path here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //update player position and apply gravity
    public void update() {

        //update player position and cooldown timers
        if(trapCooldown > 0) {
            trapCooldown--; //decrease cooldown each frame
        }

        //apply gravity if the player is above ground or jumping
        if (y < GROUND_LEVEL || isJumping) { //simulate ground at y=500 (floor of the level)
            velocityY += GRAVITY; // apply gravity while in the air
        } /*else {
            velocityY = 0; // on the ground
            y = 500; // reset to ground level
            isJumping = false; //player can jump again} */


        //update position
        x += velocityX;
        y += velocityY;


        //prevent the player from falling below ground level
        if(y >= GROUND_LEVEL) {
            y = GROUND_LEVEL; //clamp y to the ground level
            velocityY = 0; // stop the vertical movement
            isJumping = false; // player is now on the ground
        }

        // decrease invincibility timer if active
        if (invincibilityTimer > 0){
            invincibilityTimer--;
        }

        if (!gameOver) { //only update player if the game is not over
            //update player position with gravity, velocity, etc.
            x += velocityX;
            y += velocityY;
        }

        //handle power-up effects
        if (powerUpTimer > 0 ) {
            powerUpTimer--;

            if (powerUpTimer == 0) {
                //reset power effects after timer runs out
                isSpeedBoosted = false;
                isInvincible = false;
            }
        }
    }

    public boolean isGameOver() {
        return gameOver; //return the game over state
    }

    // Check for collisions with projectiles
    public void checkProjectileCollisions(List<Projectile> projectiles) {
        if (gameOver) return; //don't check collisions if the game is over

        for (Projectile projectile : projectiles) {
            if (projectile.isVisible() && getBounds().intersects(projectile.getBounds())) {
                loseLife();
                projectile.setVisible(false); // Make projectile invisible after collision
                break; // Exit the loop after a collision is detected
            }
        }
    }

    //check collision with disappearing platforms
    public void checkDisappearingPlatformCollisions(List<DisappearingPlatform> disappearingPlatforms){
        for (DisappearingPlatform platform : disappearingPlatforms) {
            if (platform.isVisible() && getBounds().intersects(platform.getBounds())) {
                //land on the platform
                y = platform.getY() - height;
                isJumping = false;

                //make  the platform disappear
                platform.disappear();
            }
        }
    }

    //handle power-up collection
    public void checkPowerUpCollection(List<PowerUp> powerUps) {
        for (PowerUp powerUp : powerUps) {
            if(!powerUp. isCollected() && getBounds().intersects(powerUp.getBounds())){
                powerUp.collect(); // mark power-up as collected
                applyPowerUpEffect(powerUp.getType()); //apply the power-up effect
            }
        }
    }

    //apply the effect of the collected power-up
    private void applyPowerUpEffect(String type){
        switch (type) {
            case "speed":
                isSpeedBoosted = true; // enable speed boost
                powerUpTimer = 300; //power up lasts for 300frames (5 seconds)
                System.out.println("Speed Boost Collected");
                break;
            case "invincibility":
                isInvincible = true; //enable invincibility
                powerUpTimer = 300; // power-up lasts for 300 frames (5 seconds)
                System.out.println("Invincibility Collected");
                break;
            case "extra_life":
                lives++; // grant an extra life
                System.out.println("Extra Life Collected! Lives: " + lives);
                break;

        }
    }

    //handle coin collection
    public void checkCoinCollection(List<Coin> coins ) {
        for (Coin coin : coins) {
            if (!coin.isCollected() && getBounds().intersects(coin.getBounds())) {
                coin.collect(); //mark coin as collected
                score += 10; //increase score by 10 for each coin collected
                System.out.println("Coin collected! " + score); //debugging log
            }
        }
    }

    //handle enemy collisions (top vs side/bottom)
    public void checkEnemyCollisions (List<Enemy> enemies){
        for (int i = 0; i < enemies.size(); i++){
            Enemy enemy = enemies.get(i);

            // Get the player's bounding box and enemy's bounding box
            Rectangle playerBounds = getBounds();
            Rectangle enemyBounds = enemy.getBounds();

            //check if the player hits the top of the enemy
            if (playerBounds.intersects(enemyBounds)){
                // Check if player is falling and above the enemy (to defeat it)
                if (velocityY > 0 ) { // ensure player is above the enemy
                    velocityY = -5; //bounce the player after defeating the enemy
                    defeatEnemy(enemies, i);
                }else  {
                    handleSideOrBottomCollision(enemy); // if the player touches the enemy from the side
                }
            }
        }
    }

    //handle when the player defeats the enemy by jumping on it
    private void defeatEnemy(List<Enemy> enemies, int index){
        System.out.println("player jumped on the enemy");
        velocityY = JUMP_STRENGTH; // bounce the player after defeating the enemy
        enemies.remove(index); // remove enemy from the game
    }

    //handle side or bottom collision with the enemy (player loses its life)
    private void handleSideOrBottomCollision(Enemy enemy){
        System.out.println("player hit the enemy from side/bottom");
        loseLife(); //player lose a life

        //apply knockback to move the player away from the enemy (optional)
        if (x < enemy.getX()) {
            x -= 50; //knock player to the left if they are on the left side
        } else{
            x += 50; // knock player to the right if they are on the right side
        }

        //set a brief invincibility timer to avoid repeated collisions
        invincibilityTimer = 60; // 1 second of invincibility
    }

    //check trap collisions
    public void checkTrapCollisions(List<Trap> traps) {
        for(Trap trap : traps) {
            if (trapCooldown == 0 && getBounds().intersects(trap.getBounds())){
                loseLife();
                trapCooldown = 60; // set 1- second cooldown
                break;
            }
        }
    }

    //lose a life if the player touches an enemy
    private void loseLife() {
        if (invincibilityTimer == 0) {
            lives--;
            System.out.println("Lives left: " + lives);


            //respawn player or handle game over if lives reach 0
            if (lives <= 0) {
                gameOver = true; // set game over flag
                showGameOverScreen(); // call method to display game over screen
                //handle game over logic here

            }
            invincibilityTimer = 60; // 1 second of invincibility after being hit
        }
    }

    /// Show the Game Over screen using the passed JFrame reference
    private void showGameOverScreen() {
        if (gameFrame != null) {
            // stop the game loop after the game over screen is displayed
            Main mainPanel = (Main) gameFrame.getContentPane().getComponent(0);
            mainPanel.stopGameLoop();

            //now switch to the game over screen
            SwingUtilities.invokeLater(() -> {
                gameFrame.getContentPane().removeAll(); // Remove current game content
                gameFrame.getContentPane().add(new GameOverScreen(gameFrame)); // Add Game Over screen
                gameFrame.revalidate(); // Refresh the frame
                gameFrame.repaint(); // Repaint to show the new content
            });
        }
    }

    // moving platform collision detection
    public void checkMovingPlatformCollisions(java.util.List<MovingPlatform> movingPlatforms) {
        for (MovingPlatform movingPlatform : movingPlatforms) {
            // if the player is falling (velocityY > 0) and their feet are within the platform bounds
            if (velocityY > 0 && getBounds(). intersects(movingPlatform.getBounds())){
                //land on the moving platform
                y = movingPlatform.getY() - height; // set the player on top of the platform
                velocityY= 0; // stop falling
                isJumping = false; //allow jumping again
            }
        }
    }



    // handle platform collision detection
    public void checkPlatformCollisions(List<Platform> platforms) {
        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();
            // if the player is falling (velocityY > 0) and their feet are within the platform bounds
            if (velocityY > 0 && getBounds().intersects(platformBounds)){
                //land on th platform
                y = platform.getY() - height; // set the player on top of the platform
                velocityY = 0; // stop falling
                isJumping = false; //allow jumping again
            }
        }
    }

    public void draw(Graphics g, int cameraX, int screenWidth, boolean drawAtCenter) {
        // Draw the player using the image

        if (playerImage != null) {
            g.drawImage(playerImage, x - cameraX, y, width, height, null); // Draw player image
        } else {
            // Fallback to rectangle if image fails to load
            g.setColor(Color.RED);
            g.fillRect(x - cameraX, y, width, height);
        }

        //Determine where to position the scoreboard(top-right or top-center)
        int scoreboardX;
        if (drawAtCenter){
            scoreboardX = screenWidth / 2- 50; //centered scoreboard
        } else {
            scoreboardX = screenWidth - 150; // top right scoreboard with padding
        }

        // draw player score and lives on the screen (aligned to the right)
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, scoreboardX, 20); //show score in the top left corner

        //draw power-up status
        if (isSpeedBoosted) {
            g.drawString("Speed Boost Active! wiiii", scoreboardX, 60);
        }
        if (isInvincible){
            g.drawString("can't see me", scoreboardX, 80);
        }

    }

    // Move player left
    public void moveLeft() {
        if (!gameOver) {
            velocityX = -4;
        }
    }

    // Move player right
    public void moveRight() {
        if (!gameOver) {
            velocityX = 4;
        }
    }

    //stop horizontal movement
    public void stopMoving() {
        velocityX = 0;
    }

    // Jump
    public void jump() {
        if (!isJumping) { // Simple condition for ground level
            velocityY = JUMP_STRENGTH; // Jump with negative velocity
            isJumping = true; // set jumping flag
        }
    }

    //getter method for lives
    public int getLives (){
        return lives;
    }

    //get the player's bounding box (for collision detection)
    public java.awt.Rectangle getBounds(){
        return new java.awt.Rectangle(x, y, width, height); // player's rectangular bounding box
    }

    // Get player's x and y (for collision detection later)
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getScore() {
        return score;
    }
    // Reset player state for a new game
    public void reset(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.lives = 3; // Reset lives
        this.gameOver = false; // Reset game over flag
        this.velocityX = 0;
        this.velocityY = 0;
        this.isJumping = false; //reset jumping state
    }

    public void completeLevel() {
        System.out.println("Level Completed!");
        // Optionally trigger a level transition or message
    }

}
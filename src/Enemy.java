import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class Enemy {
    protected int x, y; // Change access to protected for subclass access
    protected int velocityX, velocityY; // Change access to protected for subclass access
    private int width = 30, height = 40;

    private int patrolStartX, patrolEndX;
    private int speed; // speed of the enemy's movement
    private List<Projectile> projectiles;
    private int shootCooldown = 100; // time between shots
    private BufferedImage enemyImage; // Image to represent the enemy



    public Enemy(int x, int y, int patrolStartX, int patrolEndX, List<Projectile> projectiles) {
        this.x = x;
        this.y = y;
        this.speed = 2;  // speed of the enemy
        this.patrolStartX = patrolStartX;
        this.patrolEndX = patrolEndX;
        this.projectiles = projectiles; // enemy's projectiles
        this.velocityX = 2;
        this.velocityY = 0;

        // Load the enemy image
        try {
            enemyImage = ImageIO.read(new File("media/robot.jpg")); // Use your actual image path here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // update enemy position and handle patrol movement
    public void update() {
        //move the enemy left or right
        x += speed;
        if (x > patrolEndX || x < patrolStartX) {
            speed = -speed; //reverse direction

            //apply gravity or any other movement logic
            velocityY += 1;
            y += velocityY;
        }

        //prevent the enemy from falling below a certain height (ground level)
        if (y > 500) {
            y = 500; //set ground level
            velocityY = 0; // stop falling
        }

        // decrease cooldown timer for shooting
        if (shootCooldown > 0) {
            shootCooldown--;
        }else {
            shoot(); // shoot a projectile when cooldown reaches 0
            shootCooldown = 100;
        }
    }

    //shoot projectile
    private void shoot(){
        if (projectiles != null) {
            int projectileSpeed = 5;
            projectiles.add(new Projectile(x, y + 10, 20, 5, projectileSpeed));// shoot to the right
            System.out.println("Enemy shoots a projectile!");
        }
    }

    //draw the enemy relative to the camera
    public void draw(Graphics g, int cameraX) {
        if (enemyImage != null) {
            g.drawImage(enemyImage, x - cameraX, y, width, height, null); // Draw enemy image
        } else {
            // Fallback to rectangle if image fails to load
            g.setColor(Color.RED);
            g.fillRect(x - cameraX, y, width, height);
        }
    }

    // GET ENEMY BOUNDS (bounds for collision)
    public Rectangle getBounds(){
        return new Rectangle(x, y, 50, 50);
    }

    // Getter and setter for velocityY
    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }


    //getter for the enemy's Y-position
    public int getY() {
        return y;
    }

    //getter for enemy's X-position

    public int getX() {
        return x;
    }

    public void setY(int y){
        this.y = y;
    }
}

import java.awt.*;
public class Projectile {
    private int x, y;
    private int width, height;
    private int speed;
    protected boolean isVisible;

    public Projectile(int x, int y, int width, int height, int speed){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.isVisible = true;
    }

    //update the projectile
    public void update(){
        x += speed; //move the projectiles

        //if the projectile moves off the screen, it becomes invisible
        if (x < 0 || x > 4000) { //assuming 4000 is the world width for now
            isVisible = false;
        }
    }

    //draw the projectile if is visible
    public void draw (Graphics g, int cameraX){
        if (isVisible) {
            g.setColor(Color.BLACK); // black color for the projectile
            g.fillRect(x - cameraX, y, width, height); // draw relative to camera
        }
    }

    // get projectile's bounding box (for collision detection)
    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    //check if the projectile is visible (still on screen)
    public boolean isVisible(){
        return isVisible;
    }

    // Set projectile visibility
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
}

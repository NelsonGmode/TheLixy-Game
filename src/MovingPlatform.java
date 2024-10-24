
import java.awt.*;
public class MovingPlatform  extends Platform{
    private int startX, startY; // starting positions
    private int endX, endY; //END POSITION for horizontal or vertical movement
    private int velocityX, velocityY; // SPEED OF MOVEMENT

    public MovingPlatform(int startX, int startY, int endX, int endY, int width, int height, int speedX, int speedY) {
        super(startX, startY, width, height);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.velocityX = speedX;
        this.velocityY = speedY;
    }

    //update the platform's position
    public void update(){
        // move the platform horizontally or vertically
        x += velocityX;
        y += velocityY;


        //reverse direction if the platform reaches its limits
        if (getX() >= endX || getX() <= startX) {
            velocityX = -velocityX;
        }
        if (getY() >= endY || getY()<= startY) {
            velocityY = -velocityY;
        }
    }

    //draw the moving platform (just like regular platform)
    @Override
    public void draw(Graphics g, int cameraX){
        g.setColor(Color.GRAY); // gray for moving platforms
        g.fillRect( getX() - cameraX, getY(), width, height); //draw relative to camera
    }
    // get platform bounds (for collision detection)
    public Rectangle getBounds() {

        return new Rectangle( x, y, width, height);
    }
}

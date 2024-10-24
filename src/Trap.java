
import java.awt.*;
public class Trap {
    private int x , y; //trap's position
    private int width, height; // trap's size

    public Trap(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    //draw the trap
    public void draw(Graphics g, int cameraX){
        g.setColor(Color.RED); //red for  traps
        g.fillRect(x- cameraX, y, width, height); // draw the trap relative to cameraX
    }

    // get the trap's bounding box (for collision detection)
    public Rectangle getBounds(){
        return new Rectangle(x, y,width, height);
    }
}

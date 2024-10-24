
import java.awt.*;
public class Platform {
    protected int x, y; // platform's position
    protected int width, height; //platform's size

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width =  width;
        this.height = height;
    }

    //draw the platform relative to the camera
    public void draw(Graphics g, int cameraX) {
        g.setColor(Color.GRAY);
        g.fillRect(x- cameraX, y, width, height); //draw platform relative
    }

    // get platform bounds (for collision detection)
    public Rectangle getBounds() {
        return new Rectangle( x, y, width, height);
    }

    // GETTER for the platform's Y-Position (for collision detection)
    public int getX(){

        return x;
    }

    //getter for the platform's X-position

    public int getY()
    {
        return y;
    }

    // GETTER for the platform's width
    public int getWidth(){
        return width;
    }

    // GETTER for the platform's height
    public int getHeight()
    {
        return height;

    }

}


import java.awt.*;
public class Coin {
    private int x, y; //coin's position
    private int width, height; // coin's size
    private boolean collected = false; // track if the coin has been collected

    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 30; //set coin width
        this.height = 30; //set coin width
    }

    //draw the coin (only if not collected)
    public void draw(Graphics g, int cameraX) {
        if (!collected){
            g.setColor(Color.YELLOW);//coin color
            g.fillOval(x - cameraX, y, width, height); // draw a yellow circle for the coins
        }
    }

    //check if the coin has been collected by the player
    public boolean isCollected() {
        return collected;
    }

    //mark the coin as collected
    public void collect(){
        collected = true;
    }

    //get the coin's bounding box (for collision detection)
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

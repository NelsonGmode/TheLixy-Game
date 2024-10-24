
import java.awt.*;

public class PowerUp {
    private int x, y; //power-up's position
    private int width, height; //power-up's size
    private String type; // type of power (e.g., "speed", invincibility)
    private boolean collected = false;

    public PowerUp(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.width = 30; // set power-up width
        this.height = 30; // set power-up height
        this.type = type; // set the type of power-up
    }

    //draw the power-up (only if not collected)
    public void draw(Graphics g, int cameraX) {
        if(!collected) {
            // color the power-up based on its type
            switch(type){
                case "speed":
                    g.setColor(Color.BLUE);
                    break;
                case "invincibility":
                    g.setColor(Color.MAGENTA);
                    break;
                case"extra_life":
                    g.setColor(Color.ORANGE);
                    break;
            }
            g.fillRect(x - cameraX, y, width, height); // draw the power up
        }
    }

    //check if the power up has been collected by the player
    public boolean isCollected(){
        return collected;
    }

    //mark the power-up as collected
    public void collect(){
        collected = true;
    }

    //get the power-up's bounding box (for collision detection)
    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    // get the type of power-up


    public String getType() {
        return type;
    }
}

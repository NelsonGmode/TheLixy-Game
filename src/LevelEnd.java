
import java.awt.Graphics; // For drawing (fillRect)
import java.awt.Color;     // To set colors for drawing
import java.awt.Rectangle; // For collision detection

public class LevelEnd {
    private int x, y;
    private int width = 50, height = 100;

    public LevelEnd(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(Player player) {
        if (getBounds().intersects(player.getBounds())) {
            player.completeLevel(); // Call player's level complete method
        }
    }

    public void draw(Graphics g, int cameraX) {
        g.setColor(Color.GREEN);
        g.fillRect(x - cameraX, y, width, height); // Draw the level end flag
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}


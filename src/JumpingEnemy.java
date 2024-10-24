
import java.awt.*;
import java.util.List;
public class JumpingEnemy extends Enemy{
    private int jumpTimer = 0;//timers for controlling jumps
    private final int JUMP_INTERVAL =100; // interval between jumps
    private final int JUMP_STRENGTH = -10; //jump height
    public JumpingEnemy(int x, int y, int patrolStartX, int patrolEndX, List<Projectile> projectiles) {
        super(x, y, patrolStartX, patrolEndX, projectiles);
    }
    @Override
    public void update(){
        if (jumpTimer == 0) {
            velocityY = JUMP_STRENGTH; //jump every few seconds
            jumpTimer = JUMP_INTERVAL; //reset the jumptimer
        } else {
            jumpTimer--;
        }
        super.update(); // apply gravity
    }

    @Override
    public void draw(Graphics g, int cameraX) {
        g.setColor(Color.MAGENTA); // Different color for jumping enemies
        g.fillRect(x - cameraX, y, 50, 50); // Draw the enemy relative to the camera
    }
}

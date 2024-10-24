import java.awt.*;
public class DisappearingPlatform extends Platform{
    private boolean isVisible; //whether the platform is currently visible
    private int disappearTimer; // timer to track disappearance duration
    private final int DISAPPEAR_TIME = 100; // TIME THE PLATFORM STAYS INVISIBLE
    private final int REAPPEAR_TIME = 200; // TIME THE PLATFORM REAAPEARS

    public DisappearingPlatform(int x, int y, int width, int height) {
        super(x, y, width, height);
        isVisible = true;
        disappearTimer = 0;
    }

    //update the platform's state
    public void update(){
        if (!isVisible){
            disappearTimer++;
            if (disappearTimer >= REAPPEAR_TIME) {
                isVisible = true; //make platform reappear
                disappearTimer = 0; //reset timer
            }
        }
    }

    //trigger disappearance when the player steps on the platform
    public void disappear(){
        isVisible = false;
    }

    //draw the platform if it's visible
    @Override
    public void draw(Graphics g, int cameraX) {
        if (isVisible){
            g.setColor(Color.ORANGE); // orange for disappearing platforms
            g.fillRect(x- cameraX, y, width, height);
        }
    }

    //check if the platform is visible (to avoid stepping on invisible platforms)

    public boolean isVisible() {
        return isVisible;
    }
}
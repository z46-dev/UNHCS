/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class CrawlBot implements Robot {
    private int x;
    private int y;
    private int direction;

    /**
     * Creates an instance of the CrawlBot class.
     */
    public CrawlBot() {
        x = 0;
        y = 0;
        direction = 0;
    }

    /**
     * Creates an instance of the CrawlBot class.
     * @param x x coord
     * @param y y coord
     * @param direction direction
     */
    public CrawlBot(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    /**
     * Gets the x coord.
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coord.
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the direction.
     * @return direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Turns the robot to the left.
     */
    public void turnLeft() {
        this.direction += 90;
        this.direction %= 360;
    }

    /**
     * Turns the robot to the right.
     */
    public void turnRight() {
        this.direction -= 90;

        if (this.direction < 0) {
            this.direction += 360;
        }
    }

    /**
     * Moves the robot forward.
     * @param dist distance
     */
    public void moveForward(int dist) {
        switch (direction) {
            case 0:
                x += dist;
                break;
            case 90:
                y += dist;
                break;
            case 180:
                x -= dist;
                break;
            case 270:
                y -= dist;
                break;
        }
    }

    /**
     * Moves the robot backward.
     * @param dist distance
     */
    public void moveBackward(int dist) {
        switch (direction) {
            case 0:
                x -= dist;
                break;
            case 90:
                y -= dist;
                break;
            case 180:
                x += dist;
                break;
            case 270:
                y += dist;
                break;
        }
    }

    /**
     * Returns a string representation of the CrawlBot.
     * @return String
     */
    public String toString() {
        return "CrawlBot at (" + x + ", " + y + ") heading " + direction + " degrees";
    }
}

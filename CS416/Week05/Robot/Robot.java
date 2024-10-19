/**
 * An interface representing a robot.
 * @author Evan Parker
 * @version v0.0.1
 */
interface Robot {
    /**
     * Moves the robot forward.
     * @param dist distance
     */
    void moveForward(int dist);

    /**
     * Moves the robot backward.
     * @param dist distance
     */
    void moveBackward(int dist);

    /**
     * Turns the robot to the left.
     */
    void turnLeft();
    
    /**
     * Turns the robot to the right.
     */
    void turnRight();
}
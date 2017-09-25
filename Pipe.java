package project4;

/**
 *
 * @author Sara
 */
public class Pipe {

    private int locationType; // 1 = A, 2 = B, 3 = others
    private int cost;
    private PipeType type;
    // 0 = Horizontal, 1 = Vertical, 2 = DownToRight, 3 = LeftToUp, 4 = RightToUp, 5 = DownToLeft, 6 = noPipe
    private int pipeDirection;
    private String outDirection;

    public Pipe() {
    }

    public Pipe(int locationType, int cost, PipeType type, int pipeDirection) {
        this.locationType = locationType;
        this.cost = cost;
        this.type = type;
        this.pipeDirection = pipeDirection;
    }

    public void enteranceDirection(String direction) {
        switch (pipeDirection) {
            case 0:
                if (direction.equals("right")) {
                    outDirection = "left";
                } else {
                    outDirection = "right";
                }
                break;
            case 1:
                if (direction.equals("down")) {
                    outDirection = "up";
                } else {
                    outDirection = "down";
                }
                break;
            case 2:
                if (direction.equals("down")) {
                    outDirection = "right";
                } else {
                    outDirection = "down";
                }
                break;
            case 3:
                if (direction.equals("left")) {
                    outDirection = "up";
                } else {
                    outDirection = "left";
                }
                break;
            case 4:
                if (direction.equals("right")) {
                    outDirection = "up";
                } else {
                    outDirection = "right";
                }
                break;
            case 5:
                if (direction.equals("down")) {
                    outDirection = "left";
                } else {
                    outDirection = "down";
                }
                break;
        }
    }

    public String getOutDirection() {
        return outDirection;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public PipeType getType() {
        return type;
    }

    public void setType(PipeType type) {
        this.type = type;
    }

    public int getPipeDirection() {
        return pipeDirection;
    }

    public void setPipeDirection(int pipeDirection) {
        this.pipeDirection = pipeDirection;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }
}

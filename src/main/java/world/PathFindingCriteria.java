package world;

public class PathFindingCriteria {
    private boolean canBreakWalls = false;
    private boolean canOpenDoors = false;

    public boolean isCanBreakWalls() {
        return canBreakWalls;
    }

    public void setCanBreakWalls(boolean canBreakWalls) {
        this.canBreakWalls = canBreakWalls;
    }

    public boolean isCanOpenDoors() {
        return canOpenDoors;
    }

    public void setCanOpenDoors(boolean canOpenDoors) {
        this.canOpenDoors = canOpenDoors;
    }
}

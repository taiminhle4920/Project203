
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point
{
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point)other).x == this.x
                && ((Point)other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public Optional<Entity> getOccupant(WorldModel world) {
        if (this.isOccupied(world)) {
            return Optional.of(world.getOccupancyCell(this));
        }
        else {
            return Optional.empty();
        }
    }
    public boolean isOccupied(WorldModel world) {
        return world.withinBounds(this) && world.getOccupancyCell(this) != null;
    }

    public Optional<Entity> nearestEntity(
            List<Entity> entities)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), this);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition(), this);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    public Optional<Entity> findNearest(
            WorldModel world, List<Class> kinds)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind: kinds)
        {
            for (Entity entity : world.getEntities()) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }
        return this.nearestEntity(ofType);
    }

    public int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

}

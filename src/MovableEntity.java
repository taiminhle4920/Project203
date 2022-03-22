import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class MovableEntity extends AnimatedEntity{
    PathingStrategy strategy = new AStarPathingStrategy();
    public MovableEntity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            _moveToHelper(world, target, scheduler);
            return true;
        }
        else {
            Point nextPos = nextPosition( world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = nextPos.getOccupant(world);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
    public abstract void _moveToHelper(WorldModel world, Entity target, EventScheduler scheduler);


    public Point nextPosition(WorldModel world, Point destPos)
    {
        List<Point> points;
        points = strategy.computePath(getPosition(), destPos,
                p -> world.withinBounds(p) && !p.isOccupied(world),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (points.size() != 0)
            return points.get(0);
        return getPosition();
    }


}

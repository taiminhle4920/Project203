import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class AnimatedEntity extends Entity{
    public AnimatedEntity(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit) {

        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    @Override
    public void scheduleActions(
        EventScheduler scheduler,
        WorldModel world,
        ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                createAnimationAction(0),
                this.getAnimationPeriod());

}
    public  Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }
}



import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends MovableEntity {
    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {

        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        Optional<Entity> target =
                this.getPosition().findNearest(world, new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !moveTo( world,
                target.get(),
                scheduler)
                || !transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction( world, imageStore),
                    this.getActionPeriod());
        }
    }
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            AnimatedEntity miner = world.createDudeFull(this.getId(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.getResourceLimit(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public void _moveToHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        this.addResourceCount(1);
        target.changeHealth(-1);
    }
}

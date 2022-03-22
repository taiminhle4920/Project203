import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeFull extends MovableEntity{

    public DudeFull(
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

    @Override
    public void _moveToHelper(WorldModel world, Entity target, EventScheduler scheduler) {}
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        Optional<Entity> fullTarget =
                this.getPosition().findNearest(world, new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() && moveTo(world,
                fullTarget.get(), scheduler))
        {
            transform( world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }

    public void transform(

            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        AnimatedEntity miner = world.createDudeNotFull(this.getId(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getResourceLimit(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }


}

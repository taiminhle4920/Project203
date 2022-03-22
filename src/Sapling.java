import processing.core.PImage;

import java.util.List;

public class Sapling extends AnimatedEntity{
    public Sapling(

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
        this.changeHealth(1);
        if (!transformPlant( world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getHealth() <= 0) {
            Entity stump = world.createStump(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        else if (this.getHealth() >= this.getHealthLimit())
        {
            Entity tree = world.createTree("tree_" + this.getId(),
                    this.getPosition(),
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    imageStore.getImageList(Functions.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }
    public boolean transformPlant(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
            return this.transform( world, scheduler, imageStore);

    }

}

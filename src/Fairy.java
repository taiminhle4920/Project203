import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fairy extends MovableEntity {
    public Fairy(

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
        Optional<Entity> fairyTarget =
                this.getPosition().findNearest(world, new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveTo( world, fairyTarget.get(), scheduler)) {
                AnimatedEntity sapling = world.createSapling("sapling_" + this.getId(), tgtPos,
                        imageStore.getImageList(Functions.SAPLING_KEY));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler ,world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                createActivityAction( world, imageStore),
                this.getActionPeriod());
    }

    @Override
    public void _moveToHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }
}

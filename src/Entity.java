import processing.core.PImage;

import java.util.List;

public abstract class Entity{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private int health;
    private int healthLimit;

    public Entity(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;

    }
    public String getId() {
        return this.id;
    }
    public Point getPosition() {
        return this.position;
    }
    public List<PImage> getImages() {
        return this.images;
    }
    public int getImageIndex() {
        return this.imageIndex;
    }
    public int getActionPeriod() {
        return this.actionPeriod;
    }
    public int getHealth() {return this.health;}
    public int getAnimationPeriod() {return this.animationPeriod;}
    public void changeHealth(int num) {this.health += num;}
    public void nextImage() {this.imageIndex = (this.getImageIndex() + 1) % this.getImages().size();}
    public void changePosition(Point pos) {this.position = pos;}
    public int getResourceLimit(){return this.resourceLimit;}
    public int getResourceCount(){return this.resourceCount;}
    public int getHealthLimit(){return this.healthLimit;}
    public void addResourceCount(int num) { this.resourceCount = num;}
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {

           scheduler.scheduleEvent(this,
                    createAnimationAction( 0),
                    this.getAnimationPeriod());
        }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, null, null,
                repeatCount);
    }

    public PImage getCurrentImage(WorldModel world) {
            return this.getImages().get((this).getImageIndex());
        }
}

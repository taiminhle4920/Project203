import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel
{
    private final int PROPERTY_KEY = 0;
    private final String SAPLING_KEY = "sapling";
    private final int SAPLING_HEALTH_LIMIT = 5;
    private final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    private final int SAPLING_NUM_PROPERTIES = 4;
    private final int SAPLING_ID = 1;
    private final int SAPLING_COL = 2;
    private final int SAPLING_ROW = 3;
    private final int SAPLING_HEALTH = 4;

    private final String BGND_KEY = "background";
    public final int BGND_NUM_PROPERTIES = 4;
    public final int BGND_ID = 1;
    public final int BGND_COL = 2;
    public final int BGND_ROW = 3;

    private final String OBSTACLE_KEY = "obstacle";
    private final int OBSTACLE_NUM_PROPERTIES = 5;
    private final int OBSTACLE_ID = 1;
    private final int OBSTACLE_COL = 2;
    private final int OBSTACLE_ROW = 3;
    private final int OBSTACLE_ANIMATION_PERIOD = 4;

    private final String DUDE_KEY = "dude";
    private final int DUDE_NUM_PROPERTIES = 7;
    private final int DUDE_ID = 1;
    private final int DUDE_COL = 2;
    private final int DUDE_ROW = 3;
    private final int DUDE_LIMIT = 4;
    private final int DUDE_ACTION_PERIOD = 5;
    private final int DUDE_ANIMATION_PERIOD = 6;

    private final String HOUSE_KEY = "house";
    private final int HOUSE_NUM_PROPERTIES = 4;
    private final int HOUSE_ID = 1;
    private final int HOUSE_COL = 2;
    private final int HOUSE_ROW = 3;

    private final String FAIRY_KEY = "fairy";
    private final int FAIRY_NUM_PROPERTIES = 6;
    private final int FAIRY_ID = 1;
    private final int FAIRY_COL = 2;
    private  final int FAIRY_ROW = 3;
    private final int FAIRY_ANIMATION_PERIOD = 4;
    private final int FAIRY_ACTION_PERIOD = 5;

    private final String STUMP_KEY = "stump";

    private final String TREE_KEY = "tree";
    private final int TREE_NUM_PROPERTIES = 7;
    private final int TREE_ID = 1;
    private final int TREE_COL = 2;
    private final int TREE_ROW = 3;
    private final int TREE_ANIMATION_PERIOD = 4;
    private final int TREE_ACTION_PERIOD = 5;
    private final int TREE_HEALTH = 6;

    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }
    public int getNumRows(){return this.numRows;}
    public int getNumCols(){return this.numCols;}
    public Set<Entity> getEntities(){return entities;}
    public  Entity createHouse(
            String id, Point position, List<PImage> images)
    {
        return new House(id, position, images, 0, 0, 0,
                0, 0, 0);
    }

    public Entity createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new Obstacle(id, position, images, 0, 0, 0,
                animationPeriod, 0, 0);
    }

    public AnimatedEntity createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Tree(id, position, images, 0, 0,
                actionPeriod, animationPeriod, health, 0);
    }

    public Entity createStump(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Stump(id, position, images, 0, 0,
                0, 0, 0, 0);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public AnimatedEntity createSapling(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Sapling(id, position, images, 0, 0,
                SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public AnimatedEntity createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Fairy(id, position, images, 0, 0,
                actionPeriod, animationPeriod, 0, 0);
    }

    // need resource count, though it always starts at 0
    public AnimatedEntity createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new DudeNotFull(id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod, 0, 0);
    }

    // don't technically need resource count ... full
    public AnimatedEntity createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new DudeFull(id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod, 0, 0);
    }

    public boolean parseSapling(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SAPLING_COL]),
                    Integer.parseInt(properties[SAPLING_ROW]));
            String id = properties[SAPLING_ID];
            int health = Integer.parseInt(properties[SAPLING_HEALTH]);
            AnimatedEntity entity = new Sapling(id, pt, imageStore.getImageList(Functions.SAPLING_KEY), 0, 0,
                    SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, health, SAPLING_HEALTH_LIMIT);
            tryAddEntity(entity);
        }

        return properties.length == SAPLING_NUM_PROPERTIES;
    }

    public boolean parseDude(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[DUDE_COL]),
                    Integer.parseInt(properties[DUDE_ROW]));
            AnimatedEntity entity = createDudeNotFull(properties[DUDE_ID],
                    pt,
                    Integer.parseInt(properties[DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[DUDE_LIMIT]),
                    imageStore.getImageList(DUDE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == DUDE_NUM_PROPERTIES;
    }

    public boolean parseFairy(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FAIRY_COL]),
                    Integer.parseInt(properties[FAIRY_ROW]));
            AnimatedEntity entity = createFairy(properties[FAIRY_ID],
                    pt,
                    Integer.parseInt(properties[FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[FAIRY_ANIMATION_PERIOD]),
                    imageStore.getImageList(FAIRY_KEY));
            tryAddEntity(entity);
        }

        return properties.length == FAIRY_NUM_PROPERTIES;
    }

    public boolean parseTree(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                    Integer.parseInt(properties[TREE_ROW]));
            AnimatedEntity entity = createTree(properties[TREE_ID],
                    pt,
                    Integer.parseInt(properties[TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[TREE_HEALTH]),
                    imageStore.getImageList(TREE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == TREE_NUM_PROPERTIES;
    }

    public boolean parseObstacle(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = createObstacle(properties[OBSTACLE_ID], pt,
                    Integer.parseInt(properties[OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(OBSTACLE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public boolean parseHouse(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[HOUSE_COL]),
                    Integer.parseInt(properties[HOUSE_ROW]));
            Entity entity = createHouse(properties[HOUSE_ID], pt,
                    imageStore.getImageList(
                            HOUSE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == HOUSE_NUM_PROPERTIES;
    }

    public boolean processLine(
            String line, WorldModel world, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, imageStore);
                case DUDE_KEY:
                    return  world.parseDude(properties, imageStore);
                case OBSTACLE_KEY:
                    return world.parseObstacle(properties, imageStore);
                case FAIRY_KEY:
                    return world.parseFairy(properties, imageStore);
                case HOUSE_KEY:
                    return world.parseHouse(properties, imageStore);
                case TREE_KEY:
                    return world.parseTree(properties, imageStore);
                case SAPLING_KEY:
                    return world.parseSapling(properties, imageStore);
            }
        }

        return false;
    }

    public boolean parseBackground(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
           setBackground( pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }



    public void tryAddEntity(Entity entity) {
        if (entity.getPosition().isOccupied(this)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }


    public void load(
            Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), this, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }
    public boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0
                && pos.getX() < this.numCols;
    }
    public Optional<PImage> getBackgroundImage(
            Point pos)
    {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage(this));
        }
        else {
            return Optional.empty();
        }
    }

    public void setBackground(
            Point pos, Background background)
    {
        if (this.withinBounds(pos)) {
            setBackgroundCell(pos, background);
        }
    }


    public  Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    public void setOccupancyCell(Point pos, Entity entity)
    {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    public void setBackgroundCell(
            Point pos, Background background)
    {
        this.background[pos.getY()][pos.getX()] = background;
    }

    public void addEntity(Entity entity) {

        if (withinBounds( entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            getEntities().add(entity);
        }
    }
    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt( pos);
            setOccupancyCell(pos, entity);
            entity.changePosition(pos);
        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            Point point  = new Point(-1, -1);
            entity.changePosition(point);
            getEntities().remove(entity);
            setOccupancyCell(pos, null);
        }
    }

}

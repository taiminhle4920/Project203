import java.util.*;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions
{
    public static final String SAPLING_KEY = "sapling";
    public static final String STUMP_KEY = "stump";
    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_MAX = 600;
    public static  final int TREE_ANIMATION_MIN = 50;
    public static  final int TREE_ACTION_MAX = 1400;
    public static  final int TREE_ACTION_MIN = 1000;
    public static  final int TREE_HEALTH_MAX = 3;
    public static  final int TREE_HEALTH_MIN = 1;

    public static final Random rand = new Random();

    public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right",
            "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    public static boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }

    public static int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max - min);
    }


    public static void executeAction(Action action, EventScheduler scheduler) {action.executeAction(scheduler);}
}

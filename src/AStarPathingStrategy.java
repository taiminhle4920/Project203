import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy{

    private int calH(Point p1, Point p2) {
        return Math.abs((p1.getY() - p2.getY()) + (p1.getX() - p2.getX()));
    }

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<>();

        Comparator<Node> sort = Comparator.comparing(Node::getF);
        PriorityQueue<Node> openList = new PriorityQueue<>(sort);
        Map<Point, Node> closedList = new HashMap<>();

        int g = 0;
        int h = calH(start, end);
        int f = g + h;

        Node current = new Node(start, g, h, f, null);
        openList.add(current);

        while (openList.size() > 0) {
            current = openList.poll();
            if (withinReach.test(current.getPos(), end)) {
                writePath(path, current);
                break;
            }
            closedList.put(current.getPos(), current);

            List<Point> neighbors = potentialNeighbors
                    .apply(current.getPos())
                    .filter(canPassThrough)
                    .filter(p -> !closedList.containsKey(p))
                    .collect(Collectors.toList());

            for (Point neighbor : neighbors) {
                g = current.getG() + 1;
                h = calH(neighbor, end);
                f = h + g;
                Node neigh = new Node(neighbor, g,h, f, current);
                if (!openList.contains(neigh))
                    openList.add(neigh);
            }
        }
        return path;
    }

    private void writePath(List<Point> path, Node node) {
        if (node.getPrev() == null)
            return;
        path.add(0, node.getPos());
        if (node.getPrev().getPrev() != null){
            writePath(path, node.getPrev());
        }
    }



}

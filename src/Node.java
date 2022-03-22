public class Node {
    private Point position;
    private int g;
    private int h;
    private int f;
    private Node prev;


    public Node(Point position, int g, int h, int f, Node prev) {
        this.position = position;
        this.g = g;
        this.h = h;
        this.f = f;
        this.prev = prev;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        return ((Node)other).position.equals(this.position);
    }


    public int getF() { return f; }
    public int getG() { return g; }
    public int getH() { return h; }
    public Point getPos() { return position; }
    public Node getPrev() { return prev; }
}

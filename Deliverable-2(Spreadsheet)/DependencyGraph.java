import java.util.*;

public class DependencyGraph {
    // not used in this deliverable
    private final Map<Coord, Set<Coord>> edges = new HashMap<>();

    public void addEdge(Coord from, Coord to) {
        edges.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }

    public boolean detectCycle() {
        return false;
    }

    public List<Coord> topoOrder() {
        return Collections.emptyList();
    }
}

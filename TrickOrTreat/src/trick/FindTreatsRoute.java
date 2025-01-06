package trick;

import java.util.*;

import trick.NeighborhoodMap.Graph;

// import trick.NeighborhoodMap.Graph;



public class FindTreatsRoute {

    HashSet<String> visited = new HashSet<>();
    List<String> reachable = new ArrayList<>();

    public List<String> dfs(NeighborhoodMap.Graph g, String source) {
        
        visited.add(source);
        reachable.add(source);
        
        for (NeighborhoodMap.House h : g.adj(source)) { // Correct reference to House
            if (!visited.contains(h.name)) { // Check by house name
                dfs(g, h.name);
            }
        }

        return reachable;
    }

    
    public static void main(String[] args) {

        String inputFile = args[0];
        String sourceHouse = args[1];
        String outputFile = args[2];

        // Load graph from input file
        NeighborhoodMap map = new NeighborhoodMap();
        Graph graph = map.new Graph();

        StdIn.setFile(inputFile);

        // Number of houses
        int n = StdIn.readInt();

        // Skip candy details (not needed for DFS)
        for (int i = 0; i < n; i++) {
            String houseName = StdIn.readString();
            int numCandies = StdIn.readInt();
            for (int j = 0; j < numCandies; j++) {
                StdIn.readString(); // Candy name
                StdIn.readInt();    // Candy count
            }
            graph.addVertex(houseName);
        }

        // Number of edges
        int e = StdIn.readInt();

        // Read edges
        for (int i = 0; i < e; i++) {
            String from = StdIn.readString();
            String to = StdIn.readString();
            int weight = StdIn.readInt();
            graph.addEdge(from, to, weight);
            graph.addEdge(to, from, weight);
        }

        // Perform DFS
        FindTreatsRoute treatsRoute = new FindTreatsRoute();
        List<String> dfsOrder = treatsRoute.dfs(graph, sourceHouse);

        // Write the result to the output file
        StdOut.setFile(outputFile);
        for (int i = 0; i < dfsOrder.size(); i++) {
            StdOut.print(dfsOrder.get(i));
            if (i < dfsOrder.size() - 1) {
                StdOut.print(" ");
            }
        }
        StdOut.println();
    }
}

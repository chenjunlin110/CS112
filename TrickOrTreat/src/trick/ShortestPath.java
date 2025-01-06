package trick;

import java.util.*;
import trick.NeighborhoodMap.Graph;
import trick.NeighborhoodMap.House;

public class ShortestPath {
    private HashMap<String, String> previousNode;
    private HashMap<String, Integer> distanceToNode;

    public Map<String, List<String>> findAllShortestPaths(Graph graph, List<House> houses, String source) {
        previousNode = new HashMap<>();
        distanceToNode = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distanceToNode::get));

        // Initialize distances
        for (House house : houses) {
            distanceToNode.put(house.name, Integer.MAX_VALUE);
            previousNode.put(house.name, null);
        }
        distanceToNode.put(source, 0);
        queue.add(source);

        // Dijkstra's algorithm
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (visited.contains(current)) continue;
            visited.add(current);

            // Explore neighbors
            for (House neighbor : graph.adj(current)) {
                if (!visited.contains(neighbor.name)) {
                    int newDistance = distanceToNode.get(current) + (int) neighbor.weight;

                    if (newDistance < distanceToNode.get(neighbor.name)) {
                        distanceToNode.put(neighbor.name, newDistance);
                        previousNode.put(neighbor.name, current);
                        queue.add(neighbor.name);
                    }
                }
            }
        }

        // Build paths to all reachable nodes
        Map<String, List<String>> allPaths = new HashMap<>();
        for (String target : distanceToNode.keySet()) {
            if (distanceToNode.get(target) != Integer.MAX_VALUE) {
                allPaths.put(target, reconstructPath(source, target));
            }
        }

        return allPaths;
    }

    private List<String> reconstructPath(String source, String target) {
        List<String> path = new ArrayList<>();
        for (String at = target; at != null; at = previousNode.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        
        // Return empty path if target is not reachable
        if (!path.isEmpty() && !path.get(0).equals(source)) {
            return new ArrayList<>();
        }
        
        return path;
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            StdOut.println("Too few arguments. Did you put in command line arguments?");
            StdOut.println("Execute: java -cp bin trick.ShortestPath input1.in h1 shortestpaths1.out");
            return;
        }

        String inputFile = args[0];
        String sourceHouse = args[1];
        String outputFile = args[2];

        // Create an instance of NeighborhoodMap
        NeighborhoodMap map = new NeighborhoodMap();
        NeighborhoodMap.Graph graph = map.new Graph();
        List<NeighborhoodMap.House> houseList = new ArrayList<>();

        StdIn.setFile(inputFile);

        // Load houses
        int n = StdIn.readInt();
        for (int i = 0; i < n; i++) {
            String houseName = StdIn.readString();
            int numCandies = StdIn.readInt();
            // Skip candy details
            for (int j = 0; j < numCandies; j++) {
                StdIn.readString();
                StdIn.readInt();
            }
            NeighborhoodMap.House house = map.new House(houseName, 0);
            houseList.add(house);
            graph.addVertex(houseName);
        }

        // Load edges
        int e = StdIn.readInt();
        for (int i = 0; i < e; i++) {
            String from = StdIn.readString();
            String to = StdIn.readString();
            int weight = StdIn.readInt();
            graph.addEdge(from, to, weight);
            graph.addEdge(to, from, weight);
        }

        // Find all shortest paths from source
        ShortestPath sp = new ShortestPath();
        Map<String, List<String>> allPaths = sp.findAllShortestPaths(graph, houseList, sourceHouse);

        // Write output
        StdOut.setFile(outputFile);
        for (House house : houseList) {
            List<String> path = allPaths.get(house.name);
            if (path != null && !path.isEmpty()) {
                String predecessor = house.name.equals(sourceHouse) ? "null" : 
                    path.size() > 1 ? path.get(path.size() - 2) : "null";
                StdOut.println(house.name + " " + predecessor);
            }
        }
    }
}
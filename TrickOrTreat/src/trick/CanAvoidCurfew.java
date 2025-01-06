package trick;

import java.util.*;
import trick.NeighborhoodMap.Graph;
import trick.NeighborhoodMap.House;

public class CanAvoidCurfew {
    
    public HashMap<String, Integer> findShortestDistances(Graph graph, List<House> houses, String source) {
        HashMap<String, Integer> distanceToNode = new HashMap<>();
        HashMap<String, String> previousNode = new HashMap<>();
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

        return distanceToNode;
    }

    public static void main(String[] args) {
        if (args.length < 5) {
            StdOut.println("Usage: java -cp bin trick.CanAvoidCurfew <input_file> <source> <target> <curfew> <output_file>");
            return;
        }

        String inputFile = args[0];
        String sourceHouse = args[1];
        String targetHouse = args[2];
        int curfew = Integer.parseInt(args[3]);
        String outputFile = args[4];

        // Create neighborhood map
        NeighborhoodMap map = new NeighborhoodMap();
        NeighborhoodMap.Graph graph = map.new Graph();
        List<NeighborhoodMap.House> houseList = new ArrayList<>();

        // Read input file
        StdIn.setFile(inputFile);

        // Read number of houses
        int n = StdIn.readInt();

        // Load houses
        for (int i = 0; i < n; i++) {
            String houseName = StdIn.readString();
            int numCandies = StdIn.readInt();
            for (int j = 0; j < numCandies; j++) {
                StdIn.readString(); // Candy name
                StdIn.readInt();    // Candy count
            }
            NeighborhoodMap.House house = map.new House(houseName, 0);
            houseList.add(house);
            graph.addVertex(houseName);
        }

        // Read edges
        int e = StdIn.readInt();
        for (int i = 0; i < e; i++) {
            String from = StdIn.readString();
            String to = StdIn.readString();
            int weight = StdIn.readInt();
            graph.addEdge(from, to, weight);
            graph.addEdge(to, from, weight);
        }

        // Find shortest distances using Dijkstra's algorithm
        CanAvoidCurfew cac = new CanAvoidCurfew();
        HashMap<String, Integer> distances = cac.findShortestDistances(graph, houseList, sourceHouse);

        // Get distance to target house
        int distanceToTarget = distances.get(targetHouse);
        boolean canMakeIt = distanceToTarget <= curfew;

        // Write output
        StdOut.setFile(outputFile);
        StdOut.println(canMakeIt + " " + distanceToTarget);
    }
}
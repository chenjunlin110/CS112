package trick;

import java.util.*;
import trick.NeighborhoodMap.Graph;

public class FindHouseWithMostCandy {

    // Method to find the house with the most of the target candy
    public String findHouseWithMostCandy(Graph graph, HashMap<String, HashMap<String, Integer>> candyInventory,
                                         List<String> dfsOrder, String targetCandy) {
        String houseWithMostCandy = null;
        int maxCandyCount = -1;

        for (String house : dfsOrder) {
            HashMap<String, Integer> candies = candyInventory.getOrDefault(house, new HashMap<>());
            int candyCount = candies.getOrDefault(targetCandy, 0);

            if (candyCount > maxCandyCount) {
                maxCandyCount = candyCount;
                houseWithMostCandy = house;
            }
        }

        return houseWithMostCandy;
    }

    public static void main(String[] args) {
       
        String inputFile = args[0];
        String sourceHouse = args[1];
        String targetCandy = args[2];
        String outputFile = args[3];

        // Load graph and candy data
        NeighborhoodMap map = new NeighborhoodMap();
        Graph graph = map.new Graph();
        HashMap<String, HashMap<String, Integer>> candyInventory = new HashMap<>();

        StdIn.setFile(inputFile);

        // Number of houses
        int n = StdIn.readInt();

        // Load houses and their candy inventories
        for (int i = 0; i < n; i++) {
            String houseName = StdIn.readString();
            int numCandies = StdIn.readInt();
            HashMap<String, Integer> candies = new HashMap<>();
            for (int j = 0; j < numCandies; j++) {
                String candyName = StdIn.readString();
                int candyCount = StdIn.readInt();
                candies.put(candyName, candyCount);
            }
            candyInventory.put(houseName, candies);
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

        // Perform DFS to get reachable houses
        FindTreatsRoute treatsRoute = new FindTreatsRoute();
        List<String> dfsOrder = treatsRoute.dfs(graph, sourceHouse);

        // Use the method to find the house with the most candy
        FindHouseWithMostCandy finder = new FindHouseWithMostCandy();
        String houseWithMostCandy = finder.findHouseWithMostCandy(graph, candyInventory, dfsOrder, targetCandy);

        // Write the result to the output file
        StdOut.setFile(outputFile);
        if (houseWithMostCandy != null) {
            StdOut.println(houseWithMostCandy);
        }
    }
}

package trick;

import java.util.*;

import trick.NeighborhoodMap.Graph;


public class PathToMostCandy {


    public List<String> bfs(Graph g, List<String> houses, String source, String target) {

        HashMap<String, String> edgeTo = new HashMap<>();

        HashSet<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {

            String current = queue.poll();
            for (NeighborhoodMap.House house : g.adj(current)) {
                if (!visited.contains(house.name)) {
                    visited.add(house.name);
                    queue.add(house.name);
                    
                    edgeTo.put(house.name, current);

                    if (house.name.equals(target)) break;
                }
            }
        }

        List<String> path = new ArrayList<>();
        for (String at = target; at != source; at = edgeTo.get(at)) {
            path.add(at);
        }
        path.add(source);

    // Reverse the path to get it from source to target
        Collections.reverse(path);
        return path;
    }
    public static void main(String[] args) {
        if (args.length < 4) {
            StdOut.println(
                    "Too few arguments. Did you put in command line arguments? If using the debugger, add args to launch.json.");
            StdOut.println(
                    "execute: java -cp bin trick.PathToMostCandy input1.in h1 KitKat mostcandy1.out");
            return;
        }

        String inputFile = args[0];
        String sourceHouse = args[1];
        String targetCandy = args[2];
        String outputFile = args[3];

        // Load graph and candy data
        NeighborhoodMap map = new NeighborhoodMap();
        Graph graph = map.new Graph();
        List<String> houseList = new ArrayList<>();
        HashMap<String, HashMap<String, Integer>> candyInventory = new HashMap<>();

        StdIn.setFile(inputFile);

        // Number of houses
        int n = StdIn.readInt();

        // Load houses and their candy inventories
        for (int i = 0; i < n; i++) {
            String houseName = StdIn.readString();
            houseList.add(houseName);
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

        // Step 1: Find the house with the most of the target candy
        FindTreatsRoute treatsRoute = new FindTreatsRoute();
        List<String> dfsOrder = treatsRoute.dfs(graph, sourceHouse);

        FindHouseWithMostCandy finder = new FindHouseWithMostCandy();
        String houseWithMostCandy = finder.findHouseWithMostCandy(graph, candyInventory, dfsOrder, targetCandy);

        // Step 2: Find the shortest path to the house with the most candy
        PathToMostCandy pathFinder = new PathToMostCandy();
        List<String> shortestPath = pathFinder.bfs(graph, houseList, sourceHouse, houseWithMostCandy);

        // Step 3: Write the result to the output file
        StdOut.setFile(outputFile);
        for (int i = 0; i < shortestPath.size(); i++) {
            StdOut.print(shortestPath.get(i));
            StdOut.print(" ");
        }
        StdOut.println();
    }        
}
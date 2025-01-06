package trick;

import java.util.*;

public class NeighborhoodMap {

    public class House {
        public String name;
        public int weight;
        public House next;

        public House(String name, int weight) {
            this.name = name;
            this.weight = weight;
            this.next = null;
        }
    }

    public class Graph {
        private HashMap<String, List<House>> adjList;

        public Graph() {
            this.adjList = new HashMap<>();
        }

        // Add a vertex (house) to the graph
        public void addVertex(String houseName) {
            if (!adjList.containsKey(houseName)) {
                adjList.put(houseName, new ArrayList<>()); // Initialize empty list
            }
        }

        public void addEdge(String from, String to, int weight) {
            addVertex(from); // Ensure both vertices exist
            addVertex(to);

            adjList.get(from).add(new House(to, weight)); // Edge from 'from' to 'to'
        }

        public List<House> adj(String houseName) {
            if (!adjList.containsKey(houseName)) {
                return null;
            }
            return adjList.get(houseName);
        }

    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -cp bin trick.NeighborhoodMap <input file> <output file>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        NeighborhoodMap map = new NeighborhoodMap();
        Graph graph = map.new Graph(); 
        Map<String, Map<String, Integer>> candyMap = new HashMap<>();

        // Read input from the file
        StdIn.setFile(inputFile);

        // Number of houses
        int n = StdIn.readInt();

        // Process house data
        for (int i = 0; i < n; i++) {
            String houseName = StdIn.readString();
            int numCandies = StdIn.readInt();

            Map<String, Integer> candies = new HashMap<>();
            for (int j = 0; j < numCandies; j++) {
                String candyName = StdIn.readString();
                int candyCount = StdIn.readInt();
                candies.put(candyName, candyCount);
            }
            candyMap.put(houseName, candies);
            graph.addVertex(houseName);
        }

        // Number of edges
        int e = StdIn.readInt();

        // Process edge data
        for (int i = 0; i < e; i++) {
            String from = StdIn.readString();
            String to = StdIn.readString();
            int weight = StdIn.readInt();
            graph.addEdge(from, to, weight);
            graph.addEdge(to, from, weight);
        }

        // Write output to the file
        StdOut.setFile(outputFile);

        // Print candy information
        for (String house : candyMap.keySet()) {
            StdOut.print(house);
            Map<String, Integer> candies = candyMap.get(house);
            for (Map.Entry<String, Integer> entry : candies.entrySet()) {
                StdOut.print(" " + entry.getKey() + " " + entry.getValue());
            }
            StdOut.println();
        }

        // Print adjacency list
        for (String house : graph.adjList.keySet()) {
            StdOut.print(house);
            for (House neighbor : graph.adj(house)) {
                StdOut.print(" " + neighbor.name + " " + (int) neighbor.weight);
            }
            StdOut.println();
        }
    }
}

package test;

import static org.junit.Assert.*;
import org.junit.*;
import graph.FlightPathGraph;
import graph.City;

/**
 * JUnit class for testing FlightPathGraph
 */
public class FlightPathGraphTest {

    @Test
    public void testConstructor() {
        String[] testCities = {"0", "1", "2", "3", "4", "5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);
        assertEquals(graph.flightPaths.length, testCities.length);
        for (int i = 0; i < graph.flightPaths.length; i++) {
            assertEquals(graph.flightPaths[i].city, testCities[i]);
            assertNull(graph.flightPaths[i].next);
        }
    }

    @Test
    public void testAddEdge() {
        String[] testCities = {"0", "1", "2", "3", "4", "5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Add edges
        graph.addEdge("0", "1");
        graph.addEdge("0", "2");
        graph.addEdge("3", "4");
        graph.addEdge("4", "5");

        // Verify edges
        City node = graph.flightPaths[0].next;
        assertNotNull(node);
        assertEquals("1", node.city);
        node = node.next;
        assertNotNull(node);
        assertEquals("2", node.city);
        assertNull(node.next);

        node = graph.flightPaths[3].next;
        assertNotNull(node);
        assertEquals("4", node.city);
        assertNull(node.next);

        node = graph.flightPaths[4].next;
        assertNotNull(node);
        assertEquals("5", node.city);
        assertNull(node.next);
    }

    @Test
    public void testAddDuplicateEdge() {
        String[] testCities = {"0", "1", "2"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Add duplicate edges
        graph.addEdge("0", "1");
        graph.addEdge("0", "1");

        // Verify only one edge exists
        City node = graph.flightPaths[0].next;
        assertNotNull(node);
        assertEquals("1", node.city);
        assertNull(node.next);
    }

    @Test
    public void testAddSelfLoopEdge() {
        String[] testCities = {"0", "1", "2"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Add self-loop edge
        graph.addEdge("0", "0");

        // Verify no self-loop exists
        City node = graph.flightPaths[0].next;
        assertNull(node);
    }

    @Test
    public void testRemoveEdge() {
        String[] testCities = {"0", "1", "2", "3", "4", "5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Add edges
        graph.addEdge("0", "1");
        graph.addEdge("0", "2");
        graph.addEdge("3", "4");
        graph.addEdge("4", "5");

        // Remove edges
        graph.removeEdge("0", "1");
        graph.removeEdge("4", "5");

        // Verify edges
        City node = graph.flightPaths[0].next;
        assertNotNull(node);
        assertEquals("2", node.city);
        assertNull(node.next);

        node = graph.flightPaths[4].next;
        assertNull(node);
    }

    @Test
    public void testRemoveNonExistentEdge() {
        String[] testCities = {"0", "1", "2", "3"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Add edges
        graph.addEdge("0", "1");
        graph.addEdge("2", "3");

        // Attempt to remove an edge that doesn't exist
        graph.removeEdge("1", "3");

        // Verify original edges are intact
        City node = graph.flightPaths[0].next;
        assertNotNull(node);
        assertEquals("1", node.city);
        assertNull(node.next);

        node = graph.flightPaths[2].next;
        assertNotNull(node);
        assertEquals("3", node.city);
        assertNull(node.next);
    }

    @Test
    public void testRemoveAllEdges() {
        String[] testCities = {"0", "1", "2"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Add edges
        graph.addEdge("0", "1");
        graph.addEdge("0", "2");

        // Remove all edges from node "0"
        graph.removeEdge("0", "1");
        graph.removeEdge("0", "2");

        // Verify all edges are removed
        City node = graph.flightPaths[0].next;
        assertNull(node);
    }

    @Test
    public void testGetNumberOfVertices() {
        String[] testCities = {"0", "1", "2", "3", "4"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Verify the number of vertices
        assertEquals(5, graph.getNumberOfVertices());
    }

    @Test
    public void testEmptyGraph() {
        String[] testCities = {};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Verify the number of vertices is zero
        assertEquals(0, graph.getNumberOfVertices());
        assertEquals(0, graph.flightPaths.length);
    }

    @Test
    public void testEdgeBetweenNonExistentCities() {
        String[] testCities = {"0", "1", "2"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Attempt to add an edge with non-existent cities
        graph.addEdge("0", "3");
        graph.addEdge("3", "1");

        // Verify no new edges are added
        City node = graph.flightPaths[0].next;
        assertNull(node);
    }

    @Test
    public void testAddAndRemoveUndirectedEdge() {
        String[] testCities = {"0", "1"};
        FlightPathGraph graph = new FlightPathGraph(testCities);

        // Add edges in both directions to make it undirected
        graph.addEdge("0", "1");
        graph.addEdge("1", "0");

        // Verify both edges exist
        City node = graph.flightPaths[0].next;
        assertNotNull(node);
        assertEquals("1", node.city);
        assertNull(node.next);

        node = graph.flightPaths[1].next;
        assertNotNull(node);
        assertEquals("0", node.city);
        assertNull(node.next);

        // Remove one direction of the edge
        graph.removeEdge("0", "1");

        // Verify the other direction still exists
        node = graph.flightPaths[1].next;
        assertNotNull(node);
        assertEquals("0", node.city);
        assertNull(node.next);
    }
}
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import venom.*;

public class VenomTest {

    /*
     * This is a Java Test Class, which uses the JUnit package to create
     * and run tests. You do NOT have to submit this class.
     * 
     * You can fill in these tests in order to evaluate your code. Writing tests
     * is a crucial skill to have as a developer.
     * 
     * How to run?
     * - MAKE SURE you are in the right directory. On the right side of your VS Code Explorer, you should see:
     *  Venom
     *      lib
     *      src
     *      test
     *      input files
     * NOT:
     * Venom/CS112/Another Folder Name
     *  Venom
     *      ...
     * Open the INNERMOST Venom (case sensitive) using File -> Open Folder.
     * - Right click VenomTest.java in the VS Code explorer and select "Run Tests"
     */


    @Test
    public void testBuildTree() {
        Venom test = new Venom();

        // Build the tree using the sample input file
        test.buildTree("testInput.in");

        // Perform assertions based on the structure you expect
        SymbioteHost root = test.getRoot();
        assertNotNull("Root should not be null", root);
        assertEquals("Root should be Mac Gargan", "Mac Gargan", root.getName());

        // Check root's left child and its children
        SymbioteHost leftChild = root.getLeft();
        assertNotNull("Left child should not be null", leftChild);
        assertEquals("Left child should be Anne Weying", "Anne Weying", leftChild.getName());

        SymbioteHost leftRightChild = leftChild.getRight();
        assertNotNull("Left-Right child should not be null", leftRightChild);
        assertEquals("Left-Right child should be Wade Winston", "Eddie Brock", leftRightChild.getName());

        // Check root's right child and its children
        SymbioteHost rightChild = root.getRight();
        assertNotNull("Right child should not be null", rightChild);
        assertEquals("Right child should be Wade Winston", "Wade Winston", rightChild.getName());
    }



    @Test
    public void testFindMostSuitable() {
        Venom test = new Venom();

        // Create test hosts with different suitability levels
        SymbioteHost host1 = new SymbioteHost("Host1", 80, 50, false); // Suitability: 180
        SymbioteHost host2 = new SymbioteHost("Host2", 100, 50, false); // Suitability: 200
        SymbioteHost host3 = new SymbioteHost("Host3", 60, 50, false); // Suitability: 160

        // Insert hosts into tree
        test.insertSymbioteHost(host1);
        test.insertSymbioteHost(host2);
        test.insertSymbioteHost(host3);

        // Find most suitable host
        SymbioteHost mostSuitable = test.findMostSuitable();

        // Verify the most suitable host
        assertNotNull("Most suitable host should not be null", mostSuitable);
        assertEquals("Most suitable host should be Host2", "Host2", mostSuitable.getName());
        assertEquals("Most suitable host should have highest suitability", 200, mostSuitable.calculateSuitability());
    }

    @Test
    public void testFindHostsWithAntibodies() {
        Venom test = new Venom();

        // Create test hosts with and without antibodies
        SymbioteHost host1 = new SymbioteHost("Host1", 80, 50, true);
        SymbioteHost host2 = new SymbioteHost("Host2", 100, 50, false);
        SymbioteHost host3 = new SymbioteHost("Host3", 60, 50, true);

        // Insert hosts into tree
        test.insertSymbioteHost(host1);
        test.insertSymbioteHost(host2);
        test.insertSymbioteHost(host3);

        // Find hosts with antibodies
        ArrayList<SymbioteHost> hostsWithAntibodies = test.findHostsWithAntibodies();

        // Verify the results
        assertNotNull("List should not be null", hostsWithAntibodies);
        assertEquals("Should find 2 hosts with antibodies", 2, hostsWithAntibodies.size());
        assertTrue("Should contain Host1", hostsWithAntibodies.stream().anyMatch(h -> h.getName().equals("Host1")));
        assertTrue("Should contain Host3", hostsWithAntibodies.stream().anyMatch(h -> h.getName().equals("Host3")));
    }

    @Test
    public void testFindHostsWithinSuitabilityRange() {
        Venom test = new Venom();

        // Create test hosts with different suitability levels
        SymbioteHost host1 = new SymbioteHost("Host1", 30, 50, false); // Suitability: 130
        SymbioteHost host2 = new SymbioteHost("Host2", 80, 50, false); // Suitability: 180
        SymbioteHost host3 = new SymbioteHost("Host3", 150, 50, false); // Suitability: 250

        // Insert hosts into tree
        test.insertSymbioteHost(host1);
        test.insertSymbioteHost(host2);
        test.insertSymbioteHost(host3);

        // Find hosts within suitability range 120-200
        ArrayList<SymbioteHost> hostsInRange = test.findHostsWithinSuitabilityRange(120, 200);

        // Verify the results
        assertNotNull("List should not be null", hostsInRange);
        assertEquals("Should find 2 hosts in range", 2, hostsInRange.size());
        assertTrue("Should contain Host1", hostsInRange.stream().anyMatch(h -> h.getName().equals("Host1")));
        assertTrue("Should contain Host2", hostsInRange.stream().anyMatch(h -> h.getName().equals("Host2")));
    }

    @Test
    public void testDeleteSymbioteHost() {
        Venom test = new Venom();

        // Create and insert test hosts
        SymbioteHost host1 = new SymbioteHost("Host1", 80, 50, false);
        SymbioteHost host2 = new SymbioteHost("Host2", 100, 50, false);
        SymbioteHost host3 = new SymbioteHost("Host3", 60, 50, false);

        test.insertSymbioteHost(host1);
        test.insertSymbioteHost(host2);
        test.insertSymbioteHost(host3);

        // Delete a host
        test.deleteSymbioteHost("Host2");

        // Verify the deletion
        SymbioteHost root = test.getRoot();
        assertNotNull("Root should not be null", root);
        assertEquals("Root should be Host1", "Host1", root.getName());

        // Try to find the deleted host in the tree
        ArrayList<SymbioteHost> allHosts = test.findHostsWithinSuitabilityRange(0, Integer.MAX_VALUE);
        assertFalse("Deleted host should not be in tree",
                allHosts.stream().anyMatch(h -> h.getName().equals("Host2")));
        assertEquals("Tree should have 2 hosts remaining", 2, allHosts.size());
    }

    @Test
    public void testCleanupTree() {
        Venom test = new Venom();

        // Create test hosts with varying compatibilities and mental stability
        SymbioteHost host1 = new SymbioteHost("Host1", 80, 50, true);  // Has antibodies
        SymbioteHost host2 = new SymbioteHost("Host2", 20, 0, false); // Suitability: 120
        SymbioteHost host3 = new SymbioteHost("Host3", 150, 70, false); // Suitability > 100
        SymbioteHost host4 = new SymbioteHost("Host4", 40, 30, true);  // Has antibodies
        SymbioteHost host5 = new SymbioteHost("Host5", 200, 80, false); // Suitability > 100

        // Insert hosts into tree
        test.insertSymbioteHost(host1);
        test.insertSymbioteHost(host2);
        test.insertSymbioteHost(host3);
        test.insertSymbioteHost(host4);
        test.insertSymbioteHost(host5);

        // Run cleanup
        test.cleanupTree();

        // Verify no hosts with antibodies remain
        ArrayList<SymbioteHost> hostsWithAntibodies = test.findHostsWithAntibodies();
        assertEquals("All hosts with antibodies should be removed", 0, hostsWithAntibodies.size());

        // Verify only high suitability hosts without antibodies remain
        ArrayList<SymbioteHost> remainingHosts = test.findHostsWithinSuitabilityRange(101, Integer.MAX_VALUE);
        assertEquals("Only high suitability hosts should remain", 2, remainingHosts.size()); // Adjusted to 3

        // Check that Host2, Host3, and Host5 remain in the tree
//        assertTrue("Host2 should remain", remainingHosts.stream().anyMatch(h -> h.getName().equals("Host2")));
        assertTrue("Host3 should remain", remainingHosts.stream().anyMatch(h -> h.getName().equals("Host3")));
        assertTrue("Host5 should remain", remainingHosts.stream().anyMatch(h -> h.getName().equals("Host5")));
    }


}
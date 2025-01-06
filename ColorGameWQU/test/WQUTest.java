import static org.junit.Assert.*;
import org.junit.*;

import src.wqu.ColorGameWQU;
import src.wqu.Coordinate;

public class WQUTest {

    /*
     * This is a Java Test Class, which uses the JUnit package to create
     * and run tests
     * 
     * You can use this to evaluate your code. Examine these tests, as writing
     * similar test cases will help you immensly on other Assignments/Labs, as
     * well as moving forward in your CS career.
     */

    @Test
    public void testConstructor() {
        final int width = 5;
        final int height = 5;
        ColorGameWQU test = new ColorGameWQU(width, height);

        Coordinate[][] parent = test.getParent();
        int[][] size = test.getSize();
        for (int i = 0; i < parent.length; i++) {
            for (int j = 0; j < parent[i].length; j++) {
                fail("Not fully implemented yet! Add code to finish this test.");

                // First, check that parent at (i,j) equals (i,j)
                // Use parent[i][j].equals(new Coordinate(i,j)) to compare
                assertTrue(false);
                // Then, check that the size at (i,j) is equals to 1
                assertEquals(0, 1);
            }
        }
    }

    @Test
    public void testConstructor2() {
        // The previous test only considers one possible case
        // Fill this test out similarly to check another case
        final int width = 5;
        final int height = 7;
        ColorGameWQU test = new ColorGameWQU(width, height);

        fail("Not fully implemented yet! Add code to finish this test.");
    }

    @Test
    public void testUnionAndFind() {
        final int width = 5;
        final int height = 5;
        ColorGameWQU test = new ColorGameWQU(width, height);

        test.union(new Coordinate(0, 1), new Coordinate(1, 0));
        assertTrue(test.find(new Coordinate(1, 0)).equals(new Coordinate(0, 1)));
        assertFalse(test.find(new Coordinate(1, 0)).equals(new Coordinate(1, 0)));

        test.union(new Coordinate(3, 1), new Coordinate(0, 1));
        assertTrue(test.find(new Coordinate(3, 1)).equals(test.getParent()[1][0]));
        assertTrue(test.find(new Coordinate(1, 0)).equals(test.getParent()[1][0]));
        assertTrue(test.find(new Coordinate(3, 1)).equals(test.find(new Coordinate(0, 1))));

        test.union(new Coordinate(3, 1), new Coordinate(1, 1));
        assertTrue(test.find(new Coordinate(1, 1)).equals(test.getParent()[3][1]));
        assertFalse(test.find(new Coordinate(1, 1)).equals(test.getParent()[4][1]));
        assertTrue(test.find(new Coordinate(1, 1)).equals(test.getParent()[0][1]));
        assertFalse(test.find(new Coordinate(1, 1)).equals(test.getParent()[0][0]));
    }

}
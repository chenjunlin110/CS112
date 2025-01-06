import static org.junit.Assert.*;
import org.junit.*;

import election.*;

/*
* This is a Java Test Class, which uses the JUnit package to create
* and run tests
* 
* You can use this to evaluate your code. Examine these tests, as writing
* similar test cases will help you immensly on other Assignments/Labs, as
* well as moving forward in your CS career.
*/
public class ElectionAnalysisTest {

    // INPUT FILE FORMAT:
    // RaceID, State, OfficeID, Senate/House, YEAR, CANDIDATENAME, Party, VotesReceived, Winner (true/false)
    private static final String testFile = "testInput.csv";

    @Test
    public void testReadYears() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);

        // By Default, the test input csv contains three unique years (1, 2, and 6)
        // So we will traverse the years list and check they appear in the specified order
        YearNode ptr = test.years();
        assertTrue(ptr != null);
        assertTrue(ptr.getYear() == 2003);
        ptr = ptr.getNext();
        assertTrue(ptr.getYear() == 2011);
        ptr = ptr.getNext();
        assertTrue(ptr.getYear() == 2009);

        // We will also check that there are no extra nodes
        assertTrue(ptr.getNext() == null);
    }


    @Test
    public void testReadStates() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);

        int[] years = {2003, 2011, 2009};
        String[][] statesInYears = {{"A1"}, {"A2"}, {"B3", "B5"}};
        int year = 0;
        for (YearNode yr = test.years(); yr != null; yr = yr.getNext()) {
            assertTrue(yr.getYear() == years[year]);
            int state = 0;
            StateNode st = yr.getStates().getNext();
            do {
                assertTrue(st.getStateName().equals(statesInYears[year][state]));
                state++;
                st = st.getNext();
            } while ( st != yr.getStates());
            year++;
        }
        
    }

        @Test
    public void testReadElections() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        test.readElections(testFile);

        // Testing for multiple years
        YearNode yearNode = test.years();
        assertNotNull(yearNode);

        // Check the first year and its states
        assertEquals(2003, yearNode.getYear());
        StateNode stateNode = yearNode.getStates();
        assertNotNull(stateNode);

        // Checking the state "A1" in the year 2003
        assertEquals("A1", stateNode.getStateName());
        ElectionNode electionNode = stateNode.getElections();
        assertNotNull(electionNode);

        // Check the first election (RaceID, Votes, Winner)
        assertEquals(1, electionNode.getRaceID()); // Verify raceID
        assertEquals(15002, electionNode.getVotes()); // Verify total votes
        assertEquals("Candidate1", electionNode.getCandidates().get(0)); // Verify candidate name
        assertEquals("PARTY1", electionNode.getParty("Candidate1")); // Verify candidate's party
        assertEquals(true, electionNode.isWinner("Candidate1")); // Verify winner index

        // Check the second election (RaceID, Votes, Winner)
        electionNode = electionNode.getNext();
        assertEquals(1, electionNode.getRaceID());
        assertEquals(15002, electionNode.getVotes());
        assertEquals("Candidate2", electionNode.getCandidates().get(0)); // Verify candidate
        assertEquals("PARTY2", electionNode.getParty("Candidate2")); // Verify candidate's party
        assertEquals(0, electionNode.getWinner()); // Verify winner index

        // Move to the next year 2011 and its states
        yearNode = yearNode.getNext();
        assertEquals(2011, yearNode.getYear());
        stateNode = yearNode.getStates();
        assertNotNull(stateNode);
        assertEquals("A2", stateNode.getStateName());

        // Check the election in state "A2" for the year 2011
        electionNode = stateNode.getElections();
        assertEquals(1003, electionNode.getRaceID());
        assertEquals(1061, electionNode.getVotes());
        assertEquals("Candidate3", electionNode.getCandidates().get(0)); // Verify candidate
        assertEquals("Party3", electionNode.getParty(testFile)); // Verify candidate's party
        assertEquals(0, electionNode.getWinner()); // Verify winner index

        // Move to the next year 2009 and its states
        yearNode = yearNode.getNext();
        assertEquals(2009, yearNode.getYear());
        stateNode = yearNode.getStates();
        assertNotNull(stateNode);
        
        // There are two states in 2009 ("B3" and "B5")
        String[] expectedStates = {"B3", "B5"};
        int stateIndex = 0;
        do {
            assertEquals(expectedStates[stateIndex], stateNode.getStateName());

            // Checking elections for each state
            electionNode = stateNode.getElections();
            assertNotNull(electionNode);
            
            // Verify multiple elections per state
            assertEquals(1004 + stateIndex, electionNode.getRaceID());
            assertEquals(30000 + stateIndex * 10000, electionNode.getVotes());
            assertEquals("Candidate" + (4 + stateIndex), electionNode.getCandidates().get(0)); // Candidate name
            assertEquals("Party" + (4 + stateIndex), electionNode.getParty(testFile)); // Party
            assertEquals(0, electionNode.getWinner());

            // Move to next state in the circular list
            stateNode = stateNode.getNext();
            stateIndex++;
        } while (stateIndex < expectedStates.length);
    }



    @Test
    public void testAvgVotes() {
    ElectionAnalysis test = new ElectionAnalysis();
    test.readYears(testFile);
    test.readStates(testFile);
    test.readElections(testFile);

    double expectedAvgVotes = 15002.0; // Replace with your expected value based on test data
    double actualAvgVotes = test.averageVotes(2003, "A1"); // Replace with actual method and arguments

    assertEquals(expectedAvgVotes, actualAvgVotes, 0.01); // Use a delta for double comparison
}


    @Test
    public void testTotalVotes() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        test.readElections(testFile);

        int expectedTotalVotes = 15002; // Replace with your expected total votes
        int actualTotalVotes = test.totalVotes(2003, "A1"); // Replace with actual method and arguments

        assertEquals(expectedTotalVotes, actualTotalVotes);
    }


    @Test
    public void testCandidatesParty() {
        ElectionAnalysis test = new ElectionAnalysis();
        test.readYears(testFile);
        test.readStates(testFile);
        test.readElections(testFile);

        // Test for the first candidate in the file
        String candidateName1 = "Candidate1";
        String expectedParty1 = "PARTY1";
        String actualParty1 = test.candidatesParty(candidateName1);
        assertEquals(expectedParty1, actualParty1);

        // Test for the second candidate in the file
        String candidateName2 = "Candidate2";
        String expectedParty2 = "PARTY2";
        String actualParty2 = test.candidatesParty(candidateName2);
        assertEquals(expectedParty2, actualParty2);

        // Test for a candidate who doesn't exist in the elections
        String nonExistentCandidate = "NonExistentCandidate";
        String actualPartyNonExistent = test.candidatesParty(nonExistentCandidate);
        assertNull(actualPartyNonExistent); // Should return null for non-existent candidate
    }

}
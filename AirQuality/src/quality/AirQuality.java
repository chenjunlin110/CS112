package quality;

import java.util.ArrayList;

/**
 * This class represents the AirQuality system which populates a hashtable with states and counties 
 * and calculates various statistics related to air quality.
 *
 * This class is a part of the AirQuality project.
 *
 * @author Anna Lu
 * @author Srimathi Vadivel
 */

public class AirQuality {

    private State[] states; // hash table used to store states. This HT won't need rehashing.

    /**
     * **DO NOT MODIFY THIS METHOD**
     * Constructor creates a table of size 10.
     */

    public AirQuality () {
        states  = new State[10];
    }

    /**
     ** *DO NOT MODIFY THIS METHOD**
     * Returns the hash table.
     * @return the value held to represent the hash table
     */
    public State[] getHashTable() {
        return states;
    }

    /**
     *
     * DO NOT UPDATE THIS METHOD
     *
     * This method populates the hashtable with the information from the inputFile parameter.
     * It is expected to insert a state and then the counties into each state.
     *
     * Once a state is added, use the load factor to check if a resize of the hash table
     * needs to occur.
     *
     * @param inputLine A line from the inputFile with the following format:
     * State Name,County Name,AQI,Latitude,Longitude,Pollutant Name,Color
     */

    public void buildTable ( String inputFile ) {

        StdIn.setFile(inputFile); // opens the inputFile to be read
        StdIn.readLine();         // skips header

        while ( !StdIn.isEmpty() ) {

            String line = StdIn.readLine();
            State s = addState( line );
            addCountyAndPollutant(s, line );
        }
    }

    /**
     * Inserts a single State into the hash table states.
     *
     * Note: No duplicate states allowed. If the state is already present, simply
     * return the State object. Otherwise, insert at the front of the list.
     *
     * @param inputLine A line from the inputFile with the following format:
     * State Name,County Name,AQI,Latitude,Longitude,Pollutant Name,Color
     *
     * USE: Math.abs("State Name".hashCode()) as the key into the states hash table.
     * USE: hash function as: hash(key) = key % array length
     *
     * @return the State object if already present in the table or the newly created
     * State object inserted.
     */

    public State addState ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] input = inputLine.split(",");
        String StateName = input[0];

        int key = Math.abs(StateName.hashCode()) % states.length;

        State curr = states[key];

        while (curr != null) {
            if (curr.getName().equals(StateName)) {
                return curr;
            }
            curr = curr.getNext();
        }

        State newState = new State(StateName);
        newState.setNext(states[key]);
        states[key] = newState;
        return newState; // update this line
    }

    /**
     * Returns true if the counties hash table (within State) needs to be resized (re-hashed)
     *
     * Resize the hash table when (number of counties)/(array size) >= loadFactor
     *
     * @return true if resizing needs to happen, false otherwise
     */

    public boolean checkCountiesHTLoadFactor(State state) {
        // Calculate the current load factor for the counties hash table within the state
        int numberOfCounties = state.getNumberOfCounties();
        int tableSize = state.getCounties().length;
        double loadFactor = (double) numberOfCounties / tableSize;

        // Check if the load factor exceeds or is equal to the threshold for resizing
        if (loadFactor >= state.getLoadFactor()) {
            return true;
        }
        return false;
    }

    public void rehash(State state) {
        // Get the current counties hash table and its size
        County[] oldTable = state.getCounties();
        int newSize = oldTable.length * 2;
        County[] newTable = new County[newSize];

        // Iterate over each linked list in the old hash table
        for (int i = 0; i < oldTable.length; i++) {
            County current = oldTable[i];
            while (current != null) {
                // Save the next county before rehashing
                County next = current.getNext();

                // Calculate the new index in the new hash table
                int key = Math.abs(current.getName().hashCode());
                int newIndex = key % newSize;

                // Insert the county at the front of the linked list in the new table
                current.setNext(newTable[newIndex]);
                newTable[newIndex] = current;

                // Move to the next county in the old linked list
                current = next;
            }
        }

        // Update the state's counties hash table with the new table
        state.setCounties(newTable);
    }

    public void addCountyAndPollutant(State state, String inputLine) {

        String[] input = inputLine.split(",");
        String name = input[1];
        int AQI = Integer.parseInt(input[2]);
        double Latitude = Double.parseDouble(input[3]);
        double Longitude = Double.parseDouble(input[4]);
        String pollutant = input[5];
        String color = input[6];

        int countyIndex = Math.abs(name.hashCode()) % (state.getCounties().length);
        County[] counties = state.getCounties();
        County currentCounty = counties[countyIndex];

        // Search for existing county
        while (currentCounty != null) {
            if (currentCounty.getName().equals(name)) {
                boolean pollutantFound = false;
                for (Pollutant p : currentCounty.getPollutants()) {
                    if (p.getName().equals(pollutant)) {
                        p.setAQI(AQI);
                        p.setColor(color);
                        pollutantFound = true;
                        break;
                    }
                }
                if (!pollutantFound) {
                    currentCounty.getPollutants().add(new Pollutant(pollutant, AQI, color));
                }
                return;
            }
            currentCounty = currentCounty.getNext();
        }

        County newCounty = new County(name, Latitude, Longitude, null);
        newCounty.getPollutants().add(new Pollutant(pollutant, AQI, color));
        state.addCounty(newCounty);


        if (checkCountiesHTLoadFactor(state)) {
            rehash(state);
        }
    }


    public void setStatesAQIStats() {
        // Iterate through the hash table of states
        for (State state : states) {
            while (state != null) {
                // Variables for computing average AQI
                double totalAQI = 0;
                int pollutantCount = 0;

                // Variables for finding highest and lowest AQI
                County highestAQICounty = null;
                County lowestAQICounty = null;
                int highestAQI = Integer.MIN_VALUE;
                int lowestAQI = Integer.MAX_VALUE;

                // Iterate through the counties in the state
                County[] counties = state.getCounties();
                for (County county : counties) {
                    while (county != null) {
                        // Iterate through pollutants in the county to calculate AQI
                        for (Pollutant pollutant : county.getPollutants()) {
                            int aqi = pollutant.getAQI();
                            totalAQI += aqi;
                            pollutantCount++;

                            // Update highest and lowest AQI counties
                            if (aqi >= highestAQI) {
                                highestAQI = aqi;
                                highestAQICounty = county;
                            }
                            if (aqi <= lowestAQI) {
                                lowestAQI = aqi;
                                lowestAQICounty = county;
                            }
                        }
                        county = county.getNext();
                    }
                }

                // Calculate the average AQI
                double avgAQI = (pollutantCount == 0) ? 0 : totalAQI / pollutantCount;

                // Set the statistics for the state
                state.setAvgAQI(avgAQI);
                state.setHighestAQI(highestAQICounty);
                state.setLowestAQI(lowestAQICounty);
                state = state.getNext();
            }
        }
    }




    /**
     * In this method you will find all the counties within a state that have the same parameter name
     * and above the AQI threshold.
     *
     * @param stateName The name of the state
     * @param pollutantName The parameter name to filter by
     * @param AQITheshold The AQI threshold
     * @return ArrayList<County> List of counties that meet the criteria
     */

    public ArrayList<County> meetsThreshold(String stateName, String pollutantName, int AQIThreshold) {
        ArrayList<County> result = new ArrayList<>();

        // Iterate through the states array to find the matching state
        for (State state : states) {
            if (state == null) continue; // Skip empty state entries

            // Traverse the linked list of states
            while (state != null) {
                if (state.getName().equals(stateName)) {
                    // Iterate through the county array in the state
                    County[] counties = state.getCounties();
                    for (County county : counties) {
                        if (county == null) continue; // Skip null or EMPTY county entries

                        // Traverse the linked list of counties
                        while (county != null) {
                            // Check the pollutants within the county
                            for (Pollutant pollutant : county.getPollutants()) {
                                if (pollutant.getName().equals(pollutantName) && pollutant.getAQI() >= AQIThreshold) {
                                    result.add(county);
                                    break; // Move to the next county once a match is found
                                }
                            }
                            county = county.getNext(); // Move to the next county in the linked list
                        }
                    }
                    return result; // Return after processing the matching state
                }
                state = state.getNext(); // Move to the next state in the linked list
            }
        }

        return result;
    }

}

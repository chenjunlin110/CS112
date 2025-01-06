package quality;

public class TestAQIStatistics {
    public static void main(String[] args) {
        // Create an AirQuality object
        AirQuality airQuality = new AirQuality();

        // Build the table using the specified file
        airQuality.buildTable("pollutedCounties.csv");

        // Set the AQI statistics for each state
        airQuality.setStatesAQIStats();

        // Print out the states with their average, highest, and lowest AQI counties
        State[] states = airQuality.getHashTable();
        for (int i = 0; i < states.length; i++) {
            State currentState = states[i];
            if (currentState != null) {
                System.out.println("State Index " + i);
            }
            while (currentState != null) {
                System.out.println(currentState.getName() + ": AvgAQI= " + currentState.getAvgAQI() + ", HighestAQI County= " +
                        (currentState.getHighestAQI() != null ? currentState.getHighestAQI().getName() : "N/A") + ", LowestAQI County= " +
                        (currentState.getLowestAQI() != null ? currentState.getLowestAQI().getName() : "N/A"));
                currentState = currentState.getNext();
            }
        }
    }
}

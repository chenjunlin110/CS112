package quality;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        // Create an AirQuality object
        AirQuality airQuality = new AirQuality();

        // Build the tree using the specified file
        airQuality.buildTable("rehashCounties.csv");

        // Set the AQI statistics for each state
        // airQuality.setStatesAQIStats();

        // AddState.printEntireTable(airQuality.getHashTable());

        ArrayList<County> counties = airQuality.meetsThreshold("Texas", "Carbon monoxide", 143);

        // Iterate through the returned ArrayList and print out the county names
        System.out.println("Counties meeting the threshold:");
        for (County county : counties) {
            System.out.println(county.getName());
        }
    }

}

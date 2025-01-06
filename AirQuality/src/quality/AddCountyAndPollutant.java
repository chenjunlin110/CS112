package quality;

public class AddCountyAndPollutant {
    public static void main(String[] args) {
        AirQuality airQuality = new AirQuality();
        airQuality.buildTable("pollutedCounties.csv");
//         AddState.printEntireTable(airQuality.getHashTable());
         AddState.printAQIs(airQuality.getHashTable());
    }
}
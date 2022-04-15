package models;

import java.time.LocalDate;
import java.util.Map;

public class Measurement  {
    private final static int FIELD_STN = 0;
    private final static int FIELD_YYMMDDDD = 1;
    private final static int FIELD_FG = 4;
    private final static int FIELD_FXX = 9;
    private final static int FIELD_TG = 11;
    private final static int FIELD_TN = 12;
    private final static int FIELD_TX = 14;
    private final static int FIELD_SQ = 18;
    private final static int FIELD_RH = 22;
    private final static int FIELD_RHX = 23;
    private final static int NUM_FIELDS = 24;
    private final static double NUM_SCALE = 0.1;

    private final Station station;            // col0, STN
    private final LocalDate date;             // col1, YYMMDDDD
    private double averageWindSpeed;    // col4, FG in m/s  from 0.1 m/s
    private double maxWindGust;         // col9, FXX in m/s  from 0.1 m/s
    private double averageTemperature;  // col11, TG in degC  from 0.1 degC
    private double minTemperature;      // col12, TN in degC  from 0.1 degC
    private double maxTemperature;      // col14, TX in degC  from 0.1 degC
    private double solarHours;          // col18, SQ in hours  from 0.1 h
    private double precipitation;       // col22, RH in mm  from 0.1 mm, -1 = < 0.05
    private double maxHourlyPrecipitation;   // col23, RHX in mm  from 0.1 mm, -1 = < 0.05

    public Measurement(Station station, int dateNumber) {
        this.station = station;
        this.date = LocalDate.of(dateNumber / 10000, (dateNumber / 100) % 100, dateNumber % 100);
    }

    /**
     * converts a text line into a new Measurement instance
     * processes columns # STN, YYYYMMDD, FG, FXX, TG, TN, TX, SQ, RH, RHX as per documentation in the text files
     * converts integer values to doubles as per unit of measure indicators
     * empty or corrupt values are replaced by Double.NaN
     * -1 values that indicate < 0.05 are replaced by 0.0
     * @param textLine
     * @param stations  a map of Stations that can be accessed by station number STN
     * @return          a new Measurement instance that records all data values of above quantities
     *                  null if the station number cannot be resolved,
     *                      or the record is incomplete or cannot be parsed
     */
    public static Measurement fromLine(String textLine, Map<Integer,Station> stations) {
        String[] fields = textLine.split(",");

        // Check if the textline is valid.
        if (fields.length < NUM_FIELDS) return null;

        // Make a new measurement instance and add the correct fields to the constructor.
        Measurement measurement = new Measurement(stations.get(Integer.parseInt(fields[FIELD_STN].trim())), Integer.parseInt(fields[FIELD_YYMMDDDD].trim()));

        // Set all the datafields to the correct field. Check if they are empty if they are, set it to Double.NaN if they are not empty
        // turn the string into a double and multiply it by 0.1
        measurement.setAverageWindSpeed(fields[FIELD_FG].trim().isEmpty() ? Double.NaN : Double.parseDouble(fields[FIELD_FG].trim())*NUM_SCALE);
        measurement.setMaxWindGust(fields[FIELD_FXX].trim().isEmpty() ? Double.NaN : Double.parseDouble(fields[FIELD_FXX].trim())*NUM_SCALE);
        measurement.setAverageTemperature(fields[FIELD_TG].trim().isEmpty() ? Double.NaN : Double.parseDouble(fields[FIELD_TG].trim())*NUM_SCALE);
        measurement.setMinTemperature(fields[FIELD_TN].trim().isEmpty() ? Double.NaN : Double.parseDouble(fields[FIELD_TN].trim())*NUM_SCALE);
        measurement.setMaxTemperature(fields[FIELD_TX].trim().isEmpty() ? Double.NaN : Double.parseDouble(fields[FIELD_TX].trim())*NUM_SCALE);
        measurement.setSolarHours(fields[FIELD_SQ].trim().isEmpty() ? Double.NaN : Double.parseDouble(fields[FIELD_SQ].trim())*NUM_SCALE);
        measurement.setPrecipitation(fields[FIELD_RH].trim().isEmpty() ? Double.NaN : Double.parseDouble(fields[FIELD_RH].trim())*NUM_SCALE);
        measurement.setMaxHourlyPrecipitation(fields[FIELD_RHX].trim().isEmpty() ? Double.NaN : Double.parseDouble(fields[FIELD_RHX].trim())*NUM_SCALE);

        //return the new measurement instance
        return measurement;
    }

    public Station getStation() {
        return station;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAverageWindSpeed() {
        return averageWindSpeed;
    }

    public void setAverageWindSpeed(double averageWindSpeed) {
        this.averageWindSpeed = averageWindSpeed;
    }

    public double getMaxWindGust() {
        return maxWindGust;
    }

    public void setMaxWindGust(double maxWindGust) {
        this.maxWindGust = maxWindGust;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getSolarHours() {
        return solarHours;
    }

    public void setSolarHours(double solarHours) {
        this.solarHours = solarHours;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public double getMaxHourlyPrecipitation() {
        return maxHourlyPrecipitation;
    }

    public void setMaxHourlyPrecipitation(Double maxHourlyPrecipitation) {
        this.maxHourlyPrecipitation = maxHourlyPrecipitation;
    }

}

package models;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class Station {
    private final int stn;
    private final String name;
    private NavigableMap<LocalDate, Measurement> measurements;

    public Station(int id, String name) {
        this.stn = id;
        this.name = name;
        measurements = new TreeMap<>();
        //  initialize the measurements data structure with a suitable implementation class.

    }

    public Collection<Measurement> getMeasurements() {
        //return the measurements of this station
        return measurements.values();
    }

    public int getStn() {
        return stn;
    }

    public String getName() {
        return name;
    }

    /**
     * import station number and name from a text line
     *
     * @param textLine
     * @return a new Station instance for this data
     * or null if the data format does not comply
     */
    public static Station fromLine(String textLine) {
        String[] fields = textLine.split(",");
        if (fields.length < 2) return null;
        try {
            return new Station(Integer.valueOf(fields[0].trim()), fields[1].trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Add a collection of new measurements to this station.
     * Measurements that are not related to this station
     * and measurements with a duplicate date shall be ignored and not added
     *
     * @param newMeasurements
     * @return the nett number of measurements which have been added.
     */
    public int addMeasurements(Collection<Measurement> newMeasurements) {
        // add all newMeasurements to the station
        // ignore those who are not related to this station and entries with a duplicate date.
        int oldSize = 0;
        if (measurements != null) oldSize = this.getMeasurements().size();//Check if the measurement list is null. If it is not, we declare the oldSize as the size of this.measurements.
        //Stream through the newmeasurements and remove those that are not related to this station and those where the date already exists.
        //Filter these measurements on that the station number needs to be equal to this Station's station number
        //for each of the measurement put it inside of the measurements map.
        newMeasurements.stream().filter(measurement -> measurement.getStation().getStn() == this.getStn() && !this.measurements.containsKey(measurement.getDate())).forEach(measurement -> measurements.put(measurement.getDate(), measurement));
        //return new size of the measurements that belong to this station.
        return this.getMeasurements().size() - oldSize;
    }

    /**
     * calculates the all-time maximum temperature for this station
     *
     * @return the maximum temperature ever measured at this station
     * returns Double.NaN when no valid measurements are available
     */
    public double allTimeMaxTemperature() {
        // calculate the maximum wind gust speed across all valid measurements
        // stream through the measurements of this station and map them to only contain every measurement's max temperature
        // with using .max() we can compare those temperatures and get the max, if there are no valid measurements available
        // we return Double.NaN
        return getMeasurements().stream().map(Measurement::getMaxTemperature).max(Double::compare).orElse(Double.NaN);
    }

    /**
     * @return the date of the first day of a measurement for this station
     * returns Optional.empty() if no measurements are available
     */
    public Optional<LocalDate> firstDayOfMeasurement() {
        //  get the date of the first measurement at this station
        // Stream through the measurements and map them to only contain every measurement's dates.
        // We then call the .min() which gives us the earliest date that it can find or it will return Optional.empty() if no measurements are available.
        return getMeasurements().stream().map(Measurement::getDate).min(Comparator.naturalOrder());
    }

    /**
     * calculates the number of valid values of the data field that is specified by the mapper
     * invalid or empty values should be are represented by Double.NaN
     * this method can be used to check on different types of measurements each with their own mapper
     *
     * @param mapper the getter method of the data field to be checked.
     * @return the number of valid values found
     */
    public int numValidValues(Function<Measurement, Double> mapper) {
        // count the number of valid values that can be accessed in the measurements collection
        //  by means of the mapper access function
        // Stream through the measurements and for every measurement we check if the datafield that is specified by the mapper is valid or not.
        // afterwards we can get the number of valid values using .count(). Afterwards we cast this to an (int)
        // because count returns a long value.
        return (int) getMeasurements()
                .stream()
                .filter(measurement -> !Double.isNaN(mapper.apply(measurement)))
                .count();
    }

    /**
     * calculates the total precipitation at this station
     * across the time period between startDate and endDate (inclusive)
     *
     * @param startDate the start date of the period of accumulation (inclusive)
     * @param endDate   the end date of the period of accumulation (inclusive)
     * @return the total precipitation value across the period
     * 0.0 if no measurements have been made in this period.
     */
    public double totalPrecipitationBetween(LocalDate startDate, LocalDate endDate) {
        //  calculate and return the total precipitation across the given period
        // We first make a sortedMap, so we can only have the measurements from start date to the end date.
        // Afterwards we can stream through the measurements and using mapToDouble we can get for every measurement it's precipitation value
        // we then filter these values, so it only contains positive values and afterwards we can take the sum of these values so we know
        // the total precipitation between the given dates.
        return measurements
                .subMap(startDate, endDate.plusDays(1))
                .values()
                .stream()
                .filter(measurement -> !Double.isNaN(measurement.getPrecipitation()))
                .mapToDouble(Measurement::getPrecipitation)
                .sum();
    }

    /**
     * calculates the average of all valid measurements of the quantity selected by the mapper function
     * across the time period between startDate and endDate (inclusive)
     *
     * @param startDate the start date of the period of averaging (inclusive)
     * @param endDate   the end date of the period of averaging (inclusive)
     * @param mapper    a getter method that obtains the double value from a measurement instance to be averaged
     * @return the average of all valid values of the selected quantity across the period
     * Double.NaN if no valid measurements are available from this period.
     */
    public double averageBetween(LocalDate startDate, LocalDate endDate, Function<Measurement, Double> mapper) {
        // calculate and return the average value of the quantity mapper across the given period
        // We first make a sortedMap, so we can only have the measurements from start date to the end date.
        // Afterwards we stream through these measurements and apply the mapper to get the datafield of which we want to know the average between the given dates.
        //and then we take the average out of those values or else we return Double.NaN
        // if there are no valid values.
        return measurements
                .subMap(startDate, endDate.plusDays(1))
                .values()
                .stream()
                .filter(measurement -> !Double.isNaN(mapper.apply(measurement)))
                .mapToDouble(mapper::apply)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public String toString() {
        return stn + "/" + name;
    }

}

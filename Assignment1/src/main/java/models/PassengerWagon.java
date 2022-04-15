package models;

public class PassengerWagon extends Wagon{

    private final int numberOfSeats;

    /**
     * @param wagonId
     * @param numberOfSeats
     */
    public PassengerWagon(int wagonId, int numberOfSeats) {
        super(wagonId);
        this.numberOfSeats = numberOfSeats;
    }

    /**
     *
     * @return the number of seats
     */
    public int getNumberOfSeats() {
        return  numberOfSeats; //Returns the number of seats in a Wagon.
    }
}

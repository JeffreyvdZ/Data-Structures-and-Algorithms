package models;
public class FreightWagon extends Wagon {

    private int maxWeight;

    /**
     *
     * @param wagonId
     * @param maxWeight
     */
    public FreightWagon(int wagonId, int maxWeight) {
        super(wagonId);
        this.maxWeight = maxWeight;
    }


    /**
     *
     * @return return the maximum weight for this wagon.
     */
    public int getMaxWeight() {
        return maxWeight; //Returns the maximum weight of a FreightWagon
    }

}

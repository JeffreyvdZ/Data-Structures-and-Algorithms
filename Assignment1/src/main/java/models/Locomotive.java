package models;

public class Locomotive {
    private final int locNumber;
    private final int maxWagons;

    /**
     *
     * @param locNumber
     * @param maxWagons
     */
    public Locomotive(int locNumber, int maxWagons) {
        this.locNumber = locNumber;
        this.maxWagons = maxWagons;
    }

    public int getMaxWagons() {
        return maxWagons;
    }

    /**
     *
     * @return the toString for this class.
     */
    public String toString(){
        return "[Loc-" + this.locNumber + "]";
    }
}

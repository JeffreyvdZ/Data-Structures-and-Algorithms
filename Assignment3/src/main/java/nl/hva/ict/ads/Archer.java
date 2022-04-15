package nl.hva.ict.ads;

public class Archer {
    public static int MAX_ARROWS = 3;
    public static int MAX_ROUNDS = 10;


    private final int id;// Once assigned a value is not allowed to change.
    private static int countId = 135788;
    private String firstName;
    private String lastName;
    private int totalScore;
    private int misses;
    private int[] rounds;


    /**
     * Constructs a new instance of Archer and assigns a unique id to the instance.
     * Each new instance should be assigned a number that is 1 higher than the last one assigned.
     * The first instance created should have ID 135788;
     *
     * @param firstName the archers first name.
     * @param lastName  the archers surname.
     */
    public Archer(String firstName, String lastName) {
        // initialise the new archer
        //  generate and assign an new unique id
        //  initialise the scores of the archer
        this.id = countId;
        countId++;
        this.firstName = firstName;
        this.lastName = lastName;
        rounds = new int[MAX_ROUNDS];
    }

    /**
     * Registers the points for each of the three arrows that have been shot during a round.
     *
     * @param round  the round for which to register the points. First round has number 1.
     * @param points the points shot during the round, one for each arrow.
     */
    public void registerScoreForRound(int round, int[] points) {
        //If the place in the array is not empty, we need to decrease the misses, because we are updating this index of the array.
        if (rounds[round - 1] != 0) misses--;
        //initialize variable of totalPoints for this round.
        int totalPoints = 0;
        //loop through the length of the points array which is always equal to 3 loops.
        // If the point of a specific arrow is equal to zero add it to the archer's misses.
        // Add the points of every arrow to the total points variable, so at the end of the loop we know
        // the total score that the archer has gotten.
        for (int point : points) {
            if (point == 0) misses++;
            totalPoints = totalPoints + point;
        }
        // Add that total score to the right round. So we know how many score an archer has gotten for which round.
        rounds[round - 1] = totalPoints;
        //Every round we register the total score needs to get updated
        getTotalScore();
    }


    /**
     * Calculates/retrieves the total score of all arrows across all rounds
     *
     * @return
     */
    public int getTotalScore() {
        //For update purposes.
        totalScore = 0;
        // calculate/get the total score that the archer has earned across all arrows of all registered rounds
        for (int round : rounds) {
            totalScore = totalScore + round;
        }
//
        return totalScore;
    }

    /**
     * compares the scores/id of this archer with the scores/id of the other archer according to
     * the scoring scheme: highest total points -> least misses -> earliest registration
     * The archer with the lowest id has registered first
     * @param other     the other archer to compare against
     * @return  negative number, zero or positive number according to Comparator convention
     */
    public int compareByHighestTotalScoreWithLeastMissesAndLowestId(Archer other) {
        //  compares the scores/id of this archer with the other archer
        //  and return the result according to Comparator conventions
        //
        if (this.totalScore == other.totalScore){
            if (this.misses == other.misses) {
                return Integer.compare(this.getId(), other.getId());
            }
            return Integer.compare(this.misses, other.misses);
        }
        return Integer.compare(other.totalScore, this.totalScore);
    }

    public String toString(){
        return id + " (" + totalScore + ") " + firstName + " " + lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

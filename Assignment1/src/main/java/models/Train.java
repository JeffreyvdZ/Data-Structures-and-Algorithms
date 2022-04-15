package models;

public class Train {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon; //Head Node

    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;

    }

    /* three helper methods that are usefull in other methods */

    /**
     * @return Check if this Train' firstWagon has a next wagon, if there is a firstWagon instantiated.
     */
    public boolean hasWagons() {
        if (this.firstWagon == null) {
            return false;
        }
        return this.firstWagon.hasNextWagon();
    }

    /**
     * @param wagon
     * @return whether a Wagon already exists in the list you are trying to add it to.
     */
    public boolean checkIfWagonExistsInList(Wagon wagon) { //Helper method
        Wagon currentWagon = this.firstWagon;

        // Loop through the wagons to check if the wagon already exists in the current List.
        while (currentWagon != null) {
            if (currentWagon.equals(wagon)) {//If they are equal it measn you're trying to add a wagon that is already in the list and returns false.
                return true;
            }
            currentWagon = currentWagon.getNextWagon();
        }
        return false;
    }

    /**
     *
     * @return returns whether this train is a passengerTrain
     */
    public boolean isPassengerTrain() {
        return getFirstWagon() instanceof PassengerWagon;
    }

    /**
     *
     * @return whether this train is a freightTrain.
     */
    public boolean isFreightTrain() {
        return getFirstWagon() instanceof FreightWagon;
    }

    /**
     *
     * @return the first wagon.
     */
    public Wagon getFirstWagon() {
        return firstWagon;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * (sustaining all representation invariants)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     */
    public void setFirstWagon(Wagon wagon) {
        this.firstWagon = wagon;
    }

    /**
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        ////Loop through the wagons and add +1 to the number until there are no wagons anymore.
        int number = 0;
        for (Wagon currentWagon = firstWagon; currentWagon != null; currentWagon = currentWagon.getNextWagon()) {
            number++;
        }
        return number;
    }

    /**
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        return findWagonAtPosition(getNumberOfWagons()); //number of wagons is same as the last position.
    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        //Loop through the wagons and add the number of seats to totalNumberOfSeats if the wagons are PassengerWagons
        //Else return 0 if it is a FreightWagon.
        int totalNumberOfSeats = 0;
        Wagon currentWagon = this.firstWagon;
        if (currentWagon instanceof PassengerWagon) {

            while (currentWagon != null) {
                totalNumberOfSeats = totalNumberOfSeats + ((PassengerWagon) currentWagon).getNumberOfSeats();
                currentWagon = currentWagon.getNextWagon();
            }
        }
        return totalNumberOfSeats;
    }

    /**
     * calculates the total maximum weight of a freight train
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        //Loop through the wagons and add the weight of every wagon to totalMaxWeight if the wagons are FreightWagons
        //Else return 0 if it is a PassengerWagon.
        int totalMaxWeight = 0;
        Wagon currentWagon = this.firstWagon;
        if (currentWagon instanceof FreightWagon) {

            while (currentWagon != null) {
                totalMaxWeight = totalMaxWeight + ((FreightWagon) currentWagon).getMaxWeight();
                currentWagon = currentWagon.getNextWagon();
            }
        }
        return totalMaxWeight;
    }

    /**
     * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
     *
     * @param position
     * @return the wagon found at the given position
     * (return null if the position is not valid for this train)
     */
    public Wagon
    findWagonAtPosition(int position) {

        int targetPosition = 1; // initialise the targetposition as 0
        Wagon targetWagon = null; //initialise the targetWagon that we are trying to find.
        Wagon currentWagon = this.firstWagon;
        while (currentWagon != null) {// Loop through this as long as there is a next Wagon.
            // if the target position is equal to the given position we found our targetWagon,
            // if the value of the position is not compatible with the target position,
            // the targetWagon will remain null and null shall be returned.
            if (targetPosition == position) {
                targetWagon = currentWagon; //  initialise the found wagon in the targetWagon
                break; //  Break the loop because the targetWagon has been found at our given position.
            } else if (!currentWagon.hasNextWagon()) {
                break;
            }
            currentWagon.getNextWagon().setPreviousWagon(currentWagon); //Declare that the next Wagon's previous Wagon is gonna be the current Wagon.
            currentWagon = currentWagon.getNextWagon(); // Go to the next Wagon until the targetposition and the position match
            targetPosition = targetPosition + 1; // Increment of targetposition because if code reaches here it means the targetPosition was not equal with the given position index.
        }
        return targetWagon; //Return of the found Wagon.
    }

    /**
     * Finds the wagon with a given wagonId
     *
     * @param wagonId
     * @return the wagon found
     * (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        //Loop through the wagons until the wagonId is the same as the currentWagon's Id then return that wagon.
        //If that condition is not met, break the loop and return null;
        Wagon currentWagon = this.firstWagon;
        Wagon targetWagon = null;

        while (currentWagon != null) {
            if (wagonId == currentWagon.getId()) {
                targetWagon = currentWagon;
                break;
            } else if (!currentWagon.hasNextWagon()) {
                break;
            }
            currentWagon.getNextWagon().setPreviousWagon(currentWagon);
            currentWagon = currentWagon.getNextWagon();
        }

        return targetWagon;
    }

    /**
     * Determines if the given sequence of wagons can be attached to the train
     * Verfies of the type of wagons match the type of train (Passenger or Freight)
     * Verfies that the capacity of the engine is sufficient to pull the additional wagons
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return
     */
    public boolean canAttach(Wagon wagon) {
        //Validation if another wagon can be added. and if the wagons are compatible.
        if (getFirstWagon() == null) {
            return engine.getMaxWagons() > (getNumberOfWagons() + wagon.getTailLength());
        }
        return wagon.getClass().equals(getFirstWagon().getClass()) && engine.getMaxWagons() > (getNumberOfWagons() + wagon.getTailLength());
    }


    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon wagon) {

        if (wagon.hasPreviousWagon()) wagon.detachFront();
        if (checkIfWagonExistsInList(wagon)) return false;
        if (firstWagon == null) { //Check if this is the first wagon. If it is null this will be the first.
            this.firstWagon = wagon;
            return true;
        } else if (canAttach(wagon)) { // initialise a new Wagon as the first so we can loop through the wagons from the start.
            Wagon currentWagon = this.firstWagon;

            while (currentWagon.hasNextWagon()) { //While the w has a next Wagon keep looping.
                currentWagon = currentWagon.getNextWagon(); //go to the next wagon until we reach the end of the Train.
            }
            currentWagon.setNextWagon(wagon); //Add the Wagon to the rear end of the Train.
            wagon.setPreviousWagon(currentWagon);
            return true;
        }
        return false;
    }


    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon wagon) {
        if (this.firstWagon == null) { //If this wagon is null, wagon will be the firstWagon.
            this.firstWagon = wagon;
            return true;
        }
        if (canAttach(wagon)) { //If canAttach the given wagon, see insert at position 1.
            return insertAtPosition(1, wagon);
        }
        return false;
    }

    /**
     * Tries to insert the given sequence of wagons at/before the given wagon position in the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible of the engine has insufficient capacity
     * or the given position is not valid in this train)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtPosition(int position, Wagon wagon) {
        if (checkIfWagonExistsInList(wagon)) return false; //Check if the wagon already exists in this list of wagons.
        if (wagon.hasPreviousWagon()) wagon.detachFront(); //Detach front, if any

        if (canAttach(wagon)) { //If wagon can be attached...
            if (position == 1) { //If position is equal to one do...
                if (firstWagon == null) { //No need to go further if position is one and firstWagon is null, firstWagon will be the wagon in the parameter.
                    this.firstWagon = wagon;
                    return true;
                }//Check if it is a single wagon that we want to attach or multiple
                //If it has multiple wagons then connect the last wagon of wagon to the firstWagon
                // Connect firstWagon's previous to the last of wagon.
                //set new firstWagon as wagon's first wagon.
                //Else if wagon only has 1 wagon connect firstWagon's previous to wagon and wagon's next to firstWagon
                //And declare wagon as the new firstWagon.
                if (wagon.hasNextWagon()) {
                    if (insertSequenceAtPosition1(wagon)) return true;
                } else {
                    wagon.setNextWagon(this.firstWagon);
                    this.firstWagon.setPreviousWagon(wagon);
                    this.firstWagon = wagon;
                    return true;
                }
            }
            // if it is any position other than 1 where we are trying to insert then we need to find that wagon first.
            // We do this by initiating targetWagon.
            // Afterwards we check if targetWagon  is equal to 0 it returns false.
            // if it is not null we say that targetWagon’s previous wagon’s next wagon is the given wagon.
            // And the given wagons previous wagon is targetWagon’s previous wagon.
            // Afterwards we need to check if wagon is a sequence of wagons or just one wagon.
            // if it is a sequence we loop through the wagons until we get to the last wagon
            // and set that wagon’s next wagon to the targetWagon and the targetWagon’s previous wagon
            // to the last wagon in the sequence and we return true.
            Wagon targetWagon = this.findWagonAtPosition(position);
            if (targetWagon == null) return false;

            targetWagon.getPreviousWagon().setNextWagon(wagon);
            wagon.setPreviousWagon(targetWagon.getPreviousWagon());
            if (wagon.hasNextWagon()) {
                while (wagon.hasNextWagon()) {
                    wagon = wagon.getNextWagon();
                }
                wagon.setNextWagon(targetWagon);
                targetWagon.setPreviousWagon(wagon);
                return true;
            } else {
                targetWagon.setPreviousWagon(wagon);
                wagon.setNextWagon(targetWagon);
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param wagonId
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {
        Wagon targetWagon = this.findWagonById(wagonId);
        //Validation if the insertion can be made.
        if (targetWagon == null) return false;
        if (canAttach(targetWagon)) {
            if (targetWagon.hasNextWagon() && targetWagon.hasPreviousWagon()) {  //When a wagon is in the middle
                targetWagon.getPreviousWagon().setNextWagon(targetWagon.getNextWagon());
                targetWagon.getNextWagon().setPreviousWagon(targetWagon.getPreviousWagon());
                targetWagon.setPreviousWagon(null);
                targetWagon.setNextWagon(null);
                toTrain.attachToRear(targetWagon);
                return true;
            } else if (targetWagon.hasNextWagon() && !targetWagon.hasPreviousWagon()) { //Should move first Wagon correctly.
                this.firstWagon = targetWagon.getNextWagon();
                targetWagon.detachTail();
                toTrain.attachToRear(targetWagon);
                return true;
            } else if (targetWagon.hasPreviousWagon() && !targetWagon.hasNextWagon()) { //When a wagon is the last wagon.
                targetWagon.detachFront();
                toTrain.attachToRear(targetWagon);
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to split this train before the given position and move the complete sequence
     * of wagons from the given position to the rear of toTrain.
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param position
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {

        Wagon targetWagon = this.findWagonAtPosition(position);  // There is a wagon that shall be added to the toTrain, we initialise it if it passes the next few if statements.
        if (this.findWagonAtPosition(position) == null) { //Check if there is a wagon at a given position, the findWagonAtPosition method returns 0 if there can not be a wagon found given a certain position.
            return false;
        }
        if (toTrain.canAttach(targetWagon)) {
            if (targetWagon.hasNextWagon() && !targetWagon.hasPreviousWagon()) {//If the targetWagon is the first wagon.
                this.firstWagon = targetWagon.getNextWagon();// set the first wagon to null
                targetWagon.detachTail();
                toTrain.attachToRear(targetWagon); // attach the targetWagon to the rear of toTrain.
                return true;
            }
            if (targetWagon.hasPreviousWagon()) { //If targetWagon does have a previous Wagon detach and add to toTrain.
                targetWagon.detachFront();
            }
            toTrain.attachToRear(targetWagon);
            return true;
        }
        return false;
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     * the previous wagon of the last wagon becomes the second wagon
     * etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {

        if (this.getNumberOfWagons() != 0 && this.getNumberOfWagons() != 1) {
            Wagon currentWagon = this.firstWagon;
            Wagon previousWagon = null;
            Wagon nextWagon;
            while (currentWagon != null) {
                nextWagon = currentWagon.getNextWagon(); //Store the next Wagon
                currentWagon.setNextWagon(previousWagon); //Reverse the current Wagon's pointer
                previousWagon = currentWagon;// move pointer 1 position ahead
                currentWagon = nextWagon;//move pointer 1 position ahead
            }
            this.firstWagon = previousWagon;
            this.firstWagon.setPreviousWagon(null);
        }
    }

    public boolean insertSequenceAtPosition1(Wagon wagon){
        Wagon fWagon = wagon;
        while (wagon != null) {
            if (!wagon.hasNextWagon()) {
                wagon.setNextWagon(this.firstWagon);
                break;
            }
            wagon = wagon.getNextWagon();
        }
        this.firstWagon.setPreviousWagon(wagon);
        this.firstWagon = fWagon;
        return true;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param destination set the destination for this Train
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @param engine set the Locomotive for this Train
     */
    public void setEngine(Locomotive engine) {
        this.engine = engine;
    }

    /**
     * @param origin Set the origin for thisTrain
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return prints out the Train.
     */
    public String toString() {
        //Loop through the wagons and append them to the string builder.
        Wagon currentWagon = this.firstWagon;
        StringBuilder stringBuilder = new StringBuilder();

        while (currentWagon.hasNextWagon()) {
            stringBuilder.append(currentWagon);
            currentWagon = currentWagon.getNextWagon();
        }
        stringBuilder.append(currentWagon);
        //return the stringbuilder with the other required information.
        return engine + stringBuilder.toString() + " with " + getNumberOfWagons() + " from " +
                getOrigin() + " to " + getDestination();
    }
}

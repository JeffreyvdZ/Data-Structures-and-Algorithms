package models;

import java.util.Objects;

public abstract class Wagon {
    protected int id;               // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
                                    // a.k.a. the successor of this wagon in a sequence
                                    // set to null if no successor is connected
    private Wagon previousWagon;    // another wagon that is prepended at the front of this wagon
                                    // a.k.a. the predecessor of this wagon in a sequence
                                    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon (int wagonId) {
        this.id = wagonId;
    }

    /**
     * @return the id of this Wagon
     */
    public int getId() {
        return this.id;
    }
    /**
     * @return the next wagon
     */
    public Wagon getNextWagon() {
        return nextWagon;
    }
    /**
     * @return the previous wagon
     */
    public Wagon getPreviousWagon() {
        return previousWagon;
    }
    /**
     * @return  whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        return Objects.nonNull(nextWagon);
    }

    /**
     * @return  whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        return Objects.nonNull(previousWagon);
    }
    /**
     * Returns the last wagon attached to it, if there are no wagons attached to it then this wagon is the last wagon.
     * @return  the wagon
     */
    public Wagon getLastWagonAttached() {
        Wagon currentWagon = this; //Loop through the Wagons until you get the last one.
        while (currentWagon.hasNextWagon()){
            currentWagon = currentWagon.getNextWagon();
        }
       return currentWagon; //This wagon is the last wagon in it's sequence.
    }

    /**
     * @return  the length of the tail of wagons towards the end of the sequence
     * excluding this wagon itself.
     */
    public int getTailLength() {
        //Loop through the wagons and add +1 every loop.
        int tailLength = 0;
        Wagon currentWagon = this;
        while (currentWagon.hasNextWagon()){
            currentWagon = currentWagon.getNextWagon();
            tailLength++;
        }

        return tailLength; //A single Wagon should have a tail of 0, because it is the only Wagon.
    }

    /**
     * Attaches the tail wagon behind this wagon, if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     */
    public void attachTail(Wagon tail) {
        if (this.hasNextWagon()){
            throw new IllegalStateException(this + " Already has " + this.getNextWagon() + " behind it, the attachment could not be made");
        } else if (tail.hasPreviousWagon()){
            throw new IllegalStateException(tail.getPreviousWagon() + " Already has " + tail + " behind it, the attachment could not be made");
        } else {
            Wagon currentWagon = this;
            tail.reAttachTo(currentWagon);
        }
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     * @return the first wagon of the tail that has been detached
     *          or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        //detach this wagon from the next wagon, if any
        Wagon formerTail = null;
        if (this.hasNextWagon()){
            formerTail = getNextWagon();
            this.nextWagon.setPreviousWagon(null);
            this.setNextWagon(null);
        }
        return formerTail;
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     * @return  the former previousWagon that has been detached from,
     *          or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        //detach this wagon from the previous, if any
        Wagon formerFront = null;

        if (this.hasPreviousWagon()){
            formerFront = getPreviousWagon();
            this.getPreviousWagon().setNextWagon(null);
            this.setPreviousWagon(null);
        }

        return formerFront;
    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {
        front.detachTail();
        detachFront();
        previousWagon = front;
        front.setNextWagon(this);
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        if (this.hasPreviousWagon()){
            this.getPreviousWagon().setNextWagon(this.getNextWagon());

        }
        if (this.hasNextWagon()){
            this.getNextWagon().setPreviousWagon(this.getPreviousWagon());

        }
        this.setPreviousWagon(null);
        this.setNextWagon(null);
    }


    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {

        if (!hasNextWagon()) return null;
        //Check if this Wagon has a wagon attached in front of it, so
        // it can be attached again to the wagon in front of this wagon later
        Wagon head;
        if (hasPreviousWagon()) {
            head = getPreviousWagon();
        }
        else {
            head = null;
        }

        Wagon newFirst = null;
        Wagon currentWagon = this, nextWagon;

        while (currentWagon != null){
            //Get Pointer to next node
            nextWagon = currentWagon.getNextWagon();

            //push currentWagon at the beginning of the list with starting with newFirst
            currentWagon.setPreviousWagon(null);
            currentWagon.setNextWagon(newFirst);
            if ((newFirst) != null) (newFirst).setPreviousWagon(currentWagon);
            newFirst = currentWagon;

// The reversed sequence is attached again to the wagon in front of this Wagon, if any.
            if (Objects.nonNull(head)) {
                head.nextWagon = null;
                head.attachTail(currentWagon);
            }
            //Update currentWagon
            currentWagon = nextWagon;
        }

        return newFirst;

    }

    /**
     * set the id of this wagon
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set this Wagon's next wagon.
     * @param nextWagon
     */
    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;
    }

    /**
     * set this wagon's previous wagon.
     * @param previousWagon
     */
    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    /**
     * @return String of this Wagon
     */
    public String toString(){
        return "[Wagon-" + this.id + "]";
    }
    // TODO
}

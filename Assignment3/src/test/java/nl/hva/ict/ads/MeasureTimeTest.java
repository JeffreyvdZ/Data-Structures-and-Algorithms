package nl.hva.ict.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

public class MeasureTimeTest {
    private static final int MAX_ITEMS = 5000000;
    private static final int MAX_AMOUNT_OF_SECONDS = 20;

    protected Sorter<Archer> sorter = new ArcherSorter();
    protected List<Archer> archers;
    protected Comparator<Archer> scoringScheme = Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId;
    protected ChampionSelector championSelector;

    int[] randomSeed = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};

    static int randomSeedPointer = 0;

    protected int counter = 100;

    @BeforeEach
    void setup() {
        // We will call the garbage collection so that it won't impact the measurements.
        System.gc();
        // Our selector will be made with a seed from our randomseed array
        championSelector = new ChampionSelector(randomSeed[randomSeedPointer]);
        System.out.println("Current seed: " + randomSeed[randomSeedPointer]);

        archers = new ArrayList<>(championSelector.enrollArchers(counter));
    }

    @RepeatedTest(10)
    void measureInsertionSortTime() {
        // Creating an executorservice which can execute a single task at a time.
        final ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            final Future<Object> f = service.submit(() -> {
                //Timestamp before the sorting call
                long start = System.currentTimeMillis();

                //calling the sorting algorithm
                sorter.selInsSort(archers, scoringScheme);

                //Timestamp after the sorting call
                long end = System.currentTimeMillis();

                // returning the size of the archers array and the time in milliseconds for this size.
                return archers.size() + ", " + (end - start) + " ms";
            });

            // We are setting the time limit to 20 seconds in a sout, so we are printing out the returning values.
            System.out.println(f.get(MAX_AMOUNT_OF_SECONDS, TimeUnit.SECONDS));

        } catch (final TimeoutException e) { // Call this when the timelimit has been reached.
            // setting the seed to the next seed in the array when there is an error.
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
            // Error for reaching the timelimit
            System.err.println("Calculation took to long: " + MAX_AMOUNT_OF_SECONDS + " seconds");
            return;
        } catch (final Exception e) {
            // setting the seed to the next seed in the array when there is an error.
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
            throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }
        // Multiplying the number of archers by 2.
        counter = counter * 2;
        // Clearing it to delete the archers from the last sort
        championSelector.getArchers().clear();
        // remaking the archers list
        archers = new ArrayList<>(championSelector.enrollArchers(counter));

        if (archers.size() <= MAX_ITEMS) {
            // if the archers size is lower than the max items, we will call the garbage collection so that it won't impact the measurements.
            System.gc();
            // and we call the function again to loop through the multiplied archers list
            measureInsertionSortTime();
        } else {
            // if the multiplied array is bigger than the max items, we will go to the next seed and print an error message.
            System.err.println("Archers size has reached " + MAX_ITEMS);
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
        }
    }

    @RepeatedTest(10)
    void measureQuickSortTime() {
        // Creating an executorservice which can execute a single task at a time.
        final ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            final Future<Object> f = service.submit(() -> {
                //Timestamp before the sorting call
                long start = System.currentTimeMillis();

                //calling the sorting algorithm
                sorter.quickSort(archers, scoringScheme);

                //Timestamp after the sorting call
                long end = System.currentTimeMillis();

                // returning the size of the archers array and the time in milliseconds for this size.
                return archers.size() + ", " + (end - start) + " ms";
            });

            // We are setting the time limit to 20 seconds in a sout, so we are printing out the returning values.
            System.out.println(f.get(MAX_AMOUNT_OF_SECONDS, TimeUnit.SECONDS));

        } catch (final TimeoutException e) { // Call this when the timelimit has been reached.
            // setting the seed to the next seed in the array when there is an error.
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
            // Error for reaching the timelimit
            System.err.println("Calculation took to long: " + MAX_AMOUNT_OF_SECONDS + " seconds");
            return;
        } catch (final Exception e) {
            // setting the seed to the next seed in the array when there is an error.
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
            throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }
        // Multiplying the number of archers by 2.
        counter = counter * 2;
        // Clearing it to delete the archers from the last sort
        championSelector.getArchers().clear();
        // remaking the archers list
        archers = new ArrayList<>(championSelector.enrollArchers(counter));

        if (archers.size() <= MAX_ITEMS) {
            // if the archers size is lower than the max items, we will call the garbage collection so that it won't impact the measurements.
            System.gc();
            // and we call the function again to loop through the multiplied archers list
            measureQuickSortTime();
        } else {
            // if the multiplied array is bigger than the max items, we will go to the next seed and print an error message.
            System.err.println("Archers size has reached " + MAX_ITEMS);
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
        }
    }

    @RepeatedTest(10)
    void measureHeapSortTime() {
        // Creating an executorservice which can execute a single task at a time.
        final ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            final Future<Object> f = service.submit(() -> {
                //Timestamp before the sorting call
                long start = System.currentTimeMillis();

                //calling the sorting algorithm
                //Setting tops to 10, don't know which amount of tops the test should use.
                sorter.topsHeapSort(10, archers, scoringScheme);

                //Timestamp after the sorting call
                long end = System.currentTimeMillis();

                // returning the size of the archers array and the time in milliseconds for this size.
                return archers.size() + ", " + (end - start) + " ms";
            });

            // We are setting the time limit to 20 seconds in a sout, so we are printing out the returning values.
            System.out.println(f.get(MAX_AMOUNT_OF_SECONDS, TimeUnit.SECONDS));

        } catch (final TimeoutException e) { // Call this when the timelimit has been reached.
            // setting the seed to the next seed in the array when there is an error.
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
            // Error for reaching the timelimit
            System.err.println("Calculation took to long: " + MAX_AMOUNT_OF_SECONDS + " seconds");
            return;
        } catch (final Exception e) {
            // setting the seed to the next seed in the array when there is an error.
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
            throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }
        // Multiplying the number of archers by 2.
        counter = counter * 2;
        // Clearing it to delete the archers from the last sort
        championSelector.getArchers().clear();
        // remaking the archers list
        archers = new ArrayList<>(championSelector.enrollArchers(counter));

        if (archers.size() <= MAX_ITEMS) {
            // if the archers size is lower than the max items, we will call the garbage collection so that it won't impact the measurements.
            System.gc();
            // and we call the function again to loop through the multiplied archers list
            measureHeapSortTime();
        } else {
            // if the multiplied array is bigger than the max items, we will go to the next seed and print an error message.
            System.err.println("Archers size has reached " + MAX_ITEMS);
            randomSeedPointer = (randomSeedPointer == randomSeed.length - 1) ? 0 : randomSeedPointer + 1;
        }
    }
}


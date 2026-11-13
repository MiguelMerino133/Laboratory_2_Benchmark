package org.example;

import java.util.Random;

public class Algorithm_B {

    static final int[] SIZES = {100, 1_000, 10_000, 100_000, 1_000_000};
    static final int REPETITIONS = 3;
    static final long SEED = 42;
    // Worst case with n = 1,000,000 needs about 500,000 million comparisons (hours).
    // Change to true only if you want to wait for it.
    static final boolean RUN_WORST_CASE_ONE_MILLION = true;

    // ============================================================
    // ALGORITHM (auxiliary structure): remember the values already seen
    // ============================================================
    public static boolean hasDuplicates(int[] data) {
        int[] seen = new int[data.length];
        int count = 0;

        for (int i = 0; i < data.length; i++) {
            boolean found = false;

            for (int j = 0; j < count; j++) {
                if (data[i] == seen[j]) {
                    found = true;
                    break;
                }
            }

            if (found) {
                return true;
            }

            seen[count] = data[i];
            count++;
        }

        return false;
    }

    // STEP 04 - Count operations
    public static long countOperations(int[] data) {
        int[] seen = new int[data.length];
        int count = 0;
        long operations = 0;

        for (int i = 0; i < data.length; i++) {
            boolean found = false;

            for (int j = 0; j < count; j++) {
                operations++; // Count each internal comparison in the auxiliary structure
                if (data[i] == seen[j]) {
                    found = true;
                    break;
                }
            }

            if (found) {
                return operations;
            }

            seen[count] = data[i];
            count++;
        }

        return operations;
    }

    // ============================================================
    // STEP 01 - Generate datasets
    // ============================================================
    // Values 1..n without repetition, shuffled (same seed = same data as Algorithm_A)
    static int[] generateNoDuplicates(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i + 1;
        }
        Random random = new Random(SEED + size);
        for (int i = size - 1; i > 0; i--) {          // Fisher-Yates shuffle
            int j = random.nextInt(i + 1);
            int temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }
        return data;
    }

    // Same data, but the second element repeats the first one
    static int[] generateEarlyDuplicate(int size) {
        int[] data = generateNoDuplicates(size);
        data[1] = data[0];
        return data;
    }

    // ============================================================
    // STEP 05 and 06 - Measure time and repeat
    // ============================================================
    static double[] measureTimes(int[] data) {
        double[] times = new double[REPETITIONS];
        for (int t = 0; t < REPETITIONS; t++) {
            long start = System.nanoTime();
            hasDuplicates(data);
            long end = System.nanoTime();
            times[t] = (end - start) / 1_000_000.0;    // nanoseconds to milliseconds
        }
        return times;
    }

    // ============================================================
    // STEP 07 - Register table
    // ============================================================
    static void runScenario(String scenario, boolean earlyDuplicate) {
        System.out.println("\nScenario: " + scenario);
        System.out.printf("%10s%12s%18s%12s%12s%12s%12s%n",
                "n", "Duplicate", "Operations", "Test 1", "Test 2", "Test 3", "Average");

        for (int size : SIZES) {
            if (!earlyDuplicate && size == 1_000_000 && !RUN_WORST_CASE_ONE_MILLION) {
                long estimated = (long) size * (size - 1) / 2;
                System.out.printf("%10d%12s%18d%12s%12s%12s%12s%n",
                        size, "-", estimated, "-", "-", "-", "not run");
                continue;
            }
            int[] data = earlyDuplicate ? generateEarlyDuplicate(size) : generateNoDuplicates(size);
            boolean result = hasDuplicates(data);
            long operations = countOperations(data);
            double[] times = measureTimes(data);
            double average = (times[0] + times[1] + times[2]) / REPETITIONS;

            System.out.printf("%10d%12s%18d%12.4f%12.4f%12.4f%12.4f%n",
                    size, result, operations, times[0], times[1], times[2], average);
        }
    }

    public static void main(String[] args) {
        System.out.println("ALGORITHM B - Auxiliary array of seen values (times in ms)");
        runScenario("early duplicate (best case)", true);
        runScenario("no duplicates (worst case)", false);
        System.out.println("\n* n = 1,000,000 without duplicates not run: estimated operations = n(n-1)/2");
    }
}
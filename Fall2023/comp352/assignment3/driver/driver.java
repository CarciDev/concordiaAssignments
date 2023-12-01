package comp352.assignment3.driver;

import comp352.assignment3.ADT.HashTable.myHashTable;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class driver {
    public static void main(String[] args) {
        int size = 100000;
        myHashTable hashTable = new myHashTable(size);
        Random rand = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("HashTableOperations.csv"))) {
            // Write header to CSV
            writer.write("Operation,ID,Time(ms),Failure,Generated\n");

            long startTime, endTime, totalTime = 0;
            double timeTaken;
            int[] ids = new int[size]; // Store generated IDs
            int failures = 0, totalRemovals = 0;

            // Inserting unique IDs
            for (int i = 0; i < size; i++) {
                int id = hashTable.generate();
                ids[i] = id; // Store generated ID
                startTime = System.nanoTime();
                hashTable.put(id, "Value" + i);
                endTime = System.nanoTime();
                timeTaken = (endTime - startTime) / 1e6; // Convert nanoseconds to milliseconds
                totalTime += timeTaken;

                writer.write("Insert," + id + "," + timeTaken + ",No Failure,Yes\n");
            }

            // Removing IDs (both existing and non-existing)
            for (int i = 0; i < size; i++) {
                if (i % 1000 == 0) {
                    // Remove existing ID
                    int idToRemove = ids[i]; // Use stored ID
                    writeRemovalToCSV(writer, hashTable, idToRemove, totalTime, true, failures);

                    // Remove non-existing ID
                    int nonExistingId = 10000000 + rand.nextInt(90000000);
                    writeRemovalToCSV(writer, hashTable, nonExistingId, totalTime, false, failures);
                    totalRemovals += 2; // Counting both removal attempts
                }
            }

            // Calculate and write overall statistics to CSV
            double failureRate = (double) failures / totalRemovals * 100;
            writer.write("Overall Statistics,,Total Removals: " + totalRemovals + ",Failure Rate: " + failureRate + "%,\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeRemovalToCSV(BufferedWriter writer, myHashTable hashTable, int idToRemove, long totalTime,
                                          boolean isGenerated, int failures) throws IOException {
        long startTime = System.nanoTime();
        boolean success = hashTable.remove(idToRemove);
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1e6; // Convert nanoseconds to milliseconds
        totalTime += timeTaken;

        String failureStatus = success ? "No Failure" : "Failure";
        if (!success && isGenerated) failures++; // Increment failures if removal of generated ID failed
        writer.write("Remove," + idToRemove + "," + timeTaken + "," + failureStatus + "," + (isGenerated ? "Yes" : "No") + "\n");
    }
}

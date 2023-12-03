package comp352.assignment3.driver;

import comp352.assignment3.ADT.CleverSIDC.CleverSIDC;

import java.io.*;
import java.util.*;

public class TestSuite {

    public static int duplicateKeys = 0;
    public static int invalidKeys = 0;

    private static final int MAX_SIZE = 500000;
    //private static final String[] FILE_NAMES = {"Fall2023/comp352/assignment3/unique_keys.txt", "Fall2023/comp352/assignment3/duplicate_keys.txt"};

    //private static final String[] FILE_NAMES = {"Fall2023/comp352/assignment3/NASTA_test_file2.txt"};
    private static final String[] FILE_NAMES = {"Fall2023/comp352/assignment3/Datasets/NASTA_test_file1.txt"};
    private static final String CSV_FILE = "test_results.csv";
    private static List<String> results = new ArrayList<>();

    public static void main(String[] args) {
        for (String fileName : FILE_NAMES) {
            int size = getFileSize(fileName);
            CleverSIDC cleverSIDC = new CleverSIDC();
            cleverSIDC.SetSIDCThreshold(cleverSIDC, size);
            testFileOperations(cleverSIDC, fileName, size);
            results.add("\n"); // Separate results for each file
//            duplicateKeys = 0;
//            invalidKeys = 0;
        }

        // Export results to CSV
        exportToCSV();
    }

    private static int getFileSize(String fileName) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.readLine() != null && count < MAX_SIZE) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private static void testFileOperations(CleverSIDC adt, String fileName, int size) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < size) {
                int key = Integer.parseInt(line.trim());
                performOperations(adt, key);
                count++;
            }
            // Add summary for the file
            results.add("Summary for " + fileName + ",Total Operations: " + count + "duplicate keys %:" + (double) ((double) adt.getInternal_adt().getDuplicateKeys() / (double) size) * 100 + " with % of invalid keys: " + ((double) adt.getInternal_adt().getInvalidKeys() / (double) size) * 100 + "with collision count: " + adt.getInternal_adt().getCollisionCounter()+ "\n");
            System.out.println("Collisions:" + adt.getInternal_adt().getCollisionCounter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void performOperations(CleverSIDC adt, int key) {
        // Add operation
        long startTime = System.nanoTime();
        boolean addSuccess = adt.add(adt, key, "Value_" + key);
        long endTime = System.nanoTime();
        results.add("Add," + key + "," + (endTime - startTime) + "," + addSuccess);

        // GetValues operation
        startTime = System.nanoTime();
        String value = adt.getValues(adt, key);
        endTime = System.nanoTime();
        boolean getValueSuccess = value != null && value.equals("Value_" + key);
        results.add("GetValues," + key + "," + (endTime - startTime) + "," + getValueSuccess);

        // Remove operation
        startTime = System.nanoTime();
        boolean removeSuccess = adt.remove(adt, key);
        endTime = System.nanoTime();
        results.add("Remove," + key + "," + (endTime - startTime) + "," + removeSuccess);

        // NextKey operation
        startTime = System.nanoTime();
        int nextKey = adt.nextKey(adt, key);
        endTime = System.nanoTime();
        boolean nextKeySuccess = nextKey != -1;
        results.add("NextKey," + key + "," + (endTime - startTime) + "," + nextKeySuccess);

        // PrevKey operation
        startTime = System.nanoTime();
        int prevKey = adt.prevKey(adt, key);
        endTime = System.nanoTime();
        boolean prevKeySuccess = prevKey != -1;
        results.add("PrevKey," + key + "," + (endTime - startTime) + "," + prevKeySuccess);

        // RangeKey operation - requires a second key, for simplicity using key+1
        startTime = System.nanoTime();
        int range = adt.rangeKey(key, key + 1);
        endTime = System.nanoTime();
        boolean rangeKeySuccess = range != -1;
        results.add("RangeKey," + key + "," + (endTime - startTime) + "," + rangeKeySuccess);



    }

    private static void exportToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            writer.write("Operation,Key,Time (ns),Success\n");
            for (String result : results) {
                writer.write(result + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

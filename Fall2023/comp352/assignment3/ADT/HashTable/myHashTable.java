package comp352.assignment3.ADT.HashTable;

import java.util.Random;

public class myHashTable {
    private class Student {
        private int key;
        private String value;

        private boolean isDeleted;

        public Student(int key, String value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void delete() {
            this.key = -1;
            this.isDeleted = true;
        }

        public void setKey(int key) {
            this.key = key;
        }


        @Override
        public int hashCode() {
            return generateHashCode(0, this.key, 1);
        }

        private int generateHashCode(int hashcode, int value, int i) {
            //prime number : 31
            //polynomial accumulation
            int a = 31;
            if (value == 0) {
                return hashcode;
            } else {
                return generateHashCode(hashcode + (int) Math.pow(value % 10, i), value / 10, i+1);
            }
        }

    }

    private Student[] bucketArray;

    private final int primeLimit = 1000003;

    private double theoreticalMaxLoadFactor = -1;

    private int keySize = 0;

    //private int compressionCounterIndex = 0;

    private int userSize;

    private int logicalSize;

    private int[] insertionOrder;
    private int orderSize = 0;

    public myHashTable(int size) {
        if (size < 100 || size > 500000) {
            System.out.print("Error, incorrect size.");
            System.exit(0);
        } else {
            this.userSize = size;
            this.logicalSize = findNextPrime(sieveOfEratosthenes(primeLimit), 2 * size);
            this.theoreticalMaxLoadFactor = (double) this.userSize / this.logicalSize;
            this.bucketArray = new Student[logicalSize];
            System.out.println("Load factor is=" + theoreticalMaxLoadFactor);
            System.out.println("Logical Size = " + logicalSize);
            insertionOrder = new int[size];
        }
    }

    //O(nlog(logn))
    private static boolean[] sieveOfEratosthenes(int limit) {
        boolean[] primes = new boolean[limit + 1];
        for (int i = 2; i <= limit; i++) {
            primes[i] = true;
        }

        for (int p = 2; p * p <= limit; p++) {
            if (primes[p]) {
                for (int i = p * p; i <= limit; i += p) {
                    primes[i] = false;
                }
            }
        }
        return primes;
    }


    public static int findNextPrime(boolean[] primes, int N) {
        for (int i = N + 1; i < primes.length; i++) {
            if (primes[i]) {
                return i;
            }
        }
        return -1; // If no prime number is found (which is unlikely for reasonable N)
    }

    private int compressionFunction(int hashcode, int i) {
        return (hashcode + i + 3 * i * i) % this.logicalSize;
    }

    public double getActualLoadFactor(){
        return (double) this.keySize / this.userSize;
    }

    public void put(int key, String val) {
        if (this.keySize >= this.userSize) {
            System.out.println("Error: Hash table is full");
            return; // Exit if the hash table is full
        }

        Student entry = new Student(key, val);
        int exponent = 0;
        int hashcode = entry.hashCode();
        int assumedIndex = compressionFunction(hashcode, exponent);
        int actualIndex = searchViaQuadraticProbing(key, assumedIndex, exponent, hashcode);

        // Additional check to see if the key already exists and isn't marked as deleted
        if (this.bucketArray[actualIndex] != null && !this.bucketArray[actualIndex].isDeleted()) {
            System.out.println("Error: Duplicate key");
            return; // Exit if the key already exists
        }

        this.bucketArray[actualIndex] = entry;
        insertionOrder[orderSize++] = key;
        this.keySize++;
    }


    private int searchViaQuadraticProbing(int key, int index, int exponent, int hashcode) {
        while (this.bucketArray[index] != null && !(this.bucketArray[index].getKey() == key) && !this.bucketArray[index].isDeleted()) {
            exponent++;
            index = compressionFunction(hashcode, exponent);
        }
        return index;
    }

    public boolean remove(int studentID) {
        // Removing the student from the bucketArray
        Student searchProbe = new Student(studentID, "StudentSearcher");
        int hashcode = searchProbe.hashCode();
        int index = compressionFunction(hashcode, 0);
        int exponent = 0;

        while (this.bucketArray[index] != null && !(this.bucketArray[index].getKey() == studentID)) {
            exponent++;
            index = compressionFunction(hashcode, exponent);
            if (index >= this.bucketArray.length) {
                System.out.println("Error: ID not found");
                return false; // End of table reached, ID not found
            }
        }

        if (this.bucketArray[index] != null && this.bucketArray[index].getKey() == studentID) {
            this.bucketArray[index].delete(); // Mark as deleted
            this.keySize--;
        } else {
            System.out.println("Error: ID not found");
            return false; // ID not found in the bucketArray
        }

        // Removing the key from insertionOrder
        for (int i = 0; i < orderSize; i++) {
            if (insertionOrder[i] == studentID) {
                // Shift elements to the left to fill the gap
                for (int j = i; j < orderSize - 1; j++) {
                    insertionOrder[j] = insertionOrder[j + 1];
                }
                orderSize--;
                break;
            }
        }
        return true;
    }


    public int getKeySize(){
        return this.keySize;
    }

    public int[] radixSortKeys() {
        int[] keys = new int[this.keySize];
        int k = 0;
        for (Student student : bucketArray) {
            if (student != null && !student.isDeleted()) {
                keys[k++] = student.getKey();
            }
        }

        int n = keys.length;
        int maxDigits = 8; // Since keys are 8 digits long

        for (int exp = 1; maxDigits-- > 0; exp *= 10) {
            int[] output = new int[n]; // output array
            int[] count = new int[10];

            // Count frequencies
            for (int i = 0; i < n; i++) {
                count[(keys[i] / exp) % 10]++;
            }

            // Cumulative count
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }

            // Build the output array
            for (int i = n - 1; i >= 0; i--) {
                output[count[(keys[i] / exp) % 10] - 1] = keys[i];
                count[(keys[i] / exp) % 10]--;
            }

            // Copy the output array to keys, so that keys now
            // contains sorted numbers according to current digit
            for (int i = 0; i < n; i++) {
                keys[i] = output[i];
            }
        }

        return keys;
    }

    // Method to generate a random 8-digit non-existing key
    public int generate() {
        Random rand = new Random();
        int newKey;
        do {
            newKey = 10000000 + rand.nextInt(90000000); // Generate a random 8-digit number
        } while (keyExists(newKey));
        return newKey;
    }

    // Helper method to check if a key exists
    public boolean keyExists(int key) {
        for (Student student : bucketArray) {
            if (student != null && !student.isDeleted() && student.getKey() == key) {
                return true;
            }
        }
        return false;
    }

    // Method to get all values
    public String[] getValues() {
        String[] values = new String[this.keySize];
        int index = 0;
        for (Student student : bucketArray) {
            if (student != null && !student.isDeleted()) {
                values[index++] = student.getValue();
            }
        }
        return values;
    }

    public int prevKey(int key) {
        for (int i = 0; i < orderSize; i++) {
            if (insertionOrder[i] == key) {
                // Check if it's the first element
                if (i == 0) {
                    return -1; // No predecessor
                }
                return insertionOrder[i - 1];
            }
        }
        return -1; // Key not found
    }

    public int nextKey(int key) {
        for (int i = 0; i < orderSize; i++) {
            if (insertionOrder[i] == key) {
                // Check if it's the last element
                if (i == orderSize - 1) {
                    return -1; // No successor
                }
                return insertionOrder[i + 1];
            }
        }
        return -1; // Key not found
    }

    // Method to count the number of keys within a range
    public int rangeKey(int k1, int k2) {
        int count = 0;
        for (Student student : bucketArray) {
            if (student != null && !student.isDeleted()) {
                int key = student.getKey();
                if (key >= k1 && key <= k2) {
                    count++;
                }
            }
        }
        return count;
    }

    //for testing only.
    public String getBucketArrayString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bucketArray.length; i++) {
            if (bucketArray[i] != null) {
                sb.append("[").append(i).append("]: ")
                        .append(bucketArray[i].isDeleted() ? "Deleted" : bucketArray[i].getValue())
                        .append("\n");
            }
        }
        return sb.toString();
    }





}

package comp352.assignment3.ADT.HashTable;

import java.io.File;

public class myHashTable {
    private class Student{
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

        public void delete(){
            this.key = -1;
            this.isDeleted = true;
        }

        public void setKey(int key) {
            this.key = key;
        }
    }

    private Student[] bucketArray;

    private final int primeLimit = 1000003;

    private double loadFactor = -1;

    //private int compressionCounterIndex = 0;

    private int userSize;

    private int logicalSize;

    public myHashTable(int size){
        if(size < 100 || size > 500000){
            System.out.print("Error, incorrect size.");
            System.exit(0);
        }else{
            this.userSize = size;
            this.logicalSize = findNextPrime(sieveOfEratosthenes(primeLimit),2*size);
            this.loadFactor = (double) this.userSize / this.logicalSize;
            this.bucketArray = new Student[logicalSize];
            System.out.println("Load factor is=" + loadFactor);
            System.out.println("Logical Size = " + logicalSize);
        }
    }

    //O(nlog(logn))
    public static boolean[] sieveOfEratosthenes(int limit) {
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

    private int generateHashCode(Student s){
        return s.hashCode();
    }

//    private int compressionFunction(int hashcode){
//        int hOp =
//
//        return Math.pow(compressionCounterIndex++,2);
//    }


}

package comp352.assignment3.ADT.CleverSIDC;

import comp352.assignment3.ADT.HashTable.myHashTable;

public class CleverSIDC {

    private myHashTable internal_adt;
    public boolean isInitialized = false;


    public boolean SetSIDCThreshold(CleverSIDC adt, int size) {
        if (!this.isInitialized) {
            this.internal_adt = new myHashTable(size);
            this.isInitialized = true;
            return true;
        } else {
            System.out.println("Error, fixed size has been already allocated. Please create a new SIDC.");
            return false;
        }

    }

    public int generate(CleverSIDC adt) {
        if (this.isInitialized) {
            return adt.getInternal_adt().generate();
        } else {
            System.out.println("Error, fixed size has not been allocated. Please allocate a fixed size.");
            return -1;
        }

    }

    public int[] allKeys() {
        if (this.isInitialized) {
            return this.getInternal_adt().radixSortKeys();
        } else {
            System.out.println("Error, fixed size has not been allocated. Please allocate a fixed size.");
            return null;
        }
    }

    public boolean add(CleverSIDC adt, int key, String value) {
        if (this.isInitialized) {
            return adt.getInternal_adt().put(key, value);
        } else {
            System.out.println("Error, fixed size has not been allocated. Please allocate a fixed size.");
            return false;
        }

    }

    public boolean remove(CleverSIDC adt, int key) {
        if (this.isInitialized) {
            return adt.getInternal_adt().remove(key);
        } else {
            System.out.println("Error, fixed size has not been allocated. Please allocate a fixed size.");
            return false;
        }

    }

    public String getValues(CleverSIDC adt, int key) {
        if (this.isInitialized) {
            return adt.getInternal_adt().get(key);
        } else {
            System.out.println("Error, fixed size has not been allocated. Please allocate a fixed size.");
            return null;
        }

    }

    public int nextKey(CleverSIDC adt, int key) {
        if (this.isInitialized) {
            return adt.getInternal_adt().nextKey(key);
        } else {
            System.out.println("Error, fixed size has not been allocated. Please allocate a fixed size.");
            return -1;
        }
    }

    public int prevKey(CleverSIDC adt, int key) {
        if (this.isInitialized) {
            return adt.getInternal_adt().prevKey(key);
        } else {
            System.out.println("Error, fixed size has not been allocated. Please allocate a fixed size.");
            return -1;
        }
    }

    public int rangeKey(int key1, int key2) {
        if (this.isInitialized) {
            return this.getInternal_adt().rangeKey(key1, key2);
        } else {
            System.out.println("Error, fixed size has not been allocated. Please allocate a fixed size.");
            return -1;
        }
    }

    public myHashTable getInternal_adt() {
        return internal_adt;
    }
}

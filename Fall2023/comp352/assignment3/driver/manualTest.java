package comp352.assignment3.driver;

import comp352.assignment3.ADT.CleverSIDC.CleverSIDC;

public class manualTest {
    public static void main(String[] args) {
        CleverSIDC cleverSIDC = new CleverSIDC();

        cleverSIDC.SetSIDCThreshold(cleverSIDC, 500);
        cleverSIDC.add(cleverSIDC, 12345678, "Value1");
        cleverSIDC.add(cleverSIDC, 12345678, "Value444");

        System.out.println(cleverSIDC.getInternal_adt().getCollisionCounter());

//        cleverSIDC.add(cleverSIDC, 87654321, "Value2");
//
//        cleverSIDC.getValues(cleverSIDC, 12345678);
//        System.out.println(cleverSIDC.getValues(cleverSIDC, 12345678));
//        System.out.println(cleverSIDC.remove(cleverSIDC, 12345678));
//        System.out.println(cleverSIDC.getValues(cleverSIDC, 12345678));

    }
}

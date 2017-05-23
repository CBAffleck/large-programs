/**
 * Created by CampbellAffleck on 5/23/17.
 */

/*
    Tests methods in GameRules.java
 */
public class gameRulesTest extends GameRules{

    public static void main(String[] args) {
        checkMineGenerator();
    }

    /**
     *  Checks whether mineGenerator is working and prints the result.
     *
     *  @param  expected    Expected int max
     *  @param  actual      Int received
     *  @param  label       Label for the 'test' case
     */
    private static void checkLessThan(int actual, int expected, String label) {
        if (actual <= expected) {
            System.out.println("PASS: " + label + ": Expected less than " + expected + " and you gave " + actual);
        } else {
            System.out.println("FAIL: " + label + ": Expected less than " + expected + " and you gave " + actual);
        }
    }

    private static void checkMineGenerator() {
        System.out.println("Checking mine generator...");
        int colSize = 8;
        int rowSize = 8;

        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");
        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");
        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");
        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");
        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");

    }

}

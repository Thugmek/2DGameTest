package runners;

import util.Randomizers;

public class RandomizersTests {
    public static void main(String[] args) {

        for(int n = 0;n<100;n++) {
            float avg = 0;

            for (int i = 0; i < 32; i++) {
                avg += Randomizers.getByPos(i, 20, n);
                //System.out.println(Randomizers.getByPos(i,20,3));
            }

            System.out.println("avg: " + (avg / 32));
        }
    }
}

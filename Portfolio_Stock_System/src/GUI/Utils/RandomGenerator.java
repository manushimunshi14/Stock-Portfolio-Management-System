package GUI.Utils;

import java.util.Random;

public class RandomGenerator {
    private Random random;

    public RandomGenerator(){
        this.random = new Random();
    }

    public double generateRandom(double currPrice, double minPercent, double maxPercent){
        double percent = minPercent + (random.nextDouble() * (maxPercent-minPercent));
        boolean positive = Math.random() < 0.65;
        return (positive ? 1 : -1) * currPrice * percent;
    }
}

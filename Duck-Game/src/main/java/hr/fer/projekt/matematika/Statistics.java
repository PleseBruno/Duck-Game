package hr.fer.projekt.matematika;

import java.util.LinkedList;

public class Statistics {

    public static double getStdDev(LinkedList<Double> lastAvgFitnessesList) {
        double mean = lastAvgFitnessesList.stream().mapToDouble(Double::doubleValue).average().orElseThrow();

        return Math.sqrt(lastAvgFitnessesList
                                        .stream()
                                        .mapToDouble(d ->
                                                Math.pow(d - mean, 2))
                                        .sum());
    }
}

package hr.fer.projekt.genetskiAlgoritam;

import hr.fer.projekt.matematika.Matrix;
import hr.fer.projekt.neuronskaMreza.NeuralNetwork;

import java.util.*;

public class GeneticAlgorithms {

    private static final Random rand = new Random();

    public static Map<NeuralNetwork, Double> makeNewGen(Map<NeuralNetwork, Double> oldGeneration, GeneticType geneticType, int gen, int numNeuralNetworks, double alpha, double mutationChance) {
        Map<NeuralNetwork, Double> newGeneration = new HashMap<NeuralNetwork, Double>();

        switch (geneticType) {
            case DEFAULT:
                newGeneration = makeBabies(oldGeneration, gen, numNeuralNetworks, alpha, mutationChance);
                break;
            default:
                throw new IllegalArgumentException("Genetic type not supported");
        }
        return newGeneration;
    }


    private static Map<NeuralNetwork, Double> makeBabies(Map<NeuralNetwork, Double> oldGeneration, int gen, int numNeuralNetworks, double alpha, double mutationChance) {
        Map<NeuralNetwork, Double> newGeneration = new HashMap<NeuralNetwork, Double>();

        //racuna ukupni fitness
        double sum = oldGeneration.values().stream().mapToDouble(Double::doubleValue).sum();

        for (int i = 0; i < numNeuralNetworks; i++) {
            double idxPrviClan = new Random().nextDouble(0, sum);
            double idxDrugiClan = new Random().nextDouble(0, sum);

            NeuralNetwork prviClan;
            NeuralNetwork drugiClan;

            double floor = 0;
            prviClan = findRandomClan(oldGeneration, idxPrviClan, floor);
            floor = 0;
            drugiClan = findRandomClan(oldGeneration, idxDrugiClan, floor);

            newGeneration.put(createChild(prviClan, drugiClan, gen, i + 1, alpha, mutationChance), null);
        }

        return newGeneration;
    }

    private static NeuralNetwork findRandomClan(Map<NeuralNetwork, Double> oldGeneration, double idxPrviClan, double floor) {
        for (Map.Entry<NeuralNetwork, Double> entry : oldGeneration.entrySet()) {
            double value = entry.getValue();

            if (idxPrviClan < value + floor && idxPrviClan >= floor) {
                return entry.getKey();
            } else {
                floor += value;
            }
        }
        throw new RuntimeException("Clan not found - Linija:78");
    }

    private static NeuralNetwork createChild(NeuralNetwork prviClan, NeuralNetwork drugiClan, int gen, int id, double alpha, double mutationChance) {
        NeuralNetwork child = prviClan.copy();
        child.setID("NN-" + gen + "." + id);

        List<Matrix> childWeight = prviClan.getWeights();
        List<Matrix> drugiClanWeights = drugiClan.getWeights();

        List<Matrix> childBias = prviClan.getBiases();
        List<Matrix> drugiClanBiases = drugiClan.getBiases();

        Iterator<Matrix> iteratorPrvogClanaWeights = childWeight.iterator();
        Iterator<Matrix> iteratorPrvogClanaBiases = childBias.iterator();

        Iterator<Matrix> iteratorDrugogClanaWeights = drugiClanWeights.iterator();
        Iterator<Matrix> iteratorDrugogClanaBiases = drugiClanBiases.iterator();

        generateNewValues(childWeight, iteratorPrvogClanaWeights, iteratorDrugogClanaWeights, alpha, mutationChance);
        generateNewValues(childBias, iteratorPrvogClanaBiases, iteratorDrugogClanaBiases, alpha, mutationChance);

        return child;
    }

    private static void generateNewValues(List<Matrix> childBias, Iterator<Matrix> iteratorPrvogClanaBiases, Iterator<Matrix> iteratorDrugogClanaBiases, double alpha, double mutationChance) {
        for (Matrix w1 : childBias) {
            Matrix w2 = iteratorPrvogClanaBiases.next();
            Matrix w3 = iteratorDrugogClanaBiases.next();

            double[] arrPrvi = w2.toArray();
            double[] arrDrugi = w3.toArray();
            for (int i = 0; i < w1.rows; i++) {
                for (int j = 0; j < w1.cols; j++) {
                    w1.data[i][j] = BLX_ALPHA(arrPrvi[i * w1.cols + j], arrDrugi[i * w1.cols + j], alpha);
                }
            }

            for (int i = 0; i < w1.rows; i++) {
                for (int j = 0; j < w1.cols; j++) {
                    w1.data[i][j] = rand.nextDouble(0, 1) < mutationChance ? mutate() : w1.data[i][j];
                }
            }
        }
    }

    private static double BLX_ALPHA(double w1, double w2, double alpha) {
        double length = Math.abs(w2 - w1);
        if (length == 0) {
            length = 0.01;
        }
        if (w1 >= w2) {
            return new Random().nextDouble(w2 - alpha * length < -1 ? -1 : w2 - alpha * length, w1 + alpha * length > 1 ? 1 : w1 + alpha * length);
        } else {
            return new Random().nextDouble(w1 - alpha * length < -1 ? -1 : w1 - alpha * length, w2 + alpha * length > 1 ? 1 : w2 + alpha * length);
        }
    }

    private static double mutate () {
        //TODO: dodati mogucnost za vise razlicitih vrsta mutacije
        return rand.nextDouble(-1, 1);
    }
}

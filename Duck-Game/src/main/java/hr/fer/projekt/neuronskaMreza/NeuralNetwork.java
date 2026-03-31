package hr.fer.projekt.neuronskaMreza;


import hr.fer.projekt.matematika.Matrix;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class NeuralNetwork {
    private String ID;
    private final int inputNodes;
    private final int outputNodes;
    private final int[] hiddenLayers; // npr. [8, 6, 4]
    private List<Matrix> weights; // lista matrica tezina
    private List<Matrix> biases; // lista w0 tezina

    // Konstruktor za kreiranje nove neuronske mreze sa zadanim brojem ulaznih, skrivenih i izlaznih cvorova i nasumicnim inicijaliziranjem tezina
    public NeuralNetwork(String id, int inputNodes, int[] hiddenLayers, int outputNodes) {
        this.ID = id;
        this.inputNodes = inputNodes;
        this.outputNodes = outputNodes;
        this.hiddenLayers = hiddenLayers.clone();

        weights = new ArrayList<>();
        biases = new ArrayList<>();

        // Broj slojeva = input + hidden + output
        int prevNodes = inputNodes;
        for (int hiddenNodes : hiddenLayers) {
            weights.add(new Matrix(hiddenNodes, prevNodes));
            biases.add(new Matrix(hiddenNodes, 1));
            prevNodes = hiddenNodes;
        }

        // Zadnji sloj (output)
        weights.add(new Matrix(outputNodes, prevNodes));
        biases.add(new Matrix(outputNodes, 1));

        // Inicijalizacija nasumično
        randomizeParameters();
    }

    // Konstruktor za harcodanje cijele neuronske mreze procitane u txt fileu
    public NeuralNetwork(String id, int inputNodes, int[] hiddenLayers, int outputNodes, List<Matrix> weights, List<Matrix> biases) {
        this.ID = id;
        this.inputNodes = inputNodes;
        this.outputNodes = outputNodes;
        this.hiddenLayers = hiddenLayers.clone();

        this.weights = new ArrayList<>();
        for (Matrix w : weights) {
            this.weights.add(Matrix.copy(w));
        }

        this.biases = new ArrayList<>();
        for (Matrix b : biases) {
            this.biases.add(Matrix.copy(b));
        }
    }

    public void randomizeParameters() {
        for (Matrix w : weights) w.randomize();
        for (Matrix b : biases) b.randomize();
    }

    // Izracun izlaza mreze za dani ulazni niz podataka
    public double[] generateOutput(double[] inputArray) {
        Matrix current = Matrix.fromArray(inputArray);

        // Prolazak kroz sve slojeve
        for (int i = 0; i < weights.size(); i++) {
            Matrix w = weights.get(i);
            Matrix b = biases.get(i);

            // izracun net koeficijenata sloja ( wx + b )
            Matrix layer = Matrix.multiply(w, current);
            layer.add(b);
            layer.map(this::sigmoid);
            current = layer;
        }

        return current.toArray();
    }

    // Mogućnost kloniranja
    public NeuralNetwork copy() {
        NeuralNetwork clone = new NeuralNetwork(ID, inputNodes, hiddenLayers, outputNodes);
        for (int i = 0; i < weights.size(); i++) {
            clone.weights.set(i, Matrix.copy(weights.get(i)));
            clone.biases.set(i, Matrix.copy(biases.get(i)));
        }
        return clone;
    }

    // sigmuoidna funkcija za racunanje net koeficijenata
    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public void print() {
        System.out.println("Network structure for [" + this.ID + "]:");
        System.out.print("Input: " + inputNodes + " -> ");
        for (int h : hiddenLayers) System.out.print(h + " -> ");
        System.out.println("Output: " + outputNodes);
        System.out.println("Total layers: " + (hiddenLayers.length + 2));
        System.out.println("Weight matrices: ");
        for (Matrix w : weights) {
            for (int i = 0; i < w.rows; i++) {
                for (int j = 0; j < w.cols; j++) {
                    System.out.printf("%.4f ", w.data[i][j]);
                }
                System.out.println();
            }
        }
        System.out.println("Bias matrices: ");
        for (Matrix b : biases) {
            System.out.print("[ ");
            for (int i = 0; i < b.rows; i++) {
                for (int j = 0; j < b.cols; j++) {
                    System.out.printf("%.4f ", b.data[i][j]);
                }
            }
            System.out.println("]");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.ID).append("\n");
        sb.append(hiddenLayers.length).append("\n");
        sb.append(inputNodes).append("\n");
        for (int h : hiddenLayers) sb.append(h).append("\n");
        sb.append(outputNodes).append("\n");
        for (Matrix w : weights) {
            for (int i = 0; i < w.rows; i++) {
                for (int j = 0; j < w.cols; j++) {
                    sb.append(w.data[i][j]).append(" ");
                }
                sb.append("\n");
            }
        }
        for (Matrix b : biases) {
            for (int i = 0; i < b.rows; i++) {
                for (int j = 0; j < b.cols; j++) {
                    sb.append(b.data[i][j]).append(" ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static NeuralNetwork loadNeuralNetworkFromFile(String filename) throws Exception {
        try (Scanner input = new Scanner(Files.newBufferedReader(Paths.get(filename)))) {
            input.useLocale(Locale.US);

            // Read first non-empty line as ID
            String id = "";
            while (input.hasNextLine()) {
                id = input.nextLine().trim();
                if (!id.isEmpty()) break;
            }

            if (id.isEmpty()) throw new IllegalArgumentException("Empty network id in file: " + filename);

            int numHiddenLayers = input.nextInt();
            int inputNodes = input.nextInt();

            int[] hiddenLayers = new int[numHiddenLayers];
            for (int i = 0; i < numHiddenLayers; i++) {
                hiddenLayers[i] = input.nextInt();
            }
            int outputNodes = input.nextInt();

            List<Matrix> weights = new ArrayList<>();
            List<Matrix> biases = new ArrayList<>();

            int prevNodes = inputNodes;
            for (int hiddenNodes : hiddenLayers) {
                weights.add(new Matrix(hiddenNodes, prevNodes));
                biases.add(new Matrix(hiddenNodes, 1));
                prevNodes = hiddenNodes;
            }

            weights.add(new Matrix(outputNodes, prevNodes));
            biases.add(new Matrix(outputNodes, 1));

            // Fill weight matrices (row-major in file)
            for (Matrix w : weights) {
                for (int i = 0; i < w.rows * w.cols; i++) {
                    if (!input.hasNextDouble()) throw new IllegalStateException("Not enough weight values in file");
                    w.data[i / w.cols][i % w.cols] = input.nextDouble();
                }
            }

            // Fill bias matrices
            for (Matrix b : biases) {
                for (int i = 0; i < b.rows * b.cols; i++) {
                    if (!input.hasNextDouble()) throw new IllegalStateException("Not enough bias values in file");
                    b.data[i / b.cols][i % b.cols] = input.nextDouble();
                }
            }
            return new NeuralNetwork(id, inputNodes, hiddenLayers, outputNodes, weights, biases);
        }
    }

    public String getID() {return ID;}

    public void setID(String ID) {this.ID = ID;}

    public List<Matrix> getWeights() {
        return weights;
    }

    public void setWeights(List<Matrix> weights) {
        this.weights = weights;
    }

    public List<Matrix> getBiases() {
        return biases;
    }

    public void setBiases(List<Matrix> biases) {
        this.biases = biases;
    }

    public int getInputNodes() {
        return inputNodes;
    }

    public int getOutputNodes() {
        return outputNodes;
    }

    public int[] getHiddenLayers() {
        return hiddenLayers;
    }
}
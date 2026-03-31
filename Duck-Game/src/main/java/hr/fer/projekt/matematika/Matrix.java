package hr.fer.projekt.matematika;

// Matrix.java
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

public class Matrix {
    public int rows, cols;
    public double[][] data;

    // Konstruktor za inicijalizaciju matrice s danim redovima i stupcima
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
    }

    // Kreira matricu sa zadanim vrijednostima Arraya - kreira matricu dimenzija (rows*cols)
    public void fill(double... integers) {
        if (integers.length != rows * cols) {
            throw new IllegalArgumentException("Number of integers does not fit in matrix");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = integers[i * cols + j];
            }
        }
    }

    // Stvara matricu iz jednodimenzionalnog niza (vektora) i rezultatna matrica je dimenzija (n x 1)
    public static Matrix fromArray(double[] arr) {
        Matrix m = new Matrix(arr.length, 1);
        for (int i = 0; i < arr.length; i++) m.data[i][0] = arr[i];
        return m;
    }

    // Pretvara matricu u jedanodimenzionalni array (vektor)
    public double[] toArray() {
        double[] arr = new double[rows * cols];
        int idx = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                arr[idx++] = data[i][j];
        return arr;
    }

    // Popunjava matricu nasumičnim vrijednostima između 0 i 1
    public void randomize() {
        Random rand = new Random(UUID.randomUUID().hashCode());
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] = rand.nextDouble(-1,1); // [0,1]
    }

    // Dodaje drugu matricu trenutnoj matrici tako da su dimenzije jednake i zbraja elemente na jednakim pozicijama
    public void add(Matrix m) {
        if (rows != m.rows || cols != m.cols) throw new RuntimeException("Dimensions must match for add.");
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] += m.data[i][j];
    }

    // Dodaje skalar trenutnoj matrici
    public void add(double n) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] += n;
    }

    // Oduzima dvije matrice i vraća novu matricu kao rezultat
    public static Matrix subtract(Matrix a, Matrix b) {
        if (a.rows != b.rows || a.cols != b.cols) throw new RuntimeException("Dimensions must match for subtract.");
        Matrix result = new Matrix(a.rows, a.cols);
        for (int i = 0; i < a.rows; i++)
            for (int j = 0; j < a.cols; j++)
                result.data[i][j] = a.data[i][j] - b.data[i][j];
        return result;
    }

    //transponira
    public static Matrix transpose(Matrix m) {
        Matrix result = new Matrix(m.cols, m.rows);
        for (int i = 0; i < m.rows; i++)
            for (int j = 0; j < m.cols; j++)
                result.data[j][i] = m.data[i][j];
        return result;
    }

    // Množi dvije matrice i vraća novu matricu kao rezultat
    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.cols != b.rows) throw new RuntimeException("Cols of A must match rows of B for multiply.");
        Matrix result = new Matrix(a.rows, b.cols);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                double sum = 0;
                for (int k = 0; k < a.cols; k++) {
                    sum += a.data[i][k] * b.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    // Množi trenutnu matricu skalarom
    public void multiply(double n) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] *= n;
    }

    // Primjenjuje funkciju na svaki element matrice
    public void map(Function<Double, Double> func) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] = func.apply(data[i][j]);
    }

    // Stvara identicnu kopiju matrice
    public static Matrix copy(Matrix m) {
        Matrix result = new Matrix(m.rows, m.cols);
        for (int i = 0; i < m.rows; i++)
            for (int j = 0; j < m.cols; j++)
                result.data[i][j] = m.data[i][j];
        return result;
    }

    // Debug print
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                System.out.printf("%10.4f ", data[i][j]);
            System.out.println();
        }
        System.out.println();
    }
}

package hr.fer.projekt.graphing;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FitnessGrapher extends JFrame {

    public FitnessGrapher(String title, XYSeriesCollection dataset) {
        super(title);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Fitness over Generations",
                "Generation",
                "Fitness Value",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Customize chart look
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);
    }

    public static void main(String[] args) {
        String filePath = "fitness_data.txt"; // Ensure this matches your filename
        XYSeries avgSeries = new XYSeries("Average Fitness");
        XYSeries eliteSeries = new XYSeries("Elite Fitness");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Integer currentGen = null;

            // Regex patterns to capture the numbers
            Pattern genPattern = Pattern.compile("Generation:\\s*(\\d+)");
            Pattern avgPattern = Pattern.compile("Average fitness this generation:\\s*([\\d,.]+)");
            Pattern elitePattern = Pattern.compile("Elite Fitness:\\s*([\\d,.]+)");

            while ((line = br.readLine()) != null) {
                Matcher mGen = genPattern.matcher(line);
                Matcher mAvg = avgPattern.matcher(line);
                Matcher mElite = elitePattern.matcher(line);

                if (mGen.find()) {
                    currentGen = Integer.parseInt(mGen.group(1));
                } else if (mAvg.find() && currentGen != null) {
                    // Replace comma with dot to ensure Double.parseDouble works
                    double val = Double.parseDouble(mAvg.group(1).replace(",", "."));
                    avgSeries.add((double) currentGen, val);
                } else if (mElite.find() && currentGen != null) {
                    double val = Double.parseDouble(mElite.group(1).replace(",", "."));
                    eliteSeries.add((double) currentGen, val);
                }
            }

            // Prepare dataset
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(avgSeries);
            dataset.addSeries(eliteSeries);

            // Launch UI
            SwingUtilities.invokeLater(() -> {
                FitnessGrapher example = new FitnessGrapher("Evolution Statistics", dataset);
                example.setSize(800, 600);
                example.setLocationRelativeTo(null);
                example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                example.setVisible(true);
            });

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric data: " + e.getMessage());
        }
    }
}
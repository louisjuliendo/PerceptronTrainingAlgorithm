package is;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class Perceptron {

    private static int epoch = 1;
    private static int epochCounter = 1;
    private static int prediction = 0;
    private static int num_of_features = 124;
    private static int num_of_data = 1605;
    private static int training_set = num_of_data * 60 / 100;
    private static int evaluation_set = num_of_data * 30 / 100;
    private static int test_set = num_of_data * 10 / 100;
    private static double[] weights = new double[num_of_features];
    private static double[][] inputs = new double[num_of_data][num_of_features];
    private static int[] outputs = new int[num_of_data];
    private static double accuracy = 0;
    private static int totalSteps = 0;
    private static int correct = 0;

    private static DecimalFormat df = new DecimalFormat("##.000");
    private static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {

        String file = "C:/Users/louisjuliendo/Downloads/Semester 8/CC306 Intelligent System/DATASETS!/a1a.txt";
        parseFile(file);
        randomizeWeights(-10, 10);
        train(inputs, outputs, 50);
        System.out.println(sb.toString());

        String files = "C:/Users/louisjuliendo/Downloads/Semester 8/CC306 Intelligent System/DATASETS!/Test/file.txt";
        writeToFile(files);
    }

    public static void writeToFile(String files) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(files))) {
            writer.write(sb.toString());
            writer.close();
        }
    }

    public static void train(double[][] inputs, int[] outputs, int epoch) {
        for (int e = 1; e <= epoch; e++) {
            correct = 0;
        sb.append("\n ------------------------ Training ------------------------ \n");
        int counter = 1;
        for (int i = 0; i < training_set; i++) {
            sb.append("Epoch " + epochCounter + ", Step " + (counter));
            double dotProduct = 0;
            counter += 1;
            for (int j = 0; j < weights.length; j++) {
                if (j < weights.length - 1) {
                }
                dotProduct += weights[j] * inputs[i][j];
            }
            prediction = signum(dotProduct);
            sb.append("\n\t h(X" + (i + 1) + ") = " + Double.valueOf(df.format(prediction)).intValue() + " equals with y" + (i + 1) + " = " + outputs[i] + "\n");
            if (prediction != outputs[i]) {
                sb.append("\tUpdating weights: \n");
                updateWeights(i);
                sb.append("\tW = " + Arrays.toString(weights) + "\n\n");
            } else {
                correct += 1;
                sb.append("\t No update needed. \n");
                sb.append("\t W = " + Arrays.toString(weights) + "\n\n");
            }
        }
        totalSteps = training_set;
        accuracy = (double) correct / totalSteps * 100;
        sb.append("Training accuracy: " + accuracy + "\n");
        sb.append("correct: " + correct + "     totalSteps: " + totalSteps);
        epochCounter += 1;
        }
    }

    public static void updateWeights(int i) {
        for (int w = 0; w < weights.length; w++) {
            weights[w] = weights[w] + (outputs[i] * inputs[i][w]);
        }
    }
    static int x = 1;

    public static void evaluate(double[][] inputs, int[] outputs) {
        x += 1;
        correct = 0;
        int counter = 1;
        sb.append("\n ------------------------ Evaluation ------------------------ \n");
        sb.append(Arrays.toString(weights));
        for (int i = 0; i < evaluation_set; i++) {
            sb.append("\nStep " + (counter));
            double dotProduct = 0;
            for (int j = 0; j < weights.length; j++) {
                dotProduct += weights[j] * inputs[i][j];
            }
            prediction = signum((int) dotProduct);

            sb.append("\t h(X" + (i + 1) + ") = " + Double.valueOf(df.format(prediction)).intValue() + " equals with y" + (i + 1) + " = " + outputs[i] + "\n");

            if (prediction == outputs[i]) {
                correct += 1;
                sb.append("\t Correct. \n");
            } else {
                sb.append("\t\n");
            }
            counter++;
        }
        totalSteps = evaluation_set;
        accuracy = (double) correct / totalSteps * 100;
        sb.append("Evaluate accuracy: " + accuracy + "\n");
        while (accuracy < 50) {
            sb.append("Retrain in another epoch... \n");
            train(inputs, outputs, x);
            evaluate(inputs, outputs);
        }
    }

    public static void test(double[][] inputs, int[] outputs) {
        correct = 0;
        int counter = 1;
        sb.append("\n Testing \n");
        sb.append(Arrays.toString(weights));
        for (int i = 0; i < test_set; i++) {
            sb.append("\n Step " + (counter));
            double dotProduct = 0;
            for (int j = 0; j < weights.length; j++) {
                dotProduct += weights[j] * inputs[i][j];
            }
            prediction = signum((int) dotProduct);

            sb.append(" h(X" + (i + 1) + ") = " + Double.valueOf(df.format(prediction)).intValue() + " equals with y" + (i + 1) + " = " + outputs[i] + "\n");

            if (prediction == outputs[i]) {
                correct += 1;
                sb.append("\t Correct. \n");
            } else {
                sb.append("\t\n");
            }
            counter++;
        }
        totalSteps = test_set;
        accuracy = (double) correct / totalSteps * 100;
        sb.append("Final test accuracy: " + accuracy + "\n");
        System.out.println(sb);
    }

    public static void countFeatures(String line) {
        String[] inputPairs = line.split(" ");
        num_of_features = inputPairs.length;
    }

    public static void parseFile(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            double[] input = null;
            String[] inputPairs = null;
            int counter = 0;
            while (line != null) {
                double output = 0;
                input = new double[num_of_features];
                inputPairs = line.split("\\s+");
                output = Double.parseDouble(inputPairs[0]);
                for (int i = 1; i < inputPairs.length; i++) {
                    int components = Integer.parseInt(inputPairs[i].split(":")[0]);
                    double data = Double.parseDouble(inputPairs[i].split(":")[1]);
                    input[components] = data;
                }
                input[0] = 1;
                outputs[counter] = (int) output;
                inputs[counter] = input;
                counter++;
                line = br.readLine();
            }
        } 
        catch (Exception e) {
            System.out.println("Error: " + e.getStackTrace());
        }
    }

    public static int signum(double number) {
        if (number > 0) {
            return 1;
        }
        return -1;
    }

    public static void randomizeWeights(int min, int max) {
        int range = max - min;
        Random random = new Random();
        for (int i = 0; i < num_of_features; i++) {
            weights[i] = min + (range * random.nextDouble());
        }
    }

}

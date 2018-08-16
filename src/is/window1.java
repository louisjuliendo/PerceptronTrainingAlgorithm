package is;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class window1 extends javax.swing.JFrame {

    private static int epoch = 1;
    private static int epochCounter = 1;
    private static int prediction = 0;
    private static int num_of_features = 14;
    private static int num_of_data = 271;
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

    public window1() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTrain = new javax.swing.JButton();
        cbxDataset = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnTrain.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnTrain.setText("Train, Evaluate, & Test");
        btnTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrainActionPerformed(evt);
            }
        });

        cbxDataset.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "heart - 13 features 270 data", "ionosphere - 34 features 351 data", "german.nummer - 24 features 1000 data", "a1a - 123 features 1605 data", "svmguide1 - 4 features 3089 data", "w3a - 300 features 4912 data", "w4a - 300 features 7366 data", "duke breast cancer - 7129 features 44 data", "rcv1.binary - 47236 features 20242 data", "real-sim - 20958 features 72309 data", "criteo - 1000000 features 45840 data", " " }));
        cbxDataset.setToolTipText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11");
        cbxDataset.setFocusCycleRoot(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(btnTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(cbxDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(cbxDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(btnTrain)
                .addGap(34, 34, 34))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrainActionPerformed
        int cbxIndex = cbxDataset.getSelectedIndex();

        if (cbxIndex == 0) {
            System.out.println("heart");
            String file = "C:/Users/louisjuliendo/Downloads/Semester 8/CC306 Intelligent System/DATASETS!/heart.txt";
            parseFile(file);
            randomizeWeights(-10, 10);
            train(inputs, outputs, epoch);
            evaluate(inputs, outputs);
            if (accuracy > 50) {
                sb.append("Final evaluation accuracy: " + accuracy + "\n");
            }
            test(inputs, outputs);

            String files = "C:/Users/louisjuliendo/Downloads/Semester 8/CC306 Intelligent System/DATASETS!/Test/file.txt";
            try {
                writeToFile(files);
            } 
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Can't write file");
            }
        } else if (cbxIndex == 1) {
            System.out.println("ionosphere");
        } else if (cbxIndex == 2) {
            System.out.println("german.nummer");
        } else if (cbxIndex == 3) {
            System.out.println("a1a");
        } else if (cbxIndex == 4) {
            System.out.println("svmguide1");
        } else if (cbxIndex == 5) {
            System.out.println("w3a");
        } else if (cbxIndex == 6) {
            System.out.println("w4a");
        } else if (cbxIndex == 7) {
            System.out.println("duke breast cancer");
        } else if (cbxIndex == 8) {
            System.out.println("rcv1.binary");
        } else if (cbxIndex == 9) {
            System.out.println("real-sim");
        } else {
            System.out.println("criteo");
        }

    }

    public static void writeToFile(String files) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(files))) {
            writer.write(sb.toString());
            writer.close();
        }
    }

    public static void train(double[][] inputs, int[] outputs, int epoch) {
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
                sb.append("\t No update needed. \n");
                sb.append("\t W = " + Arrays.toString(weights) + "\n\n");
            }
        }
        epochCounter += 1;
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
        } catch (Exception e) {
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

    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(window1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(window1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(window1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(window1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new window1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTrain;
    private javax.swing.JComboBox<String> cbxDataset;
    // End of variables declaration//GEN-END:variables
}

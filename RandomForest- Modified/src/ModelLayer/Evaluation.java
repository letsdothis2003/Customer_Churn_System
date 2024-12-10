package ModelLayer;

import DataLayer.DataSet;
import java.util.List;

public class Evaluation {
    public static RandomForest forest;

    public static void main(String[] args) {
        // Load and prepare training data
    	DataSet trainingDataSet = new DataSet("C:\\Users\\Admin\\eclipse-workspace\\RandomForest\\src\\DataLayer\\train_data.csv");
        List<List<String>> trainingData = trainingDataSet.getData();
        double[][] X_train = new double[trainingData.size()][trainingData.get(0).size() - 1];
        int[] y_train = new int[trainingData.size()];

        for (int i = 0; i < trainingData.size(); i++) {
            List<String> row = trainingData.get(i);
            for (int j = 0; j < row.size() - 1; j++) {
                X_train[i][j] = Double.parseDouble(row.get(j));
            }
            y_train[i] = Integer.parseInt(row.get(row.size() - 1));
        }

        // Initialize and train the RandomForest
        forest = new RandomForest(10, 5, 2, 2);
        forest.fit(X_train, y_train);

        // Load and prepare test data
        DataSet testDataSet = new DataSet("C:\\Users\\Admin\\eclipse-workspace\\RandomForest\\src\\DataLayer\\test_data.csv");
        List<List<String>> testData = testDataSet.getData();
        double[][] X_test = new double[testData.size()][testData.get(0).size() - 1];
        int[] y_test = new int[testData.size()];

        for (int i = 0; i < testData.size(); i++) {
            List<String> row = testData.get(i);
            for (int j = 0; j < row.size() - 1; j++) {
                X_test[i][j] = Double.parseDouble(row.get(j));
            }
            y_test[i] = Integer.parseInt(row.get(row.size() - 1));
        }

        // Predict using the trained RandomForest model
        int[] predictions = forest.predict(X_test);

        // Calculate accuracy
        double accuracy = calculateAccuracy(y_test, predictions);
        System.out.println("Test Accuracy: " + accuracy);
    }

    private static double calculateAccuracy(int[] actualLabels, int[] predictedLabels) {
        int correctCount = 0;
        for (int i = 0; i < actualLabels.length; i++) {
            if (actualLabels[i] == predictedLabels[i]) {
                correctCount++;
            }
        }
        return (double) correctCount / actualLabels.length;
    }
}

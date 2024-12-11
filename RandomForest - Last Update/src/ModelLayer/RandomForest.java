package ModelLayer;

import java.util.*;

public class RandomForest {
    private final int nTrees;
    private final int maxDepth;
    private final int minSamplesSplit;
    private final int nFeatures;
    private final List<DecisionTree> trees;

    public RandomForest(int nTrees, int maxDepth, int minSamplesSplit, int nFeatures) {
        this.nTrees = nTrees;
        this.maxDepth = maxDepth;
        this.minSamplesSplit = minSamplesSplit;
        this.nFeatures = nFeatures;
        this.trees = new ArrayList<>();
    }

    // Fit the Random Forest
    public void fit(double[][] X, int[] y) {
        for (int i = 0; i < nTrees; i++) {
            Map<String, Object> bootstrappedData = bootstrap(X, y);
            double[][] bootstrappedX = (double[][]) bootstrappedData.get("X");
            int[] bootstrappedY = (int[]) bootstrappedData.get("y");

            DecisionTree tree = new DecisionTree(maxDepth, minSamplesSplit, nFeatures);
            tree.fit(bootstrappedX, bootstrappedY);
            trees.add(tree);
        }
    }

    // Predict using the Random Forest
    public int[] predict(double[][] X) {
        int nSamples = X.length;
        int[][] treePredictions = new int[nTrees][nSamples];

        // Collect predictions from each tree
        for (int i = 0; i < nTrees; i++) {
            int[] predictions = trees.get(i).predict(X);
            for (int j = 0; j < nSamples; j++) {
                treePredictions[i][j] = predictions[j];
            }
        }

        // Majority vote
        int[] finalPredictions = new int[nSamples];
        for (int j = 0; j < nSamples; j++) {
            Map<Integer, Integer> voteCount = new HashMap<>();
            for (int i = 0; i < nTrees; i++) {
                voteCount.put(treePredictions[i][j], voteCount.getOrDefault(treePredictions[i][j], 0) + 1);
            }
            finalPredictions[j] = voteCount.entrySet().stream()
                                           .max(Map.Entry.comparingByValue())
                                           .get()
                                           .getKey();
        }
        return finalPredictions;
    }

    // Calculate Feature Importance
    public double[] calculateFeatureImportance() {
        double[] importances = new double[nFeatures];
        for (DecisionTree tree : trees) {
            double[] treeImportances = tree.getFeatureImportance();
            for (int i = 0; i < importances.length; i++) {
                importances[i] += treeImportances[i];
            }
        }
        // Average over all trees
        for (int i = 0; i < importances.length; i++) {
            importances[i] /= nTrees;
        }
        return importances;
    }

    // Bootstrapping function
    private Map<String, Object> bootstrap(double[][] X, int[] y) {
        int nSamples = X.length;
        double[][] bootstrappedX = new double[nSamples][];
        int[] bootstrappedY = new int[nSamples];
        Random rand = new Random();

        for (int i = 0; i < nSamples; i++) {
            int randomIndex = rand.nextInt(nSamples);
            bootstrappedX[i] = X[randomIndex];
            bootstrappedY[i] = y[randomIndex];
        }

        Map<String, Object> bootstrappedData = new HashMap<>();
        bootstrappedData.put("X", bootstrappedX);
        bootstrappedData.put("y", bootstrappedY);
        return bootstrappedData;
    }


    public static void main(String[] args) {
    	testBiasInPredictionAccuracy();
        testOverfitting();
        testBootstrappingValidation();
    }

    public static void testBiasInPredictionAccuracy() {
        double[][] X = {
            {1.0, 2.0}, {1.0, 3.0}, {0.0, 1.0}, {0.0, 4.0}, {0.0, 6.0}
        };
        int[] y = {0, 0, 1, 1, 1};

        RandomForest forest = new RandomForest(10, 5, 2, 2);
        forest.fit(X, y);

        // Separate data by groups
        List<Integer> group0Indices = new ArrayList<>();
        List<Integer> group1Indices = new ArrayList<>();
        for (int i = 0; i < X.length; i++) {
            if (X[i][0] == 0.0) {
                group0Indices.add(i);
            } else {
                group1Indices.add(i);
            }
        }

        // Predict for each group
        double[][] group0X = group0Indices.stream().map(index -> X[index]).toArray(double[][]::new);
        double[][] group1X = group1Indices.stream().map(index -> X[index]).toArray(double[][]::new);
        int[] group0Y = group0Indices.stream().mapToInt(index -> y[index]).toArray();
        int[] group1Y = group1Indices.stream().mapToInt(index -> y[index]).toArray();

        int[] group0Predictions = forest.predict(group0X);
        int[] group1Predictions = forest.predict(group1X);

        // Calculate accuracy for each group
        double group0Accuracy = calculateAccuracy(group0Predictions, group0Y);
        double group1Accuracy = calculateAccuracy(group1Predictions, group1Y);

        System.out.println("Group 0 Accuracy: " + group0Accuracy);
        System.out.println("Group 1 Accuracy: " + group1Accuracy);
    }
    // Test 2: Overfitting Test
    public static void testOverfitting() {
        double[][] X = {
            {1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {5.0, 4.0}, {4.0, 6.0}
        };
        int[] y = {0, 0, 1, 1, 1};

        RandomForest forest = new RandomForest(10, 5, 2, 2);
        forest.fit(X, y);
        int[] predictions = forest.predict(X);

        System.out.println("Overfitting Test Predictions: " + Arrays.toString(predictions));
        assert Arrays.equals(predictions, y) : "Overfitting Test Failed!";
    }

    // Test 3: Bootstrapping Validation
    public static void testBootstrappingValidation() {
        double[][] X = {
            {1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {5.0, 4.0}, {4.0, 6.0}
        };
        int[] y = {0, 0, 1, 1, 1};

        RandomForest forest = new RandomForest(10, 5, 2, 2);
        Map<String, Object> bootstrappedData = forest.bootstrap(X, y);

        double[][] bootstrappedX = (double[][]) bootstrappedData.get("X");
        int[] bootstrappedY = (int[]) bootstrappedData.get("y");

        System.out.println("Bootstrapped X size: " + bootstrappedX.length);
        System.out.println("Bootstrapped Y size: " + bootstrappedY.length);

        assert bootstrappedX.length == X.length : "Bootstrapped X size mismatch!";
        assert bootstrappedY.length == y.length : "Bootstrapped Y size mismatch!";
    }
    public static double calculateAccuracy(int[] predictions, int[] trueValues) {
        int correctCount = 0;
        for (int i = 0; i < predictions.length; i++) {
            if (predictions[i] == trueValues[i]) {
                correctCount++;
            }
        }
        return (double) correctCount / predictions.length;
    }
    
}

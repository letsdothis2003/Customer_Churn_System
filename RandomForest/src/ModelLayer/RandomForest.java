package ModelLayer;

import java.util.*;

public class RandomForest {
    private final int nTrees;
    private final int maxDepth;
    private final int minSamplesSplit;
    private final int nFeatures;
    private final List<DecisionTree> trees;
    public static RandomForest forest; 
    
    public RandomForest(int nTrees, int maxDepth, int minSamplesSplit, int nFeatures) {
        this.nTrees = nTrees;
        this.maxDepth = maxDepth;
        this.minSamplesSplit = minSamplesSplit;
        this.nFeatures = nFeatures;
        this.trees = new ArrayList<>();
    }

    // Train the Random Forest
    public void fit(double[][] X, int[] y)
    {
        for (int i = 0; i < nTrees; i++) {
            // Generate a bootstrapped dataset
            Map<String, Object> bootstrappedData = bootstrap(X, y);
            double[][] bootstrappedX = (double[][]) bootstrappedData.get("X");
            int[] bootstrappedY = (int[]) bootstrappedData.get("y");

            // Create and train a new DecisionTree
            DecisionTree tree = new DecisionTree(maxDepth, minSamplesSplit, nFeatures);
            tree.fit(bootstrappedX, bootstrappedY);
            trees.add(tree);
        }
    }

    // Bootstrapping function
    private Map<String, Object> bootstrap(double[][] X, int[] y) {
        int nSamples = X.length;
        double[][] bootstrappedX = new double[nSamples][];
        int[] bootstrappedY = new int[nSamples];
        Random rand = new Random();

        for (int i = 0; i < nSamples; i++) {
            int randomIndex = rand.nextInt(nSamples); // Randomly select a sample
            bootstrappedX[i] = X[randomIndex];
            bootstrappedY[i] = y[randomIndex];
        }

        // Return both features and labels as a map
        Map<String, Object> bootstrappedData = new HashMap<>();
        bootstrappedData.put("X", bootstrappedX);
        bootstrappedData.put("y", bootstrappedY);
        return bootstrappedData;
    }

    // Predict with the Random Forest
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

        // Aggregate predictions (majority voting)
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
    
    public static void main(String[] args) {
        testBasicFunctionality();
        testOverfitting();
        testBootstrappingValidation();
    }

    // Test 1: Basic Functionality Test
    public static void testBasicFunctionality() {
        double[][] X = {
            {1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {5.0, 4.0}, {4.0, 6.0}
        };
        int[] y = {0, 0, 1, 1, 1};

        double[][] testX = {
            {1.5, 2.5}, {3.5, 4.5}, {4.5, 5.5}
        };

        RandomForest forest = new RandomForest(10, 5, 2, 2);
        forest.fit(X, y);
        int[] predictions = forest.predict(testX);

        System.out.println("Basic Functionality Test Predictions: " + Arrays.toString(predictions));
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
    
}

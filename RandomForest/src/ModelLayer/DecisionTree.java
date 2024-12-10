package ModelLayer;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class DecisionTree 
{
    private Node root;
    private int maxDepth;
    private int minSamplesSplit;
    private int nFeatures;

    public DecisionTree(int maxDepth, int minSamplesSplit, int nFeatures) {
        this.maxDepth = maxDepth;
        this.minSamplesSplit = minSamplesSplit;
        this.nFeatures = nFeatures;
    }

    public void fitFromFile(String filePath) {
        try {
            // Load data from the CSV file
            List<List<String>> data = loadCsv(filePath);

            // Separate features (X) and labels (y)
            double[][] X = new double[data.size()][data.get(0).size() - 1];
            int[] y = new int[data.size()];

            for (int i = 0; i < data.size(); i++) {
                List<String> row = data.get(i);
                for (int j = 0; j < row.size() - 1; j++) {
                    X[i][j] = Double.parseDouble(row.get(j));
                }
                y[i] = Integer.parseInt(row.get(row.size() - 1)); // Last column is the label
            }

            // Train the model
            fit(X, y);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
    }

    private List<List<String>> loadCsv(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(Arrays.asList(values));
            }
        }
        return data;
    }

    void fit(double[][] X, int[] y) {
        int nFeaturesAvailable = X[0].length;
        nFeatures = nFeatures == 0 ? nFeaturesAvailable : Math.min(nFeatures, nFeaturesAvailable);
        this.root = growTree(X, y, 0);
    }

    Node growTree(double[][] X, int[] y, int depth) {
        int nSamples = X.length;
        int nLabels = Arrays.stream(y).distinct().toArray().length;

        // Stopping criteria
        if (depth >= maxDepth || nLabels == 1 || nSamples < minSamplesSplit) {
            return new Node(mostCommonLabel(y));
        }

        // Randomly select features
        Random rand = new Random();
        Set<Integer> featureIndices = new HashSet<>();
        while (featureIndices.size() < nFeatures) {
            featureIndices.add(rand.nextInt(X[0].length));
        }

        // Find the best split
        int bestFeature = -1;
        double bestThreshold = Double.NaN;
        double bestGain = -1;

        for (int feature : featureIndices) {
            double[] thresholds = Arrays.stream(X).mapToDouble(row -> row[feature]).distinct().toArray();
            for (double threshold : thresholds) {
                double gain = informationGain(X, y, feature, threshold);
                if (gain > bestGain) {
                    bestGain = gain;
                    bestFeature = feature;
                    bestThreshold = threshold;
                }
            }
        }

        if (bestGain == -1) {
            return new Node(mostCommonLabel(y));
        }

        // Split data
        List<Integer> leftIndices = new ArrayList<>();
        List<Integer> rightIndices = new ArrayList<>();
        for (int i = 0; i < X.length; i++) {
            if (X[i][bestFeature] <= bestThreshold) {
                leftIndices.add(i);
            } else {
                rightIndices.add(i);
            }
        }

        double[][] leftX = subsetRows(X, leftIndices);
        int[] leftY = subsetArray(y, leftIndices);
        double[][] rightX = subsetRows(X, rightIndices);
        int[] rightY = subsetArray(y, rightIndices);

        Node leftChild = growTree(leftX, leftY, depth + 1);
        Node rightChild = growTree(rightX, rightY, depth + 1);
        return new Node(bestFeature, bestThreshold, leftChild, rightChild);
    }

    private double informationGain(double[][] X, int[] y, int feature, double threshold) {
        double parentEntropy = entropy(y);

        // Split data
        List<Integer> leftIndices = new ArrayList<>();
        List<Integer> rightIndices = new ArrayList<>();
        for (int i = 0; i < X.length; i++) {
            if (X[i][feature] <= threshold) {
                leftIndices.add(i);
            } else {
                rightIndices.add(i);
            }
        }

        if (leftIndices.isEmpty() || rightIndices.isEmpty()) {
            return 0.0;
        }

        int[] leftY = subsetArray(y, leftIndices);
        int[] rightY = subsetArray(y, rightIndices);
        double leftWeight = (double) leftY.length / y.length;
        double rightWeight = (double) rightY.length / y.length;

        // Weighted entropy
        double childEntropy = leftWeight * entropy(leftY) + rightWeight * entropy(rightY);
        return parentEntropy - childEntropy;
    }

    private double entropy(int[] y) {
        Map<Integer, Long> labelCounts = Arrays.stream(y)
                .boxed()
                .collect(Collectors.groupingBy(label -> label, Collectors.counting()));

        double entropy = 0.0;
        for (long count : labelCounts.values()) {
            double p = (double) count / y.length;
            entropy -= p * Math.log(p + 1e-9); // Add small value to prevent log(0)
        }
        return entropy;
    }

    private int mostCommonLabel(int[] y) {
        return Arrays.stream(y)
                .boxed()
                .collect(Collectors.groupingBy(label -> label, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey) // Safely map to the key
                .orElse(-1); // Return a default value, like -1, if no labels are present
    }

    private double[][] subsetRows(double[][] X, List<Integer> indices) {
        double[][] subset = new double[indices.size()][];
        for (int i = 0; i < indices.size(); i++) {
            subset[i] = X[indices.get(i)];
        }
        return subset;
    }

    private int[] subsetArray(int[] array, List<Integer> indices) {
        int[] subset = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            subset[i] = array[indices.get(i)];
        }
        return subset;
    }

    public int[] predict(double[][] X) {
        return Arrays.stream(X)
                .mapToInt(this::predictSingle)
                .toArray();
    }

    private int predictSingle(double[] x) {
        Node node = root;
        while (!node.isLeafNode()) {
            if (x[node.feature] <= node.threshold) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node.value;
    }
}


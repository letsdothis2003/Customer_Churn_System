package ModelLayer;

import java.util.*;



	public class DecisionTree {
	    private Node root;

	    // Starts the tree building process using the provided dataset
	    public void fit(List<Map<String, String>> data) {
	        root = buildTree(data);
	    }

	    // Recursively builds the decision tree from the given data
	    public Node buildTree(List<Map<String, String>> data) {
	        if (data.isEmpty()) {
	            return null;
	        }
	        if (isPure(data)) {
	            return new Node(getMostCommonLabel(data));
	        }

	        String bestFeature = "";
	        double bestThreshold = 0;
	        double bestImpurity = Double.MAX_VALUE;

	        for (String feature : data.get(0).keySet()) {
	            Set<Double> thresholds = new HashSet<>();
	            for (Map<String, String> row : data) {
	                thresholds.add(Double.parseDouble(row.get(feature)));
	            }

	            for (double threshold : thresholds) {
	                double impurity = calculateGiniImpurity(data, feature, threshold);
	                if (impurity < bestImpurity) {
	                    bestImpurity = impurity;
	                    bestFeature = feature;
	                    bestThreshold = threshold;
	                }
	            }
	        }

	        List<Map<String, String>> leftSplit = new ArrayList<>();
	        List<Map<String, String>> rightSplit = new ArrayList<>();
	        for (Map<String, String> row : data) {
	            if (Double.parseDouble(row.get(bestFeature)) < bestThreshold) {
	                leftSplit.add(row);
	            } else {
	                rightSplit.add(row);
	            }
	        }

	        Node left = buildTree(leftSplit);
	        Node right = buildTree(rightSplit);

	        return new Node(bestFeature, bestThreshold, left, right);
	    }

	    // Checks if all data points in a subset have the same label
	    private boolean isPure(List<Map<String, String>> data) {
	        String firstLabel = data.get(0).get("label");
	        for (Map<String, String> row : data) {
	            if (!row.get("label").equals(firstLabel)) {
	                return false;
	            }
	        }
	        return true;
	    }

	    // Determines the most common label in a subset of data
	    private String getMostCommonLabel(List<Map<String, String>> data) {
	        Map<String, Integer> labelCount = new HashMap<>();
	        for (Map<String, String> row : data) {
	            labelCount.put(row.get("label"), labelCount.getOrDefault(row.get("label"), 0) + 1);
	        }
	        return Collections.max(labelCount.entrySet(), Map.Entry.comparingByValue()).getKey();
	    }

	    // Calculates the Gini impurity for a potential split based on the given feature and threshold
	    private double calculateGiniImpurity(List<Map<String, String>> data, String feature, double threshold) {
	        List<String> leftLabels = new ArrayList<>();
	        List<String> rightLabels = new ArrayList<>();

	        for (Map<String, String> row : data) {
	            if (Double.parseDouble(row.get(feature)) < threshold) {
	                leftLabels.add(row.get("label"));
	            } else {
	                rightLabels.add(row.get("label"));
	            }
	        }

	        return (calculateGini(leftLabels) * leftLabels.size() + calculateGini(rightLabels) * rightLabels.size()) / data.size();
	    }

	    // Computes the Gini impurity for a list of labels
	    private double calculateGini(List<String> labels) {
	        Map<String, Integer> labelCount = new HashMap<>();
	        for (String label : labels) {
	            labelCount.put(label, labelCount.getOrDefault(label, 0) + 1);
	        }
	        double impurity = 1.0;
	        for (Integer count : labelCount.values()) {
	            double p = count / (double) labels.size();
	            impurity -= p * p;
	        }
	        return impurity;
	    }

	    // Predicts the label for new data based on the constructed decision tree
	    public String predict(Map<String, String> features) {
	        Node node = root;
	        while (!node.isLeafNode()) {
	            if (Double.parseDouble(features.get(node.getFeature())) < node.getThreshold()) {
	                node = node.getLeft();
	            } else {
	                node = node.getRight();
	            }
	        }
	        return node.getValue();
	    }
	}

package ModelLayer;

public class Node {
    // Attributes
    private String feature;   // Feature used for splitting
    private Double threshold; // Threshold value for the split 
    private Node left;        // Left child node
    private Node right;       // Right child node
    private String value;     // Value for leaf nodes 

    // Constructor for internal nodes
    public Node(String feature, Double threshold, Node left, Node right) {
        this.feature = feature;
        this.threshold = threshold;
        this.left = left;
        this.right = right;
        this.value = null; // Default to non-leaf node
    }

    // Constructor for leaf nodes
    public Node(String value) {
        this.feature = null; // No feature for leaf nodes
        this.threshold = null; // No threshold for leaf nodes
        this.left = null;
        this.right = null;
        this.value = value; // Set value for the leaf
    }

    // Check if the node is a leaf node
    public boolean isLeafNode() {
        return this.value != null; // Leaf nodes have a non-null value
    }

    // Getters and Setters
    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
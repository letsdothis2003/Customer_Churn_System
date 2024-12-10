package ModelLayer;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class DecisionTreeTest {

    @Test
    public void testInitialization() {
        // Verify the DecisionTree object is created with the correct parameters
        DecisionTree tree = new DecisionTree(5, 2, 3);
        assertNotNull(tree, "DecisionTree object should not be null after initialization");
    }

    @Test
    public void testFit() {
        // Verify that the tree is trained successfully and splitting is functional
        double[][] X = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        int[] y = {0, 1, 0};
        DecisionTree tree = new DecisionTree(5, 2, 2);
        tree.fit(X, y);
        assertNotNull(tree, "Tree should be trained successfully");

        // Verify splitting logic by ensuring a non-leaf root node is created
        Node rootNode = tree.growTree(X, y, 0);
        assertNotNull(rootNode, "Root node should be created during training");
        assertFalse(rootNode.isLeafNode(), "Root node should not be a leaf node for this dataset");
    }

    @Test
    public void testPredict() {
        // Verify predictions are made correctly after training
        double[][] X = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        int[] y = {0, 1, 0};
        DecisionTree tree = new DecisionTree(5, 2, 2);
        tree.fit(X, y);

        int[] predictions = tree.predict(X);
        assertArrayEquals(y, predictions, "Predictions should match the actual labels");
    }

    @Test
    public void testNodeSplitting() {
        // Verify splitting logic creates left and right subsets correctly
        double[][] X = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        int[] y = {0, 1, 0};
        DecisionTree tree = new DecisionTree(5, 2, 2);

        // Simulate splitting
        Node rootNode = tree.growTree(X, y, 0);
        assertNotNull(rootNode, "Splitting should create a root node");
        assertFalse(rootNode.isLeafNode(), "Root node should not be a leaf node for this dataset");
        assertNotNull(rootNode.left, "Left child should be created");
        assertNotNull(rootNode.right, "Right child should be created");
    }

    @Test
    public void testLeafNodePrediction() {
        // Verify predictions from a leaf node
        Node leafNode = new Node(1); // Simulate a leaf node with value 1
        assertTrue(leafNode.isLeafNode(), "Node should be identified as a leaf node");
        assertEquals(1, leafNode.value, "Leaf node value should match the expected label");
    }
}

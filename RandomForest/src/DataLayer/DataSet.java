package DataLayer;

import java.io.*;
import java.util.*;
import ModelLayer.DecisionTree;
import ModelLayer.Validation;
import ModelLayer.Evaluation;

public class DataSet 
{

    private List<Map<String, String>> data; // List of rows, each row is a map of column name -> value
    private List<String> columns;          // List of column names (header row)

    public DataSet() {
        data = new ArrayList<>();
        columns = new ArrayList<>();
    }

 // Load data from CSV and trim extra spaces
    public void loadData() throws IOException
    {
        String filePath = "C:\\Users\\Admin\\eclipse-workspace\\RandomForest\\src\\DataLayer\\DataFile.csv";

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        // Read the header row
        if ((line = br.readLine()) != null) {
            columns = Arrays.asList(line.split(","));
        }

        // Read the data rows
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            Map<String, String> row = new HashMap<>();
            for (int i = 0; i < columns.size(); i++) {
                String value = i < values.length ? values[i].trim() : null;
                row.put(columns.get(i), value == null || value.isEmpty() ? null : value);
            }
            data.add(row);
        }
        br.close();
    }

 // Split the dataset into training, validation, and testing sets
    public Map<String, List<Map<String, String>>> splitData(List<Map<String, String>> data) {
        Collections.shuffle(data); // Randomize the order of data

        int totalSize = data.size();
        int trainSize = (int) (totalSize * 0.70); // 70% size of training
        int validationSize = (int) (totalSize * 0.15); // 15% size of validation
         
        List<Map<String, String>> trainingSet = new ArrayList<>(data.subList(0, trainSize));
        List<Map<String, String>> validationSet = new ArrayList<>(data.subList(trainSize, trainSize + validationSize));
        List<Map<String, String>> testingSet = new ArrayList<>(data.subList(trainSize + validationSize, totalSize));

        Map<String, List<Map<String, String>>> splits = new HashMap<>();
        splits.put("train", trainingSet);
        splits.put("validation", validationSet);
        splits.put("test", testingSet);

        return splits;
    }  
    
    // helper functions to pass the data to the other classes in Model Layer
    public void passTrainingData(List<Map<String, String>> trainingData) 
    {
        DecisionTree decisionTree = new DecisionTree();
        decisionTree.buildTree(trainingData);
    }
    public void passValidationData(List<Map<String, String>> validationData, DecisionTree decisionTree) 
    {
        Validation validation = new Validation();
        validation.validateModel(validationData, decisionTree);
    }
    public void passTestData(List<Map<String, String>> testData, DecisionTree decisionTree) 
    {
        Evaluation evaluation = new Evaluation();
        evaluation.evaluateModel(testData, decisionTree);
    }

 // Impute missing numerical data with the mean
    public void imputeNumerical(String columnName) {
        double sum = 0;
        int count = 0;

        // Calculate the mean
        for (Map<String, String> row : data) 
        {
            String value = row.get(columnName);
            if (value != null && !value.isEmpty()) {
                try {
                    double number = Double.parseDouble(value);
                    sum += number;
                    count++;
                } catch (NumberFormatException e)
                {
                    System.out.println("Skipping invalid numerical value: " + value);
                }
            }
        }

        if (count == 0) 
        {
            System.out.println("No valid numerical values found in column " + columnName + ". Imputation skipped.");
            return;
        }

        double mean = sum / count;
        System.out.println("Mean for column " + columnName + ": " + mean);

        // Replace missing values with the mean
        for (Map<String, String> row : data) {
            String value = row.get(columnName);
            if (value == null || value.isEmpty()) {
                row.put(columnName, String.valueOf(mean));
            }
        }
    }
    
 // Impute missing categorical data with frequencies
    public void imputeCategoricalWithMode(String columnName) {
    	  Map<String, Integer> frequencyMap = new HashMap<>();

    	    // Calculate frequencies
    	    for (Map<String, String> row : data) {
    	        String value = row.get(columnName);
    	        if (value != null && !value.isEmpty()) {
    	            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
    	        }
    	    }

    	    // Find the mode
    	    String mode = null;
    	    int maxCount = 0;
    	    for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
    	        if (entry.getValue() > maxCount) {
    	            mode = entry.getKey();
    	            maxCount = entry.getValue();
    	        }
    	    }

    	    if (mode == null) {
    	        System.out.println("No valid categorical values found in column " + columnName + ". Imputation skipped.");
    	        return;
    	    }

    	    System.out.println("Mode for column " + columnName + ": " + mode);

    	    // Replace missing values with the mode
    	    for (Map<String, String> row : data) {
    	        String value = row.get(columnName);
    	        if (value == null || value.isEmpty()) {
    	            row.put(columnName, mode);
    	        }
    	    }
    	}

    public void testSplitData() 
    {
        try {
            // Load dataset
            loadData();

            // Log dataset size
            int totalSize = data.size();
            System.out.println("Total dataset size: " + totalSize);

            // Split dataset
            Map<String, List<Map<String, String>>> splits = splitData(data);

            // Log split sizes
            int trainSize = splits.get("train").size();
            int validationSize = splits.get("validation").size();
            int testSize = splits.get("test").size();

            System.out.println("Train size: " + trainSize);
            System.out.println("Validation size: " + validationSize);
            System.out.println("Test size: " + testSize);

            // Validate split proportions
            if (trainSize != (int) (totalSize * 0.70)) {
                throw new AssertionError("Training set size is incorrect.");
            }
            if (validationSize != (int) (totalSize * 0.15)) {
                throw new AssertionError("Validation set size is incorrect.");
            }
            if (testSize != totalSize - trainSize - validationSize) { // Remaining size
                throw new AssertionError("Test set size is incorrect.");
            }

            // Check overlap between splits
            for (Map<String, String> row : splits.get("train")) {
                if (splits.get("validation").contains(row) || splits.get("test").contains(row)) {
                    throw new AssertionError("Overlap found in train with validation or test: " + row);
                }
            }
            for (Map<String, String> row : splits.get("validation")) {
                if (splits.get("test").contains(row)) {
                    throw new AssertionError("Overlap found in validation with test: " + row);
                }
            }

            System.out.println("Data splitting test passed successfully!");
        } catch (IOException e) {
            System.err.println("Error during data loading or splitting: " + e.getMessage());
        } catch (AssertionError e) {
            System.err.println("Test failed: " + e.getMessage());
        }
    }


 // Method to test imputation of missing values
    public void testImputation() {
        try {
            // Create a DataSet instance
            DataSet dataSet = new DataSet();

            // Load a dataset with missing values
            dataSet.loadData(); // Ensure this dataset has some missing values

            // Perform imputation for numerical and categorical columns
            dataSet.imputeNumerical("tenure"); // Replace "tenure" with the actual numerical column
            dataSet.imputeCategoricalWithMode("gender"); // Replace "gender" with the actual categorical column

            // Count missing values after imputation
            long missingCountAfter = countMissingValues();

            // Assertion: Ensure no missing values remain
            assertEquals(missingCountAfter, 0, "Dataset should have no missing values after imputation.");

            System.out.println("Data imputation test passed successfully!");

        } catch (IOException e) {
            System.err.println("Error during data loading or imputation: " + e.getMessage());
        }
    }

    // Helper method to count missing values in the dataset
    private long countMissingValues() {
        long missingCount = 0;
        for (Map<String, String> row : data) {
            for (String value : row.values()) {
                if (value == null || value.isEmpty()) {
                    missingCount++;
                }
            }
        }
        return missingCount;
    }
 // Custom assertion for equality
    private void assertEquals(long missingCountAfter, int expected, String errorMessage) {
        if (missingCountAfter != expected) {
            throw new AssertionError(errorMessage + " Expected: " + expected + ", but got: " + missingCountAfter);
        }
    }
    public static void main(String[] args) {
        DataSet tests = new DataSet();

        // Run test cases
        tests.testImputation();
        tests.testSplitData();
    
    }


    }


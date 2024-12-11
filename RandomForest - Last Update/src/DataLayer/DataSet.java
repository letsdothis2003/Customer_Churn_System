package DataLayer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet {
    private List<List<String>> data;
    private List<String> headers;
    private List<List<String>> trainData = new ArrayList<>();
    private List<List<String>> testData = new ArrayList<>();


    public DataSet(String fileName) {
        this.data = new ArrayList<>();
        loadCsv(fileName);
    }

    private void loadCsv(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            headers = new ArrayList<>();
            if ((line = br.readLine()) != null) {
                String[] header = line.split(",");
                for (String h : header) {
                    headers.add(h.trim());
                }
            }
            while ((line = br.readLine()) != null) {
                List<String> values = new ArrayList<>();
                String[] row = line.split(",");
                for (String field : row) {
                    values.add(field.trim());
                }
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setData(List<List<String>> data) {
        this.data = data;
    }
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getHeaders() {
        return headers;
    }
    public List<List<String>> getData() {
        return data;
    }

    public void imputeData() {
        // Validate that each row has the same number of columns as the headers
        for (List<String> row : data) {
            if (row.size() != headers.size()) {
                throw new IllegalStateException("Row size does not match header size.");
            }
        }

        // Perform imputation
        for (int i = 0; i < headers.size(); i++) {
            if (isNumericColumn(i)) {
                imputeNumericColumn(i);
            } else {
                imputeCategoricalColumn(i);
            }
        }
    }

    private void imputeNumericColumn(int columnIndex) {
        double sum = 0;
        int count = 0;

        // Calculate the mean of the column (ignoring empty values)
        for (List<String> row : data) {
            if (columnIndex < row.size() && !row.get(columnIndex).isEmpty()) {
                sum += Double.parseDouble(row.get(columnIndex));
                count++;
            }
        }
        double mean = count > 0 ? sum / count : 0;

        // Replace empty values with the calculated mean
        for (List<String> row : data) {
            if (columnIndex < row.size() && row.get(columnIndex).isEmpty()) {
                row.set(columnIndex, String.valueOf(mean));
            }
        }
    }

    private void imputeCategoricalColumn(int columnIndex) {
        Map<String, Integer> frequency = new HashMap<>();

        // Count the frequency of each value in the column
        for (List<String> row : data) {
            if (columnIndex < row.size() && !row.get(columnIndex).isEmpty()) {
                String value = row.get(columnIndex);
                frequency.put(value, frequency.getOrDefault(value, 0) + 1);
            }
        }

        // Find the mode (most frequent value)
        String mode = null;
        int maxFreq = 0;
        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > maxFreq) {
                mode = entry.getKey();
                maxFreq = entry.getValue();
            }
        }

        // Replace empty values with the mode
        for (List<String> row : data) {
            if (columnIndex < row.size() && row.get(columnIndex).isEmpty()) {
                row.set(columnIndex, mode != null ? mode : ""); // Use an empty string if there's no mode
            }
        }
    }

    private boolean isNumericColumn(int columnIndex) {
        // Check if the column contains numeric data
        for (List<String> row : data) {
            if (columnIndex < row.size()) {
                try {
                    Double.parseDouble(row.get(columnIndex));
                    return true; // If any value is numeric, assume the column is numeric
                } catch (NumberFormatException e) {
                    return false; // Non-numeric value found
                }
            }
        }
        return false;
    }

    public void splitData(double trainRatio) {
        Collections.shuffle(data);
        int total = data.size();
        int trainEnd = (int) (total * trainRatio);
        int testStart = trainEnd; // Start testing set right after training set ends

        trainData = new ArrayList<>(data.subList(0, trainEnd));
        testData = new ArrayList<>(data.subList(testStart, total));

        saveDataToFile(trainData, "C:\\Users\\Admin\\eclipse-workspace\\RandomForest\\src\\DataLayer\\train_data.csv");
        saveDataToFile(testData, "C:\\Users\\Admin\\eclipse-workspace\\RandomForest\\src\\DataLayer\\test_data.csv");
    }

    public List<List<String>> getTrainData() {
        return trainData;
    }


    public List<List<String>> getTestData() {
        return testData;
    }

    private void saveDataToFile(List<List<String>> dataSet, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(String.join(",", headers));
            bw.newLine();
            for (List<String> row : dataSet) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataSet ds = new DataSet("C:\\Users\\Admin\\eclipse-workspace\\RandomForest\\src\\DataLayer\\DataFile (updated).csv");
        ds.imputeData(); // Impute missing values
        ds.splitData(0.70); // 70% training, 30% testing
    }
}

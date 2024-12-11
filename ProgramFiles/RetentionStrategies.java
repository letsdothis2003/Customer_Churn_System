import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RetentionStrategies {

    private static final String CSV_FILE_PATH = "filepath";

    public static void main(String[] customerData) {
        generateRetentionStrategies(customerData);
    }

    private static void generateRetentionStrategies(String[] customerData) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            String[] headers = br.readLine().split(","); // Read headers from the CSV

            int totalCustomers = 0;
            int similarCustomers = 0;
            double averageTenure = 0;
            double averageMonthlyCharges = 0;

            while ((line = br.readLine()) != null) {
                totalCustomers++;
                String[] existingCustomer = line.split(",");

                boolean isSimilar = true;
                for (int i = 0; i < customerData.length; i++) {
                    if (!customerData[i].isEmpty() && !customerData[i].equalsIgnoreCase(existingCustomer[i])) {
                        isSimilar = false;
                        break;
                    }
                }

                if (isSimilar) {
                    similarCustomers++;
                    averageTenure += Double.parseDouble(existingCustomer[headers.length - 3]); // Tenure index
                    averageMonthlyCharges += Double.parseDouble(existingCustomer[headers.length - 2]); // Monthly charges index
                }
            }

            if (similarCustomers > 0) {
                averageTenure /= similarCustomers;
                averageMonthlyCharges /= similarCustomers;

                StringBuilder strategies = new StringBuilder();
                strategies.append("Retention Strategies:\n");
                strategies.append("- Offer loyalty rewards for customers with tenures around ").append(averageTenure).append(" months.\n");
                strategies.append("- Provide discounts for monthly charges exceeding $").append(averageMonthlyCharges).append(".\n");
                strategies.append("- Enhance support services like Tech Support and Online Security.\n");

                JOptionPane.showMessageDialog(null, strategies.toString(), "Retention Strategies", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No similar customers found. Consider conducting a survey to gather more data.", "Retention Strategies", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading the CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

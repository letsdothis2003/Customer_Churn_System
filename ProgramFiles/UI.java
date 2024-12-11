import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI {

    public static void main(String[] args) {
        // Prompt user to start or quit
        int choice = JOptionPane.showOptionDialog(null,
                "Welcome to the Customer Data Input System. Would you like to start or quit?",
                "Customer Churn System",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Start", "Quit"},
                "Start");

        if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        SwingUtilities.invokeLater(UI::createAndShowUI);
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("Customer Data Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLayout(new GridLayout(18, 2));

        // Input labels and fields
        String[] labels = {"Gender (m/f)", "Senior Citizen (Yes/No)", "Partner (Yes/No)", "Dependents (Yes/No)",
                "Tenure", "Phone Service (Yes/No)", "Multiple Lines (Yes/No)", "Online Security (Yes/No)",
                "Online Backup (Yes/No)", "Device Protection (Yes/No)", "Tech Support (Yes/No)",
                "Streaming TV (Yes/No)", "Streaming Movies (Yes/No)", "Paperless Billing (Yes/No)",
                "Monthly Charges", "Total Charges"};

        JTextField[] textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            JTextField textField = new JTextField();
            textFields[i] = textField;
            frame.add(label);
            frame.add(textField);
        }

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isComplete = true;

                for (int i = 0; i < labels.length; i++) {
                    String input = textFields[i].getText().trim();

                    if (labels[i].contains("Yes/No")) {
                        input = input.equalsIgnoreCase("yes") ? "1" : input.equalsIgnoreCase("no") ? "0" : input;
                    } else if (labels[i].contains("Gender")) {
                        input = input.equalsIgnoreCase("m") ? "1" : input.equalsIgnoreCase("f") ? "0" : input;
                    }

                    if (input.isEmpty() || (!labels[i].contains("Gender") && labels[i].contains("Yes/No") && !(input.equals("1") || input.equals("0")))) {
                        isComplete = false;
                        break;
                    }
                }

                if (isComplete) {
                    JOptionPane.showMessageDialog(frame, "Data successfully loaded: Please wait", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Incomplete data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(new JLabel()); // Placeholder for alignment
        frame.add(submitButton);

        frame.setVisible(true);
    }
}




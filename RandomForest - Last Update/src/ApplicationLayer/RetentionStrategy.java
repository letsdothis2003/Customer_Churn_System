package ApplicationLayer;

import java.util.ArrayList;
import java.util.List;

public class RetentionStrategy {

    List<double[]> loyalCustomers = new ArrayList<>();
    List<double[]> unloyalCustomers = new ArrayList<>();

    public RetentionStrategy() {
    	// Initialize unloyal customers
        unloyalCustomers.add(new double[]{1, 0, 0, 0, 67, 1, 1, 1, 1, 1, 1, 1, 1, 1, 118.35, 7804.15});
        unloyalCustomers.add(new double[]{1, 0, 1, 0, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 117.8, 8684.8});
        unloyalCustomers.add(new double[]{0, 1, 1, 0, 48, 1, 1, 1, 1, 1, 1, 1, 1, 0, 117.45, 5438.9});
        unloyalCustomers.add(new double[]{1, 0, 0, 0, 67, 1, 1, 1, 1, 1, 1, 1, 1, 0, 116.2, 7752.3});
        unloyalCustomers.add(new double[]{1, 0, 1, 0, 70, 1, 1, 1, 1, 1, 1, 1, 1, 0, 115.65, 7968.85});
        unloyalCustomers.add(new double[]{0, 0, 0, 0, 70, 1, 1, 1, 1, 1, 1, 1, 1, 0, 115.55, 8127.6});
        unloyalCustomers.add(new double[]{0, 0, 0, 0, 41, 1, 1, 1, 1, 1, 1, 1, 1, 1, 114.5, 4527.45});
        unloyalCustomers.add(new double[]{0, 0, 1, 1, 70, 1, 1, 1, 1, 1, 1, 1, 1, 1, 114.2, 7723.9});
        unloyalCustomers.add(new double[]{0, 0, 1, 0, 41, 1, 1, 1, 1, 1, 1, 1, 1, 1, 113.6, 4594.95});
        unloyalCustomers.add(new double[]{0, 1, 0, 0, 41, 1, 1, 1, 1, 1, 0, 1, 1, 1, 113.2, 4689.5});

        // Initialize loyal customers
        loyalCustomers.add(new double[]{0, 0, 1, 1, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 118.75, 8672.45});
        loyalCustomers.add(new double[]{0, 0, 1, 1, 72, 1, 1, 1, 1, 1, 1, 1, 1, 0, 117.5, 8670.1});
        loyalCustomers.add(new double[]{1, 0, 1, 0, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 116.95, 8594.4});
        loyalCustomers.add(new double[]{1, 0, 0, 0, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 118.2, 8547.15});
        loyalCustomers.add(new double[]{0, 0, 1, 1, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 116.4, 8543.25});
        loyalCustomers.add(new double[]{0, 0, 1, 0, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 117.15, 8529.5});
        loyalCustomers.add(new double[]{0, 0, 0, 0, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 114.9, 8496.7});
        loyalCustomers.add(new double[]{0, 0, 1, 1, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 116.85, 8477.7});
        loyalCustomers.add(new double[]{0, 0, 1, 1, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 115.8, 8476.5});
        loyalCustomers.add(new double[]{1, 0, 1, 1, 72, 1, 1, 1, 1, 1, 1, 1, 1, 1, 114.05, 8468.2});

    }

    // Function to calculate cosine similarity
    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }   
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));

    }
 // Analyze similarity for a new customer with detailed output
    public String analyzeCustomerSimilarity(double[] newCustomer) {
        double loyalSimilaritySum = 0.0;
        double unloyalSimilaritySum = 0.0;

        System.out.println("Similarity with Loyal Customers:");
        for (double[] loyal : loyalCustomers) {
            double similarity = cosineSimilarity(newCustomer, loyal);
            loyalSimilaritySum += similarity;
            System.out.println(similarity);
        }
        double averageLoyalSimilarity = loyalSimilaritySum / loyalCustomers.size();

        System.out.println("Similarity with Unloyal Customers:");
        for (double[] unloyal : unloyalCustomers) {
            double similarity = cosineSimilarity(newCustomer, unloyal);
            unloyalSimilaritySum += similarity;
            System.out.println(similarity);
        }
        double averageUnloyalSimilarity = unloyalSimilaritySum / unloyalCustomers.size();

        System.out.println("Average Loyal Similarity: " + averageLoyalSimilarity);
        System.out.println("Average Unloyal Similarity: " + averageUnloyalSimilarity);

        if (averageLoyalSimilarity > averageUnloyalSimilarity) {
            return "Loyal";
        } else {
            return "Unloyal";
        }
    }


    public static void main(String[] args) {
        RetentionStrategy strategy = new RetentionStrategy();

        // Example new customer data
        double[] newCustomer =  {0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 1, 0, 1, 80.65, 144.15};
        double[] newCustomer1 = {1, 0, 0, 0, 52, 1, 1, 1, 0, 0, 1, 1, 1, 1, 79.75, 4217.8};
        double[] newCustomer2 = {1,0,0,0,2,1,0,0,1,0,0,0,0,0,49.25,97};



        // Analyze similarity for the new customer
        String result = strategy.analyzeCustomerSimilarity(newCustomer);
        System.out.println("The new customer is predicted to be: " + result);
        String result1 = strategy.analyzeCustomerSimilarity(newCustomer1);
        System.out.println("The new customer is predicted to be: " + result1);
        
        String result2 = strategy.analyzeCustomerSimilarity(newCustomer2);
        System.out.println("The new customer is predicted to be: " + result2);
    }
}


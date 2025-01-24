This file is meant to use Random Forest Machine learning algorithm to detect Customer churn or how likely they are to leave/stop support of a company or bussiness.
People who collaborated to create this are: Fahim Tanvir, Ahmed Ali, Abul Hassan 



Introduction:
Purpose:
This project aims to develop a model that predicts customer churn for a telecom company. Customer churn refers to when the customer is willing to leave the company by canceling their services or subscription. This prediction model will be trained on historical customer data to measure customer loyalty. By identifying patterns in the customers behavior, the company can take steps to retain customers that have a chance of leaving and improve the business model. 

Problem Statement:
The telecom company is currently experiencing high rates of customer turnover, leading to significant revenue loss. Retaining existing customers can be cheaper for the company than attracting new ones and also implementing changes within business depending on what sections get churned the most. Essentially, churn prediction is critical for the company’s profitability. By implementing this system, the company will be able to take proactive measures to prevent churn.
After making the prediction, the system will analyze the data of customers who left. It will then provide suggestions to help change the behavior of at-risk customers, encouraging them to act more like their peers who stayed. It will also suggest to the company on what changes or improvements can be made using data of customers who churned and looking for correlations(for example, if a customer that churned is known for having a large monthly bill, changes within company systems will be made to allow extended payment plan or even discounts for non-churned customers). 
This is the dataset we will be using:
https://www.kaggle.com/datasets/blastchar/telco-customer-churn


					User’s Guide

 1. Go to the DataSet class in DataLayer. Run it for one time only to split the data into files
Note: you don’t need to do it again anymore just for the first time you run the program.
**After that it will be automatically saved in the new files 
Note: you don’t have to do it, it’s just an extra step, to make sure the data is safe to use
You can run the Application immediately without the need for this step
![image](https://github.com/user-attachments/assets/e2314f43-ad37-466a-80ea-1ca046dfb08f)

2.  After splitting the data, go to the Application Layer and run the program
![image](https://github.com/user-attachments/assets/2fe40a5b-a7aa-4988-952f-7a04369eafba)

3. A screen or GUI will pop up as seen here. 

   
![image](https://github.com/user-attachments/assets/3e84cd72-8443-4186-951d-9a3d66672e48)

4. input the data of the customer you suspect will leave.
   
    ![image](https://github.com/user-attachments/assets/3ea9a8b3-d27d-40b2-985b-c1a19648dea9)


5. If the customer is leaving a customized message will pop up based on the customer's needs
   ![image](https://github.com/user-attachments/assets/e9d66257-3abd-4462-b507-6ec049e4cdb3)


6. You have the option to enter new customer data again or close the program.

Planning For V 2.0
For Version 2.0 of the app, we're making a change from using outside data analysis on most attributes that make the customers leave (provided in the Kaggle dataset) to using our own advanced machine learning method. This new method, called dot product similarity analysis, will help us understand and change the behavior of customers who might leave to be more like customers who stay and are loyal. This will also improve our accuracy as for Version 1.0, the accuracy ranges from 70-80, which is good but 90-100 is better. 
In Version 1.0, the problem was that the system paid too much attention(weighted more than the other attributes) to the customer's monthly and total charges. This caused the recommendations to focus too much on these charges only ignoring the other issues. To fix this in Version 2.0, we are planning to use better techniques to make sure these charges don't affect the system's decisions too much by creating more functions to memorialize those 2 attributes. This will help make our predictions and suggestions better and more useful for keeping customers. Also a proper front end that looks nicer is something that we would like to plan. 



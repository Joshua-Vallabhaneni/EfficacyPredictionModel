# EfficacyPredictionModel

## Overview
EfficacyPredictionModel is a Java-based tool for analyzing and predicting the efficacy of Tagrisso, a drug used in the treatment of Non-Small Cell Lung Cancer (NSCLC). The project integrates a MySQL database containing patient data and utilizes JDBC for data access. It employs statistical methods and machine learning algorithms to analyze and predict drug efficacy based on patient demographics and treatment details.

## Features
- **MySQL Database Integration**: Incorporates a MySQL database to store and manage data for 30 patients, including attributes such as age, sex, cancer type, drug name, dosage, treatment duration, drug efficacy, and side effects.
- **Data Analysis**: Executes SQL queries to calculate and analyze average drug efficacy, grouped by sex, age, and treatment duration.
- **Machine Learning for Prediction**: Utilizes the Apache Commons Math library for implementing OLSMultipleLinear Regression to predict drug efficacy. The model is trained with 80% of the data and tested with the remaining 20%, achieving a mean squared error of less than 0.1.

## Installation
To set up the EfficacyPredictionModel, follow these steps:
1. Ensure Java and MySQL are installed on your system.
2. Clone the repository to your local machine.
3. Set up the MySQL database and import the patient data.
4. Update the database connection details in the code as per your MySQL configuration.

## Usage
To run the EfficacyPredictionModel, execute the main method in the `EfficacyPredictor` class. The program will connect to the MySQL database, perform data analysis, and execute the regression model to predict drug efficacy.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

## Author
Joshua Vallabhaneni

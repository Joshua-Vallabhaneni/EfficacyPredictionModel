import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class EfficacyPredictor {

	/**
	 * Establishes a connection to a MySQL Database.
	 * 
	 * @param url      URL of the database.
	 * @param user     Username for logging into database.
	 * @param password Password for logging into database.
	 * @return Connection object to database.
	 * @throws ClassNotFoundException if the Driver is not found.
	 * @throws SQLException           if url is null or error in logging into
	 *                                database.
	 */
	private static Connection createConnection(String url, String user, String password)
			throws ClassNotFoundException, SQLException {
		// First, register the MySQL JDBC Driver, which allows Java the program to
		// interact with the mySQL database.
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Then, establish a connection to the database.
		return DriverManager.getConnection(url, user, password);
	}

	/**
	 * Displays data from "Tagrisso" table in "myDBS" database in mySQL.
	 * 
	 * @param con Represents connection to the database.
	 * @throws SQLException if a error in logging into database or if connection is
	 *                      closed.
	 */
	private static void printData(Connection con) throws SQLException {
		// Create a Statement object that sends SQL statements to database.
		Statement statement = con.createStatement();

		// SQL Query to get data from "Tagrisso" table.
		String sql = "SELECT * FROM Tagrisso";
		// ResultSet object holds data retrieved by query.
		ResultSet rs = statement.executeQuery(sql);

		// Iterate through the result set and print out each row; next() method moves
		// cursor to next row.
		while (rs.next()) {
			// Stores data by column name into local variables.
			int patientId = rs.getInt("Patient_ID");
			int age = rs.getInt("Age");
			String sex = rs.getString("Sex");
			String cancerType = rs.getString("Cancer_Type");
			String drugName = rs.getString("Drug_Name");
			int dosage = rs.getInt("Dosage");
			int treatmentDuration = rs.getInt("Treatment_Duration");
			double drugEfficacy = rs.getDouble("Drug_Efficacy");
			String sideEffects = rs.getString("Side_Effects");

			System.out.println("Patient ID: " + patientId + ", Age: " + age + ", Sex: " + sex + ", Cancer Type: "
					+ cancerType + ", Drug Name: " + drugName + ", Dosage: " + dosage + ", Treatment Duration: "
					+ treatmentDuration + ", Drug Efficacy: " + drugEfficacy + ", Side Effects: " + sideEffects);
		}
		// Close result set and statement so the database can release resources.
		rs.close();
		statement.close();
	}

	/*
	 * Calculates and displays the average drug efficacy by age groups from
	 * "Tagrisso" table.
	 * 
	 * @param con Represents connection to the database.
	 * 
	 * @throws SQLException if a error in logging into database or if connection is
	 * closed.
	 */
	private static void drugEfficacyByName(Connection con) throws SQLException {
		// Create a Statement object that sends SQL statements to database.
		Statement statement = con.createStatement();

		// SQL Query that calculates the avg drug efficacy by ages: 40s, 50s, 60s, 70s,
		// 80s.
		String avgEfficacyByAgeGroupSql = "SELECT " + "  CASE " // Evaluates each condition in listed order.
				+ "    WHEN Age BETWEEN 40 AND 49 THEN '40s' " // Check if age is 40-49, then classify as 40s.
				+ "    WHEN Age BETWEEN 50 AND 59 THEN '50s' " + "    WHEN Age BETWEEN 60 AND 69 THEN '60s' "
				+ "    WHEN Age BETWEEN 70 AND 79 THEN '70s' " + "    WHEN Age >= 80 THEN '80s' " // Check if age is
				// above 80, then
				// classify as 80s.
				+ "  END AS AgeGroup, " // Ends by assigning 'AgeGroup' to the result.
				+ "  ROUND(AVG(Drug_Efficacy),3) as AvgEfficacy " // Calculates the avg drug efficacy, rounds to 3
				// decimals.
				+ "FROM Tagrisso " + "GROUP BY AgeGroup " // Groups the results based on age group, resulting in
				// separate drug efficacy calculation for each age group.
				+ "ORDER BY AgeGroup"; // Puts results in order of ascending age groups.

		// ResultSet object holds data retrieved by query.
		ResultSet rs = statement.executeQuery(avgEfficacyByAgeGroupSql);

		System.out.println("\nAverage Drug Efficacy by Age Group:");
		// Iterate through the result set and print out each row; next() method moves
		// cursor to next row.
		while (rs.next()) {
			// Stores data by column name into local variables.
			String ageGroup = rs.getString("AgeGroup");
			double avgEfficacy = rs.getDouble("AvgEfficacy");
			System.out.println("Age Group: " + ageGroup + ", Average Efficacy: " + avgEfficacy);
		}

		// Close result set and statement so the database can release resources.
		rs.close();
		statement.close();
	}

	/**
	 * Calculates and displays the average drug efficacy by sex from the "Tagrisso"
	 * table.
	 * 
	 * @param con Represents connection to the database.
	 * @throws SQLException if a error in logging into database or if connection is
	 *                      closed.
	 */
	private static void drugEfficacyBySex(Connection con) throws SQLException {
		// Create a Statement object that sends SQL statements to the database.
		Statement statement = con.createStatement();

		// SQL Query that calculates the avg drug efficacy by sex.
		String avgEfficacyBySexSql = "SELECT " + "  Sex, " + // Selects the Sex column in the table.
				"  ROUND(AVG(Drug_Efficacy), 3) as AvgEfficacy " + "FROM Tagrisso " + "GROUP BY Sex"; // Groups the
		// results based
		// on sex.
		ResultSet rs = statement.executeQuery(avgEfficacyBySexSql);

		System.out.println("\nAverage Drug Efficacy by Sex:");
		while (rs.next()) {
			String sex = rs.getString("Sex");
			double avgEfficacy = rs.getDouble("AvgEfficacy");
			System.out.println("Sex: " + sex + ", Average Efficacy: " + avgEfficacy);
		}

		rs.close();
		statement.close();
	}

	/**
	 * Calculates and displays the average drug efficacy by treatment duration for
	 * less than and greater than 90 days from the "Tagrisso" table.
	 * 
	 * @param con Represents connection to the database.
	 * @throws SQLException if a error in logging into database or if connection is
	 *                      closed.
	 */
	private static void drugEfficacyByTreatmentDuration(Connection con) throws SQLException {
		// Create a Statement object that sends SQL statements to the database.
		Statement statement = con.createStatement();

		// SQL Query that divides treatment duration into two groups and calculates the
		// average drug efficacy.
		String avgEfficacyByTreatmentDurationSql = "SELECT " + "  CASE "
				+ "    WHEN Treatment_Duration < 90 THEN 'Less than 90 days' " + // Check if treatment duration <90
				// days, then classify as less than
				// 90 days.
				"    ELSE 'Greater than 90 days' " + // Remaining entries are greater than 90 days
				"  END AS DurationGroup, " + // Ends by assigning 'DurationGroup' to the result.
				"  ROUND(AVG(Drug_Efficacy), 3) as AvgEfficacy " + // Calculates the avg drug efficacy, rounds to 3
				// decimals.
				"FROM Tagrisso " + "GROUP BY DurationGroup"; // Groups the results based on the treatment duration
		// grouping

		ResultSet rs = statement.executeQuery(avgEfficacyByTreatmentDurationSql);

		System.out.println("\nAverage Drug Efficacy by Treatment Duration:");
		while (rs.next()) {
			String durationGroup = rs.getString("DurationGroup");
			double avgEfficacy = rs.getDouble("AvgEfficacy");
			System.out.println("Duration Group: " + durationGroup + ", Average Efficacy: " + avgEfficacy);
		}

		rs.close();
		statement.close();
	}

	/**
	 * Method that runs steps required for regression analysis and model evaluation.
	 * 
	 * @param con Represents connection to the database.
	 * @throws SQLException if a error in logging into database or if connection is
	 *                      closed.
	 */
	private static void performRegression(Connection con) throws SQLException {
		// Stores result of getData method to into a list.
		List<double[]> allData = getData(con);

		// Shuffling the data to make sure the same data is not split into training and
		// test each time.
		Collections.shuffle(allData);

		// Calculate the point to split data into training (%80) and test (20%) sets
		int split = (int) (allData.size() * 0.8);

		// Split the data into training and testing sublists based on split index
		List<double[]> trainingData = allData.subList(0, split);
		List<double[]> testingData = allData.subList(split, allData.size());

		// Creates a 2D array of independent variables (age, sex, and treatment
		// duration) for training set
		double[][] independentTraining = getXVar(trainingData);
		// Creates an array for drug efficacy for training set
		double[] drugEfficacyTraining = getDrugEfficacy(trainingData);

		// Creates a 2D array of independent variables testing set
		double[][] independentTesting = getXVar(testingData);
		// Creates an array for drug efficacy for testing set
		double[] drugEfficacyTesting = getDrugEfficacy(testingData);

		// OLSMultipleLinearRegression object to access methods in library
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

		// Load the y variable data and x variable data into the model
		regression.newSampleData(drugEfficacyTraining, independentTraining);

		// Using the trained model to make predictions on the test data
		double[] predictions = predict(regression, independentTesting);

		// Evaluating the performance of the model on the test data
		evaluate(predictions, drugEfficacyTesting);
	}

	/**
	 * Populates the patient data from the table in the mySQL database into an
	 * array.
	 * 
	 * @param con Represents connection to the database.
	 * @return a list of arrays containing patient data from "Tagrisso" table
	 * @throws SQLException if a error in logging into database or if connection is
	 *                      closed.
	 */
	private static List<double[]> getData(Connection con) throws SQLException {
		// ArrayList of data will contain arrays of patient data by "Tagrisso" table
		// rows.
		List<double[]> data = new ArrayList<>();
		// SQL query to select necessary columns.
		String sql = "SELECT Age, Sex, Treatment_Duration, Drug_Efficacy FROM Tagrisso";

		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery(sql);

		while (rs.next()) {
			// Iterate through the result set and store each patient attribute in local
			// variable.
			double age = rs.getDouble("Age");
			// Converts sex to a double value.
			double sex;
			if (rs.getString("Sex").equals("Male")) {
				sex = 0.0;
			} else {
				sex = 1.0;
			}
			double duration = rs.getDouble("Treatment_Duration");
			double efficacy = rs.getDouble("Drug_Efficacy");
			// Adding the patient data as an array to the list
			data.add(new double[] { age, sex, duration, efficacy });
		}
		return data; // Returning the list of data arrays

	}

	/**
	 * Extracts patient data for desired independent variable values.
	 * 
	 * @param data is a 2D array containing patient data from "Tagrisso" table
	 * @return a 2D array containing independent variable values
	 */
	private static double[][] getXVar(List<double[]> data) {
		// 2D array for independent variable values; Rows are Age, Sex and Treatment
		// Duration.
		double[][] x = new double[data.size()][3];
		// For loop to store desired independent variable attributes in array
		for (int i = 0; i < data.size(); i++) {
			x[i][0] = data.get(i)[0]; // Age
			x[i][1] = data.get(i)[1]; // Sex
			x[i][2] = data.get(i)[2]; // Treatment Duration
		}
		return x;
	}

	// Method to extract drug efficacy from the data
	/**
	 * Extracts patient data for desired dependent variable value.
	 * 
	 * @param data is a 2D array containing patient data from "Tagrisso" table
	 * @return an array storing drug efficacy values for each patient
	 */
	private static double[] getDrugEfficacy(List<double[]> data) {
		// Array for storing drug efficacy of data entry size
		double[] y = new double[data.size()];
		// For loop to store drug efficacy in array
		for (int i = 0; i < data.size(); i++) {
			y[i] = data.get(i)[3];
		}
		return y;
	}

	/**
	 * Make predictions for remainder of data (testing data) using the trained
	 * regression model.
	 * 
	 * @param regression         object to access methods in
	 *                           OLSMultipleLinearRegression library
	 * @param independentTesting a 2D array containing independent variable values
	 * @return an array with values of predictions for the testing data.
	 */
	private static double[] predict(OLSMultipleLinearRegression regression, double[][] independentTesting) {
		// Array to store the predictions for 20% of the data
		double[] predictions = new double[independentTesting.length];
		for (int i = 0; i < independentTesting.length; i++) {
			// Starting prediction with the intercept, which is the constant
			double prediction = regression.estimateRegressionParameters()[0];
			for (int j = 0; j < independentTesting[i].length; j++) {
				// Iterates through calculating regression coefficients and multiplies by the
				// testing data point value
				prediction += regression.estimateRegressionParameters()[j + 1] * independentTesting[i][j];
			}
			predictions[i] = prediction; // Stores prediction
		}
		return predictions; // Returning the array of predictions
	}

	/**
	 * Evaluates the performance of the regression model by calculating mean squared
	 * error.
	 * 
	 * @param predictions         is an array with predicted drug efficacy for the
	 *                            testing data
	 * @param drugEfficacyTesting is an array with the actual drug efficacy of the
	 *                            testing data
	 * 
	 */
	private static void evaluate(double[] predictions, double[] drugEfficacyTesting) {
		double mse = 0.0;
		for (int i = 0; i < predictions.length; i++) {
			// Summation of squared difference between prediction and actual value
			mse += Math.pow(predictions[i] - drugEfficacyTesting[i], 2);
		}
		mse /= predictions.length; // Averaging the Mean Squared Error
		System.out.println("\nMean Squared Error: " + mse);
	}

	/**
	 * Close the connection to the database.
	 * 
	 * @param con Represents connection to the database.
	 * @throws SQLException if a error in logging into database.
	 */
	private static void closeConnection(Connection con) throws SQLException {
		if (con != null && !con.isClosed()) {
			con.close();
		}
	}

	public static void main(String[] args) {
		// Details for database connection.
		String url = "jdbc:mysql://localhost:3306/myDBS";
		String user = "root";
		String password = "Joshua@529";

		try {
			Connection con = createConnection(url, user, password);

			printData(con);

			drugEfficacyByName(con);
			drugEfficacyBySex(con);
			drugEfficacyByTreatmentDuration(con);

			performRegression(con);

			closeConnection(con);
		} catch (SQLException e) {
			System.out.println("Exception is: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver is not found: " + e.getMessage());
		}
	}
}
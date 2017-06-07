package assignment3_EoinClancy;

/*
 * Class used for storing basic result information for a single test case
 * Stores: 1. Classifier result - TRUE/FALSE
 * 		   2. Model Prediction - class predicted by the model
 * 		   3. Actual Class - The actual class of the test case	
 */

public class TestResult {
	private boolean result;
	private String predicted;
	private String actual;
	
	public TestResult(boolean res, String pred, String act){
		this.result = res;
		this.predicted = pred;
		this.actual = act;
	}

	//Getters and Setters
	
	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getPredicted() {
		return predicted;
	}

	public void setPredicted(String predicted) {
		this.predicted = predicted;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}
	
}

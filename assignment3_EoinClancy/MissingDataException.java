package assignment3_EoinClancy;

public class MissingDataException extends Exception {
	/**
	 * Exception used for catching the case where the csv data has missing values
	 */
	private static final long serialVersionUID = 1L;

	public MissingDataException(String msg) {
		super(msg);
	}
}

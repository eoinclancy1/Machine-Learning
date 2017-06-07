package assignment3_EoinClancy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/*
 * This class is used for performing data manipulation of a 2D String array
 * 	Methods: sortColumn() - Sort the entire data set based on one column
 * 			 bubbleSort() - Sorting algorithm for moving the data to final location - sorts in ascending order
 * 			 sortByThreshold() - Data is already sorted by the column. 
 * 									Returns the data set that is below/above the threshold based on the parameter passed in 
 * 			 shuffleData() - Randomly shuffles the rows of the data set
 * 			 trainingData() - Returns a training set of data based on the size specified
 * 			 testData() - 	Returns a test set of data based on the size specified
 * 			 
 */

public class SortData {
	
	private int col=0;
	private int row=0;
	private String[][] data;
	
	public SortData (int NumCol, int NumRow, String[][] GivenData){
		this.row = NumRow;
		this.col = NumCol;
		this.data = GivenData;
		
	}
	
	//Sort the entire data set based on one column
	public String[][] sortColumn(int colNum){
		String[] colToOrderBy = new String[row];
		
		for(int k=0; k<row ; k++){
			colToOrderBy[k] = data[k][colNum];			//Stores the column that is to be sorted
		}

		BubbleSort(colToOrderBy);						//Sorting the entire data set based on the column

		return data;
	}
	
	//Sorting algorithm for moving the data to final location
	//Sort the data in ascending order
	private void BubbleSort(String[] array) {
	    String[] t;
	    String t2; 

	    for(int i=1; i<array.length; i++) {								//Start at 1 to avoid ordering the title
	    	for(int j=1; j<array.length-i; j++) {

		        if(Double.parseDouble(array[j]) > Double.parseDouble((array[j+1]))) { //If two values are out of position, then swap
		        	t= data[j];
		            t2 = array[j];
		            data[j] = data[j+1];
		            array[j] = array[j+1];
		            data[j+1] = t;
		            array[j+1] = t2;
		        }
	    	}
	    }
	
	}	
	

	//Data is already sorted by the column. 
	  //Returns the data set that is below/above the threshold based on the parameter LTorGT passed in
	public String[][] sortByThreshold(String[][] data, double threshold, int colNum, int LTorGT){
		
		double currIndex;
		
		//To get the data set when evaluating the less than or equal to threshold part
		if(LTorGT == 0){
			
			if(data.length == 2){		//Accounts for 1 attribute
				return data;
			}
			else if(Double.parseDouble(data[data.length-1][colNum]) == threshold){		//case where the threshold is the very last value
				return data;															//return a probability node 
			}
			
			boolean foundAboveThresh = false;
			int count = 1;
			while(!foundAboveThresh){								//Loop until a case above the threshold is found
				if(count == data.length){							//Ran into case where threshold was near end -> outOfBoundsException
					return null;
				}
				currIndex = Double.parseDouble(data[count][colNum]);	//Get the next attribute of the specified column

				if(currIndex > threshold){								//If the current value is greater than the threshold - found all lower points
					foundAboveThresh = true;							//Break while loop
				}
				count++;
			}
			String[][] ltThreshold = new String[count-1][data[0].length];	//Num rows up to that point
			System.arraycopy(data, 0, ltThreshold, 0, ltThreshold.length);	//Duplicate the data values that are less than the threshold
			return ltThreshold;												//returns the array containing the values less than the threshold
		}
		
		
		//To get the dataset when evaluating the greater than threshold part
		else{
			boolean foundAboveThresh = false;
			int count = 1;
			while(!foundAboveThresh){									//Loop until a case above the threshold is found
				if(count == data.length){								//Ran into case where threshold was near end -> outOfBoundsException
					return null;
				}
				currIndex = Double.parseDouble(data[count][colNum]);	//Get the next attribute of the specified column

				if(currIndex > threshold){								//If the current value is greater than the threshold - found all lower points
					foundAboveThresh = true;
				}
				count++;
			}
			String[][] gtThreshold = new String[(data.length-count+2)][data[0].length];	//Num rows req: +1 to negate last count++, +1 to get title
			System.arraycopy(data, 0, gtThreshold, 0, gtThreshold.length);				//Placing the title in row 0 - last row blank
			System.arraycopy(data, (count-1), gtThreshold, 1, gtThreshold.length-1);	//Rest of data pasted in
			return gtThreshold;															//Return the array containing the cases above the threshold
			
		}
		
		
	}
	
	//Randomly shuffles the rows of the data set
	public String[][] shuffleData(String[][] data){
		Random rand = new Random();
		int index;
		for(int i = data.length-1; i>1 ; i--){	//Loop over all data - except the title row (row 0)
			index = rand.nextInt(i+1); 			//Random number generated is bound by parameter (i+1)
			while (index == 0){
				index = rand.nextInt(i+1); 		//Don't want to shuffle the title column - row 0
			}
			String[] temp = data[index];
			data[index] = data[i];
			data[i] = temp;
		}
		return data;							//Return the shuffled data set
	}
	
	
	//Returns a training set of data based on the size specified
	//Data passed to this has been previously shuffled so just return the first n elements
	public String[][] trainingData(String[][] data, int size){
		String[][] trainingData = new String[size+1][];
		System.arraycopy(data, 0, trainingData, 0, trainingData.length);
		return trainingData;
	}
	//Returns a test set of data based on the size specified
	//Data passed to this has been previously shuffled and a training set extracted so
		//just take the elements at the end that have not been used for training
	public String[][] testData(String[][] data, int size){
		String[][] testData = new String[size+1][];
		testData[0] = data[0];			//Assigning the title row
		System.arraycopy(data, (data.length-1-size), testData, 1, testData.length-1);
		return testData;
	}
	
	
	public String[][] sortByThreshold(String[][] data, double threshold, String classToSortBy){
		return null;
	}
	
}

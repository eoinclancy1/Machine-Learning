package assignment3_EoinClancy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/*
 * This class is used to calculate entropy and information gain
 * 	Methods: getThreshold() - Main Method of class - Finds the best threshold for splitting the data based on the column information given
 * 			 getTotalEntropy() - Method to get the entropy of the labels/classes (i.e the classes the model is trying to predict)
 * 			 getEntropyAndGain() - Calculates the entropy and information gain of the data stored by the class based 
 * 									upon the threshold and column information passed in, returns the information gain to calling method
 * 			 getInformationGain() - Method to calculate the information gain
 * 			 log() - used to generate log of a number with a base specified by the other input
 * 			  
 */

public class InformationGain {

	private String[][] data;
	private int col;
	private int row;
	private ArrayList<String> classes = new ArrayList<String>();
	private double totalEntropy;
	
	//Constructor
	public InformationGain(int NumCol, int NumRow, String[][] dataSet){
		this.data = dataSet;		
		this.col = NumCol;
		this.row = NumRow;
	}
	
	
	//Finds the best threshold for splitting the data based on the column information given
	//Returns a node which stores the attribute, threshold and information gain
	public Node getThreshold(int column){
		
		int i = 1;
		ArrayList<Double> thresholdArr = new ArrayList<Double>();
		double value;
		String label = "";

		while(i < row){										//Gets all possible threshold values
			
			label = data[i][col-1];							//Record all unique classes
			if (!classes.contains(label)){
				classes.add(label);
			}
			value = Double.parseDouble(data[i][column]);	//Record all possible threshold values		
			if (!thresholdArr.contains(value)){
				thresholdArr.add(value);
			}
			i++;
		}
		
		totalEntropy = getTotalEntropy();					//Gets the entropy of the dependent class/label
		
		HashMap<Double, Double> threshEntropy = new HashMap<Double, Double>(thresholdArr.size()); //Stores threshold,entropy pair
		int cntr = 0;
		double g;
		
		for(Double t : thresholdArr ){						//Loop over each possible threshold value
			g = getEntropyAndGain(t, column);				//Get the information gain value for passed threshold
			threshEntropy.put(t,g);							//Stores threshold,information gain pair
		}
		
		double maxGain = -1000;								//Default information gain value - can be used to determine case where node stores single class
		double bestThresh = -1000;							//Default starting threshold value
		double thresh;
		double gain;
		for (Entry<Double, Double> entry : threshEntropy.entrySet()) {	//Loop over each hash pair and determine the best threshold to use based on highest gain observed
		    thresh = entry.getKey();
		    gain = entry.getValue();
		    if(gain > maxGain){								
		    	maxGain = gain;								//Stores the highest information gain encountered in hash table
		    	bestThresh = thresh;						//Stores the associated threshold value for that information gain
		    }
		}

		return new Node(data[0][column],null, null, null, bestThresh, maxGain); //Return a node which has the attribute that best separates the data as the title
																				//	stores the associated threshold value and the information gain for reference
	}
	
	
	
	
	//Method to get the entropy of the labels/classes (i.e the class the model is trying to predict)
	//Where entropy is: E = Sum(over all classes 'c') -P(c)*log(2) P(c)
	//Returns the total Entropy to calling function
	private double getTotalEntropy() {
		int[] countOccEach = new int[classes.size()];	//Array to store num occurrences of each class
		
		for(int i: countOccEach){						//Confirming each location is 0
			countOccEach[i] = 0;
		}
		
		double entropy = 0;
		int loopCntr = 0;
		for(String s : classes){						//Count occurrences of each class. Loop over the classes
			for(int i=1 ; i<data.length; i++){			//i=1 to skip title. Loop over each case
				if (s.equals(data[i][col-1])){			//Check if current case matches the current class being searched for
					countOccEach[loopCntr]++;			//Increment counter on match
				}
			}
			double occurances = countOccEach[loopCntr];			//Store the total number of occurrences of each
			double dataSize = (data.length-1);					//Accounting for the column headers
			double fraction = occurances/dataSize;
			entropy -= fraction* this.log(fraction, 2);			//Calculating the overall entropy. log() get the base2 of fraction
			loopCntr++;
		}
		return entropy;
	}

	
	//Calculates the entropy and information gain of the data stored by the class
		//based upon the threshold and column information passed in
	//Returns the calculated information gain to the calling function
	public double getEntropyAndGain(Double threshold, int column){

		double gtSize = 0;					//Total num of elements greater than the threshold
		double ltSize = 0;					//Total num of elements less than or equal to the threshold
		ArrayList<String[]> lessThan = new ArrayList<String[]>();		//Stores the elements less than or equal to the threshold
		ArrayList<String[]> greaterThan = new ArrayList<String[]>();	//Stores the elements greater than the threshold	

		
		for(int i=0 ; i<row-1 ; i++){									//Sort the data into lists based on position about threshold
			
			if (Double.parseDouble(data[i+1][column]) <= threshold){
				lessThan.add(data[i+1]);
			}
			else{
				greaterThan.add(data[i+1]);
			}
		}
		float entropyLt = 0;											
		float entropyGt = 0;
		
		/**** Less than or equal to threshold calculations ****/
		for(int i=0 ; i<classes.size() ; i++ ){						//looping over possible classes
			String current = classes.get(i);						//store class being used currently
			int currentClassCntr = 0;								//count number of occurrences 
			
			for(int j=0 ; j<lessThan.size(); j++){					//Loop over elements less than the threshold
				String[] s = lessThan.get(j);
				if (s[col-1].equals(current)){						//check if cases class equals current class being checked for
					currentClassCntr++;								//increment if a match is found
				}
			}
			
			double fraction;
			double occurances = currentClassCntr;
			ltSize = lessThan.size();
			if (occurances > 0){									//If there are occurrences of that class then calculate the entropy
				fraction = occurances/ltSize;
				entropyLt -= (fraction)* this.log(fraction, 2);		//Sums the total entropy for all less than threshold cases
			}
			else{													//If no occurrences - no effect on entropy
				fraction = 0;
				entropyLt -= 0;
			}
			
		}
		
		/**** Greater than threshold calculations ****/
		for(int i=0 ; i<classes.size() ; i++ ){						//looping over possible classes
			String current = classes.get(i);						//store class being used currently
			int currentClassCntr = 0;								//count number of occurrences
			for(int j=0 ; j<greaterThan.size(); j++){				//Loop over elements greater than the threshold
				String[] s = greaterThan.get(j);
				if (s[col-1].equals(current)){						//check if cases class equals current class being checked for
					currentClassCntr++;								//increment if a match is found
				}
			}
			double fraction;
			double occurances = currentClassCntr;
			gtSize = greaterThan.size();
			if(occurances > 0){										//If there are occurrences of that class then calculate the entropy
				fraction = occurances/gtSize;
				entropyGt -= (fraction)* this.log(fraction, 2);		//Sums the total entropy for all less than threshold cases
			}
			else{													//If no occurrences - no effect on entropy
				fraction = 0;
				entropyGt -= 0;
			}
			 
		}
		//Calculate the entropy - provides a measure of how well the selected threshold divides the remaining data
		double InfoGain = getInformationGain(entropyLt, entropyGt,ltSize,gtSize );
	
		return InfoGain;
	
	}
	
	
	
	//Method to calculate the information gain - a measure of how well an attribute at a selected threshold divides the data
	//ltEnt stores the entropy of the cases less than the threshold and gtEnt the entropy of the cases above the threshold
	private double getInformationGain(double ltEnt, double gtEnt, double ltTotal, double gtTotal) {
		int numSamples = (data.length - 1);
		double gain = totalEntropy - ((ltEnt*ltTotal)/numSamples) - ((gtEnt*gtTotal)/numSamples);  //totalEntropy calculated in getTotalEntropy()
		return gain;														   //return the information gain to calling function
		
	}

	
	//Returns the log of the value passed in at the base specified
	private double log(double count, double base){
		return (Math.log(count)/Math.log(base));
	}
	
}

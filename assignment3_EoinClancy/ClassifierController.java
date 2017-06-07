package assignment3_EoinClancy;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import assignment3_EoinClancy.CSVdata;
import assignment3_EoinClancy.SortData;

/* 
 * This class is used to manage the implemented C4.5 algorithm
 * 	Methods: main() - Invokes all other methods required by the program
 * 			 treeClassifier() -Implements a C4.5 style tree classifier
 * 			 getProbabilityNode() - Returns a node which contains the possible classes and their respective probabilities
 */

public class ClassifierController {
	
	public static void main(String args[]){
		
		//Note regarding file format
		//1. File must be of type .csv
		//2. First row must contain the attributes with the class being the rightmost of all attributes
		//3. The data of each attribute is then listed under the relevant attribute
		//4. There should be no ID column in the data - unless it is to be explicitly used as a means of classifying
		
		/***** Note the .csv file location must be input in the format C:\\Users\\myaccount\\...\\sampleFile.csv****/
		
		//Owls.csv file
		CSVdata tester = new CSVdata("C:\\Users\\eoin\\Documents\\Eoin\\NUIG\\4th Year\\CT475 - Machine Learning + Data Mining\\Assignment 3\\owls15.csv");
		//ilness.csv file
		//CSVdata tester = new CSVdata("C:\\Users\\eoin\\Documents\\Eoin\\NUIG\\4th Year\\CT475 - Machine Learning + Data Mining\\Assignment 1\\illness.csv");
		
		
		String[][] data = tester.DatToArray();									//tester stores an arrayList of data - convert to 2D String array
		SortData sort = new SortData(tester.colCount, tester.rowCount, data);	//Create a new sorting object
		int numCases = tester.rowCount - 1;										//Number of cases in data set - one less than num rows
		
		System.out.println("Eoin Clancy - CT475 - Assignment 3");
		System.out.println("Data Set Loaded......");
		System.out.println("Awaiting training data size input....");
		int trainingSize = Integer.parseInt(
		           JOptionPane.showInputDialog("You currently have " + numCases + " cases!\n How many do you want to use for training?" ));
		
		int testSize = data.length -1 - trainingSize;							//Size of the test set
		System.out.println("Training size = " + trainingSize);	
		System.out.println("Test size = " + testSize);
		TestData td = new TestData();											//Used for evaluating the test cases vs the model produced
		double accuracy = 0;

										
		//Method of operation
			//User has determined the sizes of the training and test sets to be used
				//Repeat the following n (eg. 10) times
					//Shuffle the entire test set
					//Select training set and test set from the shuffled data
					//Run classifier on training data
					//Test the test data on the classifier
					//Output the results to console & .csv file

		
		for(int i=1; i<=10 ; i++){
			data = sort.shuffleData(data);										//Randomly shuffles the data
			String[][] trainingData = sort.trainingData(data, trainingSize);	//Get Training set
			String[][] testData = sort.testData(data, testSize);				//Get Test set
			Node n = treeClassifier(trainingData);								//Get the hypothesis (decision tree)
			ArrayList<TestResult> tr = td.testModel(n, testData);				//Test the test cases vs the decision tree
			accuracy += tester.outputToCSV(tr, i);								//Output results	
		}
		System.out.println("The average accuracy of the classifier is: " + (accuracy/10.0) + "%");
	}
	
	
	
	/*
	 * This method implements a C4.5 style tree classifier
	 * 		It depends on numerous methods from all other classes in this package for sorting data and 
	 * 			determining values like the entropy and information gain
	 * 		The object returned by this method is a 'Node' which itself is a tree with children, which based on
	 * 			the calculations performed best divides the data set and can be used to predict new test cases
	 * 
	 * 		Operation + Assumptions - The current implementation operates as follows (can be configured for fine tuning of accuracy)
	 * 			1. Where the data set is null, a null node is returned
	 * 			2. Where the data set is all of the one class, a node with that class is returned
	 * 			3. Where a data set contains a single element, a node is created for that element and returned
	 * 			4. Where a single, odd class exists amongst cases belonging to the one class, a probability node is returned
	 * 			5. Otherwise the data set can be divided further
	 * 				a. Get the highest value of the information gain for each attribute based on all possible thresholds
	 * 				b. Choose the attribute with the highest information gain
	 * 					This attribute is best able to divide the data and reduces entropy of the data set in doing so
	 * 				c. The best attribute is stored as a node (N) containing 1. Attribute name, 2. Threshold value, 3. Information Gain calculated
	 * 				d. Loop over data set, storing values less than or equal to the threshold of N
	 * 					Call the method recursively on this data set with all attributes available for the data to be split on
	 * 					The value returned is the left child
	 * 				e. Loop over data set, storing values greater than the threshold of N
	 * 					Call the method recursively on this data set with all attributes available for the data to be split on
	 * 					The value returned is the right child
	 * 				f. return node N - which stores a tree
	 */
	
	public static Node treeClassifier(String[][] data){	
	
		if(data==null || data.length == 0){								//Where the data set is null, a null node is returned
			return null;
		}
		
		int numColumns = data[0].length;								//One column is for classes
		int numAttributes = numColumns - 1;								//Number of attributes
		SortData sort = new SortData(numColumns, data.length, data); 	//Creating the sort object
		InformationGain test;											//Object used to reference all methods to calculate information gain
		Node n = null; 													//Used to store the node coming back from the infGain request
		Node best = new Node("Default",null, null, null, -1000, -1000);	//Default best node	
		String allAtt[] = new String[numColumns];						//Stores all attributes the cases are based upon
		

		if(data.length == 2){											//Case where a single attribute must be classified
			return new Node(data[1][numAttributes],null,null,null,-1000,-1000);
		}	
		
		
		int uniqueElements = 0; //If have less than 2 unique elements, will settle for probability rather than creating new node -> over-fitting
								//Check if all attributes are the same
								//Can adjust the accepted number of uniqueElements in this method to fine tune accuracy if required 
		
		for(int i=1;  i<(data.length-1) ; i++){ 		//Loop over all rows in data set
			
			if(   !(data[i][numAttributes].equals(data[i+1][numAttributes]))   ){		//Check if successive elements are of the same class
				if (uniqueElements > 2){												//Willing to accept a single, odd element ---- can be altered
					i=data.length; 														//break the for loop when >1 classes recognised
				}
				else{
					uniqueElements++;
				}
			}
			else if(i== (data.length-2) && uniqueElements == 0){							//Have checked all values
				return new Node(data[1][numAttributes],null,null,null,-1000,-1000);			//if all same, just return one of the case classes
			}
			else if(i== (data.length-2) && uniqueElements == 1){		//Accepting a probability node where the data has a single stray element
				Node probNode = getProbabilityNode(data, numAttributes);		//Can be adjusted to fine tune for accuracy
				return probNode;
			}
		}
		
		for(int a=0; a<numAttributes; a++){								//Get the column titles
			allAtt[a] = data[0][a];
		}
		
		for(int i=0; i<numAttributes; i++){								//Loop to get the best attribute and respective threshold
			data = sort.sortColumn(i);									//Sort data set based on single column
			test = new InformationGain(numColumns, data.length, data);	//Creating a new InformationGain object, passing in the newly sorted data set
			n = test.getThreshold(i);									//Returns a node containing the highest information gain attainable from that attribute
																				//generated within the method by testing all possible thresholds
			if(n.getInfoGain() >= best.getInfoGain()){					//Examine the new node coming back each time
				best = n;												//If the new node has a higher information gain than the current one, this 
			}																//becomes the new best node
			
		}
		
	
		//Find column no. of the best class to divide by
		int lc = 0;
		int colNumber = -1000;
		
		while(lc<numAttributes){
			if (allAtt[lc] == best.getClassTitle()){
				colNumber = lc;
			}
			lc++;
		}
		if(colNumber == -1000){
			System.out.print("Error! Attribute not found");
		}
		
		
		

		Node retLeftChild;
		Node retRightChild;
		String[][] threshData;
		
		//Case j=0: Loop over data set, storing values less than or equal to the threshold of the node 'best'
		// 					Call the method recursively on this data set with all attributes available for the data to be split on
		// 					The value returned is the left child
		//Case j=1: Loop over data set, storing values greater than the threshold of the node 'best'
		// 					Call the method recursively on this data set with all attributes available for the data to be split on
		// 					The value returned is the right child
		
		
		for(int j=0; j<2; j++){									//Loops over the data set 
																//First thing to do is generate the relevant data list
			data = sort.sortColumn(colNumber);					//Sorts the data set according to the attributes which best splits the data (ie highest infoGain)

			threshData = sort.sortByThreshold(data, best.getThreshold(), colNumber, j); //Generates the data set thats <= threshold or > threshold, based on j value
			
			if (threshData.length == 2){		//Data has been split on just one element - will cause overfitting - instead use probability
				best = getProbabilityNode(data, numAttributes);		//edit the best node - set it as a probability node
				return best;
			}
			else if(Double.parseDouble(data[data.length-1][colNumber]) == best.getThreshold()){ //case where the threshold is the highest attribute value possible
				best = getProbabilityNode(data, numAttributes);			//Get a probability node based on the current data set - update the value of the best node to it
				return best;
			}
			
			if(j==0){											//Trying to find the left child
				retLeftChild = treeClassifier(threshData);		//Run the tree classifier recursively on the data which is <= to the current 'best' Node threshold
				best.setLeftChild(retLeftChild);				//     A node is returned and set to the left child of the 'best' node
			}
			
			else{												//Trying to find the right child
				retRightChild = treeClassifier(threshData);		//Run the tree classifier recursively on the data which is greater than the current 'best' Node threshold
				best.setRightChild(retRightChild);				//     A node is returned and set to the right child of the 'best' node
			}
		}
		
		
		return best;											//Recursive calls completed, all children completed, return this node
	}
	 
	
	
	
	/* 
	 * This method is used to generate a probability node
	 * 		Returns a node which contains the possible classes and their respective probabilities
	 */
	public static Node getProbabilityNode(String[][] data, int numAttributes){
		
		//Count number of occurrences of each class
		String class1 = data[1][numAttributes];						//Stores the first possible class
		boolean foundSecond = false;
		String class2 = "Error!";
		double totalNumElements = data.length -1;
		double count1 = 1;											//Already examined class1 first element
		double count2 = 0;
		
		for(int i=2; i<data.length; i++){							//Loop over entire data set - first class already noted
			if (! (data[i][numAttributes].equals(class1)) ){		//Looping to find the second class
				class2 = data[i][numAttributes];
				foundSecond = true;
				count2++;											//count number of occurrences of class2
			}
			else{
				count1++;											//count number of occurrences of class1
			}
		}
		
		if (foundSecond == true){									//create and return the probability node	
			double prob1 = count1/totalNumElements;
			double prob2 = count2/totalNumElements;
			return new Node("Probability",class1,class2,prob1,prob2);
		}
		return null;												//return null in case where second class was not found
		
	}
}

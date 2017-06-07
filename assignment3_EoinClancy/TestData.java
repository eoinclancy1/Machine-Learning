package assignment3_EoinClancy;

import java.util.ArrayList;

/*
 * Class which predicts the class to be assigned to a test case based on the tree provided
 * 	Method: testModel()- Perform tree traversal and classification, based on a given test case 
 * 			Inputs: 1. Tree - A node object which itself is composed of the children which make up the decision tree
 * 	       			2. Data set - The test data used for evaluating the performance of the classifier with actual class tag
 */


public class TestData {

	// Null/Default constructor
	public TestData(){
		
	}
	
	/*
	 * Method tests each test case against the tree by performing a tree traversal and 
	 * 		generates a result for each case returned as an arrayList
	 */
	public ArrayList<TestResult> testModel(Node tree, String[][] testData){
		
		int numAttributes = testData[0].length - 1;							//All possible attributes the cases may have
		ArrayList<TestResult> allResults = new ArrayList<TestResult>();		//Store all results
		ArrayList<String> allAtt = new ArrayList<String>();					//Store all possible attributes
		
		//Store index location of each column title
		for(int a=0; a<numAttributes; a++){
			allAtt.add(testData[0][a]);
		}
		
		
		//Loop over entire test data set
		for(int i=1 ; i<testData.length ; i++){
			
			boolean converged = false;			//Value set to true when a class has been assigned to a test case
			Node curr = tree;					//The current node being analysed
			String attribute;					
			double threshold;					//The threshold on which the data is split
			int colNum;
			String prediction = "";				//Used to store the model's predicted class for the test case
			
			
			//Repeat while no class has been assigned to the test case
			while(!converged){
				attribute = curr.getClassTitle();		//Get the attribute upon which the current node relies
				threshold = curr.getThreshold();		//Get the threshold value upon which the current node relies
				colNum = allAtt.indexOf(attribute);		//Get the column number of the attribute within the test data set
				
				
				//Check if its a single class node
				if(curr.getThreshold() == -1000){				//Single class nodes are assigned -1000 threshold in model building stage	
					prediction = attribute;						//Attribute stores the class type
					converged = true;							//Class now assigned to test case
				}
				
				
				//Case where node is a probability node
				else if(attribute.equals("Probability")){
						if(curr.getProb1() > curr.getProb2()){	//In this case - assigning class with larger probability to the test case
								prediction = curr.getClass1();
								converged = true;
						}
						else{
								prediction = curr.getClass2();
								converged = true;
						}
				}
				
				//Case where node has children
				else{
					
					//Traversing left-hand side of tree when test case attribute is less than/equal to threshold value
					if(Double.parseDouble(testData[i][colNum]) <= threshold){			
							
							if(curr.getLeftChild().equals(null)){					//Case where child is null
									prediction = curr.getClassTitle();				//Test case receives class of current node
									converged = true;
							}							
							
							else{													//Case where the node is normal node with child node
									curr = curr.getLeftChild();						//Repeat while loop with child node
							}
						}
					
					//Traversing right-hand side of tree when test case attribute is greater than threshold value
					else{
							
							if(curr.getRightChild().equals(null)){					//Case where child is null
									prediction = curr.getClassTitle();				//Test case receives class of current node
									converged = true;
							}
							
							else{													//Case where the node is normal node with child
									curr = curr.getRightChild();
							}
					
					
					}//End of threshold if-else statement
				}//End of case if statement
			}//End of while
			
			String actual = testData[i][numAttributes];								//Getting the test case's actual class
			boolean res;															//Stores boolean result of model prediction
			
			res = ( (prediction.equals(actual))? true : false );					//check if predicted equals actual class
			
			TestResult result = new TestResult(res, prediction, actual);			//Generate the result object
			allResults.add(result);													//Add to list of results
		
		}//end of for which loops over all test data
	
		return allResults;															//Return the list of results to calling class
	}
	
}

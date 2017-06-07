package assignment3_EoinClancy;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/*
 * This class is used to perform multiple activities involving CSV files
 * 	Methods:	ReadCSVfile() - reads from the specified file location and returns an arrayList of arrayList<String>
 * 				DatToArray() - converts the above generated arrayList into a 2D String array
 * 				outputToCSV() - used to write the results to a 'TestResults' file in the project folder
 * 				printResults() - used to print the results to console while writing to CSV
 */

public class CSVdata {
	
	public int rowCount = 0;			//Store number of rows
	public int colCount = 0;			//Store number of columns
	public ArrayList<ArrayList<String>> Rs = new ArrayList<ArrayList<String>>(); //Store all data first time read in
	public FileWriter wr;				//Used for outputting results
	
	
	public CSVdata(String fileLocation){								//Constructor takes data set location as input
			CSVFile Rd = new CSVFile();									//Instance of inner class
			File DataFile = new File(fileLocation);
			ArrayList<ArrayList<String>> Rs2 = Rd.ReadCSVfile(DataFile);	//Copy of Rs Object
	}
	
    /*
     * Method for reading CSV file
     * 	The method uses a buffered reader to read from the specified csv file and stores data following
     * 		The recognition of a particular set of characters (eg. ',' which represents a break)
     * 	Based on an adaption of the class written by StackOverflow User 'Priyesh'
     * 		at StackOverflow location - http://stackoverflow.com/questions/22864095/reading-data-from-a-specific-csv-file-and-displaying-it-in-a-jtable
     */
	public class CSVFile {
        
        private final ArrayList<String[]> allData = new ArrayList<String[]>();
        private String[] OneRow;										  //Rows									
        private int numCols;
        private boolean first = true;
        private int count = 0;
        private int colNum = 0;
        										//Keeps track of the number of rows
        
        
        public ArrayList<ArrayList<String>> ReadCSVfile(File DataFile) {
            try {
                BufferedReader brd = new BufferedReader(new FileReader(DataFile));	//Reads data from the csv file at location specified
                while (brd.ready()) {												//Loop while data available			
                    String st = brd.readLine(); 									//Reads in the entire row
                    OneRow = st.split(",|\\s|;");									//Split row on delimiters
                    										
                    if (first == true){												//Setting up data structure for first pass in loop
                    	numCols = OneRow.length;									//Get number of columns in the dataset (i.e no.attr + class var)
                    	while(count < numCols){										//Add a new ArrayList to overall list for each column of data
                    		Rs.add(new ArrayList<String>());
                    		count++;
                    	}
                    	first=false;												//Only execute the configuration if statement once
                    }
                    
                    for(ArrayList<String> col: Rs){									//Add the elements to their relevant column/arrayList
                    	
                    	if (OneRow[colNum] == null || OneRow[colNum].length() == 0){					//Check data-set for missing data
                    		throw new MissingDataException("Data missing! See column: " + (colNum+1));	//Custom exception thrown where the data-set has missing values
                    	}
                    	allData.add(OneRow);
                    	col.add(OneRow[colNum]);
                    	colNum++;                  
                    }
                    colNum = 0;														//Reset for next pass
                    rowCount++;
                } // end of while
                brd.close();														//Close the stream
            } // end of try
            
            catch (MissingDataException ex){
            	String errMsg = ex.getMessage();
            	System.out.println(errMsg);
            }
            catch (Exception e) {
                String errmsg = e.getMessage();
                System.out.println("File not found:" + errmsg);
            } // end of Catch
            return Rs;
        }// end of ReadFile method
    }// end of CSVFile class
    
    
    
    //Method to convert ArrayList<ArrayList<String>> to String[][]
    //Note: Didn't do in ReadCSVFile() as would need to create n (no. cols) arrays
    public String[][] DatToArray(){
		int cols = Rs.size();
		colCount = cols;
		int rows = rowCount;
		String[][] allData = new String[rows][cols];
		
		int row = 0;
		int col = 0;
			for(ArrayList<String> ListColVal : Rs){
				while(row < rows){
					allData[row][col] = ListColVal.get(row);
					row++;
					
				}
				col++;
				row=0;

			}
		return allData;
    }
    
    
    //Method writes the given data to the specified csv file
    	//CSV file will be stored in project folder
    public double outputToCSV(ArrayList<TestResult> results, int iter){
    	
    	double countTrue = 0;					//Count the number of correctly classed test cases
    	double numElements = results.size();	//Total number of test cases
    	int i = 0;
    	double accuracy = 0;						//Stores the accuracy of the model in % form
    	
    	if(iter == 1){
    		try{
    			wr = new FileWriter("TestResults.csv");	
    			
    			wr.append("Run " + iter);					//Write the Run data to the csv file
    			wr.append('\n');
    			wr.append("Result");
    			wr.append(',');
    			wr.append("Predicted Class");
    			wr.append(',');
    			wr.append("Actual Class");
    			wr.append('\n');
    			for (TestResult t : results){				//Write the results to the csv
    				wr.append(String.valueOf(t.isResult()));
    				wr.append(',');
    				wr.append(t.getPredicted());
    				wr.append(',');
    				wr.append(t.getActual());
    				wr.append('\n');
    				if(t.isResult()){
    					countTrue++;
    				}
    			}
    			while(i<5){									//Write the results summary to the csv
    				wr.append(',');
    				i++;
    			}
    			wr.append("Number correctly classified = " + countTrue);
    			wr.append(',');
    			wr.append("Number incorrectly classified = " + (numElements-countTrue));
    			wr.append(',');
    			accuracy = (countTrue/numElements) * 100;
    			wr.append("Accuracy = " + accuracy + "%");
    			wr.append('\n');
    			wr.close();									//Close the file writer
    			printResults(iter, countTrue, (numElements-countTrue), accuracy ); //Print the results summary to console
    		}
    		catch(IOException ex){
    			ex.printStackTrace();
    			System.out.println("! Check that the csv file is closed !");
    		}
    	}
    	else{
    		try {
				wr = new FileWriter("TestResults.csv",true);	//Write the Run data to the csv file
				wr.append('\n');
				wr.append('\n');
    			wr.append("Run " + iter);
    			wr.append('\n');
				wr.append("Result");
    			wr.append(',');
    			wr.append("Predicted Class");
    			wr.append(',');
    			wr.append("Actual Class");
    			wr.append('\n');
    			for (TestResult t : results){					//Write the results to the csv
    				wr.append(String.valueOf(t.isResult()));
    				wr.append(',');
    				wr.append(t.getPredicted());
    				wr.append(',');
    				wr.append(t.getActual());
    				wr.append('\n');
    				if(t.isResult()){
    					countTrue++;
    				}
    			}
    			while(i<5){										//Write the results summary to the csv
    				wr.append(',');
    				i++;
    			}
    			wr.append("Number correctly classified = " + countTrue);
    			wr.append(',');
    			wr.append("Number incorrectly classified = " + (numElements-countTrue));
    			wr.append(',');
    			accuracy = (countTrue/numElements) * 100;
    			wr.append("Accuracy = " + accuracy + "%");
    			wr.append('\n');
    			wr.close();										//Close the file writer
    			printResults(iter, countTrue, (numElements-countTrue), accuracy );	//Print the results summary to console
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return accuracy;  	
    }

    //Method to print the results to the console
	private void printResults(int runNo, double t, double f, double accuracy) {
		System.out.println("Run no. " + runNo + ": Accuracy is " + accuracy + "%");
		System.out.println("\t No. correctly classified " + t + " & No. incorrectly classified " + f);
		
	}
    
    
}

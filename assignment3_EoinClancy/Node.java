package assignment3_EoinClancy;

/*
 * Class used to create Nodes which can be used to build trees
 * Full Node features: Single parent node, 2 child nodes, node title, 
 * 					threshold, information gain.
 * 
 * Can serve as probability node as well
 * Features of probability nodes: store possible classes and respective probabilities
 */

public class Node {
	private String ClassTitle;	//Node title - same as data set attribute
	private Node parent;
	private Node rightChild;
	private Node leftChild;
	private double threshold; 	//Value used to split the data 
	private double infoGain;	//Information gain calculated
	private String class1;		//For probability node: name of first possible class
	private String class2;		//For probability node: name of second possible class
	private double prob1;		//Probability of first class
	private double prob2;		//Probability of second class
	
	//Constructor to create full node
	public Node(String ClassTitle, Node par, Node rChild, Node lChild, double thresh, double infGain){
		this.ClassTitle = ClassTitle;
		this.parent = par;
		this.rightChild = rChild;
		this.leftChild = lChild;
		this.threshold = thresh;
		this.infoGain = infGain;
	}
	
	//Account for case where probability node is required - reduces risk of over-fitting
	public Node(String ClassTitle, String class1, String class2, double prob1, double prob2){
		this.ClassTitle = ClassTitle;
		this.class1 = class1;
		this.class2 = class2;
		this.prob1 = prob1;
		this.prob2 = prob2;
	}

	//Getters and Setters
	
	public String getClassTitle() {
		return ClassTitle;
	}

	public void setClassTitle(String classTitle) {
		ClassTitle = classTitle;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	public Node getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public double getThreshold() {
		return threshold;
	}

	public String getClass1() {
		return class1;
	}
	public void setClass1(String class1) {
		this.class1 = class1;
	}
	public String getClass2() {
		return class2;
	}
	public void setClass2(String class2) {
		this.class2 = class2;
	}
	public double getProb1() {
		return prob1;
	}
	public void setProb1(double prob1) {
		this.prob1 = prob1;
	}
	public double getProb2() {
		return prob2;
	}
	public void setProb2(double prob2) {
		this.prob2 = prob2;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getInfoGain() {
		return infoGain;
	}

	public void setInfoGain(double infoGain) {
		this.infoGain = infoGain;
	}
}

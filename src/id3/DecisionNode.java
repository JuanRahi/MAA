package id3;

public class DecisionNode extends Node {
	public int attributeIndex;
	public Node[] nodes;
	public String[] attributeValues;
        
        public DecisionNode(int attribute, Node[] nodes, String[] attributeValues){
            this.attributeIndex = attribute;
            this.nodes = nodes;
            this.attributeValues = attributeValues;
        }
}

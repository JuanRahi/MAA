package id3;

public class DecisionNode extends Node {
	public int attribute;
	public Node[] nodes;
	public String[] attributeValues;
        
        public DecisionNode(int attribute, Node[] nodes, String[] attributeValues){
            this.attribute = attribute;
            this.nodes = nodes;
            this.attributeValues = attributeValues;
        }
}

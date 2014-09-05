package id3;

public class DecisionTree {
	String []allAttributes;
	Node root = null;

	public void print() {
		System.out.println("DECISION TREE");
		String indent = " ";
		print(root, indent, "");
	}

	private void print(Node nodeToPrint, String indent, String value) {
		String newIndent = indent + "  ";		
		if(nodeToPrint instanceof ClassNode){
			ClassNode node = (ClassNode) nodeToPrint;
			System.out.println(indent + value + "="+ node.className);
		}else{
			DecisionNode node = (DecisionNode) nodeToPrint;
			System.out.println(indent + allAttributes[node.attribute] + "->");
			
			for(int i=0; i< node.nodes.length; i++){
				print(node.nodes[i], newIndent, node.attributeValues[i]);
			}
		}
		
	}

	public String predictTargetAttributeValue(String[] newInstance) {
		return predict(root, newInstance);
	}


	private String predict(Node currentNode, String[] newInstance) {
		if(currentNode instanceof ClassNode){
			ClassNode node = (ClassNode) currentNode;
			return node.className;
		}else{
			DecisionNode node = (DecisionNode) currentNode;
			String value = newInstance[node.attribute];
			for(int i=0; i< node.attributeValues.length; i++){
				if(node.attributeValues[i].equals(value)){
					return predict(node.nodes[i], newInstance);
				}
			}
		}
		return null; 
	}
	
	

}

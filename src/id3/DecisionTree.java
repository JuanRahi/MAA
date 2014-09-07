package id3;

public class DecisionTree {
	String []allAttributes;
	Node root = null;

	public void print() {
		System.out.println("DECISION TREE");
		String indent = " ";
		print(root, indent, "", 1);
	}

	private void print(Node nodeToPrint, String indent, String value, int level) {
		String newIndent = indent + "  ";		
		if(nodeToPrint instanceof ClassNode){
			ClassNode node = (ClassNode) nodeToPrint;			
                        System.out.println("\033[31m" + indent + level + ". "  + value + "="+ node.className);                                                
		}else{
			DecisionNode node = (DecisionNode) nodeToPrint;
			System.out.println(indent + level + ". " + allAttributes[node.attribute] + "->");
			
			for(int i=0; i< node.nodes.length; i++){
				print(node.nodes[i], newIndent, node.attributeValues[i], level + 1);
			}
		}
		
	}	

}

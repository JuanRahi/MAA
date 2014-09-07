package id3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AlgoID3 {
	private String[] allAttributes; 
	private int indexTargetAttribute = -1; 
	private Set<String> targetAttributeValues = new HashSet<>(); 
        private int algoMode;
        private int totalInstances = -1; 
        private int contAtrMin = 17;
        private int contAtrMax = 21;
	
	public DecisionTree runAlgorithm(String input, String targetAttribute,
			String separator, int mode) throws IOException {	
	
		algoMode = mode;
		DecisionTree tree = new DecisionTree();
		BufferedReader reader = new BufferedReader(new FileReader(input));

                String line = reader.readLine();
		allAttributes = line.split(separator);
				
		
                LinkedList<Integer> remainingAttributes = new LinkedList<>();
		int pos = 0;
		
		for (int i = 0; i < allAttributes.length; i++) {		
			if (allAttributes[i].equals(targetAttribute)) {
				indexTargetAttribute = i;
			} else {
				remainingAttributes.add(i);
			}
		}

		List<String[]> instances = new ArrayList<String[]>();
		while (((line = reader.readLine()) != null)) { 
			if (line.isEmpty() == true ||
					line.charAt(0) == '#' || line.charAt(0) == '%'
							|| line.charAt(0) == '@') {
				continue;
			}
			
			String[] lineSplit = line.split(separator);
			instances.add(lineSplit);
			targetAttributeValues.add(lineSplit[indexTargetAttribute]);
		}
		reader.close(); 

                totalInstances = instances.size();
		
		tree.root = id3(remainingAttributes, instances);
		tree.allAttributes = allAttributes;				
		
		return tree; 
	}

	private Node id3(LinkedList<Integer> remainingAttributes, List<String[]> instances) {
		if (remainingAttributes.size() == 0) {
			Map<String, Integer> targetValuesFrequency = calculateFrequencyOfAttributeValues(
					instances, indexTargetAttribute);
			
			int highestCount = 0;
			String highestName = "";
			for (Entry<String, Integer> entry : targetValuesFrequency
					.entrySet()) {
				
				if (entry.getValue() > highestCount) {
					highestCount = entry.getValue();
					highestName = entry.getKey();
				}
			}
			ClassNode classNode = new ClassNode();
			classNode.className = highestName;
			return classNode;
		}

		Map<String, Integer> targetValuesFrequency = calculateFrequencyOfAttributeValues(
				instances, indexTargetAttribute);

		if (targetValuesFrequency.entrySet().size() == 1) {
			ClassNode classNode = new ClassNode();
			classNode.className = (String) targetValuesFrequency.keySet()
					.toArray()[0];
			return classNode;
		}


		double globalEntropy = 0d;
		for (String value : targetAttributeValues) {
			Integer frequencyInt = targetValuesFrequency.get(value);
			if(frequencyInt != null) {
				double frequencyDouble = frequencyInt / (double) totalInstances;
				globalEntropy -= frequencyDouble * Math.log(frequencyDouble) / Math.log(2);
			}
		}

		int attributeWithHighestGain = 0;
		double highestGain = Double.NEGATIVE_INFINITY;
		for (int attribute : remainingAttributes) {
			double gain = calculateGain(attribute, instances, globalEntropy);
			if (gain >= highestGain) {
				highestGain = gain;
				attributeWithHighestGain = attribute;
			}
		}
	
		if (highestGain == 0) {
			ClassNode classNode = new ClassNode();
			int topFrequency = 0;
			String className = null;
			for(Entry<String, Integer> entry: targetValuesFrequency.entrySet()) {
				if(entry.getValue() > topFrequency) {
					topFrequency = entry.getValue();
					className = entry.getKey();
				}
			}
			classNode.className = className;
			return classNode;
		}

		DecisionNode decisionNode = new DecisionNode();
		decisionNode.attribute = attributeWithHighestGain;

                remainingAttributes.remove(remainingAttributes.indexOf(attributeWithHighestGain));

		Map<String, List<String[]>> partitions = new HashMap<String, List<String[]>>();
		for (String[] instance : instances) {
                    String value = instance[attributeWithHighestGain];
                    // Atr con valores continuos
                    if (algoMode == 3 && attributeWithHighestGain > contAtrMin && attributeWithHighestGain < contAtrMax){
                        try{    
                            Double d = Double.parseDouble(instance[attributeWithHighestGain]);
                            int index = (int) (d/100);
                            int limit = (index*100) + 100;
                            value = "[" + Integer.toString(index*100) + " - " + Integer.toString(limit) +  "]";
                        }
                        catch(Exception ex){
                        }
                    }
                			
                    List<String[]> listInstances = partitions.get(value);
                    if (listInstances == null) {
                            listInstances = new ArrayList<String[]>();
                            partitions.put(value, listInstances);
                    }
                    listInstances.add(instance);
		}

		decisionNode.nodes = new Node[partitions.size()];
		decisionNode.attributeValues = new String[partitions.size()];

		int index = 0;
		for (Entry<String, List<String[]>> partition : partitions.entrySet()) {
			decisionNode.attributeValues[index] = partition.getKey();
			decisionNode.nodes[index] = id3(remainingAttributes,
					partition.getValue()); // recursive call
			index++;
		}
		
		return decisionNode;
	}

        
	private double calculateGain(int attributePos, List<String[]> instances,
			double globalEntropy) {

		Map<String, Integer> valuesFrequency = calculateFrequencyOfAttributeValues(
				instances, attributePos);

		double sum = 0;

		for (Entry<String, Integer> entry : valuesFrequency.entrySet()) {

                    // sum += entry.getValue() / ((double) instances.size())	
                    sum += entry.getValue() / ((double) totalInstances)
					* calculateEntropyIfValue(instances, attributePos,
							entry.getKey());
		}		
                
                // penalizar valores uniformes
                if (algoMode == 2) {
                    double splitInfo = splitInformation(valuesFrequency, totalInstances);
                    return ((globalEntropy - sum) / splitInfo);
                }    
                 
		return globalEntropy - sum;
	}
        
        private double splitInformation(Map<String, Integer> valuesFrequency, int size){
		double sum = 0;
		for (Entry<String, Integer> entry : valuesFrequency.entrySet()) {
                        double freq = entry.getValue() / ((double)size);
			sum -=  freq * Math.log(freq) / Math.log(2);;					
		}
                return sum;
        }
                
	private double calculateEntropyIfValue(List<String[]> instances,
			int attributeIF, String valueIF) {
		
		int instancesCount = 0;
		
		Map<String, Integer> valuesFrequency = new HashMap<String, Integer>();
		
		for (String[] instance : instances) {
			if (instance[attributeIF].equals(valueIF)) {
				String targetValue = instance[indexTargetAttribute];
				if (valuesFrequency.get(targetValue) == null) {
					valuesFrequency.put(targetValue, 1);
				} else {
					valuesFrequency.put(targetValue,
							valuesFrequency.get(targetValue) + 1);
				}
				instancesCount++; 
			}
		}
		double entropy = 0;
		for (String value : targetAttributeValues) {
			Integer count = valuesFrequency.get(value);
			if (count != null) {
				double frequency = count / (double) instancesCount;
				entropy -= frequency * Math.log(frequency) / Math.log(2);
			}
		}
		return entropy;
	}

	private Map<String, Integer> calculateFrequencyOfAttributeValues(
			List<String[]> instances, int indexAttribute) {
		
            Map<String, Integer> targetValuesFrequency = new HashMap<String, Integer>();
		
	
		for (String[] instance : instances) {
                        
                        if (algoMode == 3 && indexAttribute > contAtrMin && indexAttribute < contAtrMax){
                            continuousAttributes(targetValuesFrequency, instance, indexAttribute);                        
                        }
                        else{                        
                            String targetValue = instance[indexAttribute];
                            if (targetValuesFrequency.get(targetValue) == null) {
                                    targetValuesFrequency.put(targetValue, 1);
                            } else {
                                    targetValuesFrequency.put(targetValue,
                                                    targetValuesFrequency.get(targetValue) + 1);
                            }
                        }
		}
		return targetValuesFrequency;
	}
        
        private void continuousAttributes(Map<String, Integer> targetValuesFrequency, String[] instance, int indexAttribute){
            try{
                Double d = Double.parseDouble(instance[indexAttribute]);
                int index = (int) (d/100);
                int limit = (index*100) + 100;
                String key = "[" + Integer.toString(index*100) + " - " + Integer.toString(limit) +  "]";

                if (targetValuesFrequency.get(key) == null) 
                    targetValuesFrequency.put(key, 1);
                else
                    targetValuesFrequency.put(key, targetValuesFrequency.get(key) + 1);                            
            }
            catch(Exception e){
            }    
        }

	public void printStatistics() {
		System.out.println("Target attribute = "
				+ allAttributes[indexTargetAttribute]);
		System.out.print("Other attributes = ");
		for (String attribute : allAttributes) {
			if (!attribute.equals(allAttributes[indexTargetAttribute])) {
				System.out.print(attribute + " ");
			}
		}
		System.out.println();
	}
}

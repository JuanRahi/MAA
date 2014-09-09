package id3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ID3 {
    
        public static void main(String[] args) {
            try {
                Algorithm id3 = new Algorithm();
                String path = "diabetic_data.csv";
                String separator = ",";                
                int mode = 2;
                DecisionTree tree = id3.run(path, separator, mode);
                tree.print();
            } catch (IOException ex) {
                Logger.getLogger(ID3.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    
}

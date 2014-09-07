/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author juan
 */
public class ID3 {
    
        public static void main(String[] args) {
            try {
                AlgoID3 id3 = new AlgoID3();
                String path = "C:\\diabetic_data20000.txt";
                String separator = ",";
                String attr = "readmitted";                
                int mode = 2;
                DecisionTree runAlgorithm = id3.runAlgorithm(path, attr, separator, mode);
                runAlgorithm.print();
            } catch (IOException ex) {
                Logger.getLogger(ID3.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    
}

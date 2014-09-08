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
                Algorithm id3 = new Algorithm();
                String path = "diabetic_data(2000).csv";
                String separator = ",";                
                int mode = 2;
                DecisionTree tree = id3.run(path, separator, mode);
                tree.print();
            } catch (IOException ex) {
                Logger.getLogger(ID3.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    
}

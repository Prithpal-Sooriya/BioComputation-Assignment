/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float_arr;

import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class Individual {
    float[][] genes;
    int geneLength;
    int conditionLength;
    int outputLength;
    int totalLength;
    
    public Individual(int GENE_LENGTH, int CONDITION_LENGTH, int outputLength) {
        this.geneLength = GENE_LENGTH;
        this.conditionLength = CONDITION_LENGTH;
        this.outputLength = outputLength;
        this.totalLength = (conditionLength*2) + outputLength;
        
        genes = new float[geneLength][totalLength];
        
        randomiseGenes();
    }
    
    public void randomiseGenes() {
        Random rand = new Random();
        for (int i = 0; i < genes.length; i++) {
            for (int j = 0; j < genes.length; j++) {
                if(j%totalLength == totalLength-1) {
                    //output
                    genes[i][j] = rand.nextInt(2);
                }
                else {
                    genes[i][j] = rand.nextFloat();
                }
            }
        }
    }
    
    public void evaluate(float[][] dataset) {
        for (int i = 0; i < dataset.length; i++) { //loop through rules
            float[] fs = dataset[i];
            for (int j = 0; j < genes.length; j++) { //loop through genes
                
                //eval conditions
                boolean allMatched = true;
                for (int k = 0; k < fs.length-outputLength; k++) { //loop through each part of rule
                    float value = fs[k];
                    
                    //offset--> [j][k*2]
                    float lowerBound = genes[i][k*2];
                    float upperBound = genes[i][k*2]+1;
                    
                    if(!(lowerBound <= value && value <= upperBound)) {
                        allMatched = false;
                        break;
                    }
                }
                
                //eval all conditions and output
                
            }
        }
    }
    
}

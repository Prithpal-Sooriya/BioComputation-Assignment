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
public class Crossover {

    public static Individual[] singlePointCrossover(Individual parent1, Individual parent2, double CROSSOVER_RATE) {

        Individual[] children = new Individual[2];

        if (CROSSOVER_RATE > Math.random()) {
            int crossoverPosition = new Random().nextInt(parent1.getGenes().length * parent1.getGenes()[0].length);

            int counter = 0;
            int i = 0;
            while(crossoverPosition > counter) {
                int mod = counter%parent1.getGenes()[0].length;
                
                if(mod == parent1.getGenes()[0].length-1) {
                    //reached end of a rule, so i++;
                    i++;
                }
                
                //swap
                float temp = parent1.getGenes()[i][mod];
                parent1.setGeneFromIndex(i, mod, parent2.getGenes()[i][mod]);
                parent2.setGeneFromIndex(i, mod, temp);
                
                counter++;
            }
        }
        
        //set children
        children[0] = new Individual(parent1);
        children[1] = new Individual(parent2);
        
        return children;
    }

    
    public static Individual[] blendCrossover(Individual parent1, Individual parent2, double CROSSOVER_RATE, double BLEND_CROSSOVER) {
        Individual[] children = new Individual[2];
        
        if(CROSSOVER_RATE > Math.random()) {
            
            //begin blend
            for (int i = 0; i < parent1.getGenes().length; i++) { //loop through all genes
                for (int j = 0; j < parent1.getGenes()[0].length-parent1.getOutputLength(); j++) { //loop through conditions only
                    
                    //choose bit to blend
                    if(BLEND_CROSSOVER > Math.random()) {
                        float average = parent1.getGenes()[i][j] + parent2.getGenes()[i][j];
                        average /= 2;
                        
                        parent1.setGeneFromIndex(i, j, average);
                        parent2.setGeneFromIndex(i, j, average);
                    }       
                }
            }
        }
        
        //set children
        children[0] = new Individual(parent1);
        children[1] = new Individual(parent2);
        
        return children;
    }
}

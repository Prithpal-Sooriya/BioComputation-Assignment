
import assignment.task_float.Individual;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Prithpal Sooriya
 */
public class Crossover {
    public static Individual[] singlePointCrossover(Individual parent1, Individual parent2, double CROSSOVER_RATE) {
        
        Individual[] children = new Individual[2];
        
        //crossover or not
        if(CROSSOVER_RATE > Math.random()) {
            
            //decode parents
            Float[] decodedParent1 = parent1.decodeGenes();
            Float[] decodedParent2 = parent2.decodeGenes();
            
            //begin crossover
            Random rand = new Random();
            int crossoverPoint = rand.nextInt(decodedParent1.length);
            for (int i = 0; i < decodedParent1.length; i++) {
                if(i > crossoverPoint) {
                    //swap
                    Float temp = decodedParent1[i];
                    decodedParent1[i] = decodedParent2[i];
                    decodedParent2[i] = temp;
                }
            }
            
            //encode parents
            parent1.encodeGenes(decodedParent1);
            parent2.encodeGenes(decodedParent2);
        }
        
        //add parents (cloned or crossovered) to children
        children[0] = parent1;
        children[1] = parent2;
        
        return children;
    }
}

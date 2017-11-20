package assignment.task_float;

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
        if (CROSSOVER_RATE > Math.random()) {

            //decode parents
            Float[] decodedParent1 = parent1.decodeGenes();
            Float[] decodedParent2 = parent2.decodeGenes();

            //begin crossover
            Random rand = new Random();
            int crossoverPoint = rand.nextInt(decodedParent1.length);
            for (int i = 0; i < decodedParent1.length; i++) {
                if (i > crossoverPoint) {
                    //swap
                    Float temp = decodedParent1[i];
                    decodedParent1[i] = decodedParent2[i];
                    decodedParent2[i] = temp;
                }
            }

            //encode parents --> bounds are validated inside
            parent1.encodeGenes(decodedParent1);
            parent2.encodeGenes(decodedParent2);
        }

        //add parents (cloned or crossovered) to children
        children[0] = parent1;
        children[1] = parent2;

        return children;
    }

    public static Individual[] blendCrossover(Individual parent1, Individual parent2, double BLEND_CROSSOVER_RATE) {
        
        Individual[] children = new Individual[2];
        
        Float[] decodedParent1 = parent1.decodeGenes();
        Float[] decodedParent2 = parent2.decodeGenes();

        /*
        When doing blend crossover,
        we need to make sure that we are not blending the output with a condition
        
        - so add in a check for the index position
        
        - 12 bounds followed by 1 output (so check the 13th postion)
         */
        for (int i = 0; i < decodedParent2.length; i++) {
            //if divisible by 12, then is output
            //if not then blend
            if (i % 13 != 12) {
                if(BLEND_CROSSOVER_RATE > Math.random()) {
                    float f1 = decodedParent1[i];
                    float f2 = decodedParent2[i];
                    float blend = (f1+f2)/2;
                    
                    decodedParent1[i] = blend;
                    decodedParent2[i] = blend;
                }
            }
        }
        
        parent1.encodeGenes(decodedParent1);
        parent2.encodeGenes(decodedParent2);
        
        children[0] = Individual.clone(parent1);
        children[1] = Individual.clone(parent2);
        
        return children;
    }

//    public static void main(String[] args) {
//        
//        //method just to check blend crossover
//        Individual parent1 = new Individual(5, 6);
//        Individual parent2 = new Individual(5, 6);
//        
//        blendCrossover(parent1, parent2, 0.9);
//        
//    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class Crossover {

    private Crossover() {
    }

    ;
    
    public static Individual[] singlePointCrossover(Individual parent1, Individual parent2, double CROSSOVER_RATE) {

        Individual children[] = new Individual[2];

        //check if should crossover
        if (CROSSOVER_RATE > Math.random()) {
            //decode Rules -> int array
            Integer[] decodedDNA1 = parent1.getGenesAsArray();
            Integer[] decodedDNA2 = parent2.getGenesAsArray();

            //begin crossover
            Random rand = new Random();
            //have not checked if one persons dna is smaller or not
            int crossoverPoint = rand.nextInt(decodedDNA1.length);
            for (int i = 0; i < decodedDNA1.length; i++) {
                if (i > crossoverPoint) {
                    //swap
                    Integer temp = decodedDNA1[i];
                    decodedDNA1[i] = decodedDNA2[i];
                    decodedDNA2[i] = decodedDNA1[i];
                }
            }
            parent1.setGenesFromArray(decodedDNA1);
            parent2.setGenesFromArray(decodedDNA2);
        }
        //encode int array -> Rules
        children[0] = parent1;
        children[1] = parent2;

        return children;
    }

}

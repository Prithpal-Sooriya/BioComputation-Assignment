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

    public static Individual[] singlePointCrossover(Individual parent1, Individual parent2, double CROSSOVER_RATE) {

        Individual children[] = new Individual[2];

        //check if should crossover
        if (CROSSOVER_RATE > Math.random()) {
            //decode Rules -> int array
            Integer[] decodedDNA1 = parent1.decodeGenes();
            Integer[] decodedDNA2 = parent2.decodeGenes();

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
            parent1.encodeGenes(decodedDNA1);
            parent2.encodeGenes(decodedDNA2);
        }
        //encode int array -> Rules
        children[0] = parent1;
        children[1] = parent2;

        return children;
    }

    //rule based GA (GARP) also uses inversion for crossover (where the order or the rules are changed)
    public static Individual inversionCrossover(Individual ind) {
        //get 2 points
        Random rand = new Random();
        int startIndex = rand.nextInt(ind.getGenesLength());
        int endIndex = rand.nextInt(ind.getGenesLength());
        //just in case to ensure that the index's are different
        while (endIndex == startIndex) {
            endIndex = rand.nextInt(ind.getGenesLength());
        }

        //invert this section
        while (startIndex >= endIndex) {
            //we can swap
            Rule tempGene = ind.getGeneFromIndex(startIndex);
            ind.setGeneFromIndex(startIndex, ind.getGeneFromIndex(endIndex));
            ind.setGeneFromIndex(endIndex, tempGene);

            //change pointer locations
            startIndex++;
            endIndex--;
        }

        //return this member of the population
        return ind;

    }

    /*
    Random crossover
    This is where we randomly choose to crossover a piece or not.
     */
    public static Individual[] randomCrossover(Individual parent1, Individual parent2, double CROSSOVER_RATE) {
        Individual children[] = new Individual[2];

        if (CROSSOVER_RATE > Math.random()) {
            Integer[] decodedDNA1 = parent1.decodeGenes();
            Integer[] decodedDNA2 = parent2.decodeGenes();

            for (int i = 0; i < decodedDNA1.length; i++) {
                if (Math.random() > 0.5) { //50% chance of random swap
                    //swap
                    int temp = decodedDNA1[i];
                    decodedDNA1[i] = decodedDNA2[i];
                    decodedDNA2[i] = temp;
                }
            }
            parent1.encodeGenes(decodedDNA1);
            parent2.encodeGenes(decodedDNA2);
        }
        
        children[0] = parent1;
        children[1] = parent2;

        return children;
    }
}

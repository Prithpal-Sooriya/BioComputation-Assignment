/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_binary;

import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class Selection {

    private Selection() {
    }

    //tornament selection
    public static Individual tornamentSelection(Individual[] population) {
        //for this we select 2 individuals at random and pick the parent who has the higher fitness
        Random rand = new Random();
        Individual parent1 = Individual.clone(population[rand.nextInt(population.length)]);
        Individual parent2 = Individual.clone(population[rand.nextInt(population.length)]);

        return parent1.getFitness() > parent2.getFitness() ? parent1 : parent2;

    }

    //roulette wheel
    public static Individual fitnessProportionateSelection(Individual[] population) {

        FitnessFunction.normalizeFitnessToTotal(population); //normalise each persons fitness between 0-1

        //and from this, we will do roulette wheel
        double rouletteStop = Math.random();

        for (int i = 0; i < population.length; i++) {
            rouletteStop -= population[i].getNormalizedFitness();

            if (rouletteStop < 0) {
                return Individual.clone(population[i]);
            }
        }
        
        //worst case for some reason has not returned yet, return the 1st individual
        return population[0];
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class Selection {
    public static Individual tornamentSelection(Individual[] population) {
        Random rand = new Random();
        Individual parent1 = Individual.clone(population[rand.nextInt(population.length)]);
        Individual parent2 = Individual.clone(population[rand.nextInt(population.length)]);
        
        return parent1.getFitness() > parent2.getFitness() ? parent1 : parent2;
    }
    
    public static Individual fitnessProportionateSelection(Individual[] population) {
        FitnessFunction.normalizeFitnessToTotal(population);
        
        double rouletteStop = Math.random();
        for (int i = 0; i < population.length; i++) {
            rouletteStop -= population[i].getNormalisedFitness();
            if(rouletteStop <= 0) {
                return Individual.clone(population[i]);
            } 
        }
        
        //worst case (if this is ever reached for some reason) return 1st individual
        return population[0];
    }
}

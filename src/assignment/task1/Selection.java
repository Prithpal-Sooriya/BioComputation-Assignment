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
public class Selection {

    private Selection() {
    }

    //tornament selection
    public static Individual tornamentSelection(Individual[] population) {
        //for this we select 2 individuals at random and pick the parent who has the higher fitness
        Random rand = new Random();
        Individual parent1 = population[rand.nextInt(population.length)];
        Individual parent2 = population[rand.nextInt(population.length)];

        return parent1.getFitness() > parent2.getFitness() ? parent1 : parent2;

    }

    //roulette wheel
    public static Individual fitnessProportionateSelection(Individual[] population) {

        //get the total fitness
        double totalFitness = 0;
        for (Individual individual : population) {
            totalFitness += individual.getFitness();
        }

        //we will normalise all the population's fitness between 1 & 0
        //makes it easier to understand
        for (int i = 0; i < population.length; i++) {
            population[i].setFitness(
                    FitnessFunction.normalizeFitness(
                            population[i].getFitness(), 0, totalFitness, 0, 1)
            );

        }

        //and from this, we will do roulette wheel
        double rouletteStop = Math.random();

        for (int i = 0; i < population.length; i++) {
            rouletteStop -= population[i].getFitness();

            if (rouletteStop < 0) {
                return population[i];
            }
        }
        
        //worst case for some reason has not returned yet, return the 1st individual
        return population[0];
    }

}

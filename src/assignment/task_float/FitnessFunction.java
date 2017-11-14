/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Prithpal Sooriya
 */
public class FitnessFunction {

    public static void sortPopulationByFitness(Individual[] population) {
        Arrays.sort(population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                double result = o1.getFitness() - o2.getFitness();
                if (result > 0) {
                    return 1;
                }
                if (result < 0) {
                    return -1;
                }
                return 0;
            }
        });
    }

    //so we can map the fitness to a range which is easier to use --> for example 0-1
    //algorith is linear transformation and I found out how to do it here: https://stackoverflow.com/questions/345187/math-mapping-numbers
    private static double normalizeFitness(double currentFitness, double oldRangeStart, double oldRangeEnd, double newRangeStart, double newRangeEnd) {
        double m = (currentFitness - oldRangeStart) / (oldRangeEnd - oldRangeStart);
        double x = (newRangeEnd - newRangeStart);
        double c = newRangeStart;

        return (m * x) + c;
    }

    public static void normalizeFitnessToTotal(Individual[] population) {
        double totalFitness = 0.0;
        double maxFitness = 0;
        for (Individual individual : population) {
            totalFitness += individual.getFitness();
            if (individual.getFitness() > maxFitness) {
                maxFitness = individual.getFitness();
            }
        }

        for (int i = 0; i < population.length; i++) {
            population[i].setNormalisedFitness(
                    normalizeFitness(population[i].getFitness(), 0, totalFitness, 0, 1));
        }
    }

    public static void fitnessFunctionCompareRulesAll(Dataset[] trainingSet, Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i] = fitnessFunctionCompareRulesSingle(trainingSet, population[i]);
        }
    }

    public static Individual fitnessFunctionCompareRulesSingle(Dataset[] trainingSet, Individual individual) {
        //reset the fitness
        individual.setFitness(0);

        double tempFitness = 0;
        double allCorrect = 0;

        for (Dataset dataset : trainingSet) { //loop through all trainingset
            for (Rule gene : individual.getGenes()) { //loop through all genes
                for (int i = 0; i < gene.getBoundsLength(); i++) { //loop through all conditions
                    /*
                    the bounds length and the dataset.conditions.length are the same
                     */
                    
                    //check if in bounds
                    if (gene.getBoundFromIndex(i).getLowerbound() <= dataset.getConditionFromIndex(i)
                            && dataset.getConditionFromIndex(i) <= gene.getBoundFromIndex(i).getUpperbound()) {
                        allCorrect++;
                    }
                    else {
                        break;
                    }
                }
                
                //if all conditions match
                if(allCorrect >= gene.getBoundsLength()) {
                    if(gene.getOutput() == dataset.getOutput()) {
                        tempFitness++;
                    }
                    break; //break out even if output matches or not --> because all conditions match anyways
                }
            }
        }
        individual.setFitness(tempFitness);
        
        //return a clone of this (to prevent array weirdness)
        return Individual.clone(individual);
    }

    public static void convertFitnessQuadraticAll(Individual[] population, int exponent) {
        for (int i = 0; i < population.length; i++) {
            population[i] = convertFitnessQuadraticSingle(population[i], exponent);
        }
    }

    private static Individual convertFitnessQuadraticSingle(Individual individual, int exponent) {
        double tempFitness = individual.getFitness();
        tempFitness = Math.pow(tempFitness, 2);
        individual.setFitness(tempFitness);
        return individual;
    }
    
    public static void addFitnessBiasToHighest(Individual[] population, int bias) {
        double maxFitness = 0;
        int maxFitnessIndex = 0;
        
        for (int i = 0; i < population.length; i++) {
            if(population[i].getFitness() > maxFitness) {
                maxFitness = population[i].getFitness();
                maxFitnessIndex = i;
            }
        }
        
        double newFitness = population[maxFitnessIndex].getFitness();
        newFitness += bias;
        population[maxFitnessIndex].setFitness(newFitness);
    }
    
}

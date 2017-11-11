/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Prithpal Sooriya
 */
public class Testing {

    /*
    Just want to test the sorting method of the population
     */
    private static final int POPULATION_SIZE = 10;
    private static final int CHROMOSOME_LENGTH = 15; //chromosome length != DNA length, as chromosome becomes decoded and encoded to binary string later on
    private static final double CROSSOVER_RATE = 0.9; // 0.6-0.9
    private static final double MUTATION_RATE = 0.04; // 1/popsize - 1/chromosome length (or DNA length)

    public static void main(String[] args) {
        Scanner scan = new Scanner(GATest1.class.getResourceAsStream("/Files/data1.txt"));

        /*
        transfer the contents of the file into training and testing dataset
        for This first set of data, we can use all of the data as training data
         */
        ArrayList<Rule> rulesAL = new ArrayList<>();
        //read in the first line (since it is just the header)
        scan.nextLine();

        //transfer all of the data into the training set
        while (scan.hasNextLine()) {
            //read in data
            String ruleString = scan.nextLine();
            Scanner scan1 = new Scanner(ruleString);
            String classifierString = scan1.next();
            String outputString = scan1.next();
            scan1.close();

            //convert data into the variables for a rule
            int[] classifier = new int[classifierString.length()];
            for (int i = 0; i < classifier.length; i++) {
                classifier[i] = Integer.parseInt("" + classifierString.charAt(i));
            }
            int output = Integer.parseInt(outputString);

            //create the rule
            Rule rule = new Rule(classifier, output);

            //add rule to array
            rulesAL.add(rule);
        }
        scan.close();

        Rule[] trainingSet = new Rule[rulesAL.size()];
        rulesAL.toArray(trainingSet);

        /* Create Initial population */
        Individual population[] = new Individual[POPULATION_SIZE];
        //init population (which will randomise the genes
        for (int i = 0; i < population.length; i++) {
            population[i] = new Individual(CHROMOSOME_LENGTH, rulesAL.get(0).getConditionLength());
        }

        //fitness function
        FitnessFunction.fitnessFunctionCompareConditionsAll(trainingSet, population);

        //show population stats
//        System.out.println("=============before=============");
//        showEachFitness(population);
        //sort the fitness
        FitnessFunction.sortPopulationByFitness(population);

//        System.out.println("=============after=============");
//        showEachFitness(population);
        /* Create Initial population */
        Individual population2[] = new Individual[POPULATION_SIZE];
        //init population (which will randomise the genes
        for (int i = 0; i < population2.length; i++) {
            population2[i] = new Individual(CHROMOSOME_LENGTH, rulesAL.get(0).getConditionLength());
        }
        FitnessFunction.fitnessFunctionCompareConditionsAll(trainingSet, population2);
        FitnessFunction.sortPopulationByFitness(population2);

        //perform swap
        System.out.println("pop1");
        showEachFitness(population);
        System.out.println("pop2");
        showEachFitness(population2);

        //take the best individuals from parents, and replace the worst individuals from offspring
        int offset = 0;
        for (int i = 0; i < population.length; i++) {
            if (population2[population2.length - 1].getFitness() > population[i].getFitness()) {
                offset++;
            } else {
                break;
            }
        }

        //add parentpop[N-offset] to parentpop[N] to beginning of offspring
        for (int i = 0; i < offset; i++) {
            int parentIndex = population2.length - offset + i;
            population[i] = population2[parentIndex];
        }

        System.out.println("pop1 after");
        showEachFitness(population);

    }

    private static void showEachFitness(Individual[] population) {
        for (Individual individual : population) {
            System.out.println("Fitness = " + individual.getFitness());
        }
    }

}

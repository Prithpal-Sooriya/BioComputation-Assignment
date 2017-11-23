/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float_arr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author Prithpal Sooriya
 */
public class GAFloat {

    //HYPER PARAMETERS
    private static final int POPULATION_SIZE = 200;
    private static final int CHROMOSOME_LENGTH = 10; //10 is one of the smallest rulesets that you can have..
    private static final double CROSSOVER_RATE = 0.9;
    private static final double BLEND_CROSSOVER_RATE = 0.6;
    private static final double MUTATION_RATE = 0.001; //1/popsize to 1/chromosomelength
    private static final double OMEGA_OFFSET_FIXED = 0.3;
    private static final int NUMBER_OF_GENERATIONS = 3000;
    private int CONDITION_LENGTH;
    private int OUTPUT_LENGTH;
    private static double variable_omega_rate;
    //input file
    private String inputFileDir;

    /* Datasets */
    private float[][] dataset;
    private float[][] trainingSet;
    private float[][] testingSet;

    /* Individuals/Population */
    private Individual[] parentPopulation;
    private Individual[] offspringPopulation;
    private Individual bestIndividual;

    /* sort populations */
    private boolean parentPopulationSorted = false;
    private boolean offspringPopulationSorted = false;

    //constructor
    public GAFloat(String filename) {
        if (filename.equals("data3.txt")) {
            inputFileDir = "/Files/data3.txt";
        }
    }

    /*
    read file
    reads file based on inputFileDir
     */
    private void readFile() {
        Scanner scan = new Scanner(GAFloat.class.getResourceAsStream(inputFileDir));
        ArrayList<Float[]> datasetAL = new ArrayList<>();
        int conditionLengthTemp = 0;
        int outputLengthTemp = 0;

        scan.nextLine(); //read in the useless line

        while (scan.hasNextLine()) {
            //read in rule
            String ruleString = scan.nextLine();
            String[] parts = ruleString.split(" ");

            //save length of condition
            if (conditionLengthTemp == 0) {
                //output length is always 1, condition length is the rest.
                conditionLengthTemp = parts.length - 1;
            }
            if (outputLengthTemp == 0) {
                outputLengthTemp = 1;
            }

            //create set
            Float[] set = new Float[conditionLengthTemp + outputLengthTemp];
            for (int i = 0; i < set.length - 1; i++) {
                set[i] = Float.parseFloat(parts[i]);
            }
            set[set.length - 1] = Float.parseFloat(parts[parts.length - 1]);

            //add set to arraylist
            datasetAL.add(set);
        }

        scan.close();

        //populate dataset
        dataset = new float[datasetAL.size()][conditionLengthTemp + outputLengthTemp];
        for (int i = 0; i < dataset.length; i++) {
            for (int j = 0; j < dataset[i].length; j++) {
                dataset[i][j] = datasetAL.get(i)[j];
            }
        }

        //store length of condition
        CONDITION_LENGTH = conditionLengthTemp;
        OUTPUT_LENGTH = outputLengthTemp;
    }

    private void initialiseDatasets() {
        trainingSet = new float[1000][CONDITION_LENGTH + OUTPUT_LENGTH];
        testingSet = new float[1000][CONDITION_LENGTH + OUTPUT_LENGTH];

        int i = 0;
        while (i < dataset.length/2) {
            trainingSet[i] = dataset[i];
            i++;
        }
        
        while (i < dataset.length) {
            testingSet[i % trainingSet.length] = dataset[i];
            i++;
        }
    }

    private void initialisePopulations() {
        parentPopulation = new Individual[POPULATION_SIZE];
        offspringPopulation = new Individual[POPULATION_SIZE];

        //init parent and offspring
        //note: generation 0 --> parent and offspring are the same
        for (int i = 0; i < POPULATION_SIZE; i++) {
            parentPopulation[i] = offspringPopulation[i] = new Individual(CHROMOSOME_LENGTH, CONDITION_LENGTH, OUTPUT_LENGTH);
        }

        //init best individual
        bestIndividual = new Individual(CHROMOSOME_LENGTH, CONDITION_LENGTH, OUTPUT_LENGTH);
    }

    private void getBestIndividualNoSort() {
        //no sorting, so we will sequential search for best
        int bestFitnessIndex = 0;
        for (int i = 1; i < offspringPopulation.length; i++) {
            if (offspringPopulation[i].getFitness() > offspringPopulation[bestFitnessIndex].getFitness()) {
                bestFitnessIndex = i;
            }
        }
        bestIndividual = new Individual(offspringPopulation[bestFitnessIndex]);
        
    }

    private void showAverageFitness() {
        double totalFitness = 0;
        for (Individual individual : offspringPopulation) {
            totalFitness += individual.getFitness();
        }
        System.out.println("Average Fitness: " + (totalFitness / offspringPopulation.length));
    }

    private void showBestFitness() {
        System.out.println("Best Fitness: " + bestIndividual.getFitness());
    }

    //no stop condition yet
    private boolean stopCondition() {
        return false;
    }

    private void generatOffspring(float[][] set) {

        /* Selection / Crossover /  Mutation */
        for (int i = 0; i < offspringPopulation.length; i++) {

            /* Selection */
            Individual[] parents = new Individual[2];
            parents[0] = Selection.tornamentSelection(parentPopulation);
            parents[1] = Selection.tornamentSelection(parentPopulation);

            /* Crossover */
            Individual[] children = Crossover.singlePointCrossover(parents[0], parents[1], CROSSOVER_RATE);

            /* Mutation and add to offspring pop */
            children[0].mutationCreepConditions(MUTATION_RATE, OMEGA_OFFSET_FIXED);
            children[0].mutationOutput(MUTATION_RATE);
            offspringPopulation[i] = children[0];

            if (i + 1 < offspringPopulation.length) {
                i++;
                children[1].mutationCreepConditions(MUTATION_RATE, OMEGA_OFFSET_FIXED);
                children[1].mutationOutput(MUTATION_RATE);
                offspringPopulation[i] = children[1];
            }
        }
        
//        for (int i = 0; i < parentPopulation.length; i++) {
//            parentPopulation[i].evaluate(dataset);
//            offspringPopulation[i].evaluate(dataset);
//            
//        }
        
//        FitnessFunction.compareRulesAll(set, parentPopulation);
//        FitnessFunction.compareRulesAll(set, offspringPopulation);

//        sortPopulations();
        
//        swapWorstForBest();
        
        /* Set best ind in the offspring pop */
        getBestIndividualNoSort();
    }

    private void sortPopulations() {
        Comparator<Individual> comp = new Comparator<Individual>() {
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
        };
        
        Arrays.sort(parentPopulation, comp);
        Arrays.sort(offspringPopulation, comp);
        
        parentPopulationSorted = true;
        offspringPopulationSorted = true;
    }
    
    private void swapWorstForBest() {
        //check if already sorted
        if (!parentPopulationSorted || !offspringPopulationSorted) {
            sortPopulations();
        }

        /*
        find location of where to start subarray swap
        - maybe limit how much of the old generation I should keep (so it isnt a full clone of the parent!)
        - maybe limit to 3 best parents for 3 worst offspring
        */
        int offset = 0;
        for (int i = 0; i < 4; i++) {
            if (parentPopulation[parentPopulation.length - 1].getFitness() > offspringPopulation[i].getFitness()) {
                offset++;
            } else {
                break;
            }
        }

        /* commence swap */
        for (int i = 0; i < offset; i++) {
            int parentIndex = parentPopulation.length - offset + i; //checked, this index is correct
            offspringPopulation[i] = new Individual(parentPopulation[parentIndex]);
        }
    }
    
    private void copyChildrenToParents() {
        for (int i = 0; i < parentPopulation.length; i++) {
            parentPopulation[i] = new Individual(offspringPopulation[i]);
        }
    }

    public void run() {
        readFile();
        initialiseDatasets();

        initialisePopulations();

        System.out.println("Hello!");
        /* GA loop */
        int numberOfGenerations = 0;
        float[][] set = dataset;
        while (numberOfGenerations < NUMBER_OF_GENERATIONS) {

            parentPopulationSorted = false;
            offspringPopulationSorted = false;
            
            //every 10 generations, use testingSet
            if (numberOfGenerations % 10 == 0) {
                set = testingSet;
                System.out.println("*"); //just to make a note that the next output is on testing set
            } else {
                set = trainingSet;
            }
            
            /* Fitness function on parents */
            for (int i = 0; i < parentPopulation.length; i++) {
                parentPopulation[i].evaluate(set);
            }

//            FitnessFunction.compareRulesAll(set, parentPopulation);

            /* Generate offspring */
            generatOffspring(set);

            /* Show Best Fitness */
            System.out.println("Best Fitness: " + bestIndividual.getFitness()
                    + " Generations: " + numberOfGenerations);

            /* copy children to parents */
            copyChildrenToParents();

            /* Increment generations */
            numberOfGenerations++;
        }
    }

    public static void main(String[] args) {
        GAFloat run = new GAFloat("data3.txt");
        run.run();
    }
}

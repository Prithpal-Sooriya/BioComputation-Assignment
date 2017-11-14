/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Prithpal Sooriya
 */
public class GAFloat {

    //input file
    private String inputFileDir;

    //HYPER PARAMETERS
    private static final int POPULATION_SIZE = 100;
    private static final int CHROMOSOME_LENGTH = 10; //10 is one of the smallest rulesets that you can have..
    private static final double CROSSOVER_RATE = 0.9;
    private static final double MUTATION_RATE = 0.02; //1/popsize to 1/chromosomelength
    private static final double OMEGA_RATE = 0.00005;

//    private int POPULATION_SIZE;
//    private int CHROMOSOME_LENGTH;
//    private double CROSSOVER_RATE;
//    private double MUTATION_RATE;
    private int CONDITION_LENGTH;

    //Datasets
    private Dataset[] dataset;
    private Dataset[] trainingSet;
    private Dataset[] testingSet;

    //Individuls
    private Individual[] parentPopulation;
    private Individual[] offspringPopulation;
    private Individual bestIndividual;

    //booleans to see if population is sorted
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
        ArrayList<Dataset> datasetAL = new ArrayList<>();
        int conditionLengthTemp = 0;

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

            //create set
            Dataset set;
            float conditions[] = new float[conditionLengthTemp];
            for (int i = 0; i < conditions.length; i++) {
                conditions[i] = Float.parseFloat(parts[i]);
            }
            int output = Integer.parseInt(parts[parts.length - 1]);
            set = new Dataset(conditions, output);

            //add set to arraylist
            datasetAL.add(set);
        }

        scan.close();
        dataset = new Dataset[datasetAL.size()];
        datasetAL.toArray(dataset);

        //store length of condition
        CONDITION_LENGTH = conditionLengthTemp;
    }

    /*
    initialise populations
     */
    private void initialisePopulations() {
        parentPopulation = new Individual[POPULATION_SIZE];
        offspringPopulation = new Individual[POPULATION_SIZE];

        //init parent and offspring
        //note: generation 0 --> parent and offspring are the same
        for (int i = 0; i < POPULATION_SIZE; i++) {
            parentPopulation[i] = offspringPopulation[i] = new Individual(CHROMOSOME_LENGTH, CONDITION_LENGTH);
        }

        //init the best individual
        bestIndividual = new Individual(CHROMOSOME_LENGTH, CONDITION_LENGTH);
    }

    private void sortPopulations() {
        FitnessFunction.sortPopulationByFitness(parentPopulation);
        FitnessFunction.sortPopulationByFitness(offspringPopulation);

        parentPopulationSorted = true;
        offspringPopulationSorted = true;
    }

    private void swapWorstForBest() {
        //check if already sorted
        if (!parentPopulationSorted || !offspringPopulationSorted) {
            sortPopulations();
        }

        /* find location of where to start subarray swap */
        int offset = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            if (parentPopulation[parentPopulation.length - 1].getFitness() > offspringPopulation[i].getFitness()) {
                offset++;
            } else {
                break;
            }
        }

        /* commence swap */
        for (int i = 0; i < offset; i++) {
            int parentIndex = parentPopulation.length - offset + i; //checked, this index is correct
            offspringPopulation[i] = Individual.clone(parentPopulation[parentIndex]);
        }
    }

    private void setBestIndividual() {
        //check if offspring array is sorted
        if (!offspringPopulationSorted) {
            sortPopulations();
        }

        //last individual = best fitness
        bestIndividual = Individual.clone(offspringPopulation[POPULATION_SIZE - 1]);
    }

    private void showAverageFitness(Individual[] population) {
        double totalFitness = 0;
        for (Individual individual : population) {
            totalFitness += individual.getFitness();
        }

        System.out.println("Average Fitness: " + (totalFitness / population.length));
    }

    private void showBestFitness() {
        System.out.println("Best Fitness: " + bestIndividual.getFitness());
    }

    private boolean stopCondition(Dataset[] testingSet, Individual bestIndividual) {
        int numberOfCorrectRules = 0;
        int numberOfCorrectConditions = 0;
        for (Dataset set : testingSet) { //loop through testing set
            for (Rule gene : bestIndividual.getGenes()) { //loop through genes
                for (int i = 0; i < gene.getBoundsLength(); i++) {
                    /* bounds length = set conditions length */
                    if (gene.getBoundFromIndex(i).getLowerbound() <= set.getConditionFromIndex(i)
                            && set.getConditionFromIndex(i) <= gene.getBoundFromIndex(i).getUpperbound()) {
                        numberOfCorrectConditions++;
                    } else {
                        break;
                    }
                }

                //check if the conditions length matches number correct
                if (numberOfCorrectConditions >= set.getConditionLength()) {
                    //if outputs match then increment
                    if (set.getOutput() == gene.getOutput()) {
                        numberOfCorrectRules++;
                    }
                    break; //break out even if output matches --> because conditions match!
                }
            }
        }

        return numberOfCorrectRules >= testingSet.length;
    }

    private void generateOffspring(Dataset[] set) {
        /*
        clone parent pop
        - so we can do some cool stuff to the parent fitness
         */
        Individual[] parentPopulationCopy = new Individual[POPULATION_SIZE];
        for (int i = 0; i < parentPopulationCopy.length; i++) {
            parentPopulationCopy[i] = Individual.clone(parentPopulation[i]);
        }

        /* Fitness Function Improve */
//        FitnessFunction.convertFitnessQuadraticAll(parentPopulationCopy, 2);
        FitnessFunction.addFitnessBiasToHighest(parentPopulationCopy, 100); //adds 100 to best individual

        /* Selection / Crossover / Mutation */
        for (int i = 0; i < offspringPopulation.length; i++) {
            /* Selection */
            Individual[] parents = new Individual[2];
            parents[0] = Selection.fitnessProportionateSelection(parentPopulationCopy);
            parents[1] = Selection.fitnessProportionateSelection(parentPopulationCopy);

            /* Crossover */
            Individual[] children = Crossover.singlePointCrossover(parents[0], parents[1], CROSSOVER_RATE);

            /* Mutation */
            children[0] = Mutation.mutationCreepAndOutput(children[0], MUTATION_RATE, OMEGA_RATE);
            offspringPopulation[i] = children[0];
            if (i + 1 < offspringPopulation.length) {
                i++;
                children[1] = Mutation.mutationCreepAndOutput(children[1], MUTATION_RATE, OMEGA_RATE);
                offspringPopulation[i] = children[1];
            }
        }

        /* pre-subarray swap */
        FitnessFunction.fitnessFunctionCompareRulesAll(set, offspringPopulation);
        FitnessFunction.fitnessFunctionCompareRulesAll(set, parentPopulation); //this line may be redundant (since we never touched the parent, only the parent copy)

        sortPopulations();

        /* subarray swap */
        swapWorstForBest();

        /* set best individual */
        setBestIndividual();

    }

    private void copyChildrenToParents(Individual[] offSpringPopulation, Individual[] parentPopulation) {
        for (int i = 0; i < parentPopulation.length; i++) {
            parentPopulation[i] = Individual.clone(offSpringPopulation[i]);
        }
    }

    public void run() {
        //read in file and save to dataset
        readFile();

        /*
        Create training and testing sets
        training = evolve population
        testing = every 10 generations, get the best individual and test against testing.
         */
        trainingSet = Dataset.createTrainingSet(dataset);
        testingSet = Dataset.createTestingSet(dataset);

        initialisePopulations();

        /* GA loop */
        Scanner scan = new Scanner(System.in); //used for debugging
        int numberOfGenerations = 0;
        boolean stopCondition = false;
        Dataset set[];
        while (!stopCondition && numberOfGenerations < 1000) {

            //populations are not sorted
            parentPopulationSorted = offspringPopulationSorted = false;

            //every 10 generations we want to test on testingset
            if (numberOfGenerations % 10 == 0) {
                set = testingSet;
            } else {
                set = trainingSet;
            }

            /* Fitness function on parents */
            FitnessFunction.fitnessFunctionCompareRulesAll(set, parentPopulation);

            /* Generate offspring */
            generateOffspring(set);

            /* Show Best Fitness */
            showBestFitness();
            if(set == testingSet) {
                System.out.println("*");
            }

            /* Evaluate stop condition */
            stopCondition = stopCondition(trainingSet, bestIndividual);

            /* Set Next Generation */
            copyChildrenToParents(offspringPopulation, parentPopulation);
            
            /* Increment generations */
            numberOfGenerations++;
        }

        /* Onct GA/Evolution has finished, lets test the best individual on testing set */
        System.out.println("Stopped");
        System.out.println("Number of Generations: " + numberOfGenerations);
        bestIndividual = FitnessFunction.fitnessFunctionCompareRulesSingle(testingSet, bestIndividual);
        System.out.println("Number of rules passed in testing set: " + bestIndividual.getFitness());
        System.out.println("------------------------------");
        System.out.println("Best Individual");
        System.out.println(bestIndividual.toString());

    }

    public static void main(String[] args) {

        GAFloat ga = new GAFloat("data3.txt");

        ga.run();
    }

}

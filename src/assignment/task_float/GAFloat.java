/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

import assignment.Outcomes.CSVFileWriter;
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

    //output to file
    CSVFileWriter outTraining;
    CSVFileWriter outTesting;
    boolean write = false;

    //HYPER PARAMETERS
    private static int POPULATION_SIZE = 200;
    private static int CHROMOSOME_LENGTH = 10; //10 is one of the smallest rulesets that you can have..
    private static double CROSSOVER_RATE = 0.9;
    private static double BLEND_CROSSOVER_RATE = 0.4;
    private static double MUTATION_RATE = 0.01; //1/popsize to 1/chromosomelength
    private static double FIXED_OMEGA_RATE = 0.3;
    private static int NUMBER_OF_GENERATIONS = 3000;

    //hyper parameter setters
    public static void setPOPULATION_SIZE(int POPULATION_SIZE) {
        GAFloat.POPULATION_SIZE = POPULATION_SIZE;
    }

    public static void setCHROMOSOME_LENGTH(int CHROMOSOME_LENGTH) {
        GAFloat.CHROMOSOME_LENGTH = CHROMOSOME_LENGTH;
    }

    public static void setCROSSOVER_RATE(double CROSSOVER_RATE) {
        GAFloat.CROSSOVER_RATE = CROSSOVER_RATE;
    }

    public static void setBLEND_CROSSOVER_RATE(double BLEND_CROSSOVER_RATE) {
        GAFloat.BLEND_CROSSOVER_RATE = BLEND_CROSSOVER_RATE;
    }

    public static void setMUTATION_RATE(double MUTATION_RATE) {
        GAFloat.MUTATION_RATE = MUTATION_RATE;
    }

    public static void setFIXED_OMEGA_RATE(double FIXED_OMEGA_RATE) {
        GAFloat.FIXED_OMEGA_RATE = FIXED_OMEGA_RATE;
    }

    public static void setNUMBER_OF_GENERATIONS(int NUMBER_OF_GENERATIONS) {
        GAFloat.NUMBER_OF_GENERATIONS = NUMBER_OF_GENERATIONS;
    }

    public void setCONDITION_LENGTH(int CONDITION_LENGTH) {
        this.CONDITION_LENGTH = CONDITION_LENGTH;
    }

    private static double variable_omega_rate;

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
        write = false;
    }

    //constructor to allow writing to file
    public GAFloat(String inputFileName, String csvFileDir, String csvFileName) {
        if (inputFileName.equals("data3.txt")) {
            inputFileDir = "/Files/data3.txt";
        }
        String trainingFileName = csvFileName.substring(0, csvFileName.length() - 4) + "_TRAIN.csv";
        String testingFileName = csvFileName.substring(0, csvFileName.length() - 4) + "_TEST.csv";
        outTraining = new CSVFileWriter(csvFileDir, trainingFileName, "worst,averge,best");
        outTesting = new CSVFileWriter(csvFileDir, testingFileName, "worst,averge,best");
        write = true;
        POPULATION_SIZE = 25;
        CHROMOSOME_LENGTH = 32;
        CROSSOVER_RATE = 0.9;
        MUTATION_RATE = 0.03;
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

        /*
        find location of where to start subarray swap
        - maybe limit how much of the old generation I should keep (so it isnt a full clone of the parent!)
        - maybe limit to 3 best parents for 3 worst offspring --> limit to 10% parents?
         */
        int offset = 0;
        int percentOfParents = (parentPopulation.length / 100) * 10;
        for (int i = 0; i < percentOfParents; i++) {
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

    private void setBestIndividual() {
        //check if offspring array is sorted
        if (!offspringPopulationSorted) {
            sortPopulations();
        }

        //last individual = best fitness
        bestIndividual = new Individual(offspringPopulation[POPULATION_SIZE - 1]);
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
            numberOfCorrectConditions = 0;
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
            parentPopulationCopy[i] = new Individual(parentPopulation[i]);
        }

        /* Fitness Function Improve */
        FitnessFunction.convertFitnessQuadraticAll(parentPopulationCopy, 2);
        FitnessFunction.addFitnessBiasToHighest(parentPopulationCopy, 100); //adds 100 to best individual

        /* Selection / Crossover / Mutation */
        for (int i = 0; i < offspringPopulation.length; i++) {
            /* Selection */
            Individual[] parents = new Individual[2];
            parents[0] = Selection.fitnessProportionateSelection(parentPopulationCopy);
            parents[1] = Selection.fitnessProportionateSelection(parentPopulationCopy);

            /* Crossover */
 /*
            After some research (and looking through the slides),
            Single Point Crossover can be quite destructive
            - if a rule is close to a solution -> single point crossover can move that rule away from its goal.
            
            Think about replacing this with blending crossover
             */
            Individual[] children = Crossover.singlePointCrossover(parents[0], parents[1], CROSSOVER_RATE);
//            Individual[] children = Crossover.blendCrossover(parents[0], parents[1], BLEND_CROSSOVER_RATE);

            /* Mutation */
 /*
            Mutation creep is great for floating point genotypes
            However Omega is fixed
            
            - Think about decreasing mutation rate from as closer we get to the ideal solution
                - omega = 1 - number correct/total rules in set
                    - if less rules correct = higher mutation bounds
                    - if many rules correct = lower mutation bounds
                
                - More complex version
                - omega = (max want to mutate by) - (number correct / total rules)*max want to mutate by#
                - omega = 0.5 - ((#correct/total)*0.5) //so we restrict to max of 0.5 leaps in bounds
             */
            calculateVariableMutation(children[0], set);
            children[0] = Mutation.mutationCreepAndOutput(children[0], MUTATION_RATE, FIXED_OMEGA_RATE);
//            children[0] = Mutation.mutationRandom(children[0], MUTATION_RATE);
            offspringPopulation[i] = children[0];

            if (i + 1 < offspringPopulation.length) {
                i++;
                calculateVariableMutation(children[1], set);
                children[1] = Mutation.mutationCreepAndOutput(children[1], MUTATION_RATE, FIXED_OMEGA_RATE);
//                children[1] = Mutation.mutationRandom(children[1], MUTATION_RATE);
                offspringPopulation[i] = children[1];
            }
        }

        /* pre-subarray swap */
        FitnessFunction.fitnessFunctionCompareRulesAll(set, offspringPopulation);
//        FitnessFunction.fitnessFunctionCompareRulesAll(set, parentPopulation); //this line may be redundant (since we never touched the parent, only the parent copy)

        sortPopulations();

        /* subarray swap */
        swapWorstForBest();

        /* set best individual */
        setBestIndividual();

    }

    private void calculateVariableMutation(Individual ind, Dataset[] set) {
        //work out the number of rules the child can complete
        FitnessFunction.fitnessFunctionCompareRulesSingle(set, ind);
        //work out variable creep
        variable_omega_rate = FIXED_OMEGA_RATE - ((ind.getFitness() / set.length) * FIXED_OMEGA_RATE);
    }

    private void copyChildrenToParents(Individual[] offSpringPopulation, Individual[] parentPopulation) {
        for (int i = 0; i < parentPopulation.length; i++) {
            parentPopulation[i] = new Individual(offSpringPopulation[i]);
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
        Dataset set[] = dataset;
        double worstFitness, averageFitness, bestFitness;
        while (numberOfGenerations < NUMBER_OF_GENERATIONS) {

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

            //output to file it initial
            if (numberOfGenerations == 0) {
                worstFitness = getWorstFitness(parentPopulation);
                averageFitness = getAverageFitness(parentPopulation);
                bestFitness = getBestFitness(parentPopulation, true);
                if (write) {
                    outTraining.writePopulation(worstFitness + "," + averageFitness + "," + bestFitness);
                }
            }

            /* Generate offspring */
            generateOffspring(set);

            /* Show Best Fitness */
//            showBestFitness();
            System.out.println("Best Fitness: " + bestIndividual.getFitness() + " Generations: " + numberOfGenerations);
            if (set == testingSet) {
                System.out.println("*");
            }

            worstFitness = getWorstFitness(offspringPopulation);
            averageFitness = getAverageFitness(offspringPopulation);
            bestFitness = getBestFitness(offspringPopulation, false);
            if (write) {
                if (numberOfGenerations % 10 == 0) {
                    outTesting.writePopulation(worstFitness + "," + averageFitness + "," + bestFitness);
                } else {
                    outTraining.writePopulation(worstFitness + "," + averageFitness + "," + bestFitness);
                }
            }

            /* Evaluate stop condition */
            stopCondition = stopCondition(trainingSet, bestIndividual);

            /* Set Next Generation */
            copyChildrenToParents(offspringPopulation, parentPopulation);

            /* Increment generations */
            numberOfGenerations++;
        }

        if (write) {
            outTraining.close();
            outTesting.close();
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

    private double getWorstFitness(Individual[] population) {
        double worst = population[0].getFitness();
        for (int i = 1; i < population.length; i++) {
            if (population[i].getFitness() < worst) {
                worst = population[i].getFitness();
            }
        }
        return worst;
    }

    private double getAverageFitness(Individual[] population) {
        double averageFitness = 0;
        for (Individual individual : population) {
            averageFitness += individual.getFitness();
        }
        averageFitness /= population.length;
        return averageFitness;
    }

    private double getBestFitness(Individual[] population, boolean initial) {
        //return if initial population
        if (initial) {
            double bestFitness = 0;
            for (Individual individual : population) {
                if (individual.getFitness() > bestFitness) {
                    bestFitness = individual.getFitness();
                }
            }
            return bestFitness;
        }
        return bestIndividual.getFitness();
    }

    public static void main(String[] args) {

        GAFloat ga = new GAFloat("data3.txt");

        ga.run();
    }

}

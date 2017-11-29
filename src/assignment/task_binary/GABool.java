/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_binary;

import assignment.Outcomes.CSVFileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Prithpal Sooriya
 */
public class GABool {

    //HYPER PARAMETERS
//    private static final int POPULATION_SIZE = 25;
//    private static final int CHROMOSOME_LENGTH = 64; //chromosome length != DNA length, as chromosome becomes decoded and encoded to binary string later on
//    private static final double CROSSOVER_RATE = 0.9; // 0.6-0.9
//    private static final double MUTATION_RATE = 0.02; // 1/popsize - 1/chromosome length (or DNA length)
    private int POPULATION_SIZE;
    private int CHROMOSOME_LENGTH;
    private double CROSSOVER_RATE;
    private double MUTATION_RATE;

    private int CLASSIFIER_LENGTH;

    //used for rule reading from the file
    private String inputFileDir;
    private Rule[] dataset;

    //used for writing to file
    CSVFileWriter out;
    boolean write = false;

    //populations
    private Individual[] parentPopulation;
    private Individual[] offspringPopulation;
    private Individual bestIndividual;

    //vars to handle if the population is already sorted or not
    private boolean parentPopulationSorted = false;
    private boolean offspringPopulationSorted = false;

    /*
    constructor
    will require the inputFileName to be used
    will use the default
      - population size = 25
      - chromosome length = 32 //so it can work with dataset 1
      - crossover rate = 0.9
      - mutation rate = 0.03 (1/pop size to 1/chromosome length)
     */
    public GABool(String inputFileName, String csvFileDir, String csvFileName) {
        if (inputFileName.equalsIgnoreCase("data1.txt")) {
            inputFileDir = "/Files/data1.txt";
        }
        if (inputFileName.equalsIgnoreCase("data2.txt")) {
            inputFileDir = "/Files/data2.txt";
        }
        out = new CSVFileWriter(csvFileDir, csvFileName, "worst,averge,best");
        write = true;
        POPULATION_SIZE = 25;
        CHROMOSOME_LENGTH = 32;
        CROSSOVER_RATE = 0.9;
        MUTATION_RATE = 0.03;
    }

    /* Constructor that will allow writing to files*/
    public GABool(String inputFileName) {
        if (inputFileName.equalsIgnoreCase("data1.txt")) {
            inputFileDir = "/Files/data1.txt";
        }
        if (inputFileName.equalsIgnoreCase("data2.txt")) {
            inputFileDir = "/Files/data2.txt";
        }
        POPULATION_SIZE = 25;
        CHROMOSOME_LENGTH = 32;
        CROSSOVER_RATE = 0.9;
        MUTATION_RATE = 0.03;
    }

    /* manually set hyper parameters */
    public void setPopulationSize(int populationSize) {
        POPULATION_SIZE = populationSize;
    }

    public void setChromosomeLength(int chromosomeLength) {
        CHROMOSOME_LENGTH = chromosomeLength;
    }

    public void setCrossoverRate(double crossoverRate) {
        CROSSOVER_RATE = crossoverRate;
    }

    public void setMutationRate(double mutationRate) {
        MUTATION_RATE = mutationRate;
    }

    /*
    read in the file
      - set to private as this is only used for this class
     */
    private void readFile() {
        Scanner scan = new Scanner(GABool.class.getResourceAsStream(inputFileDir));
        ArrayList<Rule> rulesAL = new ArrayList<>();
        int classifierLengthTemp = 0; //just used to save the condition length

        //read in the first line (since it is just the header)
        scan.nextLine();

        while (scan.hasNextLine()) {
            //read in data
            String ruleString = scan.nextLine();
            Scanner scan1 = new Scanner(ruleString);
            String classifierString = scan1.next();
            String outputString = scan1.next();
            scan1.close();

            //convert data into the variables for a rule
            int[] classifier = new int[classifierString.length()];

            //save the length of the classifier
            if (classifierLengthTemp == 0) {
                classifierLengthTemp = classifierString.length();
            }

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
        dataset = new Rule[rulesAL.size()];
        rulesAL.toArray(dataset);

        //store the length of classifier
        CLASSIFIER_LENGTH = classifierLengthTemp;
    }

    /*
    init the populations
     */
    private void initialisePopulations() {
        parentPopulation = new Individual[POPULATION_SIZE];
        offspringPopulation = new Individual[POPULATION_SIZE];

        //init the parent and offspring
        //note: the generation 0, the parent and offspring are the same values;
        //this could cause issues (same reference pointer...)
        for (int i = 0; i < POPULATION_SIZE; i++) {
            parentPopulation[i] = offspringPopulation[i] = new Individual(CHROMOSOME_LENGTH, CLASSIFIER_LENGTH);
        }

        bestIndividual = new Individual(CHROMOSOME_LENGTH, CLASSIFIER_LENGTH);
    }

    /*
    sort the populations
      - use this for getting best individual
      - and also subarray swap worst offspring for best parents
     */
    private void sortPopulations() {
        FitnessFunction.sortPopulationByFitness(parentPopulation);
        FitnessFunction.sortPopulationByFitness(offspringPopulation);

        parentPopulationSorted = true;
        offspringPopulationSorted = true;
    }

    /*
    used to swap the worst subarray offsprings for the best subarray parents
     */
    private void swapWorstForBest() {
        //check if already sorted
        if (!parentPopulationSorted || !offspringPopulationSorted) {
            sortPopulations();
        }

        /* find location of where subarrays to swap */
        int offset = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            if (parentPopulation[parentPopulation.length - 1].getFitness() > offspringPopulation[i].getFitness()) {
                offset++;
            } else {
                break;
            }
        }

        /* commence swap */
//        System.arraycopy(parentPopulation, parentPopulation.length-offset, offspringPopulation, 0, offset);
        /*
        NOTE: might be better to swap the genes and fitness rather than the whole
              Individual object (ran into issues last time this happened...)
         */
        for (int i = 0; i < offset; i++) {
            int parentIndex = parentPopulation.length - offset + i;
            offspringPopulation[i] = Individual.clone(parentPopulation[parentIndex]);
        }
    }

    /*
    get the best individual from the offspring array
      - will be used for the stop condition
     */
    private void setBestIndividual() {
        //check if offspring has been sorted yet
        if (!offspringPopulationSorted) {
            sortPopulations();
        }

        //return last individual (which has highest fitness)
        bestIndividual = Individual.clone(offspringPopulation[POPULATION_SIZE - 1]);
    }

    /*
    get average fitness
    This can be used to get average fitness of parent or children
     */
    private void showAverageFitness(Individual[] population) {
        double totalFitness = 0;
        for (Individual individual : population) {
            totalFitness += individual.getFitness();
        }

        System.out.println("Average Fitness: " + (totalFitness / population.length));
    }

    /*
    shows best fitness
    shows best fitness of parent population of child population
    REDUNDANT
     */
    private void showBestFitness(Individual[] population) {
        double bestFitness = 0;
        for (Individual individual : population) {
            if (individual.getFitness() > bestFitness) {
                bestFitness = individual.getFitness();
            }
        }
        System.out.println("Best Fitness: " + bestFitness);
    }

    /*
    shows best fitness
    uses the bestIndividual to show best fitness
     */
    private void showBestFitness() {
        System.out.println("Best Fitness: " + bestIndividual.getFitness());
    }

    /*
    stop condition for stopping the GA (found best solution)
    will be evaluating the best individual (as they will have the most rules correct)
     */
    private boolean stopCondition(Rule[] testingSet, Individual bestIndividual) {

        int numberOfCorrectRules = 0;
        int counter = 0;
        for (Rule rule : testingSet) {
            for (Rule gene : bestIndividual.getGenes()) {
                counter = 0;
                for (int i = 0; i < gene.getConditionLength(); i++) {
                    if (gene.getConditionValueFromIndex(i) == rule.getConditionValueFromIndex(i)
                            || gene.getConditionValueFromIndex(i) == 2) {
                        counter++;
                    } else {
                        break; //no point to continue, since we know that the individual did not meet the rule.
                        //could use a return false; as we know it did not match all the rules....
                    }
                }
                if (counter == rule.getConditionLength()) {
                    if (gene.getOutput() == rule.getOutput()) {
                        numberOfCorrectRules++; //the whole rule is correct!
                    }
                    break; //go to next rule to test, even if the whole rule was not correct
                }
            }
        }

        //maybe output numberOfCorrectRules, to see where it is at...
        return numberOfCorrectRules >= testingSet.length;

    }

    private void generateOffspring() {
        /*
        clone the parentpop
          - this is so we can mess around with the parentpop fitness and do some crazy stuff to it!
         */
        //NOTE, Arrays.copyOf copies the reference/pointer of parentpopulation
        Individual[] parentPopulationCopy = new Individual[POPULATION_SIZE];
        for (int i = 0; i < parentPopulationCopy.length; i++) {
            parentPopulationCopy[i] = Individual.clone(parentPopulation[i]);
//            parentPopulationCopy[i] = new Individual(CHROMOSOME_LENGTH, CLASSIFIER_LENGTH);
//            parentPopulationCopy[i].setGenes(parentPopulation[i].getGenes());
//            parentPopulationCopy[i].setFitness(parentPopulation[i].getFitness()); 
        }

        FitnessFunction.convertFitnessQuadratic(parentPopulationCopy, 2);
        FitnessFunction.addFitnessBiasToHighest(parentPopulationCopy, 100);

        /* Selection / Crossover / Mutation */
        for (int i = 0; i < offspringPopulation.length; i++) {
            /* Selection */
            Individual[] parents = new Individual[2];
            parents[0] = Selection.fitnessProportionateSelection(parentPopulationCopy);
            parents[1] = Selection.fitnessProportionateSelection(parentPopulationCopy);

            /* Crossover */
            //creates 2 children
            Individual[] children = Crossover.singlePointCrossover(parents[0], parents[1], CROSSOVER_RATE);

            /* Mutation */
            offspringPopulation[i] = children[0];
            offspringPopulation[i] = Mutation.mutationGenericsV2(offspringPopulation[i], MUTATION_RATE);
            if (i + 1 < offspringPopulation.length) {
                i++;
                offspringPopulation[i] = children[1];
                offspringPopulation[i] = Mutation.mutationGenericsV2(offspringPopulation[i], MUTATION_RATE);
            }
        }

        //calculate children fitness
        FitnessFunction.fitnessFunctionCompareRulesAll(dataset, offspringPopulation);
        FitnessFunction.fitnessFunctionCompareRulesAll(dataset, parentPopulation);
        //sort fitness's of each population
        sortPopulations();

        //perform sub array swap
        swapWorstForBest();

        //set best individual
        setBestIndividual();

    }

    private void copyChildrenToParents(Individual[] offspringPopulation, Individual parentPopulation[]) {
//        System.arraycopy(parentPopulation, 0, offspringPopulation, 0, POPULATION_SIZE);
        for (int i = 0; i < parentPopulation.length; i++) {
            parentPopulation[i] = Individual.clone(offspringPopulation[i]);
        }
    }

    /*
    run method for the code
      - will run the GA code
     */
    public void run() {
        readFile();

        /*
        for dataset1 and dataset2, the size of the file is small
        so it is okay to have the training set to be the same size as testing set
        (train using all of the data)
         */
        Rule[] trainingSet = dataset;
        Rule[] testingSet = dataset;

        initialisePopulations();

        /* GA loop! */
        Scanner scan = new Scanner(System.in); //we can use this to examine every 1000th generation
        int numberOfGenerations = 0;
        boolean stopCondition = false;
        double worstFitness, averageFitness, bestFitness;

        while (numberOfGenerations < 1000) {

            //the fitness's of the offspring and children are not ordered anymore
            parentPopulationSorted = offspringPopulationSorted = false;

            //calc fitness of parent pop
            FitnessFunction.fitnessFunctionCompareRulesAll(dataset, parentPopulation);
            if (numberOfGenerations == 0) {
                worstFitness = getWorstFitness(parentPopulation);
                averageFitness = getAverageFitness(parentPopulation);
                bestFitness = getBestFitness(parentPopulation, true);
                if (write) {
                    out.writePopulation(worstFitness + "," + averageFitness + "," + bestFitness);
                }
            }

//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(GABool.class.getName()).log(Level.SEVERE, null, ex);
//            }
            //generate offspring
            generateOffspring();

//            System.out.println(parentPopulation[parentPopulation.length-1].getFitness());
            //show stats
            showBestFitness();

            //write to csv
            worstFitness = getWorstFitness(offspringPopulation);
            averageFitness = getAverageFitness(offspringPopulation);
            bestFitness = getBestFitness(offspringPopulation, false);
            if (write) {
                out.writePopulation(worstFitness + "," + averageFitness + "," + bestFitness);
            }

            //evaluate stop condition
            stopCondition = stopCondition(dataset, bestIndividual);

            /* set next generation */
            //need to set the children to be the next parents
//            for (int i = 0; i < parentPopulation.length; i++) {
//                parentPopulation[i].setGenes(offspringPopulation[i].getGenes());
//                parentPopulation[i].setFitness(offspringPopulation[i].getFitness());
//            }
            copyChildrenToParents(offspringPopulation, parentPopulation);

            //increment generations
            numberOfGenerations++;
        }

        /*
        If we have broken out of the loop above, we need to show the best individual
         */
        System.out.println("Stopped!");
        System.out.println("Number Of Generations: " + numberOfGenerations);
        bestIndividual = FitnessFunction.fitnessFunctionCompareRulesSingle(trainingSet, bestIndividual);
        System.out.println(bestIndividual.toString());
        //close the file writer to end print out anything saved in it
        if (write) {
            out.close();
        }
    }

    /*
    Methods down here are used to write to the file!
    - getWorstFitness
    - getAverageFitness
    - getBestFitness
     */
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
}

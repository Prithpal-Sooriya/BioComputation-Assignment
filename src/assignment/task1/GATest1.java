/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Prithpal Sooriya
 */
public class GATest1 {

    //HYPER PARAMETERS
    private static final int POPULATION_SIZE = 100;
    private static final int CHROMOSOME_LENGTH = 64; //chromosome length != DNA length, as chromosome becomes decoded and encoded to binary string later on
    private static final double CROSSOVER_RATE = 0.9; // 0.6-0.9
    private static final double MUTATION_RATE = 0.02; // 1/popsize - 1/chromosome length (or DNA length)

    public static void main(String[] args) {

        /*read in the file: SUCCESS*/
        Scanner scan = new Scanner(GATest1.class.getResourceAsStream("/Files/data2.txt"));
//        //check if it works
//        while(scan.hasNextLine()){
//            System.out.println(scan.nextLine());
//        }

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

        /* Loop through each generation */
        scan = new Scanner(System.in);
        long i = 0;
        boolean stop = true;
        while (stop) {

            /* reset the fitness just to be safe... */
            for (int j = 0; j < population.length; j++) {
                population[j].setFitness(0);
            }

            /* evaulate fitness */
//            FitnessFunction.fitnessFunctionCompareConditionsAll(trainingSet, population);
              FitnessFunction.fitnessFunctionCompareRulesAll(trainingSet, population);

            showInformation(population);

            /* evaluate stop condition */
            stop = !stopCondition(population, trainingSet);
            if (stop == false) {
                break;
            }

            /* Create new generation */
            generateOffSpringV2(trainingSet, population);
//            population = Arrays.copyOf(offSpring, offSpring.length);
            i++;
//            scan.next(); //used to wait every generation
        }

        System.out.println("NUMBER OF GENERATIONS: " + i);

    }

    /*
    Stop Condition
    For this to pass, 1 INDIVIDUAL MUST MEET ALL OF THE TEST DATA, not the population as a whole :/
    NEED TO FIX.
     */
    //tidy up the stop condition
    private static int stopConditionSingle(Rule rule, Individual individual) {
        int counter = 0;
        //check if any of the genes matches the rule
        for (Rule gene : individual.getGenes()) {
            counter = 0;
            for (int i = 0; i < rule.getConditionLength(); i++) {
                if (gene.getConditionValueFromIndex(i) == rule.getConditionValueFromIndex(i)
                        || gene.getConditionValueFromIndex(i) == 2) {
                    counter++;
                }
            }

            if (counter >= rule.getConditionLength()) {
                if (gene.getOutput() == rule.getOutput()) {
                    return 1; //condition and output matched
                }
                else{
                    return 0; //condition matched but rule did not match
                }
            }
//            if (rule.compareTo(gene) == 0) {
//                return 1; //the rule does match to a gene!
//            }
        }
        return 0; //none match 
    }

    private static boolean stopCondition(Individual[] population, Rule[] testingSet) {
        //loop through each individual and keep a counter, if that individual passes all test then return true!
        int numberCorrect = 0;
        int correctConditions = 0;

        //loop through each person
        for (Individual individual : population) {
//            numberCorrect = 0;
//            //loop through each rule
//            for (Rule rule : testingSet) {
//                numberCorrect += stopConditionSingle(rule, individual);
//            }

            FitnessFunction.fitnessFunctionCompareRulesSingle(testingSet, individual);
            numberCorrect = (int)individual.getFitness();
            
            //check if that person matches all the rules
            if (numberCorrect >= testingSet.length) {
                System.out.println("=================================");
                System.out.println("Correct Individual from population:");
                //recalculate the individuals fitness
                individual = FitnessFunction.fitnessFunctionCompareRulesSingle(testingSet, individual);

                System.out.println(individual.toString());
                return true;
            }
        }
        return false; //could not find a member of population which solves whole test set.
    }

    /*
    Output
    used to show what is inside each individual
     */
    private static void showInformation(Individual[] population) {
        double totalFitness = 0.0;
        for (Individual individual : population) {
//            System.out.println(individual.getFitness());
//            System.out.println(individual.toString());
            totalFitness += (double) individual.getFitness();
        }
        System.out.println("average fitness: " + totalFitness / population.length);
//        System.out.println("total fit " + totalFitness);
//        System.out.println("============================");
    }

    private static void generateOffSpringV2(Rule[] trainingSet, Individual[] population) {
        //clone the parent population
        Individual[] parentPopulation = Arrays.copyOf(population, population.length);

        /* other fitness evaluations */
//            FitnessFunction.convertFitnessQuadratic(population, 2);
        FitnessFunction.convertFitnessQuadratic(population, 2);
        FitnessFunction.addFitnessBiasToHighest(population, 100); //bias is a multiplication (maybe we should change this into an addition)
//            FitnessFunction.normalizeFitnessToTotal(population);

        Individual offspring[] = new Individual[population.length];

        for (int i = 0; i < offspring.length; i++) {

            //selection of parents
            Individual parents[] = new Individual[2];
            parents[0] = Selection.fitnessProportionateSelection(population);
            parents[1] = Selection.fitnessProportionateSelection(population);

            //crossover
            Individual[] children = Crossover.singlePointCrossover(parents[0], parents[1], CROSSOVER_RATE);

            //add the crossover to the offspring array
            offspring[i] = children[0];
            if (i + 1 < offspring.length) {
                i++;
                offspring[i] = children[1];
            }
        }

        //mutation --> place this for loop in the mutation function.
        for (int i = 0; i < offspring.length; i++) {
            offspring[i] = Mutation.mutationGenericsV2(offspring[i], MUTATION_RATE);
        }

        //fitness function on child
//        FitnessFunction.fitnessFunctionCompareConditionsAll(trainingSet, offspring);
        FitnessFunction.fitnessFunctionCompareRulesAll(trainingSet, offspring);
        
        //sort both old and new generation
        FitnessFunction.sortPopulationByFitness(parentPopulation);
        FitnessFunction.sortPopulationByFitness(offspring);

//        //take the best individuals from parents, and replace the worst individuals from offspring
//        int offset = 0;
//        for (int i = 0; i < offspring.length; i++) {
//            if(parentPopulation[parentPopulation.length-1].getFitness() > offspring[i].getFitness()){
//                offset++;
//            }
//            else{
//                break;
//            }
//        }
//        
//        //add parentpop[N-offset] to parentpop[N] to beginning of offspring
//        for (int i = 0; i < offset; i++) {
//            int parentIndex = parentPopulation.length - offset + i;
//            offspring[i] = parentPopulation[parentIndex];
//        }
        
        
        //copy genes into population array parameter
        for (int i = 0; i < offspring.length; i++) {
            //copy genes
            for (int j = 0; j < offspring[i].getGenesLength(); j++) {
                population[i].setGeneFromIndex(j, offspring[i].getGeneFromIndex(j));
            }
        }
        
    }

    /*
    Generate offspring
    includes selection, crossover, and mutation
     */
    private static void generateOffSpring(Rule[] trainingSet, Individual[] population) {

        /* other fitness evaluations */
//            FitnessFunction.convertFitnessQuadratic(population, 2);
        FitnessFunction.convertFitnessQuadratic(population, 2);
        FitnessFunction.addFitnessBiasToHighest(population, 100); //bias is a multiplication (maybe we should change this into an addition)
//            FitnessFunction.normalizeFitnessToTotal(population);

        Individual offspring[] = new Individual[population.length];

        for (int i = 0; i < offspring.length; i++) {

            //selection of parents
            Individual parents[] = new Individual[2];
            parents[0] = Selection.fitnessProportionateSelection(population);
            parents[1] = Selection.fitnessProportionateSelection(population);

            //crossover
            Individual[] children = Crossover.singlePointCrossover(parents[0], parents[1], CROSSOVER_RATE);

            //add the crossover to the offspring array
            offspring[i] = children[0];
            if (i + 1 < offspring.length) {
                i++;
                offspring[i] = children[1];
            }
        }

        //mutation --> place this for loop in the mutation function.
        for (int i = 0; i < offspring.length; i++) {
            offspring[i] = Mutation.mutationGenericsV2(offspring[i], MUTATION_RATE);
        }

        //lets copy to the population individually, this might be the cause of the object referencing being the same!!!
        //FIXED
        for (int i = 0; i < offspring.length; i++) {

            //copy genes
            for (int j = 0; j < offspring[i].getGenesLength(); j++) {
                population[i].setGeneFromIndex(j, offspring[i].getGeneFromIndex(j));
            }

        }
    }

}

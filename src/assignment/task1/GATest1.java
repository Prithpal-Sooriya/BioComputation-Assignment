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

    private static final int POPULATION_SIZE = 10;
    private static final int CHROMOSOME_LENGTH = 7;
    private static final double CROSSOVER_RATE = 0.9; // 0.6-0.9
    private static final double MUTATION_RATE = 0.01; // 1/popsize - 1/chromosome length

    public static void main(String[] args) {

        /*read in the file: SUCCESS*/
        Scanner scan = new Scanner(GATest1.class.getResourceAsStream("/Files/data1.txt"));
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
        int i = 0;
        System.out.println("GENERATION: " + i);
        while (!stopCondition(population, trainingSet)) {

            FitnessFunction.fitnessFunctionCompareConditionsAll(trainingSet, population);
//            FitnessFunction.convertFitnessQuadratic(population, 2);
//            FitnessFunction.addFitnessBiasToHighest(population, 100);
            
            showInformation(population);
            
            population = generateOffSpring(trainingSet, population);
            i++;
//            scan.next(); //used to wait every generation
        }

    }

    /*
    Stop Condition
    For this to pass, 1 INDIVIDUAL MUST MEET ALL OF THE TEST DATA, not the population as a whole :/
    NEED TO FIX.
     */
    private static boolean stopCondition(Individual[] population, Rule[] testingSet) {
        //loop through each individual and keep a counter, if that individual passes all test then return true!
        int numberCorrect = 0;
        int correctConditions = 0;
        for (Individual individual : population) { //for each person...
            numberCorrect = 0;
            for (Rule gene : individual.getGenes()) { //their genes
                for (Rule rule : testingSet) { //test data
                    correctConditions = 0;
                    for (int i = 0; i < gene.getConditionLength(); i++) { //each bit for condition
                        if (rule.getConditionValueFromIndex(i) == gene.getConditionValueFromIndex(i)
                                || gene.getConditionValueFromIndex(i) == 2) {
                            correctConditions++;
                        }
                    }
                    if (correctConditions >= rule.getConditionLength() && rule.getOutput() == gene.getOutput()) {
                        numberCorrect++;
                        break; //so we can test next gene
                    }
                }
            }
        }

        if (numberCorrect == testingSet.length) return true;
        else return false;
    }

    /*
    Output
    used to show what is inside each individual
     */
    private static void showInformation(Individual[] population) {
        for (Individual individual : population) {
            System.out.println(individual.toString());
        }
        System.out.println("============================");
    }

    /*
    Generate offspring
    includes selection, crossover, and mutation
     */
    private static Individual[] generateOffSpring(Rule[] trainingSet, Individual[] population) {
        Individual offspring[] = new Individual[population.length];

        for (int i = 0; i < offspring.length; i++) {

            //selection of parents
            Individual parents[] = new Individual[2];
            parents[0] = Selection.FitnessProportionateSelection(population);
            parents[1] = Selection.FitnessProportionateSelection(population);

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
            offspring[i] = Mutation.mutationGenerics(offspring[i], MUTATION_RATE);
        }

        return offspring;
    }

}

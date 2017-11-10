/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Prithpal Sooriya
 */
public class FitnessFunction {

    private FitnessFunction() {
    }

    /*
    Sorting a population by their fitness
    This will be used so we can replace less fit new population with more fit old
    population
     */
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
                return 0; //they were equal
            }
        });
    }

    //so we can map the fitness to a range which is easier to use --> for example 0-1
    //algorith is linear transformation and I found out how to do it here: https://stackoverflow.com/questions/345187/math-mapping-numbers
    public static double normalizeFitness(double currentFitness, double oldRangeStart, double oldRangeEnd, double newRangeStart, double newRangeEnd) {
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
            population[i].setFitness(
                    normalizeFitness(population[i].getFitness(), 0, totalFitness, 0, 1));
//            double newFitness = (double) population[i].getFitness() / (double) totalFitness;
//              System.out.println("nft total fit: " + totalFitness);
//            population[i].setFitness(newFitness);
//
        }
    }

    public static void fitnessFunctionCompareConditionsAll(Rule[] trainingRuleset, Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i] = fitnessFunctionCompareConditionsSingle(trainingRuleset, population[i]);
        }
    }

    //IMPLEMENTED GENERICS
    public static Individual fitnessFunctionCompareConditionsSingle(Rule[] trainingRuleset, Individual individual) {

        individual.setFitness(0); //reset each individuals fitness

        double highestMatchingRate = 0;
        double tempFitness = 0;
        double counter = 0;

        //we will increment the fitness for each condition it gets correct
        for (Rule rule : trainingRuleset) { //loop through each rule
            highestMatchingRate = 0;
            for (int i = 0; i < individual.getGenesLength(); i++) { //loop through each gene
                counter = 0;
                //compare each rule in the ruleset to the genes of the individual
                /* ADDING THE COUNTER FOR EACH BIT/PART IS CORRECT */
                for (int j = 0; j < rule.getConditionLength(); j++) { //loop through each condition
                    if (rule.getConditionValueFromIndex(j) == individual.getGeneFromIndex(i).getConditionValueFromIndex(j)
                            || individual.getGeneFromIndex(i).getConditionValueFromIndex(j) == 2) {
                        counter++;
                    }
                }
                if (rule.getOutput() == individual.getGeneFromIndex(i).getOutput()) {
                    counter++;
                }

                /* CHECK HOW MUCH OF GENE IS CORRECT */
                /* EDIT: BREAK OUT IF ALL CONDITION CORRECT, EVEN IF OUTPUT IS CORRECT! */
                if (counter >= individual.getGeneFromIndex(i).getConditionLength()) {
                    highestMatchingRate = counter; //set the heighest so we can after FOR loop
                    break; //simple conflict resolution solution, pick first gene that matches rule.
                } else {
                    //pick the highest matching so far
                    if (counter > highestMatchingRate) {
                        highestMatchingRate = counter;
                    }
                }

            } //end loop through gene
//            System.out.println("match: " + highestMatchingRate);
            tempFitness += highestMatchingRate/(rule.getConditionLength()+1);
//              tempFitness += highestMatchingRate;
        } //end loop through whole genes.

        //number correct TOTAL = 192 (rule condition + rule output * number of rules in training set)
        //(5+1) * 32 = 192.
//        tempFitness = tempFitness / trainingRuleset.length;
        if (Double.isNaN(tempFitness)) {
            System.out.println("NAN");
        }
        if (!Double.isFinite(tempFitness)) {
            System.out.println("Infinite");
        }
//        System.out.println("fitness: " + tempFitness);
        individual.setFitness(tempFitness);

        //have to return back the individual, because the changes are not kept on an Object (pass by value)
        return individual;
    }

//    public static Individual fitnessFunctionCompareConditionsSingleV2(Rule[] trainingRuleSet, Individual individual) {
//        //set the individual fitness to 0 (just to start with)
//        individual.setFitness(0);
//
//        //counter to hold the how many conditions were correct
//        double newFitness = 0;
//        double tempHighest = 0;
//        int counter = 0;
//
//        for (Rule rule : trainingRuleSet) { //each rule
//            
//            tempHighest = 0;
//
//            for (int i = 0; i < individual.getGenesLength(); i++) { //each gene
//                counter = 0;
//                Rule gene = individual.getGeneFromIndex(i);
//
//                for (int j = 0; j < gene.getConditionLength(); j++) { //each gene condition bit
//                    if (rule.getConditionValueFromIndex(j) == gene.getConditionValueFromIndex(j)
//                            || gene.getConditionValueFromIndex(j) == 2) {
//                        counter++;
//                    }
//                }
//                
//                /*
//                check if the whole condition matches
//                if the whole condition matches, check the output
//                    if output matches counter++
//                counter/size of rule
//                
//                BREAK; //if the whole rule matches, even if the output is incorrect we will still use it!
//                */
//                if(counter == rule.getConditionLength()) {
//                    if(rule.getOutput() == gene.getOutput()) {
//                        counter++;
//                    }
//                    tempHighest = (double)((double)counter/(rule.getConditionLength()+1));
//                    
//                    break; //go to next rule, as we have a matching condition (even if the output is incorrect)
//                }
//                else {
//                    //no condition matched, so lets get the temp highest
//                    if(tempHighest < (counter/rule.getConditionLength()+1)){
//                        tempHighest = (double) ((double)counter/(rule.getConditionLength()+1));
//                    }
//                }
//
//            }
//            
//            //out of checking all genes for 1 rule, lets add whatever the highest was to new fitness
////            System.out.println("tempHighest: " + tempHighest);
//            newFitness += tempHighest;
//
//        }
//        
//        //finally /number of rules
//        newFitness /= trainingRuleSet.length;
//        
//        individual.setFitness(newFitness);
//        return individual;
//
//    }
    
    //final version, we can use this to compare if all the rules in the genes in an individual are correct
    //This can be used near the end stages to see select individuals in the population who have complete sets
    //EDIT: NEEDED TO ACCOMODATE GENERIC CONDITIONS
    public static void fitnessFunctionCompareRulesAll(Rule[] trainingRuleset, Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i].setFitness(0);
            population[i] = fitnessFunctionCompareRulesSingle(trainingRuleset, population[i]);

        }
    }

    public static Individual fitnessFunctionCompareRulesSingle(Rule[] trainingRuleset, Individual individual) {
        double tempFitness = 0;
        int allCorrect = 0;
//        double incorrectFitness = 0;
        //we will increment the fitness for each condition it gets correct (Rule rule : trainingRuleset)
        for (Rule rule : trainingRuleset) { //loop through each rule
            for (int i = 0; i < individual.getGenesLength(); i++) { //loop through each gene
                allCorrect = 0;
                //compare each rule in the ruleset to the genes of the individual
                for (int j = 0; j < rule.getConditionLength(); j++) { //loop through each condition
                    if (rule.getConditionValueFromIndex(j) == individual.getGeneFromIndex(i).getConditionValueFromIndex(j)
                            || individual.getGeneFromIndex(i).getConditionValueFromIndex(j) == 2) {
                        allCorrect++;
                    }
                }

                if (allCorrect >= rule.getConditionLength()) { //if a gene matches rule
                    tempFitness++;
                    break; //we will now test the next rule (this will be so we can form a basic conflict resolution)
                }
//                else {
//                    incorrectFitness++;
//                }
                //at the end of each RULE we can div by length of rule?
                //gives us a larger fitness if we just div at end?
//                tempFitness /= rule.getConditionLength();
            }
        }

        //now at the end, we can div by size of training ruleset (as this time we are looking at complete rules.
        //so for this we can say an individual got e.g. 3/7 rules correct.
        tempFitness /= trainingRuleset.length;

        //might want to add a check, in case the result is 0..
        if (tempFitness == 0) {
            tempFitness = 0.001; //dunno bout this...
        }

        individual.setFitness(tempFitness);

        //have to return back the individual, because the changes are not kept on an Object (pass by value)
        return individual;
    }

    /*
    allows the fitness function to become quadratic, so to make individuals who match more, more relevent
     */
    // EDIT: FACING AN ISSUE WHEN SETTING AND GETTING THE FITNESS???
    public static void convertFitnessQuadratic(Individual[] population, int exponent) {
        Scanner scan = new Scanner(System.in);

//        for (int i = 0; i < population.length; i++) {
//            Individual ind = population[i];
//            double tempFitness = ind.getFitness();
//            tempFitness = ind.getFitness();
//
////            System.out.println("before " + i + ": " + tempFitness);
////            scan.nextLine();
//            tempFitness = tempFitness * 2;
////            System.out.println("After " + i + ": " + tempFitness);
////            scan.nextLine();
//            ind.setTempFitness(tempFitness);
//            population[i] = ind;
//        }
//
//        //then set the values into original fitness at the end? mitigate this issue of object mismatch
//        for (int i = 0; i < population.length; i++) {
//            System.out.println("before " + i + ": " + population[i].getFitness());
//            population[i].setFitness(population[i].getTempFitness());
//            System.out.println("After " + i + ": " + population[i].getFitness());
//            scan.nextLine();
//
//        }
        //lets see if we split the function
        for (int i = 0; i < population.length; i++) {

//            System.out.println("before " + i + ": " + population[i].getFitness());
//            System.out.println(population[i].hashCode());
            population[i] = convertFitnessQuadraticSingle(population[i], exponent);
//            System.out.println("After " + i + ": " + population[i].getFitness());
//            System.out.println(population[i].hashCode());
//            scan.nextLine();
        }

    }

    public static Individual convertFitnessQuadraticSingle(Individual individual, int exponent) {
        double tempFitness = individual.getFitness();
        tempFitness = Math.pow(tempFitness, 2);
        individual.setFitness(tempFitness);
        return individual;
    }

    public static void convertFitnessExponential(Individual[] population, int exponent) {
        for (int i = 0; i < population.length; i++) {

//            System.out.println("before " + i + ": " + population[i].getFitness());
//            System.out.println(population[i].hashCode());
            population[i] = convertFitnessExponentSingle(population[i], exponent);
//            System.out.println("After " + i + ": " + population[i].getFitness());
//            System.out.println(population[i].hashCode());
//            scan.nextLine();
        }
    }

    public static Individual convertFitnessExponentSingle(Individual individual, int exponent) {
        double tempFitness = individual.getFitness();
        tempFitness = Math.pow(2, tempFitness);
        individual.setFitness(tempFitness);
        return individual;
    }

    /*
    allows the addition of bias to an individual to the population, this allows a certain individual to be picked more
    this case will allow the most fittest individual to have a higher chance of getting picked for selection
    This bias will be MULTIPLIED TO THE CURRENT FITNESS
     */
    public static void addFitnessBiasToHighest(Individual[] population, int bias) {
        double totalFitness = 0;
        double maxFitness = 0;
        int maxFitnessIndex = 0;

        //get total and max fitness
        for (int i = 0; i < population.length; i++) {
            totalFitness += population[i].getFitness();
            if (population[i].getFitness() > maxFitness) {
                maxFitness = population[i].getFitness();
                maxFitnessIndex = i;
            }
        }

        //create the bias
        double newFitness = population[maxFitnessIndex].getFitness();
        totalFitness -= newFitness; //remove it from the total (used for if we want to use total fitness again...)
        newFitness += bias;
        totalFitness += newFitness; //add back to total (used for if we want to use total fitness again...)
        population[maxFitnessIndex].setFitness(newFitness);

        //re normalise the fitness for all population    
//        for (int i = 0; i < population.length; i++) {
//            newFitness = normalizeFitness(newFitness, 0, totalFitness, 0, 1);
////            System.out.println("ff fit " + newFitness);
//            population[i].setFitness(newFitness);
//        }
    }

    /*
    We will need to have some code that can deal with conflict resolutions (when more than 1 gene succeeds with same rules)
    Conflict resolution
    --> select gene which has highest overall fitness (how many rules can it succeed from)
    --> if the overall fitness is still the same, then choose randomly
    
    EDIT: WITH MY CURRENT CODE, THIS WILL BE HARD TO IMPLEMENT.... IM STUCK!!!
        --> TALK TO LARRY ABOUT THIS IDEA FROM THE AQ TECHNIQUE I FOUND AND WANT TO IMPLEMENT.
        --> FOUND OUT THAT WE SHOULD TO A SIMPLE COMFLICT RESOLUTION (PICK THE FIRST ONE THAT MATCHES)
     */
    public static Individual conflictResolutionFitness(Rule[] trainingRuleSet, Individual individual) {
        Map<Rule, ArrayList<Integer>> conflictMapRules = new HashMap<>();
        Map<Rule, Integer> conflictMapGenes = new HashMap<>();
        //init the conflict map
        for (Rule rule : trainingRuleSet) {
            conflictMapRules.put(rule, new ArrayList<Integer>());
        }
        for (Rule gene : individual.getGenes()) {
            conflictMapGenes.put(gene, 0);
        }

        //do fitnessFunctionCompareRules
        int allCorrect = 0;
        double tempFitness = 0;
        for (int i = 0; i < individual.getGenesLength(); i++) {
            for (Rule rule : trainingRuleSet) {
                allCorrect = 0;
                for (int j = 0; j < rule.getConditionLength(); j++) {
                    if (rule.getConditionValueFromIndex(j) == individual.getGeneFromIndex(i).getConditionValueFromIndex(j)
                            || individual.getGeneFromIndex(i).getConditionValueFromIndex(j) == 2) {
                        allCorrect++;
                    }
                }
                if (allCorrect == rule.getConditionLength()) {
                    tempFitness++;

                    //add this to the mapping of conflicts
                    //we will evaluate the map later to see any conflicts
                    ArrayList<Integer> conflictsToThisRule = conflictMapRules.get(rule);
                    conflictsToThisRule.add(i);
                    conflictMapRules.put(rule, conflictsToThisRule);

                    //this will be the map for genes, and their occurances
                    int occurrences = conflictMapGenes.get(individual.getGeneFromIndex(i));
                    occurrences++;
                    conflictMapGenes.put(individual.getGeneFromIndex(i), occurrences);

                }
            }
        }

        //evaulate the conflict map and subtract overrall fitness
        for (Rule rule : trainingRuleSet) {

            ArrayList<Integer> conflicts = conflictMapRules.get(rule);

            if (conflicts.size() > 1) {
                //if there is 2 or more genes, then this is a conflict!

                //check if they are all equal
                boolean allEqual = true;
                int checkToVal = conflictMapGenes.get(individual.getGeneFromIndex(conflicts.get(0)));
                for (int i = 1; i < conflicts.size(); i++) {
                    if (checkToVal != (int) conflictMapGenes.get(individual.getGeneFromIndex(conflicts.get(i)))) {
                        allEqual = false;
                    }
                }

                //if they are equal, then return we will use a random gene for that rule.
                if (allEqual) {
                    //DO NOT KNOW HOW TO IMPLEMENT!
                } else {
                    //test find out which one is bigger
                    int largestOccurrencesIndex = -1;
                    int largestOccurrences = -1;

                    for (int i = 0; i < conflicts.size(); i++) {
                        int occurrences = conflictMapGenes.get(individual.getGeneFromIndex(conflicts.get(i)));

                        if (occurrences > largestOccurrences) {
                            largestOccurrences = occurrences;
                            largestOccurrencesIndex = i;
                        }
                    }

                    //found the most typical gene, so we will return that
                    //DO NOT KNOW HOW TO IMPLEMENT!
                }

                //finally, we will subtract the overrall fitness (not sure if we should do this..., because technically this is not the fitness...)
            }

        }
        return null;
    }
}

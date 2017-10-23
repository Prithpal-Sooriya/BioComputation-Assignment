/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Prithpal Sooriya
 */
public class FitnessFunction {

    private FitnessFunction() {
    }

    //so we can map the fitness to a range which is easier to use --> for example 0-1
    //algorith is linear transformation and I found out how to do it here: https://stackoverflow.com/questions/345187/math-mapping-numbers
    public static double normalizeFitness(double currentFitness, double oldRangeStart, double oldRangeEnd, double newRangeStart, double newRangeEnd) {
        double m = (currentFitness - oldRangeStart) / (oldRangeEnd - oldRangeStart);
        double x = (newRangeEnd - newRangeStart);
        double c = newRangeStart;

        return (m * x) + c;
    }

    //EDIT: NEEDED TO ACCOMODATE GENERIC CONDITIONS
    public static void fitnessFunctionCompareConditionsAll(Rule[] trainingRuleset, Individual[] population){
        for (int i = 0; i < population.length; i++) {
            population[i] = fitnessFunctionCompareConditionsSingle(trainingRuleset, population[i]);
        }
    }
    public static Individual fitnessFunctionCompareConditionsSingle(Rule[] trainingRuleset, Individual individual) {

        individual.setFitness(0);
        
        double tempFitness = 0;
        double counter = 0;
        

        //we will increment the fitness for each condition it gets correct
        for (Rule rule : trainingRuleset) { //loop through each rule
            for (int i = 0; i < individual.getGenesLength(); i++) { //loop through each gene
                counter = 0;
                //compare each rule in the ruleset to the genes of the individual
                for (int j = 0; j < rule.getConditionLength(); j++) { //loop through each condition
                    if (rule.getConditionValueFromIndex(j) == individual.getGeneFromIndex(i).getConditionValueFromIndex(j)
                            || individual.getGeneFromIndex(i).getConditionValueFromIndex(j) == 2) {
//                        tempFitness++;
                        counter++;
                    }
                }
                if(rule.getOutput() == individual.getGeneFromIndex(i).getOutput()){
                    counter++;
                }
//                tempFitness += counter/rule.getConditionLength();
                if(counter == individual.getGeneFromIndex(i).getConditionLength()+1){
                    tempFitness += counter; //only add if correct?
                    break; //simple conflict resolution solution, pick first gene that matches rule.
                }
                //at the end of each RULE we can div by total rule?
                //gives us a larger fitness if we just div at end?
//                tempFitness /= rule.getConditionLength();
            }
        }

        //now at the end, we can div by total number of conditions. (of all rules compared).
        tempFitness = tempFitness / ((trainingRuleset.length * trainingRuleset[0].getConditionLength()+trainingRuleset.length));
//        tempFitness /= trainingRuleset.length;
//        System.out.println(tempFitness);
        individual.setFitness(tempFitness);

        //have to return back the individual, because the changes are not kept on an Object (pass by value)
        return individual;
    }

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
        if(tempFitness == 0) {
            tempFitness = 0.001; //dunno bout this...
        }
        
        individual.setFitness(tempFitness);

        //have to return back the individual, because the changes are not kept on an Object (pass by value)
        return individual;
    }

    /*
    allows the fitness function to become quadratic, so to make individuals who match more, more relevent
     */
    public static void convertFitnessQuadratic(Individual[] population, int exponent) {
        //calculate total fitness
        double totalFitness = 0;
        for (Individual individual : population) {
            totalFitness += individual.getFitness();
        }

        for (int i = 0; i < population.length; i++) {
            double newFitness = Math.pow(population[i].getFitness(), exponent);
            newFitness = normalizeFitness(newFitness, 0, totalFitness, 0, 1);
            population[i].setFitness(newFitness);
        }
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
        totalFitness -= newFitness; //remove it from the total
        newFitness *= bias;
        totalFitness += newFitness; //add back to total
        population[maxFitnessIndex].setFitness(newFitness);
        
        //re normalise the fitness for all population    
        for (int i = 0; i < population.length; i++) {
            newFitness = normalizeFitness(newFitness, 0, totalFitness, 0, 1);
            population[i].setFitness(newFitness);
        }
    }

    /*
    We will need to have some code that can deal with conflict resolutions (when more than 1 gene succeeds with same rules)
    Conflict resolution
    --> select gene which has highest overall fitness (how many rules can it succeed from)
    --> if the overall fitness is still the same, then choose randomly
    
    EDIT: WITH MY CURRENT CODE, THIS WILL BE HARD TO IMPLEMENT.... IM STUCK!!!
        --> TALK TO LARRY ABOUT THIS IDEA FROM THE AQ TECHNIQUE I FOUND AND WANT TO IMPLEMENT.
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

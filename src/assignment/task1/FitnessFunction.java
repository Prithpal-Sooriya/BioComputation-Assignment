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
    public static Individual fitnessFunctionCompareConditions(Rule[] trainingRuleset, Individual individual) {

        double tempFitness = 0;

        //we will increment the fitness for each condition it gets correct
        for (int i = 0; i < individual.getGenesLength(); i++) { //loop through each gene
            for (Rule rule : trainingRuleset) {
                //compare each rule in the ruleset to the genes of the individual
                for (int j = 0; j < rule.getConditionLength(); j++) { //loop through each condition
                    if (rule.getConditionValueFromIndex(j) == individual.getGeneFromIndex(i).getConditionValueFromIndex(j)
                            || individual.getGeneFromIndex(i).getConditionValueFromIndex(j) == 2) {
                        tempFitness++;
                    }
                }
                //at the end of each RULE we can div by total rule?
                //gives us a larger fitness if we just div at end?
//                tempFitness /= rule.getConditionLength();
            }
        }

        //now at the end, we can div by total number of conditions. (of all rules compared).
        tempFitness /= (trainingRuleset.length * trainingRuleset[0].getConditionLength());

        individual.setFitness(tempFitness);

        //have to return back the individual, because the changes are not kept on an Object (pass by value)
        return individual;
    }

    //final version, we can use this to compare if all the rules in the genes in an individual are correct
    //This can be used near the end stages to see select individuals in the population who have complete sets
    //EDIT: NEEDED TO ACCOMODATE GENERIC CONDITIONS
    public static Individual fitnessFunctionCompareRules(Rule[] trainingRuleset, Individual individual) {
        double tempFitness = 0;
        int allCorrect = 0;
        //we will increment the fitness for each condition it gets correct
        for (int i = 0; i < individual.getGenesLength(); i++) { //loop through each gene
            for (Rule rule : trainingRuleset) {
                allCorrect = 0;
                //compare each rule in the ruleset to the genes of the individual
                for (int j = 0; j < rule.getConditionLength(); j++) { //loop through each condition
                    if (rule.getConditionValueFromIndex(j) == individual.getGeneFromIndex(i).getConditionValueFromIndex(j)
                            || individual.getGeneFromIndex(i).getConditionValueFromIndex(j) == 2) {
                        allCorrect++;
                    }
                }
                if (allCorrect == rule.getConditionLength()) {
                    tempFitness++;
                }
                //at the end of each RULE we can div by length of rule?
                //gives us a larger fitness if we just div at end?
//                tempFitness /= rule.getConditionLength();
            }
        }

        //now at the end, we can div by size of training ruleset (as this time we are looking at complete rules.
        //so for this we can say an individual got e.g. 3/7 rules correct.
        tempFitness /= trainingRuleset.length;

        individual.setFitness(tempFitness);

        //have to return back the individual, because the changes are not kept on an Object (pass by value)
        return individual;
    }
}

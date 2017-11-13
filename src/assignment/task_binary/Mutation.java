/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_binary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class Mutation {

    public static final double MUTATION_RATE_GENERIC = 0.25;

    //private constructor
    private Mutation() {
    }

    /*
    mutation: to do this we first need to decode the genes, mutate, and then encode back the genes
    NOTE: this will be basic mutation, where each Rule condition AND output will change at same mutation rate.
    NOTE: we will also not be including generics into this (where # can be represented as 2)
     */
    public static Individual mutationBasic(Individual ind, double MUTATION_RATE) {
        Rule newRule;
        for (int i = 0; i < ind.getGenesLength(); i++) { //loop through each genes
            newRule = ind.getGeneFromIndex(i);
            for (int j = 0; j < newRule.getConditionLength(); j++) { //loop through conditions
                if (MUTATION_RATE > Math.random()) { //mutate condition
                    newRule.setConditionValueFromIndex(
                            j, newRule.getConditionValueFromIndex(j) ^ 1);
                }
            }
            if (MUTATION_RATE > Math.random()) { //mutate output
                newRule.setOutput(newRule.getOutput() ^ 1);
            }
            //set new rule back to gene position
            ind.setGeneFromIndex(i, newRule);
        }
        return ind;
    }

    public static Individual limitGenerics(Individual ind, int maxGenerics) {
        int numberOfGenerics = 0;
        for (int i = 0; i < ind.getGenesLength(); i++) { //loop through each gene

            //check how many generics
            numberOfGenerics = 0;
            for (int j : ind.getGeneFromIndex(i).getCondition()) {
                if (j == 2) {
                    numberOfGenerics++;
                }
            }

            if (numberOfGenerics >= maxGenerics) {
                Random rand = new Random();

                if (numberOfGenerics == ind.getGeneFromIndex(i).getConditionLength()) {
                    //the whole rule condition is generic

                    //we can mutate a random position back to binary
                    if (numberOfGenerics == ind.getGeneFromIndex(i).getConditionLength()) {
                        ind.getGeneFromIndex(i).setConditionValueFromIndex(
                                rand.nextInt(ind.getGeneFromIndex(i).getConditionLength()), rand.nextInt(2));
                    }
                } else {
                    //wanted to limit generics < condition size

                    //pick a random generic (and sequential search until we find it) and mutate back to binary
                    int randomGenericNumber = rand.nextInt(numberOfGenerics);
                    int numberOfGenericsPassed = 0;
                    for (int j = 0; j < ind.getGeneFromIndex(i).getConditionLength(); j++) {
                        if(ind.getGeneFromIndex(i).getConditionValueFromIndex(j) == 2){
                            numberOfGenericsPassed++;
                            
                            if(numberOfGenericsPassed > randomGenericNumber){
                                //change this generic to binary
                                ind.getGeneFromIndex(i).setConditionValueFromIndex(j, rand.nextInt(2));
                                break; //we can break out of the loop!
                            }
                        }
                    }
                }
            }
        }
        return ind;
    }

    /*
    More complex mutations:
    These mutations will include generics (#). These will represent if a value is a 1 or a 0.
    Generics are to only be used on the Rule Conditions, not the output!
    Make generic rate/chance... maybe 50% bitwise mutation/50% generic (if not 25% chance?)
    CREATE GENERIC_MUTATION RATE ABOVE?
        --> if generics makes the fitness worse, then remove
        --> if generics make teh fitness better, then it will be kept (this is usually more of the case!
     */
    public static Individual mutationGenerics(Individual ind, double MUTATION_RATE) {
        Rule newRule;
        Random rand = new Random();
        for (int i = 0; i < ind.getGenesLength(); i++) { //loop through genes
            newRule = ind.getGeneFromIndex(i);
            for (int j = 0; j < newRule.getConditionLength(); j++) { //loop through conditions
                if (MUTATION_RATE >= Math.random()) { //normal mutation
                    //check if normal bitwise mutation or generic mutation
                    if (MUTATION_RATE_GENERIC >= Math.random()) {
                        newRule.setConditionValueFromIndex(j, 2);
                    } else { //generic mutation
                        /*
                        if not generic mutation we need to do normal (bitwise mutation)
                        NEED TO CONSIDER:
                        - if current condition value is # (make it a random 1 or 0)
                        - if current condition is a 1/0 (bitwise flip)
                         */
                        newRule.setConditionValueFromIndex(j,
                                newRule.getConditionValueFromIndex(j) == 2
                                ? rand.nextInt(2)
                                : newRule.getConditionValueFromIndex(j) ^ 1
                        );
                    }
                }
            }
            if (MUTATION_RATE > Math.random()) {
                newRule.setOutput(newRule.getOutput() ^ 1);
            }

            //forgot to set the new gene back to the individual :P
            ind.setGeneFromIndex(i, newRule);

        }

        return ind;
    }

    /*
    The upper mutation generics might be seen as flawed
        - as we first check if a mutation has occured, and then check if we should do a generic mutation
        - this will be multiplication of mutation_rate an mutation_rate_generic
            - e.g. 0.5 * 0.25 = 0.125
                - meaning a very small chance for generic mutation!!
    
    This version will hopefully solve that (as well as the allowing not all the condition to be generic --> no 22222)
    
     */
    public static Individual mutationGenericsV2(Individual ind, double MUTATION_RATE) {

        //var to hold how many generics are in a gene
        int numberOfGenerics = 0;

        //Random number generator
        Random rand = new Random();

        //loop through each gene for an individual
        for (int i = 0; i < ind.getGenes().length; i++) {
            Rule newRule = ind.getGeneFromIndex(i);

            //now do mutations on condition
            for (int j = 0; j < newRule.getConditionLength(); j++) {

                if (MUTATION_RATE >= Math.random()) {

                    /*
                        generic muation!
                    
                        options:
                        if(bit = 0) -> new val = 1 or 2
                        if(bit = 1) -> new val = 0 or 2
                        if(bit = 2) -> new val = 0 or 1
                        
                        This is a bit of a hacky shortcut, because I don't want the multiple if's
                     */
                    Integer[] valsTemp = {0, 1, 2};
                    ArrayList<Integer> vals = new ArrayList<>(Arrays.asList(valsTemp));
                    vals.remove(newRule.getConditionValueFromIndex(j));
                    newRule.setConditionValueFromIndex(j, vals.get(rand.nextInt(2))); //Pick a random index from the remaining 2 items in list

                }
            }
            
            //do mutations on output
            if (MUTATION_RATE >= Math.random()) {
                //bitflip only on output
                newRule.setOutput(newRule.getOutput() ^ 1);
            }

            //set the newrule back into gene position
            ind.setGeneFromIndex(i, newRule);

        }
        
        return ind;
    }

    /*
    Maybe might do a more complex mutation
    For example, if the conditions for a rule are correct but the output is incorrect,
    I could give the output exclusively a higher mutation rate?
     */
    public static Rule mutationOutput(Rule rule, double MUTATION_RATE_OUTPUT) {
        if (MUTATION_RATE_OUTPUT > Math.random()) {
            rule.setOutput(rule.getOutput() ^ 1);
        }
        return rule;
    }

}

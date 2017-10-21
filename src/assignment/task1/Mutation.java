/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

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
    public Individual mutationBasic(Individual ind, double MUTATION_RATE) {
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

    /*
    More complex mutations:
    These mutations will include generics (#). These will represent if a value is a 1 or a 0.
    Generics are to only be used on the Rule Conditions, not the output!
    Make generic rate/chance... maybe 50% bitwise mutation/50% generic (if not 25% chance?)
    CREATE GENERIC_MUTATION RATE ABOVE?
        --> if generics makes the fitness worse, then remove
        --> if generics make teh fitness better, then it will be kept (this is usually more of the case!
     */
    public Individual mutationGenerics(Individual ind, double MUTATION_RATE) {
        Rule newRule;
        Random rand = new Random();
        for (int i = 0; i < ind.getGenesLength(); i++) { //loop through genes
            newRule = ind.getGeneFromIndex(i);
            for (int j = 0; j < newRule.getConditionLength(); j++) { //loop through conditions
                if (MUTATION_RATE > Math.random()) { //normal mutation
                    //check if normal bitwise mutation or generic mutation
                    if (MUTATION_RATE_GENERIC > Math.random()) {
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
            if(MUTATION_RATE > Math.random()) {
                newRule.setOutput(newRule.getOutput()^1);
            }
        }
        
        return ind;
    }

    /*
    Maybe might do a more complex mutation
    For example, if the conditions for a rule are correct but the output is incorrect,
    I could give the output exclusively a higher mutation rate?
    */
    public Rule mutationOutput(Rule rule, double MUTATION_RATE_OUTPUT) {
        if(MUTATION_RATE_OUTPUT > Math.random()) {
            rule.setOutput(rule.getOutput()^1);
        }
        return rule;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

/**
 *
 * @author Prithpal Sooriya
 */
public class Mutation {

    //private constructor
    private Mutation() {
    }

    /*
    mutation: to do this we first need to decode the genes, mutate, and then encode back the genes
    NOTE: this will be basic mutation, where each Rule condition AND output will change at same mutation rate.
    NOTE: we will also not be including generics into this (where # can be represented as 2)
     */
    public Individual mutationBasic(Individual ind, double MUTATION_RATE) {
        for (int i = 0; i < ind.getGenesLength(); i++) { //loop through all genes/RULES
            Rule newRule = ind.getGeneFromIndex(i);
            for (Rule rule : ind.getGenes()) { //loop through conditions and output
                newRule = rule;
                for (int j = 0; j < newRule.getConditionLength(); j++) { //mutate conditions
                    if (MUTATION_RATE > Math.random()) {
                        newRule.setConditionValueFromIndex(j, newRule.getConditionValueFromIndex(j) ^ 1); //bit flip
                    }
                }
                if (MUTATION_RATE > Math.random()) { //mutate output
                    newRule.setOutput(newRule.getOutput() ^ 1);
                }
            }
            ind.setGeneFromIndex(i, newRule);
        }
        return ind;
    }
    

}

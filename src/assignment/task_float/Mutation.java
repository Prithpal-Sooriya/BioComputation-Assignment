/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

/**
 *
 * @author Prithpal Sooriya
 */
public class Mutation {
    public static Individual mutationCreepAndOutput(Individual ind, double MUTATION_RATE, double OMEGA) {
        Rule newRule;
        for (int i = 0; i < ind.getGeneLength(); i++) { //loop through each gene
            newRule = ind.getGeneFromIndex(i);
            
            /* Mutate condition using creep */
            for (int j = 0; j < newRule.getBoundsLength(); j++) { //loop through each condition/bound
                if(MUTATION_RATE > Math.random()) {
                    //mutate!
                    float lowerBound = newRule.getBoundFromIndex(j).getLowerbound();
                    float upperBound = newRule.getBoundFromIndex(j).getUpperbound();
                    
                    lowerBound += Math.random()>0.50 ? Math.random()*OMEGA : -Math.random()*OMEGA;
                    upperBound += Math.random()>0.50 ? Math.random()*OMEGA : -Math.random()*OMEGA;
                    newRule.getBoundFromIndex(j).setLowerbound(lowerBound);
                    newRule.getBoundFromIndex(j).setUpperbound(upperBound);
                    
                    newRule.getBoundFromIndex(j).validateBounds();
                }
            }
            
            /* Mutate output using bitflip */
            if(MUTATION_RATE > Math.random()) {
                newRule.setOutput(newRule.getOutput()^1);
            }
            
            /* add newRule back into gene position */
            ind.setGeneFromIndex(newRule, i);
        }
        
        return Individual.clone(ind);
    }
    
    public static Individual mutationRandom(Individual ind, double MUTATION_RATE) {
        Rule newRule;
        for (int i = 0; i < ind.getGeneLength(); i++) {
            newRule = ind.getGeneFromIndex(i);
            
            /* Mutate using random */
            for (int j = 0; j < newRule.getBoundsLength(); j++) {
                if(MUTATION_RATE > Math.random()) {
                    
                    float lowerBound = (float) Math.random();
                    float upperBound = (float) Math.random();
                    newRule.getBoundFromIndex(j).setLowerbound(lowerBound);
                    newRule.getBoundFromIndex(j).setUpperbound(upperBound);
                    
                    newRule.getBoundFromIndex(j).validateBounds();
                }
            }
            if(MUTATION_RATE > Math.random()) {
                newRule.setOutput(newRule.getOutput()^1);
            }
            ind.setGeneFromIndex(newRule, i);
        }
        
        return Individual.clone(ind);
    }
}

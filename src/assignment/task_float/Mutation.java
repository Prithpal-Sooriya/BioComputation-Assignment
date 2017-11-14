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
    public static Individual mutationCreep(Individual ind, double MUTATION_RATE, double omega) {
        Rule newRule;
        for (int i = 0; i < ind.getGeneLength(); i++) { //loop through each gene
            newRule = ind.getGeneFromIndex(i);
            
            for (int j = 0; j < newRule.getBoundsLength(); j++) { //loop through each condition/bound
                if(MUTATION_RATE > Math.random()) {
                    //mutate!
                    float lowerBound = newRule.getBoundFromIndex(j).getLowerbound();
                    float upperBound = newRule.getBoundFromIndex(j).getUpperbound();
                    
                    lowerBound += Math.random()>50 ? Math.random()*omega : -Math.random()*omega;
                    upperBound += Math.random()>50 ? Math.random()*omega : -Math.random()*omega;
                    
                    newRule.getBoundFromIndex(j).setLowerbound(lowerBound);
                    newRule.getBoundFromIndex(j).setUpperbound(upperBound);
                    
                    newRule.getBoundFromIndex(j).validateBounds();
                }
            }
            
            ind.setGeneFromIndex(newRule, i);
        }
        
        return Individual.clone(ind);
    }
}

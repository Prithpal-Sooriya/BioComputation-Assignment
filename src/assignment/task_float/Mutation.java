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
                if (MUTATION_RATE > Math.random()) {
                    //mutate!
                    float lowerBound = newRule.getBoundFromIndex(j).getLowerbound();
                    float upperBound = newRule.getBoundFromIndex(j).getUpperbound();

                    lowerBound += Math.random() > 0.50 ? Math.random() * OMEGA : -Math.random() * OMEGA;
                    upperBound += Math.random() > 0.50 ? Math.random() * OMEGA : -Math.random() * OMEGA;
                    newRule.getBoundFromIndex(j).setLowerbound(lowerBound);
                    newRule.getBoundFromIndex(j).setUpperbound(upperBound);

                    newRule.getBoundFromIndex(j).validateBounds();
                }
            }

            /* Mutate output using bitflip */
            if (MUTATION_RATE > Math.random()) {
                newRule.setOutput(newRule.getOutput() ^ 1);
            }

            /* add newRule back into gene position */
            ind.setGeneFromIndex(newRule, i);
        }

        return new Individual(ind);
    }

    /*
    different version compared to first mutationCreepAndOutput
    - this version will test each lower and upper bound individually
        - this can allow only the lower/upper bound to be mutated (which can be a good thing!)
     */
    public static Individual mutationCreepAndOutputV2(Individual ind, double MUTATION_RATE, double OMEGA) {
        Rule newRule;
        float newValue = 0;
        float creep = 0;
        for (int i = 0; i < ind.getGeneLength(); i++) { //loop through genes
            newRule = ind.getGeneFromIndex(i);

            //mutation creep
            for (int j = 0; j < newRule.getBoundsLength(); j++) { //loop through each bound in rule
                //mutate lower bound    
                if (MUTATION_RATE > Math.random()) {
                    newValue = newRule.getBoundFromIndex(j).getLowerbound();
                    creep = (float) (Math.random() * OMEGA);
                    newValue += Math.random() > 0.50 ? creep : -creep;
                    
                    newRule.getBoundFromIndex(j).setLowerbound(newValue);
                }
                //mutate upper bound
                if (MUTATION_RATE > Math.random()) {
                    newValue = newRule.getBoundFromIndex(j).getUpperbound();
                    creep = (float) (Math.random() * OMEGA);
                    newValue += Math.random() > 0.50 ? creep : -creep;
                    
                    newRule.getBoundFromIndex(j).setUpperbound(newValue);
                }
            }
            //mutation output
            if(MUTATION_RATE > Math.random()) {
                newRule.setOutput(newRule.getOutput()^1);
            }
            
            //add newRule back to gene location
            ind.setGeneFromIndex(newRule, i);
        }
        
        return new Individual(ind);
    }

    public static Individual mutationRandom(Individual ind, double MUTATION_RATE) {
        Rule newRule;
        for (int i = 0; i < ind.getGeneLength(); i++) {
            newRule = ind.getGeneFromIndex(i);

            /* Mutate using random */
            for (int j = 0; j < newRule.getBoundsLength(); j++) {
                if (MUTATION_RATE > Math.random()) {

                    float lowerBound = (float) Math.random();
                    float upperBound = (float) Math.random();
                    newRule.getBoundFromIndex(j).setLowerbound(lowerBound);
                    newRule.getBoundFromIndex(j).setUpperbound(upperBound);

                    newRule.getBoundFromIndex(j).validateBounds();
                }
            }
            if (MUTATION_RATE > Math.random()) {
                newRule.setOutput(newRule.getOutput() ^ 1);
            }
            ind.setGeneFromIndex(newRule, i);
        }

        return new Individual(ind);
    }
}

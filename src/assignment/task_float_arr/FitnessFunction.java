/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float_arr;

/**
 *
 * @author Prithpal Sooriya
 */
public class FitnessFunction {
    public static void compareRulesAll(float[][] set, Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i] = compareRulesSingle(set, population[i]);
        }
    }
    
    public static Individual compareRulesSingle(float[][] set, Individual individual) {
        
        int tempFitness = 0;
        
        for (int i = 0; i < set.length; i++) { //loop through rules
            float[] fs = set[i];
            for (int j = 0; j < individual.getGenes().length; j++) { //loop through each gene

                //eval conditions
                boolean allMatched = true;
                for (int k = 0; k < fs.length-1; k++) { //loop through each part of rule
                    float value = fs[k];
                    //offset--> [j][k*2]
                    int offset = k*2;
                    float lowerBound = individual.getGenes()[j][offset];
                    float upperBound = individual.getGenes()[j][offset+1];
                    if (!(lowerBound <= value && value <= upperBound)) {
                        allMatched = false;
                        break;
                    }
                }

                //eval all conditions and output
                if (allMatched) {
                    if (individual.getGenes()[j][individual.getGenes()[j].length-1]
                            == fs[fs.length - 1]) {
                        tempFitness++;
                    }
                    break; //go to next rule to eval
                }
            }
        }
        
        individual.setFitness(tempFitness);
        return new Individual(individual);
    }
}

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
public class FitnessFunction {

    private FitnessFunction() {
    }

    //so we can map the fitness to a range which is easier to use --> for example 0-1
    //algorith is linear transformation and I found out how to do it here: https://stackoverflow.com/questions/345187/math-mapping-numbers
    private static double normalizeFitness(double currentFitness, double oldRangeStart, double oldRangeEnd, double newRangeStart, double newRangeEnd) {
       double m = (currentFitness-oldRangeStart)/(oldRangeEnd-oldRangeStart);
       double x = (newRangeEnd-newRangeStart);
       double c = newRangeStart;
       
       return (m*x) + c;
    }
    
    public static Individual fitnessFunctionCompareConditions(Rule[] trainingRuleset, Individual individual) {

        double tempFitness = 0;

        //we will increment the fitness for each condition it gets correct
        for (int i = 0; i < individual.getGenesLength(); i++) { //loop through each gene
            for (Rule rule : trainingRuleset) {
                //compare each rule in the ruleset to the genes of the individual
                for (int j = 0; j < rule.getConditionLength(); j++) { //loop through each condition
                    if (rule.getConditionValueFromIndex(j)
                            == individual.getGene(i).getConditionValueFromIndex(j)) {
                        tempFitness++;
                    }
                }
                //at the end of each RULE we can div by total rule?
                //gives us a larger fitness if we just div at end?
//                tempFitness /= rule.getConditionLength();
            }
        }
        
        //now at the end, we can div by total number of conditions.
        tempFitness /= (trainingRuleset.length*trainingRuleset[0].getConditionLength());
        
        individual.setFitness(tempFitness);
        
        //have to return back the individual, because the changes are not kept on an Object (pass by value)
        return individual;
    }
}

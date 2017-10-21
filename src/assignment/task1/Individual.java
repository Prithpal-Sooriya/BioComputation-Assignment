/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Prithpal Sooriya
 */
public class Individual {

    private Rule genes[];
    private double fitness;

    public Individual(int numberOfGenes, int sizeOfCondition) {

        genes = new Rule[numberOfGenes]; //lets have each individual have 7 rules maximum..

        //initialise the genes
        for (int i = 0; i < genes.length; i++) {
            genes[i] = new Rule(sizeOfCondition, 0);

        }

        //should I do random initial genes in here or nah.....
        randomiseGenes(this.genes);

    }

    private static void randomiseGenes(Rule genes[]) {
        for (int i = 0; i < genes.length; i++) {
            Rule.randomiseRule(genes[i]);
        }
    }
    
    /*Setters and Getters*/

    public Rule[] getGenes() {
        return genes;
    }

    public int getGenesLength() {
        return genes.length;
    }
    
    //since we are using a class, we need a way of converting the class into a binary/int array
    //so we can use it for mutation and crossover.
    public Integer[] decodeGenes() {
        int sizeOfIntArray = genes.length * (genes[0].getConditionLength() + genes[0].getOutputLength());
        ArrayList<Integer> integerList = new ArrayList<>();
        //now we need to populate the array
        for (Rule rule : genes) {
            for (int i = 0; i < rule.getConditionLength(); i++) {
                integerList.add(rule.getConditionValueFromIndex(i)); //add the conditions of each rule
            }
            integerList.add(rule.getOutput()); //add the output of each rule
        }
        
        //now we can return this concatenation
        Integer[] array = integerList.toArray(new Integer[sizeOfIntArray]);
        return array;
    }
    
    //this might break (from where the arrayIndex is incremented.
    public void encodeGenes(Integer[] array) {
        //so we need to convert this integer array back into the list
        int arrayIndex = 0;
        for (int i = 0; i < genes.length; i++) {
            //set the new condition
            for (int j = 0; j < genes[i].getConditionLength(); j++) {
                genes[i].setConditionValueFromIndex(j, array[arrayIndex]);
                arrayIndex++;
            }
            //set new output
            genes[i].setOutput(array[arrayIndex]);
            arrayIndex++;
            
        }
    }

    public void setGeneFromIndex(int i, Rule rule) {
        genes[i] = rule;
    }
    
    public Rule getGeneFromIndex(int i) {
        return genes[i];
    }
    
    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /*toString*/
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        //rules
        for (int i = 0; i < genes.length; i++) {
            ret.append(genes[i].toString());
        }
        //fitness
        ret.append("Fitness = " + fitness);

        return ret.toString();

    }

}

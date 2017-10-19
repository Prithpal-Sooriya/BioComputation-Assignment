/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

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
    public Rule getGene(int i) {
        //need index out of bounds checks later
        return genes[i];
    }

    public Rule[] getGenes() {
        return genes;
    }

    public int getGenesLength() {
        return genes.length;
    }
    
    public void setGenes(Rule[] genes) {
        this.genes = genes;
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

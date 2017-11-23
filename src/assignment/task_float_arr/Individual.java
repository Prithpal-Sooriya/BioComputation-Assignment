/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float_arr;

import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class Individual {

    private float[][] genes;
    private int geneLength;
    private int conditionLength;
    private int outputLength;
    private double fitness;
    private int totalLength;

    public Individual(int GENE_LENGTH, int CONDITION_LENGTH, int outputLength) {
        this.geneLength = GENE_LENGTH;
        this.conditionLength = CONDITION_LENGTH;
        this.outputLength = outputLength;
        this.totalLength = (conditionLength * 2) + outputLength;
        this.fitness = 0;
        this.genes = new float[geneLength][totalLength];
        randomiseGenes();
        organiseBounds();
    }

    public Individual(Individual clone) {
        //WHEN COPYING OVER GENES, I NEED TO ENSURE THAT WE ARE NOT COPYING REFERENCE
        this.geneLength = clone.getGeneLength();
        this.totalLength = clone.getTotalLength();
        float[][] newGenes = new float[this.geneLength][this.totalLength];
        for (int i = 0; i < newGenes.length; i++) {
            for (int j = 0; j < newGenes[i].length; j++) {
                newGenes[i][j] = clone.getGenes()[i][j]; //.getGenes() adds extra unnessary function

            }
        }
        this.genes = newGenes;
        this.geneLength = clone.getGeneLength();
        this.conditionLength = clone.getConditionLength();
        this.outputLength = clone.getOutputLength();
        this.fitness = clone.getFitness();
    }

    public void randomiseGenes() {
        Random rand = new Random();
        for (int i = 0; i < genes.length; i++) {
            for (int j = 0; j < genes[i].length; j++) {
                if (j == genes[i].length - 1) {
                    //output
                    genes[i][j] = rand.nextInt(2);
                } else {
                    genes[i][j] = (float) Math.random();
                }
            }
        }
    }

    public void organiseBounds() {
        for (int i = 0; i < genes.length; i++) { //loop the each gene
            for (int j = 0; j < (genes[i].length - outputLength) / 2; j++) { //loop through conditions only
                //we just want to check bounds
                int offset = j * 2;
                float lowerBound = Math.min(genes[i][offset], genes[i][offset + 1]);
                float upperBound = Math.max(genes[i][offset], genes[i][offset + 1]);
                genes[i][offset] = lowerBound;
                genes[i][offset + 1] = upperBound;
            }
        }
    }

    public void evaluate(float[][] dataset) {
        double tempFitness = 0;
        for (int i = 0; i < dataset.length; i++) { //loop through rules
            float[] fs = dataset[i];
            for (int j = 0; j < genes.length; j++) { //loop through each gene
                //eval conditions
                boolean allMatched = true;
                for (int k = 0; k < fs.length - outputLength; k++) { //loop through each part of rule
                    float value = fs[k];
                    //offset--> [j][k*2]
                    int offset = k * 2;
                    float lowerBound = genes[j][offset];
                    float upperBound = genes[j][offset + 1];
                    if (!(lowerBound <= value && value <= upperBound)) {
                        allMatched = false;
                        break;
                    }
                }

                //eval all conditions and output
                if (allMatched) {
                    if (genes[j][this.totalLength - 1] == fs[fs.length - 1]) {
                        tempFitness++;
                    }
                    break; //go to next rule to eval
                }
            }
        }

        //set individuals new fitness
        this.fitness = tempFitness;
    }

    public void mutationCreepConditions(double MUTATION_RATE, double OMEGA_OFFSET) {
        for (int i = 0; i < genes.length; i++) { //loop through each gene
            for (int j = 0; j < conditionLength; j++) { //loop through each condition bound pair (only)

                if (MUTATION_RATE > Math.random()) {
                    int offset = j * 2;
                    float lowerBound = genes[i][offset];
                    float upperBound = genes[i][offset + 1];
                    lowerBound += Math.random() > 0.5 ? Math.random() * OMEGA_OFFSET : -Math.random() * OMEGA_OFFSET;
                    upperBound += Math.random() > 0.5 ? Math.random() * OMEGA_OFFSET : -Math.random() * OMEGA_OFFSET;

                    lowerBound = endBound(lowerBound);
                    upperBound = endBound(upperBound);

                    genes[i][offset] = Math.min(lowerBound, upperBound);
                    genes[i][offset + 1] = Math.max(lowerBound, upperBound);
                }
            }
        }
    }

    //this version will perform creep on 1 bound at a time
    //so in some cases, only the lower/upper bound will be creeped :D
    public void mutationCreepConditionsV2(double MUTATION_RATE, double OMEGA_OFFSET) {
        for (int i = 0; i < genes.length; i++) { //loop through genes
            for (int j = 0; j < genes.length - outputLength; j++) { //loop through each bound
                if (MUTATION_RATE > Math.random()) {
                    float newValue = genes[i][j];
                    float creep = (float) (Math.random() * OMEGA_OFFSET);
                    newValue += Math.random() > 0.5 ? creep : -creep;

                    newValue = endBound(newValue);

                    genes[i][j] = newValue;
                }
            }
        }

        //will need to reorganise bounds
        organiseBounds();
    }

    public void mutationOutput(double MUTATION_RATE) {
        for (int i = 0; i < genes.length; i++) {
            if (MUTATION_RATE > Math.random()) {
                //bit flip
                genes[i][genes[i].length - 1] = (int) genes[i][genes[i].length - 1] ^ 1;
            }
        }
    }

    private float endBound(float newValue) {
        if (newValue < 0) {
            return 0;
        }
        if (newValue > 1) {
            return 1;
        } else {
            return newValue;
        }
    }

    private float phaseBound(float newValue) {
        while (newValue < 0) {
            newValue = 1 - newValue; //keep feeding through top till between 0-1
        }

        while (newValue > 0) {
            newValue = newValue - 1; //keep feeding through bottom till between 0-1 
        }

        return newValue;
    }

    public float[][] getGenes() {
        return genes;
    }

    public void setGenes(float[][] genes) {
        this.genes = genes;
    }

    public void setGeneFromIndex(int i, int j, float val) {
        this.genes[i][j] = val;
    }

    public int getGeneLength() {
        return geneLength;
    }

    public void setGeneLength(int geneLength) {
        this.geneLength = geneLength;
    }

    public int getConditionLength() {
        return conditionLength;
    }

    public void setConditionLength(int conditionLength) {
        this.conditionLength = conditionLength;
    }

    public int getOutputLength() {
        return outputLength;
    }

    public void setOutputLength(int outputLength) {
        this.outputLength = outputLength;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

}

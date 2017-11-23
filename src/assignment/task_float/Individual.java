/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Prithpal Sooriya
 */
public class Individual {
    
    private Rule genes[];
    private double fitness;
    private double normalisedFitness;
    
    //constructor with genes given and fitness given
    public Individual(Rule genes[], double fitness) {
        this.genes = genes;
        this.fitness = fitness;
    }
    
    //constructor with no genes given, so randomise genes
    public Individual(int CHROMOSOME_LENGTH, int condition_size) {
        this.genes = new Rule[CHROMOSOME_LENGTH];
        
        //populate genes
        for (int i = 0; i < genes.length; i++) {
            genes[i] = new Rule(condition_size); //this constructor will randomise the rule
        }
    }
    
    /*
    This will create a new individual using the same values as the parent
    To prevent some issues (like copying reference) I will need to use .clone, or some other method
    */
    public Individual(Individual clone) {
        this.fitness = clone.getFitness();
        this.normalisedFitness = clone.getNormalisedFitness();
//        this.genes = clone.getGenes(); //this will be invalid, as this is copying reference!
        this.genes = new Rule[clone.getGeneLength()];
        for (int i = 0; i < genes.length; i++) {
            genes[i] = new Rule(clone.getGeneFromIndex(i));
        }
    }
    
    
    /*
    clone function
    
    This is used to prevent wierdness from using Object[] which contains
    Object[] which contains ....
    
    I could use deepClone, but thought I might just make my own :P
    EDIT: this clone funtion is quite computationally expensive (like by alot XD)
    */
    public static Individual clone(Individual ind) {
        Rule[] genes = new Rule[ind.getGeneLength()];
        double fitness = ind.getFitness();
        
        for (int i = 0; i < genes.length; i++) {
            genes[i] = new Rule(ind.getGeneFromIndex(i).getBounds(), ind.getGeneFromIndex(i).getOutput());
            
        }
        
        return new Individual(genes, fitness);
    }
    
    
    /*
    encode genes
    with this version we cannot do Crossover Blending
      - I could make a more complicated decodeGenes to allow blending later
    */
    public Float[] decodeGenes() {
        //populate the 
//        float[] allGenes = new float[genes.length + (genes[0].getBoundsLength() * 2)];

        //arraylist is easier
        ArrayList<Float> allGenes = new ArrayList<>();
        
        for (Rule gene : genes) { //loop through all rules
            //add bounds
            for (Bound bound : gene.getBounds()) { //loop through all bounds
                allGenes.add(bound.getLowerbound());
                allGenes.add(bound.getUpperbound());
            }
            //add output
            allGenes.add((float) gene.getOutput());
        }
        
        return allGenes.toArray(new Float[allGenes.size()]);
    }
    
    public void encodeGenes(Float[] decodedGenes){
//        Rule[] newGenes = new Rule[genes.length];
        
        //use arraylist as it is easier to use
        if(decodedGenes.length%(genes[0].getBoundsLength()+genes[0].getOutputLength()) == 0) {
            //it is divisible by whole rule
            //thank fuck this is correct! dunno what I would do if it wasnt!
            
            //convert list into arraylist
            ArrayList<Float> decodedGenesList = new ArrayList<Float>(Arrays.asList(decodedGenes));
            
            while(decodedGenesList.size() > 0) {
                for (int i = 0; i < genes.length; i++) {
                    for (int j = 0; j < genes[i].getBoundsLength(); j++) {
                        genes[i].getBoundFromIndex(j).setLowerbound(decodedGenesList.get(0));
                        decodedGenesList.remove(0);
                        genes[i].getBoundFromIndex(j).setUpperbound(decodedGenesList.get(0));
                        decodedGenesList.remove(0);
                        
                        //make sure the lower and upper bounds are in the correct position
                        genes[i].getBoundFromIndex(j).validateBounds();
                    }
                    genes[i].setOutput((int) (float)(decodedGenesList.get(0)));
                }
            }
        }
    }
    
    //setters and getters

    public Rule getGeneFromIndex(int i) {
        return genes[i];
    }
    
    public Rule[] getGenes() {
        return genes;
    }

    public void setGeneFromIndex(Rule gene, int i) {
        this.genes[i] = gene;
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

    public double getNormalisedFitness() {
        return normalisedFitness;
    }

    public void setNormalisedFitness(double normalisedFitness) {
        this.normalisedFitness = normalisedFitness;
    }
    
    public int getGeneLength() {
        return this.genes.length;
    }
    
    
    //toString()
    @Override
    public String toString() {
        return Arrays.toString(genes) + "Fitness = " + fitness;
    }
    
    
}

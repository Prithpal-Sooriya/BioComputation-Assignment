/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class Rule {
    private Bound bounds[];
    private int output;
    
    //constructor that gives bounds and output
    public Rule(Bound bounds[], int output){
        
        //might be getting issues with copying bounds
        this.bounds = new Bound[bounds.length];
        for (int i = 0; i < bounds.length; i++) {
            float lowerBound = bounds[i].getLowerbound();
            float upperBound = bounds[i].getUpperbound();
            this.bounds[i] = new Bound(lowerBound, upperBound);
        }
        
//        this.bounds = bounds;
        this.output = output;
    }
    
    //constructor that does not give bounds, so randomises it
    public Rule(int conditionSize) {
        bounds = new Bound[conditionSize];
        
        //initialise bounds and randomise bounds
        for (int i = 0; i < bounds.length; i++) {
            //empty constructor will randomise bounds
            bounds[i] = new Bound();
        }
        
        //randomise output
        output = new Random().nextInt(2);
    }
    
    /*
    This is a function that will be used to allow cloning
    This is to prevent 2 rules using the same reference
    */
    public Rule(Rule clone) {
        this.output = clone.getOutput();
//        this.bounds = clone.getBounds(); //this will not work as this is copying by reference!
        this.bounds = new Bound[clone.getBoundsLength()];
        for (int i = 0; i < bounds.length; i++) {
            bounds[i] = new Bound(clone.getBoundFromIndex(i));
        }
    }
    
    //setters and getters
    public Bound getBoundFromIndex(int i) {
        if(i >= bounds.length || i < 0) {
            return null;
        }
        
        return bounds[i];
    }
    
    public Bound[] getBounds() {
        return bounds;
    }

    public void setBoundFromIndex(int i, Bound bound) {
        if(i >= bounds.length || i < 0) {
            System.err.println("setBoundFromIndex(): incorrect bound, i = " + i);
        }
        else {
            bounds[i] = bound;
        }
    }
    
    public void setBounds(Bound[] bounds) {
        this.bounds = bounds;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
    
    public int getBoundsLength() {
        return bounds.length;
    }

    public int getOutputLength() {
        return 1;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(bounds) + " " + output + "\n";
    }
}

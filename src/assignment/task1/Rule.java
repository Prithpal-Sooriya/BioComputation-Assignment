/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task1;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class Rule {

    private static final double EXTREME_MUTATION_RATE_OUTPUT = 0.7;

    private int[] condition;
    private int output;

    public Rule(int sizeOfCondition, int output) {
        this.condition = new int[sizeOfCondition];
        this.output = output;
    }

    public static void randomiseRule(Rule rule) {
        //randomise condition
        Random rand = new Random();
        for (int i = 0; i < rule.getConditionLength(); i++) {
            rule.setConditionValueFromIndex(i, rand.nextInt(2));
        }
        //randomise output
        rule.setOutput(rand.nextInt(2));
    }
    
    
    /*getters and setters*/
    public int getConditionValueFromIndex(int i) {
        if(i >= condition.length) {
            return -1; //need to check for this
        }
        return condition[i];
    }
    
    public void setConditionValueFromIndex(int i, int val) {
        if(val < 0 || val > 1 ) {
            //error!!!
            System.err.println("SetConditionValueFromIndex - Val error: val = " + val);
        }
        else if(i >= condition.length){
            //error
            System.err.println("SetConditionValueFromIndex - Index error: index = " + i);
        }
        else {
            condition[i] = val;
        }
    }
    
    public int getConditionLength() {
        return condition.length;
    }

    public int[] getCondition() {
        return condition;
    }

    public void setCondition(int[] condition) {
        this.condition = condition;
    }

    public int getOutput() {
        return output;
    }
    
    public int getOutputLength() {
        return 1; //if there were multiple outputs, then we will return the output arrays length.
    }

    public void setOutput(int output) {
        this.output = output;
    }

    /*to string*/
    @Override
    public String toString() {
        return "" + Arrays.toString(condition) + "," + output+"\n";
    }

}
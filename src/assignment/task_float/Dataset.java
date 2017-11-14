/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

import java.util.Arrays;

/**
 *
 * @author Prithpal Sooriya
 */
public class Dataset {

    private float[] conditions;
    private int output;

    //constructor
    public Dataset(float[] conditions, int output) {
        this.conditions = conditions;
        this.output = output;
    }

    //setters and getters
    public float getConditionFromIndex(int i) {
        return conditions[i];
    }

    public float[] getConditions() {
        return conditions;
    }

    public void setConditionFromIndex(float condition, int i) {
        conditions[i] = condition;
    }

    public void setConditions(float[] conditions) {
        this.conditions = conditions;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }

    //toString()
    @Override
    public String toString() {
        return Arrays.toString(conditions) + " " + output;
    }

    //Static methods
    public static Dataset[] createTrainingSet(Dataset dataset[]) {
        Dataset trainingSet[] = new Dataset[dataset.length / 2];

        /*
            we need to populate the training class
            - we can take 1st subarray of training set
            - or we can do % (dataset/size) times to get the required size
            
            stick simple and split dataset into 2 parts, 1st part is training, 2nd half is testing/validation
         */
        for (int i = 0; i < trainingSet.length; i++) {
            trainingSet[i] = dataset[i];
        }

        return trainingSet;
    }

    public static Dataset[] createTestingSet(Dataset dataset[]) {
        Dataset testingSet[] = new Dataset[dataset.length / 2];

        //null testingSet
        for (int i = 0; i < testingSet.length; i++) {
            testingSet[i] = null;
        }

        //add 2nd half of dataset --> first half is for testingSet
        int testingIndex = 0;
        for (int i = dataset.length / 2; i < dataset.length && testingIndex < testingSet.length; i++) {
            testingSet[testingIndex] = dataset[i];
            testingIndex++;
        }

        //if the dataset wasn't even then testingSet will have last index empty
        //populate last index if not filled
        if (testingSet[testingSet.length - 1] == null) {
            //add in the first item in dataset
            testingSet[testingSet.length - 1] = dataset[0];
        }

        return testingSet;
    }
}

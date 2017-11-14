/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Prithpal Sooriya
 */
public class GAFloat {

    //input file
    private String inputFileDir;

    //hyperparameters
    private static int CONDITION_LENGTH;

    //inner class for how to handle dataset
    private class Dataset {

        float[] conditions;
        int output;

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
        

    }

    private Dataset[] dataset;
    private Dataset[] trainingSet;
    private Dataset[] testingSet;

    //constructor
    public GAFloat(String filename) {
        if (filename.equals("data3.txt")) {
            inputFileDir = "/Files/data3.txt";
        }
    }

    /*
    read file
    reads file based on inputFileDir
     */
    private void readFile() {
        Scanner scan = new Scanner(GAFloat.class.getResourceAsStream(inputFileDir));
        ArrayList<Dataset> datasetAL = new ArrayList<>();
        int conditionLengthTemp = 0;

        scan.nextLine(); //read in the useless line

        while (scan.hasNextLine()) {
            //read in rule
            String ruleString = scan.nextLine();
            String[] parts = ruleString.split(" ");

            //save length of condition
            if (conditionLengthTemp == 0) {
                //output length is always 1, condition length is the rest.
                conditionLengthTemp = parts.length - 1;
            }

            //create set
            Dataset set;
            float conditions[] = new float[conditionLengthTemp];
            for (int i = 0; i < conditions.length; i++) {
                conditions[i] = Float.parseFloat(parts[i]);
            }
            int output = Integer.parseInt(parts[parts.length-1]);
            set = new Dataset(conditions, output);
            
            //add set to arraylist
            datasetAL.add(set);
        }

        scan.close();
        dataset = new Dataset[datasetAL.size()];
        datasetAL.toArray(dataset);
        
        //store length of condition
        CONDITION_LENGTH = conditionLengthTemp;
    }

    public void run() {
        //read in file and save to dataset
        readFile();

        //lets check if reading the file was correct
        for (Dataset dataset1 : dataset) {
            System.out.println(dataset1.toString());
        }
    }

    public static void main(String[] args) {

        GAFloat ga = new GAFloat("data3.txt");

        ga.run();
    }

}

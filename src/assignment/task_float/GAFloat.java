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

    //HYPER PARAMETERS
    private static final int POPULATION_SIZE = 100;
    private static final int CHROMOSOME_LENGTH = 100;
    private static final double CROSSOVER_RATE = 0.9;
    private static final double MUTATION_RATE = 0.02; //1/popsize to 1/chromosomelength

//    private int POPULATION_SIZE;
//    private int CHROMOSOME_LENGTH;
//    private double CROSSOVER_RATE;
//    private double MUTATION_RATE;
    private int CONDITION_LENGTH;

    //inner class for how to handle dataset
    private static class Dataset {

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
            Dataset testingSet[] = new Dataset[dataset.length/2];
            
            //null testingSet
            for (int i = 0; i < testingSet.length; i++) {
                testingSet[i] = null;
            }
            
            //add 2nd half of dataset --> first half is for testingSet
            int testingIndex = 0;
            for (int i = dataset.length/2; i < dataset.length && testingIndex < testingSet.length; i++) {
                testingSet[testingIndex] = dataset[i];
                testingIndex++;
            }
            
            //if the dataset wasn't even then testingSet will have last index empty
            //populate last index if not filled
            if(testingSet[testingSet.length-1] == null) {
                //add in the first item in dataset
                testingSet[testingSet.length-1] = dataset[0];
            }
            
            return testingSet;
        }
    }

    //Datasets
    private Dataset[] dataset;
    private Dataset[] trainingSet;
    private Dataset[] testingSet;

    //Individuls
    private Individual[] parentPopulation;
    private Individual[] offspringPopulation;
    private Individual bestIndividual;
    
    //booleans to see if population is sorted
    private boolean parentPopulationSorted = false;
    private boolean offspringPopulationSorted = false;
    
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
            int output = Integer.parseInt(parts[parts.length - 1]);
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

    /*
    initialise populations
    */
    private void initialisePopulations() {
        parentPopulation = new Individual[POPULATION_SIZE];
        offspringPopulation = new Individual[POPULATION_SIZE];
        
        //init parent and offspring
        //note: generation 0 --> parent and offspring are the same
        for (int i = 0; i < POPULATION_SIZE; i++) {
            parentPopulation[i] = offspringPopulation[i] = new Individual(CHROMOSOME_LENGTH, CONDITION_LENGTH);
        }
        
        //init the best individual
        bestIndividual = new Individual(CHROMOSOME_LENGTH, CONDITION_LENGTH);
    }
    
    public void run() {
        //read in file and save to dataset
        readFile();

        //lets check if reading the file was correct
//        for (Dataset dataset1 : dataset) {
//            System.out.println(dataset1.toString());
//        }

        trainingSet = Dataset.createTrainingSet(dataset);
        testingSet = Dataset.createTestingSet(dataset);
        
        //check trainingset
//        for (Dataset dataset1 : trainingSet) {
//            System.out.println(dataset1.toString());
//        }
        //check testingset
//        for (Dataset dataset1 : testingSet) {
//            System.out.println(dataset1.toString());
//        }
        
        
        
    }

    public static void main(String[] args) {

        GAFloat ga = new GAFloat("data3.txt");

        ga.run();
    }

}

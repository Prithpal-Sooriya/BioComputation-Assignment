/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.SimpleGA;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Prithpal Sooriya
 */
public class SimpleGA {

    /*
    SIMPLE GA:
    Create an initial population (random bits)
    WHILE
        fitness function 
        selection - crossover - mutation
    REPEAT
     */
    final static int POPULATION_SIZE = 50;
    final static int GENE_SIZE = 50;
    final static double CROSSOVER_RATE = 0.9; //should be between 0.6-0.9 (60% - 90%) --> almost always there will be crossover, small chance of cloning
    final static double MUTATION_RATE = 0.02; //should be between 1/popsize - 1/genesize (1/50 = 0.02) --> however I found this to be too high last time.. so I made it even smaller

    public static void main(String[] args) {
        Individual[] population = new Individual[POPULATION_SIZE];

        for (int i = 0; i < population.length; i++) {
            population[i] = new Individual(GENE_SIZE);
        }

        createInitialPopulation(population, POPULATION_SIZE);
        int numberOfGenerations = 0;
        while (reachedTarget(population, GENE_SIZE)) {

            printImportantInfo(population);

            fitnessFunction(population);

            population = selection_crossover_mutation(population, CROSSOVER_RATE, MUTATION_RATE);

            numberOfGenerations++;
        }

        System.out.println("TARGET REACHED, NUMBER OF GENERATIONS TAKEN: " + numberOfGenerations);
    }

    //stop condition (this is only for the gaSimple, im not sure what the stop condition for the coursework might be...
    public static boolean reachedTarget(Individual[] population, int target) {
        boolean targetNotReached = true;
        for (Individual individual : population) {
            if (Arrays.stream(individual.genes).sum() == target) {
                targetNotReached = false;
            }
        }
        return targetNotReached;
    }

    public static void printImportantInfo(Individual[] population) {
        //print lowest, highest and average fitness
        //you can show the array by using Arrays.toString()
        int lowestFitness = 11111111, highestFitness = 0, totalFitness = 0;
        double averageFitness = 0;
        for (Individual individual : population) {
            totalFitness += individual.getFitness();
            if (individual.getFitness() < lowestFitness) {
                lowestFitness = individual.getFitness();
            }
            if (individual.getFitness() > highestFitness) {
                highestFitness = individual.getFitness();
            }
        }

        averageFitness = totalFitness / population.length;

        System.out.println("Lowest Fitness: " + lowestFitness);
        System.out.println("Highest Fitness: " + highestFitness);
        System.out.println("Average Fitness: " + averageFitness);
        System.out.println("======================================");
    }

    /*
    just to make things easier, I'll shove selection, crossover, mutation all in one!!!
     */
    public static Individual[] selection_crossover_mutation(Individual[] population, double CROSSOVER_RATE, double MUTATION_RATE) {
        Individual[] childrenPool = new Individual[population.length];

        for (int i = 0; i < childrenPool.length; i++) {
            //select 2 parents
            Individual parent1 = selection(population);
            Individual parent2 = selection(population);
//            Individual parent1 = selectionRoulette(population);
//            Individual parent2 = selectionRoulette(population);

            //crossover = create 2 children
            Individual[] tempChildren = crossover(parent1, parent2, CROSSOVER_RATE);

            //mutate the 2 children
            mutation(tempChildren, MUTATION_RATE);

            //add children to the pool
            childrenPool[i] = tempChildren[0]; //add first child
            i++;
            if (i < childrenPool.length) {
                childrenPool[i] = tempChildren[1]; //add second child (if there is space, if the population was odd there would be no space!)
            }
        }
        return childrenPool;
    }

    /*
    This will create the initial population
     */
    public static void createInitialPopulation(Individual[] population, int populationSize) {
        //randomise the genes in the population!
        Random rand = new Random();
        for (int i = 0; i < population.length; i++) { //loop through the population
            for (int j = 0; j < population[i].getGenes().length; j++) { //loop through each persons genes
                population[i].setGeneFromIndex(j, rand.nextInt(2));
            }
        }
    }

    /*
    This will perform the fitness function for each person (see how fit they are)
    This will vary for the type of problem you are doing --> simpleGA = who has the most 1's, coursework = how closely does it match to the input
    You can do multiple things to the fitness function to make it better
        --> such as make it exponential/quadratic so bigger numbers are more fitter
        --> add bias to highest number
    I will do both to show you
     */
    public static void fitnessFunction(Individual[] population) {
        int totalFitness = 0;
        
        int highestFitnessIndex = 0;
        for (int i = 0; i < population.length; i++) {
            int newFitness = Arrays.stream(population[i].genes).sum();
            totalFitness += newFitness;
            if (population[i].getFitness() > population[highestFitnessIndex].getFitness()) {
                highestFitnessIndex = i;
            }
//            //linear
            population[i].setFitness(newFitness);

//            //quadratic SLOW FOR SOME REASON....
//            population[i].setFitness((int)Math.pow(newFitness, 2)); //quadratic
        }

        //exponential (in my bad code, it requires linear
        //HAHAH EXPONENTIAL IS BREAKING MY CODE (PROBS TOO MUCH MEMORY USED!!!)
//        double exponent = 0;
//        for (int i = 0; i < population.length; i++) {
//            exponent = (double)1+ ((double)population[i].getFitness() / (double)totalFitness); //so it will be e.g. 1.26 because...
//            population[i].setFitness((int) Math.pow(2, exponent)); //... n^normalFitness would be MASSIVE
//        }

        //bias (make the one with the highest fitness higher chance to get...
//        population[highestFitnessIndex].setFitness(population[highestFitnessIndex].getFitness() + 10);
        
        //heck you can even add a bias on a solution which is not the fittest --> as this will lead to diversity in the population!
        double averageFitness = totalFitness/population.length;
        double closestAverage = 100000000;
        int closestAverageIndex = 0;
        for (int i = 0; i < population.length; i++) {
            if(Math.abs(population[i].getFitness() - averageFitness) < closestAverage) {
                closestAverage = population[i].getFitness();
                closestAverageIndex = i;
            }
        }
//        population[closestAverageIndex].setFitness((int)closestAverage + 10);
    }

    /*
    This is to select a person from the population (we can then use this person for the mating pool to make children with another person selected
    There are MANY different types of selections --> the one on the worksheet is TORNAMENT SELECTION and is pretty good at getting fitter people for mating.
    Problem with this, is that you can't use the benefits of bais fitness, or quadratic fitness
     */
    public static Individual selection(Individual[] population) {
        Random rand = new Random();
        Individual person1 = population[rand.nextInt(population.length)];
        Individual person2 = population[rand.nextInt(population.length)];

        if (person1.getFitness() > person2.getFitness()) {
            return person1;
        } else {
            return person2;
        }

    }

    /*
    This uses the roulette wheel to select a parent based on probability
    */
    public static Individual selectionRoulette(Individual[] population) {

        //work out total fitness
        double totalFitness = 0;
        for (Individual individual : population) {
            totalFitness += individual.getFitness();
        }

        //make a random number from 0-1
        double whereTheRouletteWheelStopped = Math.random();
        for (int i = 0; i < population.length; i++) {
            //keep subtracting till negative.. once this happens we know which person to stop on
            double fitnessAsDouble = population[i].getFitness();
            whereTheRouletteWheelStopped -= (double) (fitnessAsDouble / totalFitness);
            if (whereTheRouletteWheelStopped <= 0) {
                return population[i];
            }
        }

        //worst case return the first person
        return population[0];
    }

    /*
    This is where we get 2 parents (from the mating pool) and perform crossover --> this will form the children
    KEEP IN MIND THIS RETURNS 2 CHILDREN!
    
    how it will look (| is the crossover point)
    parent1 = 111|11
    parent2 = 000|00
    child1 = 11100
    child2 = 00011
     */
    public static Individual[] crossover(Individual parent1, Individual parent2, double CROSSOVER_RATE) {
        Individual child[] = new Individual[2];

        child[0] = parent1;
        child[1] = parent2;

        //check if we should crossover or not
        if (CROSSOVER_RATE > Math.random()) { //math.random = random number between 0-1 (e.g. 0.52)
            int crossoverPoint = new Random().nextInt(parent1.genes.length); //both parents have same length, lets just get length from 1 of them...
            for (int i = 0; i < parent1.genes.length; i++) {
                if (i > crossoverPoint) {
                    //swap
                    int tempGene = child[0].getGeneFromIndex(i);
                    child[0].setGeneFromIndex(i, child[1].getGeneFromIndex(i));
                    child[1].setGeneFromIndex(i, tempGene);
                }
            }
        }

        return child;
    }

    /*
    This is mutation
    For binary, this is just bit flipping!
     */
    public static void mutation(Individual[] population, double MUTATION_RATE) {
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population[i].getGenes().length; j++) {
                if (MUTATION_RATE > Math.random()) {
                    //bit flip
                    population[i].setGeneFromIndex(
                            j,
                            population[i].getGeneFromIndex(j) ^ 1
                    );
                }
            }
        }
    }

    /*Inner class cuz im lazy*/
    static class Individual {

        private int[] genes;
        private int fitness;

        public Individual(int numberOfGenes) {
            genes = new int[numberOfGenes];
            fitness = 0;
        }

        /*GETTERS AND SETTERS*/
        public int[] getGenes() {
            return genes;
        }

        public int getGeneFromIndex(int i) {
            return genes[i];
        }

        public void setGenes(int[] genes) {
            this.genes = genes;
        }

        public void setGeneFromIndex(int i, int value) {
            genes[i] = value;
        }

        public int getFitness() {
            return fitness;
        }

        public void setFitness(int fitness) {
            this.fitness = fitness;
        }

    }

}

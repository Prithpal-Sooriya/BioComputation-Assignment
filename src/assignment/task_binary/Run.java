/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_binary;

/**
 *
 * @author Prithpal Sooriya
 */
public class Run {

    public static void main(String[] args) {

        GABool run = new GABool("data1.txt", "./src/Files/data1","Data1-pop10-gene100-cross09-mut001v5.csv");
        run.setPopulationSize(10);
        run.setChromosomeLength(100);
        run.setCrossoverRate(0.9);
        run.setMutationRate(0.01);
        
        run.run();
    }

}

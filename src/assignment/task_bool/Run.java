/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_bool;

/**
 *
 * @author Prithpal Sooriya
 */
public class Run {
    
    public static void main(String[] args) {
        
        GABool run = new GABool("data2.txt");
        run.setPopulationSize(25);
        run.setChromosomeLength(64);
        run.setCrossoverRate(0.9);
        run.setMutationRate(0.03);
        
        run.run();
    }
    
}

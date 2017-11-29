/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_float;

/**
 *
 * @author Prithpal Sooriya
 */
public class Run {
    
    /*run method for the GAFLoat*/
    public static void main(String[] args) {
        
        GAFloat run = new GAFloat("data3.txt", "./src/Files/data3", "Data3-pop200-gene10-cross09-mut001v5.csv");
        
        GAFloat.setPOPULATION_SIZE(200);
        GAFloat.setCHROMOSOME_LENGTH(10);
        GAFloat.setCROSSOVER_RATE(0.7);
        GAFloat.setBLEND_CROSSOVER_RATE(0.6);
        GAFloat.setMUTATION_RATE(0.01);
        GAFloat.setFIXED_OMEGA_RATE(0.3);
        GAFloat.setNUMBER_OF_GENERATIONS(3000);
        
        run.run();
    }
}

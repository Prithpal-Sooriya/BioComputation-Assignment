/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.task_bool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Prithpal Sooriya
 */
public class Testing {

    /*
    Just want to test the sorting method of the population
     */
    private static final int POPULATION_SIZE = 10;
    private static final int CHROMOSOME_LENGTH = 15; //chromosome length != DNA length, as chromosome becomes decoded and encoded to binary string later on
    private static final double CROSSOVER_RATE = 0.9; // 0.6-0.9
    private static final double MUTATION_RATE = 0.04; // 1/popsize - 1/chromosome length (or DNA length)

    public static void main(String[] args) {
     
        int[] nums1 = {1,2,3,4,5,6,7,8,9};
        int[] nums2 = {1,2,3,4,5,6,6,7,8};
        
        int offset = 0;
        for (int i = 0; i < nums1.length; i++) {
            if (nums2[nums2.length - 1] > nums1[i]) {
                offset++;
            } else {
                break;
            }
        }
        
        System.out.println("Length = " + nums2.length);
        System.out.println("Offset = " + offset);
        System.arraycopy(nums2, nums2.length-offset, nums1, 0, offset);
        
        System.out.println(Arrays.toString(nums1));

    }

}

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
public class Bound {

    private float lowerbound;
    private float upperbound;

    //constructor given bounds
    public Bound(float lowerbound, float upperbound) {
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;

//        validateBounds();

    }

    //constructor with no bounds given, so randomises bounds
    public Bound() {
        this.lowerbound = (float) Math.random();
        this.upperbound = (float) Math.random();

        validateBounds();
    }

    /*
    validate bounds
    if bounds are incorrect (lowerbound>upperbound), swap
     */
    public void validateBounds() {
        float tempLowerbound = Math.min(lowerbound, upperbound);
        float tempUpperbound = Math.max(lowerbound, upperbound);
        this.lowerbound = tempLowerbound;
        this.upperbound = tempUpperbound;

        //limit values between 0-1
//        if(lowerbound < (float)0) lowerbound = 0;
//        if(lowerbound > (float)1) lowerbound = 1;
//        if(upperbound < (float)0) upperbound = 0;
//        if(upperbound > (float)1) upperbound = 1;
    }

    //methods to handle bounds
    private float bounceBound(float bound, float creep) {
        //creep approches 0 or 1 even if you flip the sign (e.g. bound = 0.5, creep = 0.51)
        if (bound + creep > 1 && bound - creep < 0) {
            return bound + (creep / 2);
        }

        //creep is -ve and approaches 0
        if (bound + creep < 0) {
            return bound - creep;
        }
        //creep is +ve and approaches 1
        if (bound + creep > 1) {
            return bound - creep;
        }

        //all okay, we don't need to "bounce"
        return bound+creep;
    }

    /*
    phase bound
    - if bound becomes -ve then "phase through top"
        - bound = -0.2 --> bound = 1 - 0.2 = 0.8
        - bound = 1.4 --> bound = 1.4 - 1 = 0.4
    
    NOTE: deal with addition only, so we don't cause double negatives!
     */
    private float phaseBound(float bound, float creep) {
        float tempBound = bound + creep;
        if (tempBound < 0) {
            return 1 + tempBound; //out from bottom in from top
        }
        if (tempBound > 1) {
            return tempBound - 1; //out from top in from bottom
        }
        //all okay, didnt need to phase
        return tempBound;
    }

    private float absBound(float bound, float creep) {
        float tempBound = bound + creep;
        if(tempBound < 0) {
            return Math.abs(tempBound);
        }
        if(tempBound > 1) {
            /*
            -0.2 --> 0.2 --> -0.2 + 0.4 = 0.2
            -0.4 --> 0.4 --> -0.4 + 0.8 = 0.4
            SO
            1.2 --> ? --> 1.2 - 0.4 = 0.8
            1.4 --> ? --> 1.4 - 0.8 = 0.6
            */
            float diff = 1 - tempBound;
            return tempBound - (diff*2);
        }
        //all okay, we did not need to abs
        return tempBound;
    }

    //setter and getters
    public float getLowerbound() {
        return lowerbound;
    }

    public void setLowerbound(float lowerbound) {
        //find out the creep!
        float creep = lowerbound - this.lowerbound;
        this.lowerbound = phaseBound(lowerbound, creep);
    }

    public float getUpperbound() {
        return upperbound;
    }

    public void setUpperbound(float upperbound) {
        //find out creep!
        float creep = upperbound - this.upperbound;
        this.upperbound = phaseBound(upperbound, creep);
    }

    @Override
    public String toString() {
        return lowerbound + "<=x<=" + upperbound;
    }

}

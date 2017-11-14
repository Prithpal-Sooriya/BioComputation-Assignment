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

        validateBounds();

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

    //setter and getters
    public float getLowerbound() {
        return lowerbound;
    }

    public void setLowerbound(float lowerbound) {
        if (lowerbound < 0.0f) {
            this.lowerbound = 0;
        } else if (lowerbound > 1.0f) {
            this.lowerbound = 1;
        } else {
            this.lowerbound = lowerbound;
        }
    }

    public float getUpperbound() {
        return upperbound;
    }

    public void setUpperbound(float upperbound) {
        if (upperbound < 0.0f) {
            this.upperbound = 0;
        } else if (upperbound > 1.0f) {
            this.upperbound = 1;
        } else {
            this.upperbound = upperbound;
        }
    }

    @Override
    public String toString() {
        return lowerbound + "<=x<=" + upperbound;
    }

}

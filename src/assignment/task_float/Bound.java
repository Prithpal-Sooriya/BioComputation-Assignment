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
    }

    //setter and getters
    public float getLowerbound() {
        return lowerbound;
    }

    public void setLowerbound(float lowerbound) {
        if (lowerbound < 0) {
            this.lowerbound = 0;
        }
        if (lowerbound > 1) {
            this.lowerbound = 1;
        } else {
            this.lowerbound = lowerbound;
        }
    }

    public float getUpperbound() {
        return upperbound;
    }

    public void setUpperbound(float upperbound) {
        if (upperbound < 0) {
            this.upperbound = 0;
        }
        if (upperbound > 1) {
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

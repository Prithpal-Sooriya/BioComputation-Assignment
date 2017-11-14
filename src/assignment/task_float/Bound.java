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
        lowerbound = Math.min(lowerbound, upperbound);
        upperbound = Math.max(lowerbound, upperbound);
    }
    
    //setter and getters
    public float getLowerbound() {
        return lowerbound;
    }

    public void setLowerbound(float lowerbound) {
        this.lowerbound = lowerbound;
    }

    public float getUpperbound() {
        return upperbound;
    }

    public void setUpperbound(float upperbound) {
        this.upperbound = upperbound;
    }

    @Override
    public String toString() {
        return lowerbound + "<=x<=" + upperbound;
    }
    
    
}

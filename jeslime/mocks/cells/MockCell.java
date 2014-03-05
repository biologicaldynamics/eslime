/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package cells;

import structural.identifiers.Coordinate;

/**
 * Mock cell class used for testing. We make it extend from BehaviorCell
 * for compatibility with BehaviorCell-only classes. (BehaviorCell is a
 * subclass of Cell which is capable of engaging in arbitrary behaviors,
 * which can then be used for agent-based modeling.)
 *
 * Created by dbborens on 1/13/14.
 */
public class MockCell extends BehaviorCell {

    private int considerCount;
    private Cell child;
    private int state;
    private double fitness;
    private double production;
    private String lastTriggeredBehaviorName;
    private Coordinate lastTriggeredCaller;
    private boolean divisible;
    private boolean died;

    @Override
    public int consider() {
        return considerCount;
    }

    @Override
    public void apply() {

    }

    @Override
    public Cell divide() {
        return child;
    }

    @Override
    public Cell clone(int state) {
        return child;
    }

    @Override
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setProduction(double production) {
        this.production = production;
    }

    public void setChild(Cell child) {
        this.child = child;
    }

    public void setConsiderCount(int considerCount) {
        this.considerCount = considerCount;
    }

    @Override
    public boolean isDivisible() {
        return divisible;
    }

    @Override
    protected void setDivisible(boolean divisible) {
        this.divisible = divisible;
    }

    @Override
    public double getProduction(String solute) {
        return production;
    }

    public Coordinate getLastTriggeredCaller() {
        return lastTriggeredCaller;
    }

    public String getLastTriggeredBehaviorName() {
        return lastTriggeredBehaviorName;
    }

    @Override
    public void trigger(String behaviorName, Coordinate caller) {
        lastTriggeredBehaviorName = behaviorName;
        lastTriggeredCaller = caller;
    }

    @Override
    public void die() {
        died = true;
    }

    public boolean died() {
        return died;
    }

    @Override
    public void feed(double delta) {

    }
}

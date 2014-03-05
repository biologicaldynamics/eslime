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

import agent.action.Action;
import agent.action.MockAction;
import agent.control.BehaviorDispatcher;
import agent.control.MockBehaviorDispatcher;
import structural.identifiers.Coordinate;
import test.EslimeLatticeTestCase;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/25/14.
 */
public class BehaviorCellTest extends EslimeLatticeTestCase {

    private BehaviorCell query;
    private MockBehaviorDispatcher dispatcher;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dispatcher = new MockBehaviorDispatcher();
        query = new BehaviorCell(layerManager, 1, 1.0, 0.5);
        query.setDispatcher(dispatcher);
        layer.getUpdateManager().place(query, origin);
    }

    public void testConsiderAndApply() {
        int result = query.consider();
        assertEquals(1, result);

        result = query.consider();
        assertEquals(2, result);

        query.apply();

        result = query.consider();

        assertEquals(1, result);
    }

    public void testDivide() throws Exception {
        Cell child = query.divide();
        assertEquals(child, query);

        // Fitness should be half for each
        assertEquals(0.5, query.getFitness(), epsilon);
        assertEquals(0.5, child.getFitness(), epsilon);
    }

    public void testClone() throws Exception {
        Cell clone = query.clone();
        assertEquals(clone, query);

        // Since no division took place, fitness should be original for each
        assertEquals(1.0, query.getFitness(), epsilon);
        assertEquals(1.0, clone.getFitness(), epsilon);
    }

    public void testTrigger() throws Exception {
        String triggerName = "TEST";
        Coordinate caller = new Coordinate(0, 0, 0);
        query.trigger(triggerName, caller);

        assertEquals(triggerName, dispatcher.getLastTriggeredName());
        assertEquals(caller, dispatcher.getLastTriggeredCaller());
    }

    public void testDie() throws Exception {
        assertTrue(layer.getViewer().isOccupied(origin));
        query.die();
        assertFalse(layer.getViewer().isOccupied(origin));
    }

    public void testEquals() {
        // Difference based on dispatcher (in)equality.
        BehaviorCell other = new BehaviorCell(layerManager, 1, 1.0, 0.5);
        MockBehaviorDispatcher d2 = new MockBehaviorDispatcher();
        other.setDispatcher(d2);
        d2.setOverrideEquals(true);

        // ...unequal dispatcher.
        d2.setReportEquals(false);
        assertNotEquals(query, other);

        // ...equal dispatcher.
        d2.setReportEquals(true);
        assertEquals(query, other);

        // Test a cell that differs in division threshold.
        other = new BehaviorCell(layerManager, 1, 1.0, 1.0);
        other.setDispatcher(d2);
        assertNotEquals(query, other);

        // Test a cell that differs in state.
        other = new BehaviorCell(layerManager, 2, 1.0, 0.5);
        other.setDispatcher(d2);
        assertNotEquals(query, other);
    }

    /**
     * The base BehaviorCell class automatically marks itself
     * as divisible according to its fitness. So if a call
     * to setFitness() puts it above or below the threshold,
     * that should be noted.
     */
    public void testDivisibilityThresholding() {
        double threshold = query.getThreshold();

        // Start off below threshold.
        query.setFitness(threshold / 2);
        assertDivisibilityStatus(false);

        // Adjust above threshold.
        query.setFitness(threshold * 2);
        assertDivisibilityStatus(true);

        // Adjust below threshold again.
        query.setFitness(threshold / 2);
        assertDivisibilityStatus(false);
    }

    private void assertDivisibilityStatus(boolean expected) {
        boolean layerActual = layer.getViewer().isDivisible(origin);
        boolean cellActual = query.isDivisible();
        assertEquals(expected, layerActual);
        assertEquals(expected, cellActual);
    }
}

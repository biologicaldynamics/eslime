/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package processes;

import control.identifiers.Coordinate;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 4/20/14.
 */
public class MockStepState extends StepState {
    public MockStepState() {
        this(0.0);
    }

    public MockStepState(double startTime) {
        super(startTime);
        highlightMap = new HashMap<>();
    }

    private HashMap<Integer, Coordinate[]> highlightMap;

    @Override
    public Coordinate[] getHighlights(Integer channel) {
        return highlightMap.get(channel);
    }

    public void setHighlights(Integer channel, Coordinate[] highlights) {
        highlightMap.put(channel, highlights);
    }
}

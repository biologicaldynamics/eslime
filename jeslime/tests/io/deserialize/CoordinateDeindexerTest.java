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

package io.deserialize;

import test.EslimeTestCase;
import structural.identifiers.Coordinate;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dbborens on 12/10/13.
 */
public class CoordinateDeindexerTest extends EslimeTestCase {

    private ExposedDeindexer deindexer;

    @Override
    public void setUp() {
        deindexer = new ExposedDeindexer(fixturePath);
    }

    public void testIndex() {
        // This coordinate has index 1 -- see test fixture
        Coordinate input = new Coordinate(0, 1, 1);
        assertEquals(1, (int) deindexer.getIndex(input));
    }

    public void testDeindex() {
        Coordinate expected = new Coordinate(0, 1, 1);
        Coordinate actual = deindexer.getCoordinate(1);
        assertEquals(expected, actual);
    }

    public void testParseCoordinate()  {
        String input = "(3, 5, 9 | 0)";
        Coordinate expected = new Coordinate(3, 5, 9, 0);
        Coordinate actual = deindexer.parseCoordinate(input);
        assertEquals(expected, actual);
    }

    private class ExposedDeindexer extends CoordinateDeindexer {

        public HashMap<Integer, Coordinate> indexToCoord;

        public ExposedDeindexer(String path) {
            super(path);
            indexToCoord = super.indexToCoord;
        }

        public void deindex() throws IOException {
            super.deindex();
        }

        public Coordinate parseCoordinate(String token) {
            return super.parseCoordinate(token);
        }
    }
}

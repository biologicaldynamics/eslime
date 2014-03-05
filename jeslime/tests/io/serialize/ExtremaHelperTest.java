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

package io.serialize;

import test.EslimeTestCase;
import structural.identifiers.Coordinate;
import structural.identifiers.Extrema;

import java.io.StringWriter;

/**
 * Created by dbborens on 12/11/13.
 */
public class ExtremaHelperTest extends EslimeTestCase {

    private StringWriter sw;
    private ExtremaHelper helper;

    @Override
    protected void setUp() throws Exception {
        sw = new StringWriter();
        helper = new ExtremaHelper(sw);
    }

    public void testPush() throws Exception {
        Extrema a = new Extrema();
        a.consider(0.0, new Coordinate(0, 0, 0, 0), 2.0);
        a.consider(7.0, new Coordinate(1, 0, 0, 0), 1.0);
        helper.push("a", a);

        String expected = "a>0.0@(0, 0, 0 | 0 | 2.0):7.0@(1, 0, 0 | 0 | 1.0)";
        String actual = sw.toString();

        assertEquals(expected, actual);
    }
}

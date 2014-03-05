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

package layers;

import io.project.GeometryManager;
import layers.cell.CellLayer;
import layers.solute.SoluteLayer;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by dbborens on 12/31/13.
 */
public class MockLayerManager extends LayerManager {
    public MockLayerManager() {
        super();
        soluteLayers = new HashMap<>();
    }

    public CellLayer getCellLayer() {
        return cellLayer;
    }

    public void setCellLayer(CellLayer cellLayer) {
        this.cellLayer = cellLayer;
    }

    private CellLayer cellLayer;

    private HashMap<String, SoluteLayer> soluteLayers;


    public void addSoluteLayer(String s, SoluteLayer layer) {
        soluteLayers.put(s, layer);
    }

    public SoluteLayer getSoluteLayer(String s) {
        return soluteLayers.get(s);
    }

    public SoluteLayer[] getSoluteLayers() {
        return soluteLayers.values().toArray(new SoluteLayer[0]);
    }
}

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

package layers.cell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;

import cells.Cell;
import geometry.Geometry;
import structural.Flags;
import structural.identifiers.Coordinate;

/**
 * 
 * @test CellLookupManagerTest
 * @author David Bruce Borenstein
 *
 */
public class CellLookupManager {
	
	private CellLayerContent content;
	private CellLayerIndices indices;
	private Geometry geom;
	
	public CellLookupManager(Geometry geom, CellLayerContent content, CellLayerIndices indices) {
		this.content = content;
		this.indices = indices;
		this.geom = geom;
	}
	
	/**
	 * Get the state of neighboring cells. Vacant cells are ignored.
	 * @param coord
	 * @return
	 */
	public int[] getNeighborStates(Coordinate coord) {
		content.checkExists(coord);

	    // Get set of neighbors
		Coordinate[] neighbors = geom.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);

		// Allocate return vector
		int[] states = new int[neighbors.length];

		// Check state of each neighbor
	    for (int i = 0; i < neighbors.length; i++) {
			Coordinate query = neighbors[i];
			states[i] = content.get(query).getState();
		}

		// Return
		return states;
	}

	/**
	 * Get the site or sites with the minimum L1 (Manhattan) distance,
	 * up to the specified maximum distance. If maxDistance is -1, the
	 * search is unbounded.
	 * @param coord
	 * @param maxDistance
	 * @return
	 */
	// TODO: Create a second function that doesn't have the second
	// argument for unbounded searching?
	public Coordinate[] getNearestVacancies(Coordinate coord, int maxDistance) {

		content.checkExists(coord);


		// If there are no vacancies, just return now. This should prevent infinite
		// loop even when searching without bound.
		if (!geom.isInfinite() && (indices.getOccupiedSites().size() > content.getCanonicalSites().length)) {
			throw new IllegalStateException("Consistency failure.");
		} else if (!geom.isInfinite() && (indices.getOccupiedSites().size() == content.getCanonicalSites().length)) {
	    	return new Coordinate[0];
		}

		// Initialize return object
		ArrayList<Coordinate> res = new ArrayList<Coordinate>();
		
		// Loop through looking for vacancies (starting with target site)
		int r = 0;

		// I included this extra map so I could check for duplicates in best 
		// case O(1) time, but if I have to do that, doesn't it seem like I should
		// be returning a set instead of building two data structures?
		HashSet<Coordinate> incl = new HashSet<Coordinate>();

		while ((maxDistance == -1) || (r <= maxDistance)) {

			// We want to check every site, so don't use circumnavigation restriction.
			Coordinate[] annulus = geom.getAnnulus(coord, r, Geometry.APPLY_BOUNDARIES);

	        for (int i = 0; i < annulus.length; i++) {

				Coordinate query = annulus[i];

				if (query.hasFlag(Flags.UNDEFINED)) {
					System.err.println("WARNING: Cleaning undefined coordinate " + query + ". Undefined coordinates should not be returned in getAnnulus(...).");
					continue;
				}
				// Sanity check
				content.checkExists(query);

				if (!indices.isOccupied(query) && !incl.contains(query)) {
			
					incl.add(query);
					res.add(query);
				}

			}

			// If we've managed to populate res, it means that we founds targets
			// in the current annulus, so return.
			if (res.size() > 0) {
				return(res.toArray(new Coordinate[0]));
			}

			r++;
		} 

		return(res.toArray(new Coordinate[0]));
	}

    public Coordinate getCellLocation(Cell cell) {
        CellLocationIndex locationIndex = indices.getCellLocationIndex();
        return locationIndex.locate(cell);
    }
}

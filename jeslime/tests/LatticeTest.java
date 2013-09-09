import java.util.HashSet;

import cells.Cell;
import cells.SimpleCell;
import junit.framework.TestCase;
import structural.Lattice;
import structural.identifiers.Coordinate;
import geometries.HexRing;


public class LatticeTest extends TestCase {

	public void testConstructor() {
		HexRing geom = new HexRing(6, 6);
		
		try {
			new Lattice(geom);
		} catch (Exception ex) {
			fail();
		}
	}

	public void testInterrogate() {
		HexRing geom = new HexRing(6, 6);
		Lattice lattice = new Lattice(geom);

		// Set up a cell
		Cell toPlace = new SimpleCell(1);

		Coordinate coord = new Coordinate(2, 3, 0);

		lattice.place(toPlace, coord);

		// Get its properties through the lattice.
		// TODO This should be replaced with epsilon equality
		assertEquals(toPlace.getFitness(), lattice.getFitness(coord), 1E-10);
		assertEquals(toPlace.getState(), lattice.getState(coord));
	}

	public void testNeighborStates() {
		HexRing geom = new HexRing(6, 6);
		Lattice lattice = new Lattice(geom);

		// Set up one cell
		Cell toPlace = new SimpleCell(1);
		Coordinate coord = new Coordinate(2, 3, 0);
		lattice.place(toPlace, coord);

		// All neighbors should be vacant
		Coordinate[] neighbors = lattice.getNearestVacancies(coord, -1);
		assertEquals(neighbors.length, 6);

		// Add an occupied neighbor
		Coordinate coordAbove = new Coordinate(3, 3, 0);
		lattice.place(new SimpleCell(2), coordAbove);

		// Check that the right cell is placed
		assertFalse(lattice.getState(coord) == lattice.getState(coordAbove));

		// Check neighborhood
		assertEquals(5, lattice.getNearestVacancies(coord, -1).length);
		assertEquals(5, lattice.getNearestVacancies(coordAbove, -1).length);

		// Add a cell at adjacent to southern boundary
		Coordinate south = new Coordinate(2, 1, 0);
		lattice.place(new SimpleCell(1), south);

		// Should be short one vacant neighbor (hard BCs for cells)
		assertEquals(5, lattice.getNearestVacancies(south, -1).length);

		// Add a cell at origin (should be just like south)
		Coordinate origin = new Coordinate(0, 0, 0);
		lattice.place(new SimpleCell(1), origin);

		// Should be short one vacant neighbor (hard BCs for cells)
		assertEquals(5, lattice.getNearestVacancies(origin, -1).length);
	}

	public void testVacancyModel() {
		HexRing geom = new HexRing(6, 6);
		Lattice lattice = new Lattice(geom);

		// Set up one cell
		Cell toPlace = new SimpleCell(1);
		Coordinate coord = new Coordinate(2, 3, 0);
		lattice.place(toPlace, coord);

		// List of vacancies should be canonical neighbors
		Coordinate[] cVec = geom.getCellNeighbors(coord);
		Coordinate[] nVec = lattice.getNearestVacancies(coord, -1);
		assertEquals(cVec.length, nVec.length);

		HashSet<Coordinate> cSet = new HashSet<Coordinate>(cVec.length);
		
		for (int i = 0; i < cVec.length; i++)
			cSet.add(cVec[i]);
		
		for (int i = 0; i < nVec.length; i++) {
			assertTrue(cSet.contains(nVec[i]));
		}

		// Fill all but one canonical neighbor
		for (int i = 0; i < nVec.length - 1; i++) {
			lattice.place(new SimpleCell(0), nVec[i]);
		}

		// List of vacancies should be only remaining canonical neighbor
		assertEquals(1, lattice.getNearestVacancies(coord, -1).length);

		// Fill that one -- should have 12 nearest vacancies now
		lattice.place(new SimpleCell(0), nVec[nVec.length - 1]);
		assertEquals(12, lattice.getNearestVacancies(coord, -1).length);

		// Now try getNearestVacancies with a maximum radius of 1--shouldn't have any

		lattice.getNearestVacancies(coord, 1);

		Coordinate[] foo = lattice.getNearestVacancies(coord, 1);

		assertEquals(0, foo.length);
		assertEquals(12, lattice.getNearestVacancies(coord, 2).length);
	}

	public void testNoOverwriteOnPlace() {
		HexRing geom = new HexRing(6, 6);
		Lattice lattice = new Lattice(geom);

		// Set up one cell
		Cell toPlace = new SimpleCell(1);
		Coordinate coord = new Coordinate(2, 3, 0);
		lattice.place(toPlace, coord);

		Cell second = new SimpleCell(2);
		
		boolean thrown = false;
		try {
			lattice.place(second, coord);
		} catch (Exception ex) {
			thrown = true;
		}
		assertTrue(thrown);		
	}

	public void testNoOverwriteOnMove() {
		HexRing geom = new HexRing(6, 6);
		Lattice lattice = new Lattice(geom);

		// Set up one cell
		Cell toPlace = new SimpleCell(1);
		Coordinate coord = new Coordinate(2, 3, 0);
		lattice.place(toPlace, coord);

		Cell second = new SimpleCell(2);
		Coordinate sc = new Coordinate(3, 3, 0);
		lattice.place(second, sc);
		
		boolean thrown = false;
		try {
			lattice.move(coord, sc);
		} catch (Exception ex) {
			thrown = true;
		}
		assertTrue(thrown);
		
	}

	/*******************/
	/* FUNCTIONAL TEST */
	/*******************/
	
	public void latticeFunctionalTest() {
		HexRing geom = new HexRing(6, 6);
		Lattice lattice = new Lattice(geom);

		// Unoccupied lattice: occupied and divisible sites should be empty, vacant == canonical
		Coordinate[] canonical = geom.getCanonicalSites();
		HashSet<Coordinate> vSet = lattice.getVacantSites();

		Coordinate[] vacant = vSet.toArray(new Coordinate[0]);

		assertEquals(canonical.length, vacant.length);

		HashSet<Coordinate> cSet = new HashSet<Coordinate>(canonical.length);
		for (int i = 0; i < canonical.length; i++)
			cSet.add(canonical[i]);
		
		for (int i = 0; i < vacant.length; i++) {
			assertTrue(cSet.contains(vacant[i]));
		}

		assertEquals(0, lattice.getDivisibleSites().size());
		assertEquals(0, lattice.getOccupiedSites().size());

		// Place one cell
		Cell toPlace = new SimpleCell(1);
		Coordinate coord = new Coordinate(2, 3, 0);
		lattice.place(toPlace, coord);

		// Indices should reflect the placement
		assertEquals(1, lattice.getOccupiedSites().size());
		assertEquals(1, lattice.getDivisibleSites().size());
		assertEquals(canonical.length - 1, lattice.getVacantSites().size());

		// Divide cell to a neighboring site
		Coordinate[] targets = lattice.getNearestVacancies(coord, -1);
		Coordinate child = targets[0];

		lattice.divideTo(coord, child);

		assertEquals(lattice.getState(coord), lattice.getState(child));

		// Indices should reflect the division
		assertEquals(2, lattice.getOccupiedSites().size());

		assertEquals(2, lattice.getDivisibleSites().size());

		assertEquals(canonical.length - 2, lattice.getVacantSites().size());

		// Tell only one cell to consider...
		assertEquals(1, lattice.consider(child));

		// Swap
		lattice.swap(coord, child);

		// The consider count should be consistent with the swap
		assertEquals(2, lattice.consider(coord));
		assertEquals(1, lattice.consider(child));

		// Move one of them
		Coordinate destination = targets[1];
		lattice.move(coord, destination);
		assertEquals(3, lattice.consider(destination));

		// Indices should reflect the move
		assertEquals(2, lattice.getOccupiedSites().size());
		assertEquals(2, lattice.getDivisibleSites().size());
		assertEquals(canonical.length - 2, lattice.getVacantSites().size());

		// Banish the other one
		lattice.banish(child);

		// Indices should reflect the banishment
		assertEquals(1, lattice.getOccupiedSites().size());
		assertEquals(1, lattice.getDivisibleSites().size());
		assertEquals(canonical.length - 1, lattice.getVacantSites().size());

		// Check exact values of indices
		assertEquals(destination, (lattice.getOccupiedSites().iterator().next()));
		assertEquals(destination, (lattice.getDivisibleSites().iterator().next()));

		vSet = lattice.getVacantSites();
		assertFalse(vSet.contains(destination));
	}

}

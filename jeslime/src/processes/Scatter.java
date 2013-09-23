package processes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import cells.Cell;
import cells.SimpleCell;

import structural.Lattice;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

public class Scatter extends CellProcess {
	
	private int numGroups;
	private int groupSize;
	
	private Random random;
	
	public Scatter(Lattice lattice, int numGroups, int groupSize) {
		super(lattice);
		this.numGroups = numGroups;
		this.groupSize = groupSize;
		
		// TODO This should be core infrastructure
		random = new Random();
	}

	public Coordinate[] iterate() throws HaltCondition {

		ArrayList<Coordinate> highlight = new ArrayList<Coordinate>();
		
		// Construct initial set of candidates
		Coordinate[] canonicals = lattice.getCanonicalSites();
		
		HashSet<Coordinate> candidates = new HashSet<Coordinate> (canonicals.length);
		
		for (Coordinate c : canonicals) {
			if (!lattice.getOccupiedSites().contains(c)) {
				candidates.add(c);
			}
		}
		
		for (int i = 0; i < numGroups; i++) {
			for (int j = 0; j < groupSize; j++) {

				// TODO: This should be a unified exception that gets passed up to
				//       the Simulator.
				if (candidates.isEmpty()) {
					throw new LatticeFullEvent(lattice.getGillespie()); 
				}

				// Choose target randomly
				Coordinate[] cVec = candidates.toArray(new Coordinate[0]);
				
				int o = random.nextInt(cVec.length);
				Coordinate target = cVec[o];

				// Create and assign cell
				Cell cell = new SimpleCell(i+1);

				lattice.place(cell, target);
				highlight.add(target);
				candidates.remove(target);
			}
		}

		return highlight.toArray(new Coordinate[0]);
	}
}

package processes.discrete;

import cells.Cell;
import geometry.Geometry;
import io.project.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Adds a fixed amount of biomass to every cell with a cell
 * state matching the target.
 * 
 * @author dbborens
 *
 */
public class MutateAll extends CellProcess {

	// How much biomass to accumulate per time step
	private int ancestral;
	private int mutant;

    public MutateAll(ProcessLoader loader, LayerManager layerManager, int id,
                GeneralParameters p) {
        super(loader, layerManager, id, p);

		ancestral = Integer.valueOf(e.element("mutation").attribute("ancestral").getText());
		mutant = Integer.valueOf(e.element("mutation").attribute("mutant").getText());
		
	}

	public MutateAll(CellLayer layer, LayerManager layerManager,
			int ancestral, int mutant) {
		super(null, layerManager, 0, null);
		
		this.ancestral = ancestral;
		this.mutant = mutant;
	}
	
	public void target(GillespieState gs) throws HaltCondition {
		// This process only has one event: it affects all relevant cells.
		if (gs != null) {
			gs.add(getID(), 1, 1D);
		}
	}
	
	public void fire(StepState state) throws HaltCondition {
		// If the target state doesn't exist, don't waste any time
		// checking cells.
		int targetCount = layer.getViewer().getStateMapViewer().getCount(ancestral);
		if (targetCount == 0) {
			return;
		}
		
		// Feed the cells.
		for (Coordinate site : activeSites) {
			if (layer.getViewer().isOccupied(site) && layer.getViewer().getCell(site).getState() == ancestral) {
				Cell child = layer.getViewer().getCell(site).clone(mutant);
				layer.getUpdateManager().banish(site);
				layer.getUpdateManager().place(child, site);
			}
		}
	}

}

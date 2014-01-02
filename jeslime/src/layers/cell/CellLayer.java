package layers.cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import cells.Cell;
import layers.Layer;
import structural.Flags;
import structural.identifiers.Coordinate;
import geometry.Geometry;

/**
 * 
 * @test CellIntegrationTest
 * @author David Bruce Borenstein
 *
 */
public class CellLayer extends Layer {

	private CellLayerIndices indices;
	private CellLayerContent content;

    private String id;

	public CellLayer(Geometry geom, int id) {
        geometry = geom;
        this.id = Integer.toString(id);
		indices = new CellLayerIndices();
		content = new CellLayerContent(geometry, indices);
	}

    @Override
    public String getId() {
        return id;
    }

    public CellLookupManager getLookupManager() {
		return new CellLookupManager(geometry, content, indices);
	}
	
	public CellUpdateManager getUpdateManager() {
		return new CellUpdateManager(content, indices);
	}
	
	public CellLayerViewer getViewer() {
		return new CellLayerViewer(this, content, indices);
	}
}

package agent;

import agent.action.Action;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.identifiers.Coordinate;

/**
 * A Behavior is an ordered sequence of Actions, associated
 * with a particular agent cell and invoked by name. Behaviors
 * can be triggered (invoked) either by the actions of other
 * cells or directly via a top-down process in your model.
 *
 * Each cell has its own set of Behaviors, which can affect the
 * neighborhood of the cell as well as the cell itself. These
 * Behaviors can include Actions that trigger the Behaviors of
 * neighboring cells.
 *
 * The defining feature of a Behavior is its ordered list of
 * Actions, called the "action sequence." When triggered (invoked),
 * the Actions in the action sequence are fired one at a time.
 *
 * Created by David B Borenstein on 1/21/14.
 */
public class Behavior {

    private final Cell callback;
    private final LayerManager layerManager;

    // Each action in the actionSequence array is fired,
    // in order, when the trigger(...) method is invoked.
    private final Action[] actionSequence;

    protected LayerManager getLayerManager() {
        return layerManager;
    }

    protected Cell getCallback() {
        return callback;
    }

    public Behavior(Cell callback, LayerManager layerManager, Action[] actionSequence) {
        this.callback = callback;
        this.layerManager = layerManager;
        this.actionSequence = actionSequence;
    }

    public void run(Coordinate caller) {
        for (Action action : actionSequence) {
            action.run(caller);
        }
    }

    /**
     * Behaviors are equal if and only if their action sequences
     * consist of an equivalent list of actions.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        // The object should be a behavior.
        if (!(obj instanceof Behavior)) {
            return false;
        }

        Behavior other = (Behavior) obj;

        // The object should have the same number of actions.
        if (other.getActionSequence().length != this.getActionSequence().length) {
            return false;
        }

        // Each action should be equivalent, and in the correct order.
        for (int i = 0; i < getActionSequence().length; i++) {
            Action p = this.getActionSequence()[i];
            Action q = other.getActionSequence()[i];
            if (!p.equals(q)) {
                return false;
            }
        }

        // The behaviors are equivalent.
        return true;
    }

    protected Action[] getActionSequence() {
        return actionSequence;
    }
}

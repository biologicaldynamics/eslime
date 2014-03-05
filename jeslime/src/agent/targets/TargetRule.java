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

package agent.targets;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.GeneralParameters;
import structural.identifiers.Coordinate;

import java.util.Arrays;
import java.util.Random;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 *
 * NOTE: Do not confuse 'callback' and 'caller.' The callback
 * is the cell associated with this Targeter object (i.e., whose
 * behavior is being described.) The caller is the cell that
 * triggered the behavior, if any.
 *
 * Created by dbborens on 2/7/14.
 */
public abstract class TargetRule {

    protected Random random;
    protected int maximum;
    protected BehaviorCell callback;
    protected LayerManager layerManager;
    /**
     *
     * @param callback The cell whose behavior is being described
     * @param layerManager
     */
    public TargetRule(BehaviorCell callback, LayerManager layerManager, int maximum, Random random){
        this.callback = callback;
        this.layerManager = layerManager;
        this.maximum = maximum;
        this.random = random;
    }

    public int getMaximum() {
        return maximum;
    }

    /**
     * Returns the list of target coordinates that satisfy
     * the action's target descriptor.
     *
     * @param caller The cell that triggered the action.
     */
    public Coordinate[] report(BehaviorCell caller) {
        Coordinate[] candidates = getCandidates(caller);
        Coordinate[] targets = respectMaximum(candidates);
        return targets;
    }

    private Coordinate[] respectMaximum(Coordinate[] candidates) {
        // If maximum is < 0, it means that there is no maximum; return all.
        if (maximum < 0) {
            return candidates;
        }
        // If there the number of candidates does not exceed the max, return.
        if (candidates.length <= maximum) {
            return candidates;
        }

        // Otherwise, permute and choose the first n, where n = maximum.
        permute(candidates);

        Coordinate[] reduced = Arrays.copyOfRange(candidates, 0, maximum);

        return reduced;
    }

    @Override
    /**
     * Targeting rules are equal if and only if they are of the
     * same class.
     */
    public boolean equals(Object obj) {
        Class objClass = obj.getClass();
        Class myClass = getClass();

        // Must be same class of targeting rule.
        if (!objClass.equals(myClass)) {
            return false;
        }

        // Other targeting rule must have same maximum.
        TargetRule other = (TargetRule) obj;
        if (other.maximum != this.maximum) {
            return false;
        }

        return true;
    }

    public abstract TargetRule clone(BehaviorCell child);

    public BehaviorCell getCallback() {
        return callback;
    }

    protected abstract Coordinate[] getCandidates(BehaviorCell caller);

    /**
     * Fischer-Yates shuffling algorithm for permuting the contents of
     * a coordinate array.
     */
    private void permute(Coordinate[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(arr, i ,j);
        }
    }

    private void swap(Coordinate[] arr, int i, int j) {
        Coordinate temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}

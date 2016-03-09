package com.sourceallies.android.zonebeacon.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Defines a coordinator layout behavior so that our floating action button adjusts
 * correctly when a snackbar comes onto the screen.
 */
public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    /**
     * Default constructor so that it can be applied from the layout XML
     *
     * @param context sent from the layout XML file (Nullable)
     * @param attrs   sent from the layout XML (Nullable)
     */
    public FloatingActionButtonBehavior(@Nullable Context context, @Nullable AttributeSet attrs) {
    }

    /**
     * Whether or not the children layout for the coordinator layout should adjust for snackbars
     *
     * @param parent     Coordinator layout from the root of the layout
     * @param child      The linearlayout direct children of the coordinator layout
     * @param dependency the new view that just came onto the screen and was added to the coordinator layout.
     * @return true if the new view is a snackbar
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    /**
     * Adjusts the translation of the FAB layout based on the size of the dependancy (snackbar)
     *
     * @param parent     Coordinator layout from the root of the layout
     * @param child      The linearlayout direct children of the coordinator layout
     * @param dependency the new view that just came onto the screen and was added to the coordinator layout.
     * @return true
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
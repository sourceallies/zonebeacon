package com.sourceallies.android.zonebeacon.api;

/**
 * Adheres to the strategy pattern. This interface can be implemented in various different ways.
 * Each control unit will need to interpret commands differently, so that logic is handled here.
 * Once implemented, that interpreter can be loaded into the app so that we can support different
 * control unit types easily.
 */
public interface Interpreter {
}

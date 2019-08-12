/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.concurrentchaining.core;

import ec.tstoolkit.timeseries.simplets.TsData;

/**
 *
 * @author Deutsche Bundesbank
 */
public final class ConcurrentChainingMethods {

    public static TsData unchain(TsData ts) {
        return ts.div(ts.lead(1)).times(100);
    }

    public static TsData chain(TsData ts) {
        TsData chained = ts.extend(1, 0);
        chained.set(0, 100);
        chained.applyRecursively(100, (x, y) -> y != 0 ? x * y / 100 : x);
        return chained;
    }

    private ConcurrentChainingMethods() {
    }
}

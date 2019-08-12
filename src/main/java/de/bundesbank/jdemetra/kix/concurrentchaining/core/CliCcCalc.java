/*
 * Copyright 2017 Deutsche Bundesbank
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package de.bundesbank.jdemetra.kix.concurrentchaining.core;

import static de.bundesbank.jdemetra.kix.concurrentchaining.core.ConcurrentChainingMethods.chain;
import static de.bundesbank.jdemetra.kix.concurrentchaining.core.ConcurrentChainingMethods.unchain;
import de.bundesbank.kix.parser.ICalc;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsPeriod;

/**
 *
 * @author s4504tw
 */
public class CliCcCalc implements ICalc {

    private TsData weightedSumData;
    private TsData weightedSumWeights;
    private final TsPeriod referenzPeriod;

    public CliCcCalc(final TsData weightedSumData, final TsData weightedSumWeights, final TsPeriod referenzYear) {
        this.weightedSumData = replaceMissingValuesWithZero(unchain(weightedSumData));
        this.weightedSumWeights = replaceMissingValuesWithZero(weightedSumWeights.lead(1));
        this.referenzPeriod = referenzYear;
    }

    @Override
    public final void plus(final TsData index, final TsData weight) {
        TsData leadedWeight = replaceMissingValuesWithZero(weight.lead(1));
        TsData unchainedIndex = replaceMissingValuesWithZero(unchain(index));
        weightedSumData = replaceMissingValuesWithZero(weightedSumData.times(weightedSumWeights)
                .plus(unchainedIndex.times(leadedWeight))
                .div(weightedSumWeights.plus(leadedWeight)));
        weightedSumWeights = weightedSumWeights.plus(leadedWeight);
    }

    @Override
    public final void minus(final TsData index, final TsData weight) {
        TsData leadedWeight = replaceMissingValuesWithZero(weight.lead(1));
        TsData unchainedIndex = replaceMissingValuesWithZero(unchain(index));
        weightedSumData = replaceMissingValuesWithZero(weightedSumData.times(weightedSumWeights)
                .minus(unchainedIndex.times(leadedWeight))
                .div(weightedSumWeights.minus(leadedWeight)));
        weightedSumWeights = weightedSumWeights.minus(leadedWeight);
    }

    @Override
    public final TsData getResult() {
        return chain(weightedSumData).index(referenzPeriod, 100);
    }

    protected final TsData replaceMissingValues(final TsData ts, final double replacement) {
        return ts.transform(x -> Double.isNaN(x) ? replacement : x);
    }

    protected final TsData replaceMissingValuesWithZero(final TsData ts) {
        return replaceMissingValues(ts, 0);
    }

}

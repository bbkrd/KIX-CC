/*
 * Copyright 2017 Deutsche Bundesbank
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package de.bundesbank.jdemetra.kix.concurrentchaining.core;

import static de.bundesbank.jdemetra.kix.concurrentchaining.core.ConcurrentChainingMethods.unchain;
import de.bundesbank.kix.parser.ICalc;
import ec.tstoolkit.timeseries.simplets.TsData;
import org.netbeans.api.annotations.common.NonNull;

/**
 *
 * @author Thomas Witthohn
 */
public class CtgCcCalc implements ICalc {

    private final TsData totalData, totalWeight;
    private TsData result;
    private final int lag;

    public CtgCcCalc(@NonNull TsData totalData, TsData totalWeight, int lag) {
        this.totalData = unchain(totalData);
        this.totalWeight = totalWeight;
        this.lag = lag;
    }

    @Override
    public final void plus(TsData index, TsData weight) {
        TsData temp = unchain(index).minus(100).times(weight.div(totalWeight).lead(1));
        TsData interimResult = temp.lead(lag - 1);
        TsData multiplier = new TsData(temp.getDomain(), 1);
        for (int i = lag - 1; i > 0; i--) {
            multiplier = multiplier.times(totalData.lead(i).div(100));
            interimResult = interimResult.plus(multiplier.times(temp.lead(i - 1)));
        }
        result = interimResult;
    }

    @Override
    public final void minus(TsData index, TsData weight) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public final TsData getResult() {
        return result;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.concurrentchaining;

import static de.bundesbank.jdemetra.kix.concurrentchaining.core.ConcurrentChainingMethods.unchain;
import de.bundesbank.kix.parser.AbstractParser;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import java.util.regex.Pattern;

/**
 *
 * @author Deutsche Bundesbank
 */
//@ServiceProvider(service = IParser.class)
public final class UncCc extends AbstractParser {

    private static final String[] VALID_CONTROL_CHARACTER = {"unc.cc"};
    private static final Pattern SYNTAX = Pattern.compile("unc\\.cc,[iw]\\d+");

    @Override
    public TsData compute(final String formula, final TsVariables indices, final TsVariables weights) {
        String lowerCaseFormula = formula.toLowerCase().replaceAll("\\s*", "");
        if (!SYNTAX.matcher(lowerCaseFormula).matches()) {
            setErrorMessage("Formula doesn't match required syntax");
            return null;
        }

        TsData timeSeries;
        String[] temp = lowerCaseFormula.split(",");
        if (indices.contains(temp[1])) {
            timeSeries = extractData(indices.get(temp[1]));
        } else if (weights.contains(temp[1])) {
            timeSeries = extractData(weights.get(temp[1]));
        } else {
            setErrorMessage(temp[1] + " doesn't exist");
            return null;
        }
        return unchain(timeSeries);
    }

    @Override
    public String[] getValidControlCharacter() {
        return VALID_CONTROL_CHARACTER.clone();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.concurrentchaining;

import static de.bundesbank.jdemetra.kix.concurrentchaining.core.ConcurrentChainingMethods.chain;
import de.bundesbank.kix.parser.AbstractParser;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.util.regex.Pattern;

/**
 *
 * @author Deutsche Bundesbank
 */
//@ServiceProvider(service = IParser.class)
public final class ChaCc extends AbstractParser {

    private static final String[] VALID_CONTROL_CHARACTER = {"cha.cc"};
    private static final Pattern SYNTAX = Pattern.compile("cha\\.cc,[iw]\\d+(,\\d{4})?");

    @Override
    public TsData compute(final String formula, final TsVariables indices, final TsVariables weights) {
        String lowerCaseFormula = formula.toLowerCase().replaceAll("\\s*", "");
        if (!SYNTAX.matcher(lowerCaseFormula).matches()) {
            setErrorMessage("Formula doesn't match required syntax");
            return null;
        }

        String[] splitFormula = lowerCaseFormula.split(",");
        TsData timeSeries;
        if (indices.contains(splitFormula[1])) {
            timeSeries = extractData(indices.get(splitFormula[1]));
        } else if (weights.contains(splitFormula[1])) {
            timeSeries = extractData(weights.get(splitFormula[1]));
        } else {
            setErrorMessage(splitFormula[1] + " doesn't exist");
            return null;
        }
        if (splitFormula.length > 2) {
            return chain(timeSeries).index(TsPeriod.year(Integer.parseInt(splitFormula[2])), 100);
        }
        return chain(timeSeries);
    }

    @Override
    public String[] getValidControlCharacter() {
        return VALID_CONTROL_CHARACTER.clone();
    }

}

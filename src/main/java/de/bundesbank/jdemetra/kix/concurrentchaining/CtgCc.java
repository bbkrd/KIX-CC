package de.bundesbank.jdemetra.kix.concurrentchaining;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static de.bundesbank.jdemetra.kix.concurrentchaining.CliCc.REPLACEMENT;
import static de.bundesbank.jdemetra.kix.concurrentchaining.CliCc.REPLACEMENT_REGEX;
import de.bundesbank.jdemetra.kix.concurrentchaining.core.CtgCcCalc;
import de.bundesbank.kix.parser.AbstractParser;
import de.bundesbank.kix.parser.ICalc;
import de.bundesbank.kix.parser.IParser;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import java.util.regex.Pattern;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Deutsche Bundesbank
 */
@ServiceProvider(service = IParser.class)
public final class CtgCc extends AbstractParser {

    private static final int CONTRIBUTOR_POSITION = 1;
    private static final int CONTRIBUTOR_WEIGHTS_POSITION = 2;
    private static final int TOTAL_POSITION = 3;
    private static final int TOTAL_WEIGHTS_POSITION = 4;

    private static final String[] VALID_CONTROL_CHARACTER = {"wbgcc", "ctg.cc"};
    private static final Pattern SYNTAX = Pattern.compile("(wbgcc|ctg\\.cc),i\\d*(,w\\d*)?,i\\d*(,w\\d*)?,\\d{1,3}");

    @Override
    public TsData compute(final String formula, final TsVariables indices, final TsVariables weights) {
        String lowerCaseFormula = formula.toLowerCase().replaceAll("\\s*", "");
        if (!SYNTAX.matcher(lowerCaseFormula).matches()) {
            setErrorMessage("Formula doesn't match required syntax");
            return null;
        }
        lowerCaseFormula = addMissingWeights(lowerCaseFormula);
        String[] splitFormula = lowerCaseFormula.split(",");

        if (!isDataAvailable(splitFormula, indices, weights)) {
            return null;
        }

        int lag = Integer.parseInt(splitFormula[splitFormula.length - 1]);
        TsData contributorData = extractData(indices.get(splitFormula[CONTRIBUTOR_POSITION]));
        TsData contributorWeights = extractData(weights.get(splitFormula[CONTRIBUTOR_WEIGHTS_POSITION]));
        TsData totalData = extractData(indices.get(splitFormula[TOTAL_POSITION]));
        TsData totalWeights = extractData(weights.get(splitFormula[TOTAL_WEIGHTS_POSITION]));

        ICalc calc = new CtgCcCalc(totalData, totalWeights, lag);
        calc.plus(contributorData, contributorWeights);
        return calc.getResult();
    }

    @Override
    public String[] getValidControlCharacter() {
        return VALID_CONTROL_CHARACTER.clone();
    }

    private String addMissingWeights(final String input) {
        return REPLACEMENT_REGEX.matcher(input).replaceAll(REPLACEMENT);
    }

}

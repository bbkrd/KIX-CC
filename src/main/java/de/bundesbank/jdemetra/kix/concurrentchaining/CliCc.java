/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.concurrentchaining;

import de.bundesbank.jdemetra.kix.concurrentchaining.core.CliCcCalc;
import de.bundesbank.kix.parser.AbstractParser;
import de.bundesbank.kix.parser.ICalc;
import de.bundesbank.kix.parser.IParser;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.util.Locale;
import java.util.regex.Pattern;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Deutsche Bundesbank
 */
@ServiceProvider(service = IParser.class)
public final class CliCc extends AbstractParser {

    private static final String[] VALID_CONTROL_CHARACTER = {"kixcc", "cli.cc"};
    private static final Pattern SYNTAX = Pattern.compile("(kixcc|cli\\.cc)(,i\\d*(,w\\d*)?,[+-])+,i\\d*(,w\\d*)?,((q[1-4]\\/)|(((i){1,3}|iv)-)|((0[1-9]|1[0-2])\\/))?\\d{4}");
    static final Pattern REPLACEMENT_REGEX = Pattern.compile("i((?:\\d)+)(?=,(((([+-]){1}|i(\\d)+),)|(((q[1-4]\\/)|(((i){1,3}|iv)-)|((0[1-9]|1[0-2])\\/))?\\d+)))");
    static final String REPLACEMENT = "i$1,w$1";

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
        TsPeriod refYear = parsePeriod(splitFormula[splitFormula.length - 1]);
        if (refYear == null) {
            return null;
        }

        ICalc calculator = null;
        for (int i = 1; i < splitFormula.length; i += 3) {
            TsData addData = extractData(indices.get(splitFormula[i]));
            TsData addWeights = extractData(weights.get(splitFormula[i + 1]));

            if (calculator == null) {
                calculator = new CliCcCalc(addData, addWeights, refYear);
            } else if (splitFormula[i - 1].equals("+")) {
                calculator.plus(addData, addWeights);
            } else {
                calculator.minus(addData, addWeights);
            }
        }

        return calculator.getResult();

    }

    @Override
    public String[] getValidControlCharacter() {
        return VALID_CONTROL_CHARACTER.clone();
    }

    private String addMissingWeights(final String input) {
        return REPLACEMENT_REGEX.matcher(input).replaceAll(REPLACEMENT);
    }

    private TsPeriod parsePeriod(final String input) {
        String string = input.toUpperCase(Locale.ENGLISH);
        if (string.matches("\\d{4}")) {
            int year = Integer.parseInt(string);
            return TsPeriod.year(year);
        } else if (string.matches("Q[1-4]\\/\\d{4}")) {
            int quarter = Integer.parseInt(string.substring(1, 2));
            int year = Integer.parseInt(string.substring(string.length() - 4));
            return new TsPeriod(TsFrequency.Quarterly, year, quarter - 1);

        } else if (string.matches("((I){1,3}|IV)-\\d{4}")) {
            String quarterAsText = string.substring(0, string.indexOf('-'));
            int quarter;
            switch (quarterAsText) {
                case "I":
                    quarter = 0;
                    break;
                case "II":
                    quarter = 1;
                    break;
                case "III":
                    quarter = 2;
                    break;
                case "IV":
                    quarter = 3;
                    break;
                default:
                    throw new IllegalArgumentException("No valid quarter");
            }
            int year = Integer.parseInt(string.substring(string.length() - 4));
            return new TsPeriod(TsFrequency.Quarterly, year, quarter);

        } else if (string.matches("(0[1-9]|1[0-2])\\/\\d{4}")) {
            int month = Integer.parseInt(string.substring(0, 2));
            int year = Integer.parseInt(string.substring(string.length() - 4));
            return new TsPeriod(TsFrequency.Monthly, year, month - 1);
        }

        setErrorMessage("The reference period (" + string + ") isn't following the needed format");
        return null;

    }

}

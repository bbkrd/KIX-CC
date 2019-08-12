/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.concurrentchaining;

import ec.tstoolkit.timeseries.regression.TsVariable;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.utilities.DefaultNameValidator;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Deutsche Bundesbank
 */
public class CliCcTest {

    public CliCcTest() {
    }

    private final String VALIDATOR = ",= +-";

    private final double[] index1Data = {103.0, 105.9, 109.7, 111.6, 113.6, 103.4, 106.9, 112.0,
                                         110.8, 105.6, 105.6, 105.6, 100.0, 103.0, 106.2, 109.2,
                                         112.6, 121.1, 124.8, 128.4, 132.4};

    private final double[] index2Data = {114.0, 113.6, 113.8, 111.8, 110.2, 108.5, 106.8, 105.1,
                                         103.4, 103.1, 102.2, 101.0, 100.0, 97.6, 96.8, 95.8,
                                         94.6, 93.9, 92.9, 91.9, 90.8};

    private final double[] weight1Data = {455.0, 429.3, 410.7, 397.8, 280.4, 375.8, 250.4, 70.3,
                                          20.0, 0.0, 0.0, 35.6, 75.0, 159.5, 395.6, 530.7,
                                          806.3, 920.7, 925.5, 915.3, 892.1};

    private final double[] weight2Data = {176.0, 190.3, 186.4, 184.3, 198.2, 195.0, 200.7, 207.7,
                                          220.5, 205.0, 210.0, 205.0, 209.0, 226.0, 215.8, 218.4,
                                          220.5, 223.9, 217.3, 225.6, 224.7};

    private final TsVariables indices = new TsVariables("i", new DefaultNameValidator(VALIDATOR));
    private final TsVariables weights = new TsVariables("w", new DefaultNameValidator(VALIDATOR));
    CliCc instance = new CliCc();

    @After
    public void clear() {
        indices.clear();
        weights.clear();
    }

    private void quarterlyData() {
        indices.set("i1", new TsVariable(new TsData(TsFrequency.Quarterly, 2012, 3, index1Data, false)));
        indices.set("i2", new TsVariable(new TsData(TsFrequency.Quarterly, 2012, 3, index2Data, false)));
        weights.set("w1", new TsVariable(new TsData(TsFrequency.Quarterly, 2012, 3, weight1Data, false)));
        weights.set("w2", new TsVariable(new TsData(TsFrequency.Quarterly, 2012, 3, weight2Data, false)));
    }

    @Test
    public void testCompute1() {
        System.out.println("compute");
        quarterlyData();
        String formula = "KIXCC,i1,+,i2,Q4/2015";

        double[] expResultData = {102.5281568406620, 104.5093608577150, 107.1641900536850,
                                  107.8529028329020, 108.6850924171440,
                                  102.2733776855380, 104.0051453692590,
                                  106.0228716912100, 104.4543523151970,
                                  103.7688291685810, 102.8629906986330,
                                  101.6552060720340, 100.0000000000000,
                                  99.0260563380282, 99.8231148279489,
                                  101.2836935983110, 103.1479202350980,
                                  109.0983873612180, 111.5523801624800,
                                  113.9300468251070, 116.5077969931750};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2012, 3, expResultData, false);

        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);
    }

    @Test
    public void testCompute2() {
        System.out.println("compute");
        quarterlyData();
        String formula = "KIXCC,i1,+,i2,IV-2015";

        double[] expResultData = {102.5281568406620, 104.5093608577150, 107.1641900536850,
                                  107.8529028329020, 108.6850924171440,
                                  102.2733776855380, 104.0051453692590,
                                  106.0228716912100, 104.4543523151970,
                                  103.7688291685810, 102.8629906986330,
                                  101.6552060720340, 100.0000000000000,
                                  99.0260563380282, 99.8231148279489,
                                  101.2836935983110, 103.1479202350980,
                                  109.0983873612180, 111.5523801624800,
                                  113.9300468251070, 116.5077969931750};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2012, 3, expResultData, false);

        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);
    }

    @Test
    public void testComputeWrongSyntax() {
        System.out.println("compute");
        String formula = "KIXCC,i1,+,i2,V-2015";
        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);

        String expErrorMessage = "Formula doesn't match required syntax";
        String resultErrorMessage = instance.getErrorMessage();
        assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testComputeDataNotAvailable() {
        System.out.println("compute");
        String formula = "KIXCC,i1,+,i2,IV-2015";
        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);

        String expErrorMessage = "The following data is not available: i1 i2 w1 w2";
        String resultErrorMessage = instance.getErrorMessage();
        assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testComputeDataNotAvailable2() {
        System.out.println("compute");
        String formula = "KIXCC,i1,+,i2,w1,IV-2015";
        TsData expResult = null;
        TsData result = instance.compute(formula, indices, weights);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);

        String expErrorMessage = "The following data is not available: i1 i2 w1";
        String resultErrorMessage = instance.getErrorMessage();
        assertEquals(expErrorMessage, resultErrorMessage);
    }

    @Test
    public void testGetValidControlCharacter() {
        System.out.println("getValidControlCharacter");
        String[] expResult = {"kixcc", "cli.cc"};
        String[] result = instance.getValidControlCharacter();
        assertArrayEquals(expResult, result);
    }

}

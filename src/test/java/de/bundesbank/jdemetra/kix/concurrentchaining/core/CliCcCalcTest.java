package de.bundesbank.jdemetra.kix.concurrentchaining.core;

import de.bundesbank.jdemetra.kix.concurrentchaining.TsDataAsserter;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import org.junit.Test;

/**
 *
 * @author Thomas Witthohn
 */
public class CliCcCalcTest {

    public CliCcCalcTest() {
    }

    @Test
    public void testGetResult() {
        double[] index1Data = {103.0, 105.9, 109.7, 111.6, 113.6, 103.4, 106.9, 112.0,
                               110.8, 105.6, 105.6, 105.6, 100.0, 103.0, 106.2, 109.2,
                               112.6, 121.1, 124.8, 128.4, 132.4};

        double[] index2Data = {114.0, 113.6, 113.8, 111.8, 110.2, 108.5, 106.8, 105.1,
                               103.4, 103.1, 102.2, 101.0, 100.0, 97.6, 96.8, 95.8,
                               94.6, 93.9, 92.9, 91.9, 90.8};

        double[] weight1Data = {455.0, 429.3, 410.7, 397.8, 280.4, 375.8, 250.4, 70.3,
                                20.0, 0.0, 0.0, 35.6, 75.0, 159.5, 395.6, 530.7,
                                806.3, 920.7, 925.5, 915.3, 892.1};

        double[] weight2Data = {176.0, 190.3, 186.4, 184.3, 198.2, 195.0, 200.7, 207.7,
                                220.5, 205.0, 210.0, 205.0, 209.0, 226.0, 215.8, 218.4,
                                220.5, 223.9, 217.3, 225.6, 224.7};

        TsData index1 = new TsData(TsFrequency.Quarterly, 2012, 3, index1Data, false);
        TsData index2 = new TsData(TsFrequency.Quarterly, 2012, 3, index2Data, false);
        TsData weight1 = new TsData(TsFrequency.Quarterly, 2012, 3, weight1Data, false);
        TsData weight2 = new TsData(TsFrequency.Quarterly, 2012, 3, weight2Data, false);

        CliCcCalc instance = new CliCcCalc(index1, weight1, new TsPeriod(TsFrequency.Quarterly, 2015, 3));

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

        instance.plus(index2, weight2);

        TsData result = instance.getResult();

        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);
    }

    @Test
    public void testMinus() {

        double[] index1Data = {100.00, 100.18, 99.59, 99.25, 98.88, 98.86, 99.35,
                               101.02, 101.54, 104.90, 104.35, 103.67, 104.21,
                               102.69, 102.75, 102.27, 103.23, 103.02, 101.49,
                               100.81, 100.51, 100.26};

        double[] index2Data = {100.00, 100.17, 99.59, 99.27, 98.89, 98.87, 99.36, 100.98,
                               101.48, 104.78, 104.22, 103.57, 104.13, 102.67, 102.79, 102.35,
                               103.37, 103.15, 101.64, 100.94, 100.61, 100.31};

        double[] weight1Data = {200.2203, 205.7359, 212.2831, 215.2142, 218.3956, 219.4641, 217.7633, 217.113,
                                214.5442, 210.2688, 221.0795, 219.5461, 225.4744, 220.1602, 215.0061, 217.2407,
                                224.7735, 225.453, 225.8753, 226.9487, 229.6081};

        double[] weight2Data = {14.1296, 14.7846, 15.0229, 14.9123, 13.9532, 13.5497, 13.3217, 13.2743,
                                13.2579, 12.9899, 13.1484, 12.7088, 12.4069, 11.8861, 11.4773, 11.5769,
                                11.7483, 11.6469, 11.4709, 11.1488, 11.489};

        TsData index1 = new TsData(TsFrequency.Quarterly, 2011, 3, index1Data, false);
        TsData index2 = new TsData(TsFrequency.Quarterly, 2011, 3, index2Data, false);
        TsData weight1 = new TsData(TsFrequency.Quarterly, 2011, 3, weight1Data, false);
        TsData weight2 = new TsData(TsFrequency.Quarterly, 2011, 3, weight2Data, false);

        CliCcCalc instance = new CliCcCalc(index1, weight1, new TsPeriod(TsFrequency.Quarterly, 2012, 3));

        double[] expResultData = {101.133497729112, 101.316305917170, 100.718835249683, 100.373440988674,
                                  100.000000000000, 99.979773323188, 100.475326782425, 102.167548411183,
                                  102.694759593984, 106.096837982243, 105.541271532017, 104.851641740086,
                                  105.396520240782, 103.855746221555, 103.912962955966, 103.425239179981,
                                  104.392713123532, 104.180888917492, 102.632441398206, 101.945813027066,
                                  101.643978104731, 101.393805027425};

        TsData expResult = new TsData(TsFrequency.Quarterly, 2011, 3, expResultData, false);

        instance.minus(index2, weight2);

        TsData result = instance.getResult();

        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);
    }

    @Test
    public void testPlusWithMissingValue() {
        double[] index1Data = {100.00, 100.18, 99.59, 99.25, 98.88, 98.86, 99.35,
                               101.02, 101.54, 104.90, 104.35, 103.67, 104.21,
                               102.69, Double.NaN, 102.27, 103.23, 103.02, 101.49,
                               100.81, 100.51, 100.26};

        double[] index2Data = {100.00, 100.17, 99.59, 99.27, 98.89, 98.87, 99.36, 100.98,
                               101.48, 104.78, 104.22, 103.57, 104.13, 102.67, 102.79, 102.35,
                               103.37, 103.15, 101.64, 100.94, 100.61, 100.31};

        double[] weight1Data = {200.2203, 205.7359, 212.2831, 215.2142, 218.3956, 219.4641, 217.7633, 217.113,
                                214.5442, 210.2688, 221.0795, 219.5461, 225.4744, 220.1602, 215.0061, 217.2407,
                                224.7735, 225.453, 225.8753, 226.9487, 229.6081};

        double[] weight2Data = {14.1296, 14.7846, 15.0229, 14.9123, 13.9532, 13.5497, 13.3217, 13.2743,
                                13.2579, 12.9899, 13.1484, 12.7088, 12.4069, 11.8861, 11.4773, 11.5769,
                                11.7483, 11.6469, 11.4709, 11.1488, 11.489};

        TsData index1 = new TsData(TsFrequency.Quarterly, 2011, 3, index1Data, false);
        TsData index2 = new TsData(TsFrequency.Quarterly, 2011, 3, index2Data, false);
        TsData weight1 = new TsData(TsFrequency.Quarterly, 2011, 3, weight1Data, false);
        TsData weight2 = new TsData(TsFrequency.Quarterly, 2011, 3, weight2Data, false);

        CliCcCalc instance = new CliCcCalc(index1, weight1, new TsPeriod(TsFrequency.Quarterly, 2012, 3));
        double[] expResultData = {101.131985468081000, 101.313356396172000,
                                  100.717355708081000, 100.374843703302000,
                                  100.000000000000000, 99.979773585612600,
                                  100.475320833486000, 102.161312065549000,
                                  102.686033139300000, 106.080539314600000,
                                  105.523723527295000, 104.837731890544000,
                                  105.384951044771000, 103.850918760509000,
                                  5.325769655740800, 0.268734062415365,
                                  0.271264517554065, 0.270711419868510,
                                  0.266693780515862, 0.264904478706187,
                                  0.264112511713293, 0.263449357173796
        };
        TsData expResult = new TsData(TsFrequency.Quarterly, 2011, 3, expResultData, false);

        instance.plus(index2, weight2);

        TsData result = instance.getResult();

        TsDataAsserter.assertTsDataEquals(expResult, result,
                                          0.000000000005);
    }

}

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

import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author s4504tw
 */
public class CtgCcCalcTest {

    private final double[] aValues = {103.0, 105.9, 109.7, 111.6, 113.6, 103.4, 106.9, 112.0,
                                      110.8, 105.6, 105.6, 105.6, 100.0, 103.0, 106.2, 109.2,
                                      112.6, 121.1, 124.8, 128.4, 132.4};
    private final TsData a = new TsData(TsFrequency.Quarterly, 2012, 3, aValues, false);

    private final double[] aWeightsValues = {455.0, 429.3, 410.7, 397.8, 280.4, 375.8, 250.4, 70.3,
                                             20.0, 0.0, 0.0, 35.6, 75.0, 159.5, 395.6, 530.7,
                                             806.3, 920.7, 925.5, 915.3, 892.1};

    private final TsData aWeights = new TsData(TsFrequency.Quarterly, 2012, 3, aWeightsValues, false);

    private final double[] totalValues = {102.5281568406620, 104.5093608577150,
                                          107.1641900536850, 107.8529028329020,
                                          108.6850924171440, 102.2733776855380,
                                          104.0051453692590, 106.0228716912100,
                                          104.4543523151970, 103.7688291685810,
                                          102.8629906986330, 101.6552060720340,
                                          100.0000000000000, 99.0260563380281,
                                          99.8231148279489, 101.2836935983110,
                                          103.1479202350980, 109.0983873612180,
                                          111.5523801624800, 113.9300468251070,
                                          116.5077969931750};
    private final TsData total = new TsData(TsFrequency.Quarterly, 2012, 3, totalValues, false);

    private final double[] totalWeightsValues = {631.0, 619.6, 597.1, 582.1,
                                                 478.6, 570.8, 451.1, 278.0,
                                                 240.5, 205.0, 210.0, 240.6,
                                                 284.0, 385.5, 611.4, 749.1,
                                                 1026.8, 1144.6, 1142.8, 1140.9,
                                                 1116.8};

    private final TsData totalWeights = new TsData(TsFrequency.Quarterly, 2012, 3, totalWeightsValues, false);

    public CtgCcCalcTest() {
    }

    @Test
    public void testGetResultLag1() {
        CtgCcCalc instance = new CtgCcCalc(total, totalWeights, 1);
        double[] expResult = {2.030218638930360, 2.486206032586130,
                              1.191309500018850, 1.224709200835680,
                              -5.260501580313470, 2.228539401078130,
                              2.648219434086790, -0.270940390544710,
                              -0.390281978729632, 0.000000000000000,
                              0.000000000000000, -0.784654525302904,
                              0.792253521126761, 1.285431856245200,
                              1.827795427369630, 2.205797139717840,
                              5.927769870490400, 2.457661026472210,
                              2.336114401873950, 2.499256612462860};

        instance.plus(a, aWeights);
        TsData result = instance.getResult();
        Assert.assertArrayEquals(expResult, result.internalStorage(), 0.000000000005);
    }

    @Test
    public void testGetResultLag4() {
        CtgCcCalc instance = new CtgCcCalc(total, totalWeights, 4);
        double[] expResult = {7.09795787804229000, -0.49901880992896600,
                              -0.78443823829721900, 0.59061035365493600,
                              -0.89354738036556800, 4.24212223466659000,
                              1.98005509159720000, -0.65544847505716600,
                              -1.15390949349635000, -0.00519294046836816,
                              1.23224476754956000, 3.04173909865569000,
                              6.12384113995441000, 11.55853255176620000,
                              12.87709269666690000, 13.46292126104720000,
                              13.81417608751630000};

        instance.plus(a, aWeights);
        TsData result = instance.getResult();
        Assert.assertArrayEquals(expResult, result.internalStorage(), 0.000000000005);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMinus() {
        CtgCcCalc instance = new CtgCcCalc(total, totalWeights, 4);
        instance.minus(a, aWeights);
    }

}

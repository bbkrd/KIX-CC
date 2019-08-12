/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.jdemetra.kix.concurrentchaining.core;

import de.bundesbank.jdemetra.kix.concurrentchaining.TsDataAsserter;
import static de.bundesbank.jdemetra.kix.concurrentchaining.core.ConcurrentChainingMethods.chain;
import static de.bundesbank.jdemetra.kix.concurrentchaining.core.ConcurrentChainingMethods.unchain;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import net.trajano.commons.testing.UtilityClassTestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Deutsche Bundesbank
 */
public class ConcurrentChainingMethodsTest {

    public ConcurrentChainingMethodsTest() {
    }

    @Test
    public void testUnchain() {
        double[] aValues = {103.0, 105.9, 109.7, 111.6, 113.6, 103.4, 106.9, 112.0,
                            110.8, 105.6, 105.6, 105.6, 100.0, 103.0, 106.2, 109.2,
                            112.6, 121.1, 124.8, 128.4, 132.4};
        TsData a = new TsData(TsFrequency.Quarterly, 2012, 3, aValues, false);

        double[] expResultData = {102.8155339805830, 103.5882908404150,
                                  101.7319963536920, 101.7921146953410,
                                  91.0211267605634, 103.3849129593810,
                                  104.7708138447150, 98.9285714285714,
                                  95.3068592057762, 100.0000000000000,
                                  100.0000000000000, 94.6969696969697,
                                  103.0000000000000, 103.1067961165050,
                                  102.8248587570620, 103.1135531135530,
                                  107.5488454706930, 103.0553261767130,
                                  102.8846153846150, 103.1152647975080};

        TsData expResult = new TsData(TsFrequency.Quarterly, 2013, 0, expResultData, false);

        TsData result = unchain(a);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);
    }

    @Test
    public void testChain() {
        double[] values = {101.9323511492870, 102.5402788555800,
                           100.6426706336060, 100.7715968345620,
                           94.1006493264071, 101.6932731888900,
                           101.9400254812270, 98.5205839542040,
                           99.3437103084541, 99.1270611057226,
                           98.8258317025440, 98.3717449051638,
                           99.0260563380282, 100.8048977404490,
                           101.4631668956430, 101.8405989854400,
                           105.7688677702440, 102.2493392071300,
                           102.1314351689890, 102.2625727276540};

        TsData ts = new TsData(TsFrequency.Quarterly, 2013, 0, values, false);

        double[] expResultData = {100, 101.9323511492870, 104.5217171125280,
                                  105.1934474941500, 106.0051168051820,
                                  99.7515032328922, 101.4405686926490,
                                  103.4085415735890, 101.8786990168260,
                                  101.2100796172970, 100.3265774673890,
                                  99.1485746008440, 97.5341828834483,
                                  96.5842548909989, 97.3616593762463,
                                  98.7862229452884, 100.6044811625740,
                                  106.4082206517830, 108.8017024785130,
                                  111.1207402295980, 113.6349277928000};
        TsData expResult = new TsData(TsFrequency.Quarterly, 2012, 3, expResultData, false);

        TsData result = chain(ts);
        TsDataAsserter.assertTsDataEquals(expResult, result, 0.000000000005);
    }

    @Test
    public void testChain2() {
        double[] values = {101.9323511492870, 102.5402788555800,
                           100.6426706336060, 100.7715968345620,
                           94.1006493264071, 101.6932731888900,
                           101.9400254812270, 98.5205839542040,
                           99.3437103084541, 0,
                           98.8258317025440, 98.3717449051638,
                           99.0260563380282, 100.8048977404490,
                           101.4631668956430, 101.8405989854400,
                           105.7688677702440, 102.2493392071300,
                           102.1314351689890, 102.2625727276540};

        TsData ts = new TsData(TsFrequency.Quarterly, 2013, 0, values, false);

        double[] expResult = {100, 101.9323511492870, 104.5217171125280,
                              105.1934474941500, 106.0051168051820,
                              99.7515032328922, 101.4405686926490,
                              103.4085415735890, 101.8786990168260,
                              101.2100796172970, 101.2100796172970,
                              100.0217029486010, 98.3930944743986,
                              97.4348011669473, 98.2190516799510,
                              99.6561603293465, 101.4904306052970,
                              107.3452793463680, 109.7598388017090,
                              112.0992986073540, 114.6356267655350};

        TsData result = chain(ts);
        Assert.assertArrayEquals(expResult, result.internalStorage(), 0.000000000005);
    }

    @Test
    public void testConcurrentChainingMethodsWellDefined() throws ReflectiveOperationException {
        UtilityClassTestUtil.assertUtilityClassWellDefined(ConcurrentChainingMethods.class);
    }
}

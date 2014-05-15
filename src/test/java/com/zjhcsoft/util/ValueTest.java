package com.zjhcsoft.util;

import com.zjhcsoft.test.BaseTest2;
import org.junit.Test;

public class ValueTest extends BaseTest2 {
    @Test
    public void test(){
        float P1010205 = Value.SummaryValue("1010205","201404","20285140");
        float P1010206 = Value.SummaryValue("1010206","201404","20285140");
        float P1010207 = Value.SummaryValue("1010207","201404","20285140");
        float P1010106 = Value.SummaryValue("1010106", "201404", "20285140");
        float P1010208 = Value.SummaryValue("1010208", "201404", "20285140");
        System.out.println(P1010205);
        System.out.println(P1010206);
        System.out.println(P1010207);
        System.out.println(P1010106);
        System.out.println(P1010208);
        System.out.println(P1010205*P1010206*P1010207*P1010106);
    }
}
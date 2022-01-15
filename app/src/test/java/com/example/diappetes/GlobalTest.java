package com.example.diappetes;

import junit.framework.TestCase;

import org.junit.Test;


public class GlobalTest extends TestCase {
    @Test
    public void testGettingAndSetting(){
        Global globTest = new Global();
        String nhs = "0123456789";
        String uid = "128uhwfnw93eujdkdnmewd";
        globTest.setNhsNum(nhs);
        globTest.setUID(uid);
        assertEquals(nhs, globTest.getNhsNum());
        assertEquals(uid, globTest.getUID());
    }

}
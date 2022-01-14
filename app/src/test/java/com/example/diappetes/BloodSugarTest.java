package com.example.diappetes;

import junit.framework.TestCase;

import org.junit.Test;

public class BloodSugarTest extends TestCase {

    @Test
    public void testIsReasonable(){
        String alphabetical = "High";
        String tooHigh = "300";
        String typo = "4%2";
        String reasonableValue = "6.7";
        assertEquals(false, BloodSugar.isReasonable(alphabetical));
        assertEquals(false, BloodSugar.isReasonable(tooHigh));
        assertEquals(false, BloodSugar.isReasonable(typo));
        assertEquals(true, BloodSugar.isReasonable(reasonableValue));

    }

    @Test
    public void testCheckHyperglucemia(){
        BloodSugar page = new BloodSugar();
        assertEquals(true, page.checkHyperglucemia(11.3,true));
        assertEquals(true, page.checkHyperglucemia(11.3,false));
        assertEquals(true, page.checkHyperglucemia(8.3,false));
        assertEquals(false, page.checkHyperglucemia(8.3,true));
        assertEquals(false, page.checkHyperglucemia(5.3,false));
        assertEquals(false, page.checkHyperglucemia(5.3,true));



    }
}
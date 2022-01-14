package com.example.diappetes;

import junit.framework.TestCase;

import org.junit.Assert;

public class SignUp1Test extends TestCase {

    public void testIsNumeric() {

        String testString = "0709947227";
        boolean expected = true;
        boolean result = SignUp1.isNumeric(testString);
        assertEquals(expected, result);

    }

    public void testValidateEmail() {
        SignUp1 page = new SignUp1();
        String invalidTestEmail = "bert@gmail";
        String validTestEmail = "bert@gmail.com";
        assertEquals(false, page.validateEmail(invalidTestEmail));
        assertEquals(true, page.validateEmail(validTestEmail));

    }

    public void testValidatePassword(){
        SignUp1 page = new SignUp1();
        String badTestPassword = "PASSWORD";
        String goodTestPassword = "Abcd1234!";
        assertEquals(false, page.validatePassword(badTestPassword));
        assertEquals(true, page.validatePassword(goodTestPassword));

    }
}
package com.outsideoftime.algorithm;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Auther: wangboxue
 * @Date: 2018/12/14 16:02
 * @Description:
 */
public class StringAlgorithmTest {

    @Test
    public void reverse() {
        assertEquals("hgfad",new StringAlgorithm().reverse("dafgh"));
    }
}
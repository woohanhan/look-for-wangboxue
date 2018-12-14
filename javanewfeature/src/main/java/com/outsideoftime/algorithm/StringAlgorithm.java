package com.outsideoftime.algorithm;

/**
 * @Auther: wangboxue
 * @Date: 2018/12/14 15:53
 * @Description:
 */
public class StringAlgorithm {
    public static String reverse(String str) {
        char[] result = str.toCharArray();
        for (int i = 0;i<result.length/2;i++){
            char temp = result[i];
            result[i] = result[result.length-1-i];
            result[result.length - 1 - i] = temp;
        }
        return String.valueOf(result);
    }
}

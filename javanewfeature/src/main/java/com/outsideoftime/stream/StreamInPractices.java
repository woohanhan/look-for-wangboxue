package com.outsideoftime.stream;

import com.outsideoftime.algorithm.StringAlgorithm;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Auther: wangboxue
 * @Date: 2018/12/14 14:20
 * @Description:
 */
public class StreamInPractices {

    public void mapOfStream() {
        String[] array = {"ab", "cd", "vd"};
        Stream<String> stream = Arrays.stream(array);
        stream.filter(t -> t.contains("d"))
                .map(t -> StringAlgorithm.reverse(t))
                .flatMap(t -> t.chars()
                        .mapToObj(d -> (char) d))
                .forEach(t -> System.out.println(t));
    }
}

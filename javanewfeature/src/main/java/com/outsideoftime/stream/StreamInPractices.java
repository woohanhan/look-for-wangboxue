package com.outsideoftime.stream;

import com.outsideoftime.algorithm.StringAlgorithm;

import java.util.*;
import java.util.stream.Collectors;
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

    public static void main(String[] args) {

        Set<String> set = new HashSet<>();

        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");

        List<String> s = new StreamInPractices().s(strings);
        System.out.println(s.size());
    }

    private List<String> s(List<String> list){
        Set<String> set = new HashSet<>();
        set.add("2");
        if (set.contains("2")) System.out.println("0000000");
        return list.stream()
                .filter(t->!set.contains(t))
                .collect(Collectors.toList());

    }
}

package com.zjhcsoft.qin.exchange.utils;

import java.util.ArrayList;
import java.util.List;

public class LangUtils {

    public static <E> List<E> toList(Iterable<E> iterable) {
        if(iterable instanceof List) {
            return (List<E>) iterable;
        }
        List<E> list = new ArrayList<>();
        if(iterable != null) {
            for(E e: iterable) {
                list.add(e);
            }
        }
        return list;
    }

}

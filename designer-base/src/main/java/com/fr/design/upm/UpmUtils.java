package com.fr.design.upm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-18
 */
public class UpmUtils {

    public static String[] findMatchedExtension(String... extensions) {
        List<String> list = new ArrayList<>();
        for (String ext : extensions) {
            String[] arr = ext.split("\\.");
            list.add(arr[arr.length - 1]);
        }
        return list.toArray(new String[0]);
    }
}

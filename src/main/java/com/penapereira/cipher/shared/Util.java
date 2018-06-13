package com.penapereira.cipher.shared;

import java.util.Iterator;
import java.util.List;

public class Util {

    public String listToString(String lineSeparator, List<String> lines) {
        StringBuilder text = new StringBuilder();
        Iterator<String> i = lines.iterator();
        while (i.hasNext()) {
            text.append(i.next());
            text.append(lineSeparator);
        }
        return text.toString();
    }

    public String listToString(List<String> lines) {
        return listToString(System.getProperty("line.separator"), lines);
    }
}

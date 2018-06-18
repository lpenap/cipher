package com.penapereira.cipher.shared;

import java.util.Iterator;
import java.util.List;

public class StringUtil {

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

    /**
     * Trim, removes excess inner spaces and everything that is not a letter or number.
     * 
     * @param dirty String to sanitize
     * @return The sanitized String
     */
    public String sanitizeString(String dirty) {
        return dirty.trim().replaceAll("  +", " ").replaceAll("[^\\p{L}\\p{Z}\\d]", "");
    }
}

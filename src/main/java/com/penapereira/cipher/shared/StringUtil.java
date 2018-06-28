package com.penapereira.cipher.shared;

import java.util.Iterator;
import java.util.List;

public class StringUtil {

    /**
     * Implodes a list of Strings into a single one.
     * 
     * @param lineSeparator String to append between each element.
     * @param lines List of Strings to be imploded.
     * @return The imploded String.
     */
    public String listToString(String lineSeparator, List<String> lines) {
        StringBuilder text = new StringBuilder();
        Iterator<String> i = lines.iterator();
        while (i.hasNext()) {
            text.append(i.next());
            text.append(lineSeparator);
        }
        return text.toString();
    }

    /**
     * Implodes a list of Strings with the system default line separator.
     * 
     * @param lines List of Strings to be imploded.
     * @return The imploded String.
     */
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

    /**
     * Pads the given string to the left with the required padding character. No change is made if the required size is
     * bigger than the given string.
     * 
     * @param str String to be padded.
     * @param size The required size of the String.
     * @param pad Character to be added.
     * @return The padded string.
     */
    public String padLeft(String str, int size, char pad) {
        if (size > str.length()) {
            StringBuilder result = new StringBuilder(size);
            int paddingSize = size - str.length();
            for (int i = 0; i < paddingSize; i++) {
                result.append(pad);
            }
            result.append(str);
            str = result.toString();
        }
        return str;
    }
}

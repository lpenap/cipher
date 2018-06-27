package com.penapereira.cipher.view.swing.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

public class SearchMonitor {

    private final Logger log = LoggerFactory.getLogger(getClass());
    protected BufferedReader reader;
    protected List<Pair<Integer, Integer>> searchResults;
    protected Integer currentIndex;
    protected Integer matches;

    public SearchMonitor() {
        clearSearch();
    }

    public synchronized void clearSearch() {
        searchResults = new ArrayList<>();
        currentIndex = 0;
        matches = 0;
    }

    public synchronized Pair<Integer, Integer> getNext() {
        currentIndex = ++currentIndex % searchResults.size();
        return searchResults.get(currentIndex);
    }

    public synchronized Pair<Integer, Integer> getPrevious() {
        currentIndex = --currentIndex % searchResults.size();
        return searchResults.get(currentIndex);
    }

    public synchronized Pair<Integer, Integer> getCurrent() {
        return searchResults.get(currentIndex);
    }

    public synchronized int getCurrentIndex() {
        return currentIndex;
    }

    public synchronized boolean isSearchEmpty() {
        return searchResults.isEmpty();
    }

    public synchronized int getMatches() {
        return matches;
    }

    public synchronized int search(String text, String query) {
        clearSearch();
        reader = new BufferedReader(new StringReader(text));
        try {
            String line = reader.readLine();
            int currentIndex = 0;
            while (line != null) {
                log.trace("Searching line, current index {}", currentIndex);
                line = line.toLowerCase();
                processLine(currentIndex, line, query);
                currentIndex += line.length() + 1;
                line = reader.readLine();
            }
        } catch (IOException e) {
            log.error("IO Error while searching the document text. "
                    + "This does not means the document file is damaged, "
                    + "but that search operation could read the text from memory.");
        }
        return matches;
    }

    protected void processLine(int globalIndex, String line, String query) {
        int indexOf = line.indexOf(query);
        while (indexOf != -1) {
            matches++;
            int indexEnd = indexOf + query.length();
            log.trace("Match found. Total matches {}. Current line index {}", matches, indexOf);
            searchResults.add(Pair.of(globalIndex + indexOf, globalIndex + indexEnd));
            indexOf = line.indexOf(query, indexEnd);
        }
    }
}

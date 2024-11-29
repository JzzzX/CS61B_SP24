package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap map;
    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        String output = "";
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        for (String word: words) {
            TimeSeries ts = map.weightHistory(word, startYear, endYear);
            output += word + ": " + ts.toString() + "\n";
        }
        return output;
    }
}

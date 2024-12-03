package ngrams;

import edu.princeton.cs.algs4.In;
import net.sf.saxon.functions.Minimax;

import java.sql.Time;
import java.util.Collection;
import java.util.HashMap;

//import static ngrams.TimeSeries.MAX_YEAR;
//import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    /** Stores time series data for each word (word -> timeline mapping). */
    private HashMap<String, TimeSeries> wordHistories;

    /** Stores total word counts for each year. */
    private TimeSeries totalCounts;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     * @param wordsFilename  the file containing word count data (tab-separated)
     * @param countsFilename the file containing total word count data (comma-separated)
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // Initialize data structures
        wordHistories = new HashMap<>();
        totalCounts = new TimeSeries();

        // Process the counts file (total words per year)
        processCountsFile(countsFilename);

        // Process the words file (individual word frequencies)
        processWordsFile(wordsFilename);
    }

        /**
         * Processes the counts file containing yearly total word counts.
         * Format: year,totalCount,pages,sources
         */
        private void processCountsFile(String countsFilename) {
            In in = new In(countsFilename);
            int lineNumber = 0;

            while (!in.isEmpty()) {
                lineNumber += 1;
                String nextLine = in.readLine();

                // Skip header line
                if (lineNumber == 1) {
                    continue;
                }

                try {
                    // Split and validate line data
                    String[] parts = nextLine.split(",");
                    if (parts.length < 2) {
                        continue;
                    }

                    // Parse and validate data
                    int year = Integer.parseInt(parts[0]);
                    double count = Double.parseDouble(parts[1]);

                    if (year < TimeSeries.MIN_YEAR || year > TimeSeries.MAX_YEAR) {
                        continue;  // Skip years outside valid range
                    }

                    // Store valid data point
                    totalCounts.put(year, count);
                } catch (NumberFormatException e) {
                    continue;  // Skip lines with invalid number format
                }
            }
        }

        /**
         * Processes the words file containing individual word frequencies.
         * Format: word    year    count   sources
         */
        private void processWordsFile(String wordsFilename) {
            In wordsIn = new In(wordsFilename);
            int lineNumber = 0;

            while (!wordsIn.isEmpty()) {
                lineNumber += 1;
                String nextLine = wordsIn.readLine();

                // Skip header line
                if (lineNumber == 1) {
                    continue;
                }

                try {
                    // Split and validate line data
                    String[] parts = nextLine.split("\t");
                    if (parts.length < 3) {
                        continue;
                    }

                    // Parse and validate data
                    String word = parts[0];
                    int year = Integer.parseInt(parts[1]);
                    double count = Double.parseDouble(parts[2]);

                    if (year < TimeSeries.MIN_YEAR || year > TimeSeries.MAX_YEAR) {
                        continue;  // Skip years outside valid range
                    }

                    // Update or create time series for this word
                    if (wordHistories.containsKey(word)) {
                        wordHistories.get(word).put(year, count);
                    } else {
                        TimeSeries newTimeSeries = new TimeSeries();
                        newTimeSeries.put(year, count);
                        wordHistories.put(word, newTimeSeries);
                    }
                } catch (NumberFormatException e) {
                    continue;  // Skip lines with invalid number format
                }
            }
        }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // Check whether words exist
        if (!wordHistories.containsKey(word)) {
            return new TimeSeries();
        }
        // Get original TimeSeries
        TimeSeries originalTS = wordHistories.get(word);
        // The copy of returned TimeSeries
        return new TimeSeries(originalTS, startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!wordHistories.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries originalTS = wordHistories.get(word);
        return new TimeSeries(originalTS, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCounts, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!wordHistories.containsKey(word)) {
            return new TimeSeries();
        }
        // Get the count history for the word within specified years
        TimeSeries wordCounts = countHistory(word, startYear, endYear);
        // Get the total word counts for all years within the range
        TimeSeries totalCountHistory = new TimeSeries(totalCounts, startYear, endYear);
        // Calculate and return relative frequency (word count / total count)
        return wordCounts.dividedBy(totalCountHistory);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return weightHistory(word, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries summedWeights = new TimeSeries();
        for (String word : words) {
            TimeSeries currentWeight = weightHistory(word, startYear, endYear);
            summedWeights = summedWeights.plus(currentWeight);
        }
        return summedWeights;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }
}

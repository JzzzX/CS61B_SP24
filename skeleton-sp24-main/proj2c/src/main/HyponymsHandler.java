package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import ngrams.NGramMap;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wordnet;
    private NGramMap ngm;

    /**
     * HyponymsHandler 构造函数
     * @param synsetsFile 同义词集文件路径
     * @param hyponymsFile 下位词关系文件路径
     */
    public HyponymsHandler(String synsetsFile, String hyponymsFile, String wordsFile, String countsFile) {
        if (synsetsFile == null || hyponymsFile == null ||
            wordsFile == null || countsFile == null) {
            throw new IllegalArgumentException("File paths cannot be null");
        }
        wordnet = new WordNet(synsetsFile, hyponymsFile);
        ngm = new NGramMap(wordsFile, countsFile);
    }

    @Override
    public String handle (NgordnetQuery query) {
        // 从 query 中获取查询参数
        List<String> words = query.words();
        int k = query.k();
        int startYear = query.startYear();
        int endYear = query.endYear();

        // 参数有效性检查
        if (words == null || words.isEmpty()) {
            return "[]"; // 若没有输入词，返回空列表
        }

        Set<String> resultSet;
        // 根据查询类型选择不同的处理方式
        if (query.ngordnetQueryType().equals(NgordnetQueryType.HYPONYMS)) {
            resultSet = words.size() == 1 ?
                    wordnet.getHyponyms(words.get(0)) :
                    wordnet.getCommonHyponyms(words);
        } else {  // ANCESTORS
            resultSet = words.size() == 1 ?
                    wordnet.getAncestors(words.get(0)) :
                    wordnet.getCommonAncestors(words);
        }


        // k = 0
        if (k == 0) {
            // 原有的k=0处理逻辑
            List<String> sortResults = new ArrayList<>(resultSet);
            return formatResult(sortResults);
        }

        // k > 0 考虑词频
        // 创建词-频率映射的列表
        List<WordFreq> wordFreqs = new ArrayList<>();
        for (String word : resultSet) {
            double count = getWordCount(word, startYear, endYear);
            if (count > 0) {  // 只添加出现次数大于0的词
                wordFreqs.add(new WordFreq(word, count));
            }
        }

        if (wordFreqs.isEmpty()) {
            return "[]";  // 如果没有任何词在指定时间范围内出现，返回空列表
        }

        // 按照频率降序，词字母升序排序
        Collections.sort(wordFreqs);
        // 取前 k 个词
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, wordFreqs.size()); i++) {
            result.add(wordFreqs.get(i).word);
        }

        return formatResult(result);
    }

    /**
     * 计算单词在指定时间范围内的总出现次数
     */
    private double getWordCount(String word, int startYear, int endYear) {
        return ngm.countHistory(word, startYear, endYear)
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /**
     * 辅助方法 WordFreq 来帮助排序
     */
    private static class WordFreq implements Comparable<WordFreq> {
        String word;
        double frequency;

        WordFreq(String word, double frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(WordFreq other) {
            if (this.frequency != other.frequency) {
                // 频率降序
                return Double.compare(other.frequency, this.frequency);
            }
            // 频率相同时按字母升序
            return this.word.compareTo(other.word);
        }
    }

    /**
     * 格式化结果辅助方法
     */
    private String formatResult(List<String> words) {
        if (words.isEmpty()) {
            return "[]";
        }
        // 按字母顺序排序
        Collections.sort(words);
        return "[" + String.join(", ", words) + "]";
    }


}

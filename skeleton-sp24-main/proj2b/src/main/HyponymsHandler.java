package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wordnet;

    /**
     * HyponymsHandler 构造函数
     * @param synsetsFile 同义词集文件路径
     * @param hyponymsFile 下位词关系文件路径
     */
    public HyponymsHandler(String synsetsFile, String hyponymsFile) {
        if (synsetsFile == null || hyponymsFile == null) {
            throw new IllegalArgumentException("File paths cannot be null");
        }
        wordnet = new WordNet(synsetsFile, hyponymsFile);
    }

    public HyponymsHandler(WordNet wordnet) {
        super();
    }

    @Override
    public String handle (NgordnetQuery query) {
        // 从 query 中获取查询参数
        List<String> words = query.words();

        // 参数有效性检查
        if (words == null || words.isEmpty()) {
            return "[]"; // 若没有输入词，返回空列表
        }

        // 获取查询结果
        Set<String> hyponyms;
        if (words.size() == 1) {
            // 单个下位词查询
            hyponyms = wordnet.getHyponyms(words.get(0));
        } else {
            hyponyms = wordnet.getCommonHyponyms(words);
        }

        // 将查询结果转化为有序列表
        List<String> sortResults = new ArrayList<>(hyponyms);
        Collections.sort(sortResults); // 按字母顺序排序

        // 将查询结果格式化转化为字符串返回
        // 使用 StringBuilder 构建结果字符串
        StringBuilder result = new StringBuilder("[");

        // 遍历排序后的列表，构建结果字符串
        for (int i = 0; i < sortResults.size(); i++) {
            result.append(sortResults.get(i));
            // 如果不是最后一个元素，添加逗号和空格
            if (i < sortResults.size() - 1) {
                result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }

}

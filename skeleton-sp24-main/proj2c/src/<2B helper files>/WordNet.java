package main;

import java.util.*;

import edu.princeton.cs.algs4.In;

public class WordNet {
    // 同义词集映射
    private Map<Integer, Set<String>> idToWords; // ID -> 词集
    private Map<String, Set<Integer>> wordToIds; // 词 -> ID集
    // 下位词关系图
    private Graph graph;

    /**
     * WordNet 构造函数
     * @param synsetsFile 同义词集文件路径
     * @param hyponymsFile 下位词关系文件路径
     */
    public WordNet(String synsetsFile, String hyponymsFile) {
        idToWords = new HashMap<>();
        wordToIds = new HashMap<>();
        graph = new Graph();

        if (synsetsFile == null || hyponymsFile == null) {
            throw new IllegalArgumentException("File paths cannot be null");
        }

        // 解析文件
        parseSynsets(synsetsFile); // 先处理synsets，建立基础映射关系
        parseHyponyms(hyponymsFile); // 再处理hyponyms，建立词之间的关系
    }

    /**
     * 处理同义词 parseSynsets ，解析文件辅助方法
     * @param synsetsFile 同义词集文件路径
     */
    private void parseSynsets(String synsetsFile) {
        // 1. 使用 In 类读取文件
        In in = new In(synsetsFile);
        if (in == null) {
            throw new IllegalArgumentException("Could not read file: " + synsetsFile);
        }

        // 2. 逐行读取文件
        while (in.hasNextLine()) {
            String line = in.readLine();

            // 3. 处理单行数据
            String[] parts = line.split(","); // 分割每一行，用逗号分隔
            int id = Integer.parseInt(parts[0]); // 解析ID，将ID字符串转换为整数
            String[] synonyms = parts[1].split(" "); // 获取同义词集字符串并分割成单个词

            // 4. 循环处理同义词
            Set<String> synonymSet = new HashSet<>();
            for (String word: synonyms) {
                synonymSet.add(word);
                // 5. 更新 wordToIds 映射
                if (!wordToIds.containsKey(word)) {
                    wordToIds.put(word, new HashSet<>());  // 如果这个词第一次出现，为它创建一个新的ID集合
                }
                wordToIds.get(word).add(id); // 将当前ID添加到这个词的ID集合中
            }
            idToWords.put(id, synonymSet); // 更新 idToWords
            graph.addNode(id); // 将ID添加到图中作为节点
        }
    }


    /**
     * 处理下位词关系 parseHyponyms，解析文件辅助方法
     * @param hyponymsFile 下位词文件关系路径
     */
    private void parseHyponyms(String hyponymsFile) {
        // 1. 使用 In 类读取文件
        In in = new In(hyponymsFile);
        if (in == null) {
            throw new IllegalArgumentException("File not found: " + hyponymsFile);
        }

        // 2. 添加文件读取循环
        while (in.hasNextLine()) {
            String line = in.readLine();

            // 3. 处理单行数据，解析ID
            String[] ids = line.split(","); // 分割这一行，得到所有ID
            int hypernym = Integer.parseInt(ids[0]); // 解析上位词ID（第一个ID）

            // 4. 处理下位词和添加边
            for (int i = 1; i < ids.length; i++) {
                int hyponym = Integer.parseInt(ids[i]); // 解析当前下位词ID
                graph.addEdge(hypernym, hyponym); // 在图中添加一条边,从上位词指向下位词
            }
        }
    }

    /**
     * 下位词查询方法，返回一个词的所有下位词
     * @param word 要查询的词
     * @return 包含所有下位词的集合
     */
    public Set<String> getHyponyms(String word) {
        if (word == null || !wordToIds.containsKey(word)) {
            throw new IllegalArgumentException("Word does not exist in WordNet");
        }
        // 获取这个词对应的所有同义词集ID
        Set<Integer> startIds = wordToIds.get(word);

        // 从这些ID出发，找到所有可达节点
        Set<Integer> reachableIds = new HashSet<>();
        for (int startId : startIds) {
            // 对每个起始ID，进行图的遍历
            // 使用 graph 的 getNeighbors 方法找到所有可达节点
            dfs(startId, reachableIds);
        }

        // 将可达节点对应的词收集起来
        Set<String> hyponyms = new HashSet<>();
        for (int id : reachableIds) {
            // 将这个ID对应的所有词加入结果集
            hyponyms.addAll(idToWords.get(id));
        }

        return hyponyms;
    }

    /**
     * 深度优先搜索遍历图，辅助方法
     * @param v 当前访问的节点ID
     * @param visited 存储已访问节点的集合
     */
    private void dfs(int v, Set<Integer> visited) {
        // 将当前节点标记为已访问
        visited.add(v);
        // 访问所有未访问的邻居节点
        for (int neighbor : graph.getNeighbors(v)) {
            // 如果邻居节点未被访问过，则递归访问它
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }

    /**
     * 返回多个词的共同下位词
     * @param words 要查询的词列表
     * @return 包含所有共同下位词的集合
     */
    public Set<String> getCommonHyponyms(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Words list cannot be null");
        }
        for (String word : words) {
            if (!wordToIds.containsKey(word)) {
                throw new IllegalArgumentException("Word '" + "' dose not exist in WordNet");
            }
        }

        // 获取第一个词的下位词作为初始结果集
        Set<String> result = getHyponyms(words.get(0));

        // 遍历剩余的词，求交集
        for (int i = 1; i < words.size(); i++) {
            Set<String> currentHyponyms = getHyponyms(words.get(i));
            result.retainAll(currentHyponyms);
        }

        return result;
    }
}

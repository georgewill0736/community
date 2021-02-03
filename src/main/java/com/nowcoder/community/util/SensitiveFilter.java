package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 杜俊宏
 * Date on 2021/1/23 15:05
 */

@Component
public class SensitiveFilter {

    public static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符
    public static final String REPLACEMENT="***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ) {
                String keyWord;
                while ((keyWord = reader.readLine())!=null) {
                    //添加到前缀树
                    this.addKeyWord(keyWord);
                }
        } catch (IOException e) {
            logger.error("加载敏感期文件异常"+e.getMessage());
        }
    }

    //将敏感词添加到前缀树
    private void addKeyWord(String keyWord) {
        TrieNode tempNode = rootNode;
        for (int i = 0;i<keyWord.length();i++) {
            char c = keyWord.charAt(i);
            TrieNode sonNode = tempNode.getSonNode(c);
            if (sonNode == null) {
                sonNode = new TrieNode();
                tempNode.addSonNode(c,sonNode);
            }
            //指向子节点
            tempNode=sonNode;
            //设置结束的标识
            if (i==keyWord.length()-1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词，参数为过滤的文本，返回为过滤后的文本
     * @param text
     * @return
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin=0;
        //指针3
        int position=0;

        StringBuilder sb = new StringBuilder();

        while (begin<text.length()) {
            if (position < text.length()) {
                char c = text.charAt(position);
                // 跳过符号
                if (isSymbol(c)) {
                    if (tempNode == rootNode) {
                        sb.append(c);
                        begin++;
                    }
                    //无论符号在开头或中间,指针3都往下走
                    position++;
                    continue;
                }
                //检查下级节点
                tempNode = tempNode.getSonNode(c);
                if (tempNode == null) {
                    // 以begin开头的字符串不是敏感词
                    sb.append(text.charAt(begin));
                    // 进入下一个位置
                    position = ++begin;
                    // 重新指向根节点
                    tempNode = rootNode;
                }// 发现敏感词
                else if (tempNode.isKeywordEnd()) {
                    sb.append(REPLACEMENT);
                    begin = ++position;
                }
                // 检查下一个字符
                else {
                    position++;
                }
            } else {
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            }
        }
        return sb.toString();
    }

    private boolean isSymbol(Character c) {
        //2E80~9FFF是东亚文字
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }

    //前缀树
    private class TrieNode {

        //关键词结束的标记
        private boolean isKeywordEnd = false;

        //子节点(key是下级字符，value为下级节点)
        private Map<Character,TrieNode> sonNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addSonNode(Character c,TrieNode node) {
            sonNodes.put(c,node);
        }

        //获取子节点
        public TrieNode getSonNode(Character c) {
            return sonNodes.get(c);
        }

    }

}

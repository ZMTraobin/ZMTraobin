package com.cmig.future.csportal.common.participle;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: 中文分词
 * @author: zhangtao
 * @create: 2018-09-26 09:31
 **/
public interface MyWordSegmenter {
    /**
     * 获取文本的所有分词结果
     *
     * @param text 文本
     * @return 所有的分词结果，去除重复
     */
    default Set<String> seg(String text) {
        return segMore(text).values().stream().collect(Collectors.toSet());
    }

    /**
     * 获取文本的所有分词结果
     *
     * @param text 文本
     * @return 所有的分词结果，KEY 为分词器模式，VALUE 为分词器结果
     */
    Map<String, String> segMore(String text);
}

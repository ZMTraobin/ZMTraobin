package com.cmig.future.csportal.common.participle.impl;

import com.cmig.future.csportal.common.participle.MyWordSegmenter;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:word分词器
 * @author: zhangtao
 * @create: 2018-09-26 09:32
 **/
public class WordEvaluation implements MyWordSegmenter {

    /*
    移除停用词：
    List<Word> words = WordSegmenter.seg("我要去超市");
    保留停用词：List<Word> words = WordSegmenter.segWithStopWords("我要去超市");
    System.out.println(words);
    输出：移除停用词：[杨尚川, apdplat, 应用级, 产品, 开发平台, 作者]
    保留停用词：[杨尚川, 是, apdplat, 应用级, 产品, 开发平台, 的, 作者]
    */

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();
        for(SegmentationAlgorithm segmentationAlgorithm : SegmentationAlgorithm.values()){
            map.put(segmentationAlgorithm.getDes(), seg(text, segmentationAlgorithm));
        }
        return map;
    }

    private static String seg(String text, SegmentationAlgorithm segmentationAlgorithm) {
        StringBuilder result = new StringBuilder();
        for(Word word : WordSegmenter.seg(text, segmentationAlgorithm)){
            result.append(word.getText()).append(" ");
        }
        return result.toString();
    }

    public static Set<String> segSimple(String text){
        List<Word> words = WordSegmenter.seg(text);
        if(null!=words){
            Set<String> set=new HashSet<>();
            words.forEach(item->{
                set.add(item.getText());
            });
            return set;
        }
        return null;
    }

    public static Map<String, Set<String>> contrast(String text){
        Map<String, Set<String>> map = new LinkedHashMap<>();
        map.put("word分词器", new WordEvaluation().seg(text));
        return map;
    }

    public static Map<String, Map<String, String>> contrastMore(String text){
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        map.put("word分词器", new WordEvaluation().segMore(text));
        return map;
    }

    public static void show(Map<String, Set<String>> map){
        map.keySet().forEach(k -> {
            System.out.println(k + " 的分词结果：");
            AtomicInteger i = new AtomicInteger();
            map.get(k).forEach(v -> {
                System.out.println("\t" + i.incrementAndGet() + " 、" + v);
            });
        });
    }

    public static void showMore(Map<String, Map<String, String>> map){
        map.keySet().forEach(k->{
            System.out.println(k + " 的分词结果：");
            AtomicInteger i = new AtomicInteger();
            map.get(k).keySet().forEach(a -> {
                System.out.println("\t" + i.incrementAndGet()+ " 、【"   + a + "】\t" + map.get(k).get(a));
            });
        });
    }

    public static void main(String[] args) {
        show(contrast("我要去超市"));
        showMore(contrastMore("我要去超市"));
        System.out.println(segSimple("我要去超市"));
    }
}

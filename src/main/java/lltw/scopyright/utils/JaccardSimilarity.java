package lltw.scopyright.utils;

import java.util.HashSet;
import java.util.Set;
import org.springframework.util.StringUtils;
import com.huaban.analysis.jieba.JiebaSegmenter;

public class JaccardSimilarity {

    public static double calculateJaccardSimilarity(String text1, String text2) {
        Set<String> set1 = tokenize(text1);
        Set<String> set2 = tokenize(text2);

        System.out.println("Tokens for text1: " + set1);  // Debugging
        System.out.println("Tokens for text2: " + set2);  // Debugging

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    private static Set<String> tokenize(String text) {
        Set<String> tokens = new HashSet<>();
        if (StringUtils.hasText(text)) {
            JiebaSegmenter segmenter = new JiebaSegmenter();
            tokens.addAll(segmenter.sentenceProcess(text));
        }
        return tokens;
    }
}

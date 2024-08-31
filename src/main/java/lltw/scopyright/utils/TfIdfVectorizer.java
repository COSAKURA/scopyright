package lltw.scopyright.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Sakura
 */
public class TfIdfVectorizer {

    public Map<CharSequence, Integer> vectorize(String content) {
        Map<CharSequence, Integer> vector = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(content);

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase();
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }

        return vector;
    }
}

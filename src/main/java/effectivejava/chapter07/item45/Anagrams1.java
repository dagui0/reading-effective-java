package effectivejava.chapter07.item45;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Anagrams1 {

    public static void main(String[] args) throws Exception {

        File dictionary = new File(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        Map<String, Set<String>> groups = new HashMap<>();
        try (Scanner s = new Scanner(dictionary, StandardCharsets.UTF_8)) {
            while (s.hasNext()) {
                String word = s.next();

                // V computeIfAbsent(K key, Function<K, V> mapper) map에 키가 있으면 리턴, 없으면 람다 실행
                groups.computeIfAbsent(alphabetize(word), (unused) -> new TreeSet<>()) // TreeSet
                    .add(word);
            }
        }

        for (Set<String> group : groups.values()) {
            if (group.size() >= minGroupSize) {
                System.out.println(group.size() + ": " + group);
            }
        }
    }

    private static String alphabetize(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}

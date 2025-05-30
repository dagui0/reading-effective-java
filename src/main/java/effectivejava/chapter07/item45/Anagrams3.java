package effectivejava.chapter07.item45;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Anagrams3 {

    public static void main(String[] args) throws Exception {

        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        Map<String, Set<String>> groups = new HashMap<>();
        try (Stream<String> stream = Files.lines(dictionary, StandardCharsets.UTF_8)) {
            for (String word: iterableOf(stream)) {
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

    private static <T> Iterable<T> iterableOf(Stream<T> stream) {
        return stream::iterator;
    }

    private static <T> Stream<T> streamOf(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
    private static <T> Stream<T> streamOf(Iterable<T> iterable, boolean parallel) {
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }


    private static String alphabetize(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}

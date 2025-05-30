package effectivejava.chapter07.item45;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Anagrams2 {

    public static void main(String[] args) throws Exception {

        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        try (Stream<String> words = Files.lines(dictionary, StandardCharsets.UTF_8)) {
            words.collect(
                groupingBy(word -> word.chars()
                    .sorted()
                    .collect(
                        StringBuilder::new,
                        (sb, c) -> sb.append((char) c),
                        StringBuilder::append).toString(),
                        toCollection(TreeSet::new)))
                .values().stream()
                .filter(group -> group.size() >= minGroupSize)
                .map(group -> group.size() + ": " + group)
                .forEach(System.out::println);
        }
    }
}

package effectivejava.chapter09.item59;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class RandomTest {

    static Random rnd = new Random();
    static int random(int n) {
        return Math.abs(rnd.nextInt()) % n;
    }

    @Test
    public void testRandom() {

        int max = 79;
        int count = 100;

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(random(max));
        }

        Map<Integer, Integer> countMap = list.stream()
                .collect(Collectors.toMap(
                        i -> i,
                        i -> 1,
                        Integer::sum
                ));

        System.out.println("Random numbers: " + list);
        System.out.println("Random statistics: " + countMap);
    }

    @Test
    public void testRandom2() {

        int max = 2 * (Integer.MAX_VALUE / 3);
        int count = 1000000;

        int low = 0;
        for (int i = 0; i < count; i++) {
            if (random(max) < max / 2)
                low++;
        }

        System.out.println("Under middle value: " + low);
    }
}

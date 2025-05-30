package lambdaspecial.java;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.*;

public class LambdaTest {

    @Test
    public void testLambdaWithType() {

        BinaryOperator<Integer> sumAndDouble = (Integer a, Integer b) -> (a + b) * 2;
        assertEquals(30, sumAndDouble.apply(5, 10));
    }

    @Test
    public void testLambda() {

        // (int, int) -> int
        @SuppressWarnings("Convert2MethodRef")
        BiFunction<Integer, Integer, Integer> f = (a, b) -> Integer.sum(a, b);
        BiFunction<Integer, Integer, Integer> g = Integer::sum;

        // 이론적으로는 동등하지만 실제로는 다르다 ㅋㅋ
        // assertEquals(f, g);
        assertNotEquals(f, g);
    }

    @Test
    public void testAsynchronousCall() {

        try (ExecutorService executorService = Executors.newFixedThreadPool(2)) {

            System.out.println("메인 스레드: 비동기 작업을 제출합니다...");

            Future<String> future1 = executorService.submit(() -> {
                System.out.println("비동기 작업 1: 시작 (스레드: " + Thread.currentThread().getName() + ")");
                try {
                    // 시뮬레이션을 위해 3초간 대기
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 인터럽트 상태를 다시 설정
                    System.err.println("비동기 작업 1: 인터럽트됨");
                    return "작업 1 실패 (인터럽트)";
                }
                System.out.println("비동기 작업 1: 완료");
                return "작업 1 결과: 성공!";
            });
            Future<Integer> future2 = executorService.submit(() -> {
                System.out.println("비동기 작업 2: 시작 (스레드: " + Thread.currentThread().getName() + ")");
                try {
                    // 시뮬레이션을 위해 2초간 대기
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("비동기 작업 2: 인터럽트됨");
                    return -1; // 실패 시 특정 값 반환
                }
                System.out.println("비동기 작업 2: 완료");
                return 123; // 작업 2 결과 (정수)
            });

            System.out.println("메인 스레드: 작업들이 제출되었으며, 다른 작업을 계속 수행할 수 있습니다.");

            for (int i = 0; i < 3; i++) {
                System.out.println("메인 스레드: 다른 작업 수행 중... (" + i + ")");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("\n메인 스레드: 비동기 작업 결과를 기다립니다...");

            try {
                // 작업 1의 결과 가져오기 (최대 5초 대기)
                // future1.get()은 작업이 완료될 때까지 현재 스레드를 블로킹합니다.
                String result1 = future1.get(5, TimeUnit.SECONDS); // 타임아웃 설정 가능
                System.out.println("메인 스레드: 작업 1 결과 수신 - " + result1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("메인 스레드: 작업 1 결과 대기 중 인터럽트됨");
            } catch (ExecutionException e) {
                System.err.println("메인 스레드: 작업 1 실행 중 예외 발생 - " + e.getCause());
            } catch (TimeoutException e) {
                System.err.println("메인 스레드: 작업 1 결과 대기 시간 초과");
                future1.cancel(true); // 작업 취소 시도
            }

            try {
                // 작업 2의 결과 가져오기 (완료될 때까지 대기)
                Integer result2 = future2.get();
                System.out.println("메인 스레드: 작업 2 결과 수신 - " + result2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("메인 스레드: 작업 2 결과 대기 중 인터럽트됨");
            } catch (ExecutionException e) {
                System.err.println("메인 스레드: 작업 2 실행 중 예외 발생 - " + e.getCause());
            }

            System.out.println("\n메인 스레드: 모든 작업에 대한 결과 처리를 시도했습니다.");

            // ExecutorService 종료
            // 새로운 작업은 더 이상 받지 않고, 진행 중인 작업이 완료되면 종료합니다.
            executorService.shutdown();
            try {
                // 모든 작업이 종료될 때까지 최대 1분 대기
                if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                    // 타임아웃 발생 시 강제 종료
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                // awaitTermination 중 인터럽트 발생 시 강제 종료
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }

            System.out.println("메인 스레드: 프로그램 종료.");
        }
    }

    @Test
    public void testHaskellBrooksCurrying() {

        BinaryOperator<Integer> add = (a, b) -> a + b;
        assertEquals(7, add.apply(3, 4));

        Function<Integer, UnaryOperator<Integer>> addCurrying = (a) -> (b) -> a + b;
        assertEquals(7, addCurrying.apply(3).apply(4));

    }

    @Test
    public void testFunctionClassName() {

        // 람다식은 리터럴이 아니다.
        // int val = ((a, b) -> a + b)(1, 2);  // 컴파일 안됨

        BinaryOperator<Integer> addLambda = (a, b) -> a + b;
        BinaryOperator<Integer> addAnonClz = new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return a + b;
            }
        };

        // 람다식은 익명 클래스이지만 .class 파일이 만들어지지는 않는다!! 클래스명은 매번 달라짐
        // assertEquals("lambdaspecial.java.LambdaTest$$Lambda/0x00000288811608c0", addLambda.getClass().getName());
        assertEquals("lambdaspecial.java.LambdaTest$1", addAnonClz.getClass().getName());
    }

    void main() {
        List<String> words = List.of("apple", "banana", "kiwi", "grape");
        Collections.sort(words, comparingInt(String::length));

        List<String> tokens = getTokens();
        Map<String, Integer> tokenCounts = new HashMap<>();

        tokens.forEach(token -> {
            tokenCounts.merge(token, 1, (count, incr) -> count + incr);
        });

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        int accumulated = numbers.stream()
                .reduce(MyAlgorithm::accumulate).orElse(0);
        int sum = numbers.stream()
                .reduce((prior, next) -> prior + next).orElse(0);

        "Hello world!".chars()
                .forEach(System.out::println); // 72101108...
        "Hello world!".chars().forEach(c -> System.out.println((char)c));
        System.out.println("Hello world!");

    }
    void main2() {
        List<String> words = Arrays.asList("hello", "world");
        // 메서드 참조는 타입 추론이 안되므로, 람다식으로 써야 함
        words.sort(String::compareToIgnoreCase);
        //words.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
    }

    private List<String> getTokens() {
        return List.of();
    }


    private class MyAlgorithm {
        public static int accumulate(int a, int b) {
            return a + b;
        }
    }


    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        NINE, TEN, JACK, QUEEN, KING
    }
    public enum Suit { SPADES, HEARTS, DIAMONDS, CLUBS }
    public record Card(Suit suit, Rank rank) {
        public static List<Card> newDeck() {
            List<Card> deck = new ArrayList<>();
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    deck.add(new Card(suit, rank));
                }
            }
            return deck;
        }
        public static List<Card> newDeckLambda() {
            return Stream.of(Suit.values())
                .flatMap(suit ->
                    Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .toList();
        }
    }

    @Test
    public void testSideEffects() {
        File file = new File("src/test/resources/words.txt");
        Map<String, Long> freq = new HashMap<>();
        try (Stream<String> words = new Scanner(file).tokens()) {
            words.forEach(token ->
                    freq.merge(token.toLowerCase(), 1L, Long::sum));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPureFunction() {
        File file = new File("src/test/resources/words.txt");
        Map<String, Long> freq;
        try (Stream<String> words = new Scanner(file).tokens()) {
            freq = words.collect(
                groupingBy(String::toLowerCase, counting()));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static long pi(long n) {
        return LongStream.rangeClosed(2, n)
                .filter(i -> i == 2 || i % 2 != 0) // 2와 홀수만 필터링
                .parallel() // 병렬 스트림
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
}

class MySimpleCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;
    public MySimpleCache(int capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    /// LinkedHashMap의 put() 구현
    /// ```java
    /// public V put(K key, V value) {
    ///     if (removeEldestEntry(new Entry(key, value))) {
    ///         super.remove(eldestKey);
    ///     }
    ///     ...
    /// }
    /// ```
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity; // capacity를 넘으면 가장 오래된 항목을 제거
    }
}

@FunctionalInterface
interface EldestEntryRemovalFunction<K, V> {
    // (Map, Map.Entry) -> boolean
    boolean removeEldestEntry(Map<K, V> map, Map.Entry<K, V> eldest);
}

abstract class LinkedHashMapLambda<K, V> implements Map<K, V> {

    private final EldestEntryRemovalFunction<K, V> removal;

    public LinkedHashMapLambda(EldestEntryRemovalFunction<K, V> removal) {
        this.removal = removal;
    }

    static class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException("setValue not supported");
        }
    }

    public V put(K key, V value) {
        if (removal.removeEldestEntry(this, new Entry<>(key, value)))
            return remove(null);
        return null;
    }
}

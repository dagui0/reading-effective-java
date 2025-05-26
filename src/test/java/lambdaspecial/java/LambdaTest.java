package lambdaspecial.java;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

public class LambdaTest {

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

}



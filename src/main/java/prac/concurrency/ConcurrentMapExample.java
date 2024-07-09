package prac.concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMapExample {
    private static final int NUM_THREADS = 10;
    private static final int NUM_ENTRIES = 1000;

    public static void main(String[] args) throws InterruptedException {
        ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();

        // 쓰레드 배열
        Thread[] threads = new Thread[NUM_THREADS];

        // 쓰레드를 생성하여 ConcurrentMap에 데이터를 삽입
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_ENTRIES; j++) {
                    final String threadName = Thread.currentThread().getName() + "-" + j;
                    System.out.println("threadName = " + threadName);
                    map.put(threadName, j);
                }
            });
        }

        // 모든 쓰레드 시작
        for (Thread thread : threads) {
            thread.start();
        }

        // 모든 쓰레드가 작업을 마칠 때까지 대기
        for (Thread thread : threads) {
            thread.join();
        }

        // 맵의 크기 확인
        System.out.println("Map size: " + map.size());

        // 맵의 데이터 검증
        for (int i = 0; i < NUM_THREADS; i++) {
            for (int j = 0; j < NUM_ENTRIES; j++) {
                String key = "Thread-" + i + "-" + j;
                Integer value = map.get(key);
                if (value == null || value != j) {
                    System.out.println("Data inconsistency found: " + key + " -> " + value);
                }
            }
        }

        System.out.println("Data consistency check complete.");
    }
}


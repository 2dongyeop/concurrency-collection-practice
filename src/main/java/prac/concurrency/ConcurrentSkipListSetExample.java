package prac.concurrency;

import java.util.concurrent.ConcurrentSkipListSet;

public class ConcurrentSkipListSetExample {
    private static final int NUM_THREADS = 10;
    private static final int NUM_ENTRIES = 1000;

    public static void main(String[] args) throws InterruptedException {
        ConcurrentSkipListSet<String> set = new ConcurrentSkipListSet<>();

        // 쓰레드 배열
        Thread[] threads = new Thread[NUM_THREADS];

        // 쓰레드를 생성하여 ConcurrentSkipListSet에 데이터를 삽입
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_ENTRIES; j++) {
                    set.add("Thread-" + threadIndex + "-" + j);
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

        // 집합의 크기 확인
        System.out.println("Set size: " + set.size());

        // 집합의 데이터 검증
        for (int i = 0; i < NUM_THREADS; i++) {
            for (int j = 0; j < NUM_ENTRIES; j++) {
                String key = "Thread-" + i + "-" + j;
                if (!set.contains(key)) {
                    System.out.println("Data inconsistency found: " + key);
                }
            }
        }

        System.out.println("Data consistency check complete.");
    }
}
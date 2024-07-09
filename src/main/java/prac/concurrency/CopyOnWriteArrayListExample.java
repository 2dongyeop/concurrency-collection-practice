package prac.concurrency;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListExample {
    private static final int NUM_THREADS = 10;
    private static final int NUM_ENTRIES = 100;

    public static void main(String[] args) throws InterruptedException {
        List<String> list = new CopyOnWriteArrayList<>();

        // 쓰레드 배열
        Thread[] threads = new Thread[NUM_THREADS];

        // 쓰레드를 생성하여 CopyOnWriteArrayList에 데이터를 삽입
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_ENTRIES; j++) {
                    String threadName = Thread.currentThread().getName();
                    String value = threadName + "-" + j;
                    list.add(value);
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

        // 리스트의 크기 확인
        System.out.println("List size: " + list.size());

        // 리스트의 데이터 검증
        int expectedSize = NUM_THREADS * NUM_ENTRIES;
        if (list.size() != expectedSize) {
            System.out.println("Data inconsistency found. Expected size: " + expectedSize + ", Actual size: " + list.size());
        } else {
            System.out.println("Data consistency check complete. All entries are present.");
        }
    }
}
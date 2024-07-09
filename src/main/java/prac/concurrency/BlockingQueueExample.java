package prac.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueExample {
    private static final int NUM_PRODUCERS = 10;
    private static final int NUM_CONSUMERS = 10;
    private static final int NUM_ENTRIES = 1000;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(NUM_PRODUCERS * NUM_ENTRIES);

        // 프로듀서 스레드 배열
        Thread[] producerThreads = new Thread[NUM_PRODUCERS];
        // 컨슈머 스레드 배열
        Thread[] consumerThreads = new Thread[NUM_CONSUMERS];

        // 프로듀서 스레드를 생성하여 BlockingQueue에 데이터를 삽입
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            final int threadIndex = i;
            producerThreads[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < NUM_ENTRIES; j++) {
                        String item = "Producer-" + threadIndex + "-" + j;
                        queue.put(item);
                        System.out.println("Produced: " + item);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 컨슈머 스레드를 생성하여 BlockingQueue에서 데이터를 소비
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumerThreads[i] = new Thread(() -> {
                try {
                    while (true) {
                        String item = queue.take();
                        System.out.println("Consumed: " + item);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 모든 프로듀서 스레드 시작
        for (Thread thread : producerThreads) {
            thread.start();
        }

        // 모든 컨슈머 스레드 시작
        for (Thread thread : consumerThreads) {
            thread.start();
        }

        // 모든 프로듀서 스레드가 작업을 마칠 때까지 대기
        for (Thread thread : producerThreads) {
            thread.join();
        }

        // 모든 컨슈머 스레드를 인터럽트하여 종료
        for (Thread thread : consumerThreads) {
            thread.interrupt();
        }

        System.out.println("All producers have finished producing. Consumers have been interrupted.");
    }
}
package prac.concurrency;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkedDequeExample {
    private static final int NUM_PRODUCERS = 10;
    private static final int NUM_CONSUMERS = 10;
    private static final int NUM_ENTRIES = 1000;

    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();

        // 프로듀서 스레드 배열
        Thread[] producerThreads = new Thread[NUM_PRODUCERS];
        // 컨슈머 스레드 배열
        Thread[] consumerThreads = new Thread[NUM_CONSUMERS];

        // 프로듀서 스레드를 생성하여 ConcurrentLinkedDeque에 데이터를 삽입
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            final int threadIndex = i;
            producerThreads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_ENTRIES; j++) {
                    String item = "Producer-" + threadIndex + "-" + j;
                    deque.addFirst(item); // 덱의 앞에 삽입
                    System.out.println("Produced: " + item);
                }
            });
        }

        // 컨슈머 스레드를 생성하여 ConcurrentLinkedDeque에서 데이터를 소비
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumerThreads[i] = new Thread(() -> {
                while (true) {
                    String item = deque.pollLast(); // 덱의 뒤에서 소비
                    if (item != null) {
                        System.out.println("Consumed: " + item);
                    } else {
                        // 덱이 비어있으면 잠시 대기
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
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

        // 잠시 대기하여 컨슈머가 남은 아이템을 소비하도록 함
        Thread.sleep(1000);

        // 모든 컨슈머 스레드를 인터럽트하여 종료
        for (Thread thread : consumerThreads) {
            thread.interrupt();
        }

        // 모든 컨슈머 스레드가 종료될 때까지 대기
        for (Thread thread : consumerThreads) {
            thread.join();
        }

        System.out.println("All producers have finished producing. All consumers have finished consuming.");
    }
}

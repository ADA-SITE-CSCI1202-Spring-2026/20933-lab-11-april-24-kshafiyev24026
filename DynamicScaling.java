class MathTask implements Runnable {
    private int threadId;
    private int totalThreads;

    MathTask(int threadId, int totalThreads) {
        this.threadId = threadId;
        this.totalThreads = totalThreads;
    }

    public void run() {
        long result = 0;
        for (int i = threadId; i < 10_000_000; i += totalThreads) {
            result += (long)(i * i * i) + (long)(i * threadId);
        }
    }
}

public class DynamicScaling {

    static long runWithThreads(int threadCount) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new MathTask(i, threadCount));
        }

        long start = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        long end = System.currentTimeMillis();
        return end - start;
    }

    public static void main(String[] args) throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Logical cores on this machine: " + cores);

        long time1 = runWithThreads(1);
        System.out.println("Time with 1 thread:    " + time1 + " ms");

        long timeN = runWithThreads(cores);
        System.out.println("Time with " + cores + " threads: " + timeN + " ms");

        System.out.println("Speedup: " + String.format("%.2f", (double) time1 / timeN) + "x");
    }
}

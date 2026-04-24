class SharedResource {
    private int value;
    private boolean bChanged = false;

    public synchronized void set(int v) {
        value = v;
        bChanged = true;
        notify();
    }

    public synchronized int get() {
        while (!bChanged) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        bChanged = false;
        return value;
    }
}

public class CoordinationDemo {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Producer setting value: " + i);
                resource.set(i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                int val = resource.get();
                System.out.println("Consumer got value:    " + val);
            }
        });

        consumer.start();
        producer.start();
    }
}

import java.util.concurrent.*;

public class CF {
    static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws InterruptedException {
        int limit = 1000;
        long start = System.currentTimeMillis();
        synchronous(limit);
        System.out.println("Synchronous task " + (System.currentTimeMillis() - start) / 1000 + " sec");

        start = System.currentTimeMillis();
        asynchronous(limit);
        System.out.println("Asynchronous task " + (System.currentTimeMillis() - start) / 1000 + " sec");

    }


    public static void asynchronous(int limit) {
        CompletableFuture<Void>[] futures = new CompletableFuture[limit];
        for (int i = 0; i < limit; i++) {
            final int orderId = i + 1;
            futures[i] = CompletableFuture.supplyAsync(() -> {
                        try {
                            return getOrderId(orderId);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }, executorService)
                    .thenApply(orderId1 -> {
                        try {
                            return dispatchOrder(orderId1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenAccept(CF::sendEmail);
        }

        CompletableFuture.allOf(futures).join();
        executorService.shutdown();
    }

    public static void synchronous(int limit) throws InterruptedException {
        for (int i = 0; i <= limit; i++) {
            int orderId = getOrderId(i + 1);
            boolean dispatchResult = dispatchOrder(orderId);
            sendEmail(dispatchResult);
        }
    }

    public static Integer getOrderId(final int orderId) throws InterruptedException {
        Thread.sleep(100);
        return orderId;
    }

    public static boolean dispatchOrder(Integer orderId) throws InterruptedException {
        Thread.sleep(100);
        return orderId % 2 == 0;
    }

    public static void sendEmail(boolean isDispatched) {

    }
}
